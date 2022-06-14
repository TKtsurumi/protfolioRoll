package com.example.demo.domain.file;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class fileMain {
	MultipartFile imgFile; //添付ファイル
	String imgPath; //ファイルパス名
	Boolean result; //ファイル名判定
	public fileMain(MultipartFile imgFile) {
		this.result = true;
		this.imgFile = imgFile;
		statusImg();
	}
	
	/**呼び出し時の処理*/
	private void statusImg() {
		String imgPathPut = this.imgFile.getOriginalFilename();
		imgPathPut = imgPathPut.toLowerCase();
		
		//拡張子チェック
		if(imgPathPut.indexOf(".jpg")==-1&&imgPathPut.indexOf(".png")==-1) {
			this.result = false;
		}
		//ファイル名に現在日時を追加する
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		this.imgPath = sdf.format(date) + "_" + imgPathPut;
	}

}
