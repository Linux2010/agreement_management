package com.rytc.system.service;

import java.util.List;

import com.rytc.common.service.IService;
import com.rytc.system.domain.SysLog;

public interface LogService extends IService<SysLog> {
	
	List<SysLog> findAllLogs(SysLog log);
	
	void deleteLogs(String logIds);
}
