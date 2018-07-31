package com.rytc.system.service;

import com.rytc.common.service.IService;
import com.rytc.system.domain.UserRole;

public interface UserRoleService extends IService<UserRole> {

	void deleteUserRolesByRoleId(String roleIds);

	void deleteUserRolesByUserId(String userIds);
}
