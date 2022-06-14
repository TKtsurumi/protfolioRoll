package com.example.demo.form;

import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class SearchUserForm {

	@Pattern(regexp="^[A-Za-z0-9]{4}+$",message="[社員ID]は半角数字4桁です。")
	private String id;
	
	/**氏名プロパティ*/
	private String name;

}
