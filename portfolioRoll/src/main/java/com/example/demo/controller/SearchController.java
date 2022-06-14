package com.example.demo.controller;

import java.io.File;
import java.util.ArrayList;
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

import com.example.demo.entity.User;
import com.example.demo.form.ListUserForm;
import com.example.demo.form.SearchUserForm;
import com.example.demo.service.UserService;

@Controller
public class SearchController {
	@Autowired
	private UserService service;
	
	/**add初期画面*/
	@RequestMapping("/search")
	public String search(@ModelAttribute("user")SearchUserForm user,Model model) {
		return "search"; //検索画面に遷移
	}
	
	/**全件表示*/
	@PostMapping("/allSearch")
	public String allPost(Model model) {
		//全件取得
		List<User> userList = service.findAll();
		
		
		// ==エラー処理==
		List<String> errorList = new ArrayList<String>();
		
		//検索結果が0件の時の処理
		if(userList.size()==0) {
			errorList.add("検索結果がありません。");
			model.addAttribute(errorList);
			return "search"; //検索画面へ遷移
		}
		// == エラー処理終了 ==
		
		//検索結果をListUserForm型に
		List<ListUserForm> formList = new ArrayList<ListUserForm>();
		for(User u : userList) {
			ListUserForm form = new ListUserForm();
			form.setId(String.valueOf(u.getId()));
			form.setName(u.getName());
			form.setPost(u.getPost());
			form.setImgPath("img/"+u.getImgPath());
			
			formList.add(form);
		}
		model.addAttribute("rollList", formList);
		return "search"; //検索画面へ遷移
	}
	
	/**検索時*/
	@PostMapping("/rollSearch")
	public String searchPost(@Validated @ModelAttribute("user")SearchUserForm user,BindingResult result ,Model model) {
		// ==エラー処理==
		List<String> errorList = new ArrayList<String>();
		//入力フラグ設定
		boolean idFlag = user.getId().equals(""); //未入力をtrue
		boolean nameFlag = user.getName().equals(""); //未入力をtrue
		
		//=条件未入力
		if(idFlag && nameFlag) {
			errorList.add("検索条件が未入力です");
			model.addAttribute("errorList", errorList);
			return "search"; // 検索画面へ遷移
		}
		
		// =入力処理
		if (!idFlag && result.hasErrors()) { // 入力エラーがあればエラーリストを作成して
			for (ObjectError errors : result.getAllErrors()) {
				errorList.add(errors.getDefaultMessage());
			}
			model.addAttribute("errorList", errorList);
			return "search"; // 検索画面へ遷移
		}
		
		//entityを呼び出して、積める
		User entity = new User();
		if(!user.getId().equals("")) {
			entity.setId(Long.parseLong(user.getId()));
		}else {
			entity.setId(0);
		}
		entity.setName(user.getName());
		
		//検索結果をuserListに代入
		List<User> userList = service.findByUser(entity);
		
		//検索結果が0件の時の処理
		if(userList.size()==0) {
			errorList.add("検索結果がありません。");
			model.addAttribute("errorList",errorList);
			return "search"; //検索画面へ遷移
		}
		//== エラー処理終了
		
		//検索結果をListUserForm型に
		List<ListUserForm> formList = new ArrayList<ListUserForm>();
		for(User u : userList) {
			ListUserForm form = new ListUserForm();
			form.setId(String.valueOf(u.getId()));
			form.setName(u.getName());
			form.setPost(u.getPost());
			form.setImgPath("img/"+u.getImgPath());
			
			formList.add(form);
		}
		model.addAttribute("rollList", formList);
		
		return "search"; //検索画面に遷移
	}
	
	/**削除*/
	@PostMapping("/rollDel")
	public String rollDel(@RequestParam("delId") String id, Model model) {
		//存在確認
		List<String> errorList = new ArrayList<String>();
		if(service.findById(Long.parseLong(id)).isEmpty()) {
			errorList.add("該当名簿は削除済みです。");
			model.addAttribute("errorList",errorList);
			return "search"; //検索画面へ遷移
		}
		
		//再検索
		User entity = service.findById(Long.parseLong(id)).get();
		
		//添付画像削除
		File delFile = new File("src/main/resources/static/img/"+entity.getImgPath());
		delFile.delete();
		
		//データ削除
		service.deleteById(entity.getId());
		
		return "redirect:/search";
	}
	
	/**編集*/
	@PostMapping("/rollEdit")
	public ModelAndView rollEdit(@RequestParam("editId") String id, Model model) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/edit");
		
		//存在確認
		List<String> errorList = new ArrayList<String>();
		if(service.findById(Long.parseLong(id)).isEmpty()) {
			errorList.add("該当名簿は削除済みです。");
			mav.addObject("errorList", errorList);
			mav.setViewName("search");
			return mav; //検索画面へ遷移
		}
		
		mav.addObject("editId", id);
		
		return mav; //編集画面に遷移
	}

}
