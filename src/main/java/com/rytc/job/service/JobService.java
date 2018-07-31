package com.rytc.job.service;

import java.util.List;

import com.rytc.common.service.IService;
import com.rytc.job.domain.Job;

public interface JobService extends IService<Job>{
	
	Job findJob(Long jobId);

	List<Job> findAllJobs(Job job);

	void addJob(Job job);

	void updateJob(Job job);

	void deleteBatch(String jobIds);

	int updateBatch(String jobIds, String status);

	void run(String jobIds);

	void pause(String jobIds);

	void resume(String jobIds);
}
