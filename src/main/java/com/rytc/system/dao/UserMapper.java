package com.rytc.system.dao;

import java.util.List;

import com.rytc.common.config.MyMapper;
import com.rytc.system.domain.User;
import com.rytc.system.domain.UserWithRole;

public interface UserMapper extends MyMapper<User> {

	List<User> findUserWithDept(User user);
	
	List<UserWithRole> findUserWithRole(Long userId);
	
	User findUserProfile(User user);
}