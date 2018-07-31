package com.rytc.job.dao;

import java.util.List;

import com.rytc.common.config.MyMapper;
import com.rytc.job.domain.Job;

public interface JobMapper extends MyMapper<Job> {
	
	List<Job> queryList();
}