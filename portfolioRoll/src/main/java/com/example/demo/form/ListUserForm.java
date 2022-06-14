package com.example.demo.form;

import lombok.Data;

@Data
public class ListUserForm {
	
	/**IDプロパティ*/
	private String id;
	
	/**氏名プロパティ*/
	private String name;
	
	/**役職プロパティ*/
	private String post;
	
	/**パスプロパティ*/
	private String imgPath;

}
