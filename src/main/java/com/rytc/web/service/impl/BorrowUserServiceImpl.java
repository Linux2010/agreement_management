package com.rytc.web.service.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rytc.common.service.impl.BaseService;
import com.rytc.web.dao.BorrowUserMapper;
import com.rytc.web.domain.BorrowUser;
import com.rytc.web.service.BorrowUserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("borrowUserService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BorrowUserServiceImpl extends BaseService<BorrowUser> implements BorrowUserService {

	@Autowired
	private BorrowUserMapper borrowUserMapper;
	
	@Override
	public List<BorrowUser> findAllBorrowUser(BorrowUser borrowUser) {
		try {
			Example example = new Example(BorrowUser.class);
			Criteria criteria = example.createCriteria();
			criteria.andCondition("is_del=", "2");
			if (StringUtils.isNotBlank(borrowUser.getUserName())) {
				criteria.andCondition("user_name=", borrowUser.getUserName());
			}
			if (StringUtils.isNotBlank(borrowUser.getPhone())) {
				criteria.andCondition("phone=", borrowUser.getPhone());
			}
			if (borrowUser.getDeptId()!=null) {
				criteria.andCondition("dept_id=", borrowUser.getDeptId());
			}
			
			return this.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<BorrowUser>();
		}
	}
	@Override
	public BorrowUser findByPhone(String phone) {
		Example example = new Example(BorrowUser.class);
		example.createCriteria().andCondition("lower(phone)=", phone.toLowerCase());
		List<BorrowUser> list = this.selectByExample(example);
		if (list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void addBorrowUser(BorrowUser borrowUser) {
		borrowUser.setIsDel("2");
		this.borrowUserMapper.insert(borrowUser);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteBorrowUser(String borrowUserIds) {
		List<String> list = Arrays.asList(borrowUserIds.split(","));
		for (String borrowUserId : list) {
			BorrowUser borrowUser = this.selectByKey(Integer.parseInt(borrowUserId));
			borrowUser.setIsDel("1");
			this.updateNotNull(borrowUser);
		}
//		this.batchDelete(list, "borrowUserId", BorrowUser.class);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void updateBorrowUser(BorrowUser borrowUser) {
		
		this.updateNotNull(borrowUser);
		
	}
	@Override
	public BorrowUser findById(Integer borrowUserId) {
		
		return this.selectByKey(borrowUserId);
	}
	@Override
	public BorrowUser findByIdNum(String idNum) {
		Example example = new Example(BorrowUser.class);
		example.createCriteria().andCondition("lower(id_num)=", idNum.toLowerCase());
		List<BorrowUser> list = this.selectByExample(example);
		if (list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
		
	}
	@Override
	public List<BorrowUser> findbyUserName(String userName) {
		try {
			Example example = new Example(BorrowUser.class);
			if (StringUtils.isNotBlank(userName)) {
				example.createCriteria().andLike("userName", userName);
			}
			
			return this.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<BorrowUser>();
		}
	}
}
