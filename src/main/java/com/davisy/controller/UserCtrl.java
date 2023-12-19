package com.davisy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.davisy.config.JwtTokenUtil;
import com.davisy.entity.Roles;
import com.davisy.entity.User;
import com.davisy.mongodb.documents.Search;
import com.davisy.reponsitory.RolesReponsitory;
import com.davisy.service.impl.SearchServiceImp;
import com.davisy.service.impl.UserServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
public class UserCtrl {
	



	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	RolesReponsitory reponsitory;
	
	@Autowired
	private SearchServiceImp searchImp;
	
	@Autowired JwtTokenUtil jwtTokenUtil;
	
	@GetMapping("/v1/oauth/user/{email}")
	public ResponseEntity<User> getU(@PathVariable String email){
		return ResponseEntity.status(200).body(userService.findByEmail(email));
	}
	
	@GetMapping("v1/oauth/alluser")
	public List<User> getAll(){
		return userService.findAll();
	}
	
	@GetMapping("/v1/oauth/user/role/{role}")
	public ResponseEntity<Roles> getUs(@PathVariable String role){
		return ResponseEntity.status(200).body(reponsitory.findByName(role));
	}
	@PostMapping("/v1/oauth/user/search")
	public ResponseEntity<Search> toSearch(HttpServletRequest request, @RequestParam String content){
		String email = jwtTokenUtil.getEmailFromHeader(request);
		User user = userService.findByEmail(email);
		try {
			
			return ResponseEntity.status(200).body(searchImp.search(user.getUser_id()+"", content));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ResponseEntity.status(500).body(null);
	}
	@GetMapping("/v1/oauth/user/search")
	public ResponseEntity<List<Search>> toSearch(HttpServletRequest request){
		String email = jwtTokenUtil.getEmailFromHeader(request);
		User user = userService.findByEmail(email);
		try {
			return ResponseEntity.status(200).body(searchImp.get10Search(user.getUser_id()+""));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ResponseEntity.status(500).body(null);
	}
}
