package com.example.demo.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AddUserForm {
	
	/**IDプロパティ*/
	@NotEmpty(message="[社員ID]は必須です。")
	@Pattern(regexp="^[A-Za-z0-9]{4}+$",message="[社員ID]は半角数字4桁です。")
	private String id;
	
	/**氏名プロパティ*/
	@NotEmpty(message="[氏名]は必須です。")
	@Pattern(regexp="^[^ -~｡-ﾟ]+$",message="[氏名]は全角のみ使用可能です。")
	private String name;
	
	/**性別プロパティ*/
	private String sex;
	
	/**年齢プロパティ*/
	@NotEmpty(message="[年齢]は必須です。")
	@Pattern(regexp="^[A-Za-z0-9]{2}+$",message="[年齢]は半角数字2桁です。")
	private String age;
	
	/**役職プロパティ*/
	private String post;
	
	/**入社日プロパティ*/
	@NotEmpty(message="[入社日]は必須です。")
	private String jDate;
	
	/**顔写真プロパティ*/
	@NotNull(message="[顔写真]は必須です。")
	private MultipartFile imgFile;

}
