package com.rytc.system.dao;

import java.util.List;

import com.rytc.common.config.MyMapper;
import com.rytc.system.domain.Role;
import com.rytc.system.domain.RoleWithMenu;

public interface RoleMapper extends MyMapper<Role> {
	
	List<Role> findUserRole(String userName);
	
	List<RoleWithMenu> findById(Long roleId);
}