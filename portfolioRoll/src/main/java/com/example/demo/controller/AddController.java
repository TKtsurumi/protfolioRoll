package com.example.demo.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Timestamp;
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

import com.example.demo.domain.file.fileMain;
import com.example.demo.entity.User;
import com.example.demo.form.AddUserForm;
import com.example.demo.service.UserService;

@Controller
public class AddController {
	@Autowired
	private UserService service;

	/**add初期画面*/
	@RequestMapping("/add")
	public String add(@ModelAttribute("user")AddUserForm user,String jDate,Model model) {
		//当日の日付をjDateに入れて、addに渡す
		Date toDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		user.setJDate(sdf.format(toDate));
		model.addAttribute("user",user);
		
		return "add"; //登録画面に遷移
	}
	
	/**登録ボタン押下後処理*/
	@PostMapping("/rollAdd")
	public String rollAdd(@Validated @ModelAttribute("user")AddUserForm user, BindingResult result, Model model) {
		//==エラー処理==
		//=入力処理
		if(result.hasErrors()) { //入力エラーがあればエラーリストを作成して
			List<String> errorList = new ArrayList<String>();
			for(ObjectError errors : result.getAllErrors()) {
				errorList.add(errors.getDefaultMessage());
			}
			model.addAttribute("errorList",errorList);
			return "add"; //登録画面へ遷移
		}
		
		//=添付ファイルチェック
		fileMain fm = new fileMain(user.getImgFile());
		if(!fm.getResult()) {
			model.addAttribute("errorList","[顔写真]は「jpg」もしくは「png」を添付してください。");
			return "add"; //登録画面へ遷移
		}
		//=重複チェック
		if(this.service.findById(Long.parseLong(user.getId())).isPresent()) {
			model.addAttribute("errorList","[社員ID]が登録済みです。");
			return "add"; //登録画面へ遷移
		}
		//==エラー処理終了==
		
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
	    User entity = new User();
	    entity.setId(Long.parseLong(user.getId()));
	    entity.setName(user.getName());
	    entity.setAge(Long.parseLong(user.getAge()));
	    entity.setPost(user.getPost());
	    entity.setSex(user.getSex());
	    entity.setImgPath(fm.getImgPath());
	    //入社日は、sqlに変更してから積める
	    Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(user.getJDate());
        String fDate = sdf.format(date);
	    entity.setJoinedDate(java.sql.Date.valueOf(fDate));
	    entity.setRecordDate(new Timestamp(System.currentTimeMillis()));
		
		//データベースに登録
	    this.service.save(entity);
		
		return "redirect:/add";
	}

}
