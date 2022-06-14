package com.example.demo.entity;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.relational.core.mapping.Embedded.Nullable;

import lombok.Data;

@Entity
@Data
@Table(name="ROLL_TABLE")
public class User {
	@Id
	@Column(name="USER_ID")
	@Nullable
	private long id;
	
	@Column(name="USER_NAME")
	@Nullable
	private String name;
	
	@Column(name="USER_SEX")
	@Nullable
	private String sex;
	
	@Column(name="USER_POST")
	@Nullable
	private String post;
	
	@Column(name="IMG_PATH")
	@Nullable
	private String imgPath;
	
	@Column(name="JOINED_DATE")
	@Nullable
	private Date joinedDate;
	
	@Column(name="RECORD_DATE")
	@Nullable
	private Timestamp recordDate;
	
	@Column(name="USER_AGE")
	private Long age;

}
