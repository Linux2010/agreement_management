package com.rytc.web.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rytc.common.annotation.Log;
import com.rytc.common.controller.BaseController;
import com.rytc.common.domain.QueryRequest;
import com.rytc.common.domain.ResponseBo;
import com.rytc.common.util.FileUtils;
import com.rytc.system.domain.User;
import com.rytc.web.domain.BorrowUser;
import com.rytc.web.service.BorrowUserService;
/**
 * 借款人的controller
 * 
 *@author lzp
 * 2018年7月17日
 */
@Controller
public class BorrowUserController extends BaseController{

	@Autowired
	private BorrowUserService borrowUserService;

	@Log("获取借款人信息")
	@RequestMapping("borrowUser")
	@RequiresPermissions("borrowUser:list")
	public String index() {
		return "web/borrowUser/borrowUser";
	}

	@RequestMapping("borrowUser/list")
	@ResponseBody
	public Map<String, Object> borrowUserList(QueryRequest request, BorrowUser borrowUser) {
		//获取当前登录用户信息
		User user = (User)SecurityUtils.getSubject().getPrincipal();
		borrowUser.setDeptId(user.getDeptId());
		
		PageHelper.startPage(request.getPageNum(), request.getPageSize());
		List<BorrowUser> list = this.borrowUserService.findAllBorrowUser(borrowUser);
		PageInfo<BorrowUser> pageInfo = new PageInfo<BorrowUser>(list);
		return getDataTable(pageInfo);
	}
	
	@RequestMapping("borrowUser/excel")
	@ResponseBody
	public ResponseBo borrowUserExcel(BorrowUser borrowUser) {
		try {
			List<BorrowUser> list = this.borrowUserService.findAllBorrowUser(borrowUser);
			return FileUtils.createExcelByPOIKit("借款人信息表", list, BorrowUser.class);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("borrowUser/csv")
	@ResponseBody
	public ResponseBo borrowUserCsv(BorrowUser borrowUser){
		try {
			List<BorrowUser> list = this.borrowUserService.findAllBorrowUser(borrowUser);
			return FileUtils.createCsv("借款人信息表", list, BorrowUser.class);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}
	@RequestMapping("borrowUser/checkPhone")
	@ResponseBody
	public boolean checkPhone(String phone, String oldphone) {
		if (StringUtils.isNotBlank(oldphone) && phone.equalsIgnoreCase(oldphone)) {
			return true;
		}
		BorrowUser result = this.borrowUserService.findByPhone(phone);
		if (result != null)
			return false;
		return true;
	}
	@Log("新增借款人 ")
	@RequiresPermissions("borrowUser:add")
	@RequestMapping("borrowUser/add")
	@ResponseBody
	public ResponseBo addBorrowUser(BorrowUser borrowUser) {
		try {
			//获取当前登录用户信息
			User user = (User)SecurityUtils.getSubject().getPrincipal();
			borrowUser.setDeptId(user.getDeptId());
			this.borrowUserService.addBorrowUser(borrowUser);
			return ResponseBo.ok("新增借款人成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("新增借款人失败，请联系网站管理员！");
		}
	}

	@Log("删除借款人")
	@RequiresPermissions("borrowUser:delete")
	@RequestMapping("borrowUser/delete")
	@ResponseBody
	public ResponseBo deleteBorrowUser(String ids) {
		try {
			this.borrowUserService.deleteBorrowUser(ids);
			return ResponseBo.ok("删除借款人成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("删除借款人失败，请联系网站管理员！");
		}
	}

	@Log("修改借款人 ")
	@RequiresPermissions("borrowUser:update")
	@RequestMapping("borrowUser/update")
	@ResponseBody
	public ResponseBo updateBorrowUser(BorrowUser borrowUser) {
		try {
			this.borrowUserService.updateBorrowUser(borrowUser);
			return ResponseBo.ok("修改借款人成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("修改借款人失败，请联系网站管理员！");
		}
	}
	@RequestMapping("borrowUser/getBorrowUser")
	@ResponseBody
	public ResponseBo getBorrowUser(Integer borrowUserId) {
		try {
			BorrowUser borrowUser = this.borrowUserService.findById(borrowUserId);
			return ResponseBo.ok(borrowUser);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("获取借款人信息失败，请联系网站管理员！");
		}
	}
}
