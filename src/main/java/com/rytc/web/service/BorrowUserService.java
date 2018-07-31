package com.rytc.web.service;

import java.util.List;

import com.rytc.common.service.IService;
import com.rytc.web.domain.BorrowUser;

public interface BorrowUserService extends IService<BorrowUser>{

	List<BorrowUser> findAllBorrowUser(BorrowUser borrowUser);

	BorrowUser findByPhone(String phone);

	void addBorrowUser(BorrowUser borrowUser);

	void deleteBorrowUser(String ids);

	void updateBorrowUser(BorrowUser borrowUser);

	BorrowUser findById(Integer borrowUserId);

	BorrowUser findByIdNum(String idNum);

	List<BorrowUser> findbyUserName(String userName);

}
