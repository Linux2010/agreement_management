package com.rytc.job.service;

import java.util.List;

import com.rytc.common.service.IService;
import com.rytc.job.domain.JobLog;

public interface JobLogService extends IService<JobLog>{

	List<JobLog> findAllJobLogs(JobLog jobLog);

	void saveJobLog(JobLog log);
	
	void deleteBatch(String jobLogIds);
}
