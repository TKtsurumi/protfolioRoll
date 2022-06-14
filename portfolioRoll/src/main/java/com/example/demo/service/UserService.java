package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository repository;
	
	/**1件登録*/
	@Transactional
	public void save(User entity) {
		 repository.save(entity);
	}
	
	/**1件重複検索*/
	@Transactional
	public Optional<User> findById(long id) {
		return repository.findById(id);
	}
	
	/**全件取得*/
	@Transactional
	public List<User> findAll(){
		return repository.findAll();
	}
	
	/**1件削除*/
	@Transactional
	public void deleteById(long id) {
		repository.deleteById(id);
	}
	
	/**リスト検索*/	
	@Transactional
	public List<User> findByUser(User entity){
		long id = entity.getId();
		String name = entity.getName();
		// 入力値によって呼び出すメソッドを変更
		if (id != 0 && !name.equals("")) {
			return repository.findByIdAndNameContaining(id, name);
		} else if (id != 0) {
			return repository.findByIdAndNameContaining(id, name);
		} else if (!name.equals("")) {
			return repository.findByNameContaining(name);
		};
		return null;
	}

}
