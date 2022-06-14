package com.example.demo.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.domain.file.fileMain;
import com.example.demo.entity.User;
import com.example.demo.form.EditUserForm;
import com.example.demo.service.UserService;

@Controller
public class EditController {
	@Autowired
	private UserService service;

	/**edit初期画面*/
	@RequestMapping("/edit")
	public String edit(@ModelAttribute("user")EditUserForm user,@RequestParam("editId") String editId,Model model) {
		
		//再検索
		User entity = service.findById(Long.parseLong(editId)).get();
		
		//EditUserFormにつめる
		user.setId(editId);
		user.setName(entity.getName());
		user.setPost(entity.getPost());
		user.setAge(String.valueOf(entity.getAge()));
		user.setSex(entity.getSex());
		user.setJDate(new SimpleDateFormat("yyyy-MM-dd").format(entity.getJoinedDate()));
		user.setImgPath("img/"+entity.getImgPath());

		
		//遷移時の日時をsDateに入れる
		Timestamp tp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		user.setSDate(sdf.format(tp));
		
		model.addAttribute("user",user);
		
		return "edit"; //編集画面に遷移
	}
	
	/**編集処理*/
	@PostMapping("/editData")
	public ModelAndView editData(@Validated @ModelAttribute("user")EditUserForm user,BindingResult result,Model model) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("edit");
		
		//==エラー処理==
		List<String> errorList = new ArrayList<String>();
		//=入力処理
		if(result.hasErrors()) { //入力エラーがあればエラーリストを作成して
			for(ObjectError errors : result.getAllErrors()) {
				errorList.add(errors.getDefaultMessage());
			}
			mav.addObject("errorList", errorList);
			return mav; //編集画面画面へ遷移
		}
		
		//再検索
		User entity = service.findById(Long.parseLong(user.getId())).get();
		
		//==排他エラー処理==
		Timestamp RDate = entity.getRecordDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		Timestamp SDate = null;
		try {
			date = sdf.parse(user.getSDate());
			SDate = new Timestamp(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(RDate.after(SDate)) {
			errorList.add("該当の名簿は既に編集済みです");
			mav.addObject("errorList", errorList);
			mav.setViewName("redirect:/search");
			return mav; //登録画面に遷移
		}
		
		//=添付ファイルチェック
		fileMain fm = new fileMain(user.getImgFile());
		if(!fm.getResult()) {
			errorList.add("[顔写真]は「jpg」もしくは「png」を添付してください。");
			mav.addObject("errorList", errorList);
			return mav; //編集画面へ遷移
		}
		
		//添付画像削除
		File delFile = new File("src/main/resources/static/img/"+entity.getImgPath());
		delFile.delete();
		
		//画像を保存
		try {
			final BufferedInputStream bis = new BufferedInputStream(fm.getImgFile().getInputStream());
			final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("src/main/resources/static/img/"+fm.getImgPath()));
			byte[] data = new byte[1024];
			int len;

			// 処理開始
			while ((len = bis.read(data)) != -1) {
				bos.write(data, 0, len);
			}
			bis.close();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	    //entityに積める
	    User editEntity = new User();
	    editEntity.setId(Long.parseLong(user.getId()));
	    editEntity.setName(user.getName());
	    editEntity.setAge(Long.parseLong(user.getAge()));
	    editEntity.setPost(user.getPost());
	    editEntity.setSex(user.getSex());
	    editEntity.setImgPath(fm.getImgPath());
	    //入社日は、sqlに変更してから積める
	    Date editDate = new Date();
        SimpleDateFormat esdf = new SimpleDateFormat(user.getJDate());
        String fDate = esdf.format(editDate);
        editEntity.setJoinedDate(java.sql.Date.valueOf(fDate));
        editEntity.setRecordDate(new Timestamp(System.currentTimeMillis()));
        
        //変更処理
        service.save(editEntity);
		
		mav.setViewName("redirect:/search");
		return mav; //

	}

}
