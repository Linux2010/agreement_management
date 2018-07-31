package com.rytc.system.service;

import java.util.List;

import com.rytc.system.domain.UserOnline;

public interface SessionService {

	List<UserOnline> list();

	boolean forceLogout(String sessionId);
}
