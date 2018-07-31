package com.rytc.web.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
import com.rytc.web.domain.Agreement;
import com.rytc.web.domain.BorrowUser;
import com.rytc.web.service.AgreementService;
import com.rytc.web.service.BorrowUserService;
/**
 * 协议的controller
 *@author lzp
 * 2018年7月17日
 */
@Controller
public class AgreementController extends BaseController {
	@Autowired
	private AgreementService agreementService;
	@Autowired
	private BorrowUserService borrowUserService;

	@Log("获取协议信息")
	@RequestMapping("agreement")
	@RequiresPermissions("agreement:list")
	public String index() {
		return "web/agreement/agreement";
	}

	@RequestMapping("agreement/list")
	@ResponseBody
	public Map<String, Object> borrowUserList(QueryRequest request, Agreement agreement) {
		PageHelper.startPage(request.getPageNum(), request.getPageSize());
		List<Agreement> list = this.agreementService.findAllAgreement(agreement);
		//处理借款人信息
		for (Agreement am : list) {
			BorrowUser borrowUser = borrowUserService.findById(am.getBorrowUserId());
			if(borrowUser != null) {
				am.setUserName(borrowUser.getUserName());
				am.setPhone(borrowUser.getPhone());
				am.setIdNum(borrowUser.getIdNum());
			}
		}
		PageInfo<Agreement> pageInfo = new PageInfo<Agreement>(list);
		return getDataTable(pageInfo);
	}
	
	@RequestMapping("agreement/excel")
	@ResponseBody
	public ResponseBo agreementExcel(Agreement agreement) {
		try {
			List<Agreement> list = this.agreementService.findAllAgreement(agreement);
			//处理借款人信息
			for (Agreement am : list) {
				BorrowUser borrowUser = borrowUserService.findById(am.getBorrowUserId());
				am.setUserName(borrowUser.getUserName());
				am.setPhone(borrowUser.getPhone());
				am.setIdNum(borrowUser.getIdNum());
			}
			return FileUtils.createExcelByPOIKit("协议信息表", list, Agreement.class);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("导出Excel失败，请联系网站管理员！");
		}
	}

	@RequestMapping("agreement/csv")
	@ResponseBody
	public ResponseBo borrowUserCsv(Agreement agreement){
		try {
			List<Agreement> list = this.agreementService.findAllAgreement(agreement);
			//处理借款人信息
			for (Agreement am : list) {
				BorrowUser borrowUser = borrowUserService.findById(am.getBorrowUserId());
				am.setUserName(borrowUser.getUserName());
				am.setPhone(borrowUser.getPhone());
				am.setIdNum(borrowUser.getIdNum());
			}
			return FileUtils.createCsv("协议信息表", list, Agreement.class);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("导出Csv失败，请联系网站管理员！");
		}
	}
	
	@Log("新增协议 ")
	@RequiresPermissions("agreement:add")
	@RequestMapping("agreement/add")
	@ResponseBody
	public ResponseBo addAgreement(Agreement agreement) {
		try {
			agreementService.addAgreement(agreement); 
			return ResponseBo.ok("新增协议成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("新增协议失败，请联系网站管理员！");
		}
	}

	@Log("删除协议")
	@RequiresPermissions("agreement:delete")
	@RequestMapping("agreement/delete")
	@ResponseBody
	public ResponseBo deleteAgreement(String ids) {
		try {
			List<String> list = Arrays.asList(ids.split(","));
			for (String s : list) {
				Agreement agreement = agreementService.findById(Integer.parseInt(s));
				if(agreement.getAgreementNo() != null) {
					
					return ResponseBo.error("勾选协议中存在已生成的协议，不可删除！！！");
				}
			}
			agreementService.deleteAgreement(list);
			return ResponseBo.ok("删除协议成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("删除协议失败，请联系网站管理员！");
		}
	}

	@Log("修改协议 ")
	@RequiresPermissions("agreement:update")
	@RequestMapping("agreement/update")
	@ResponseBody
	public ResponseBo updateAgreement(Agreement agreement) {
		try {
			agreementService.updateAgreement(agreement);
			return ResponseBo.ok("修改协议成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("修改协议失败，请联系网站管理员！");
		}
	}
	@RequestMapping("agreement/getAgreement")
	@ResponseBody
	public ResponseBo getAgreement(Integer agreementId) {
		try {
			Agreement agreement = this.agreementService.findById(agreementId);
			if(agreement.getAgreementNo()!=null) {
				return ResponseBo.error("协议已生成不可修改");
			}
			if(agreement.getState()=="1") {
				return ResponseBo.error("已提交的协议不可修改");
			}
			return ResponseBo.ok(agreement);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("获取协议信息失败，请联系网站管理员！");
		}
	}
	/**
	 * 提交
	 * @param agreementId
	 * @return
	 */
	@RequestMapping("agreement/onSubmit")
	@ResponseBody
	public ResponseBo onSubmit(Integer id) {
		try {
			this.agreementService.onSubmit(id);
			return ResponseBo.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("获取协议信息失败，请联系网站管理员！");
		}
	}
	@Log("生成协议 ")
	@RequestMapping("agreement/creAgreement")
	@ResponseBody
	public ResponseBo creAgreement(Agreement agreement) {
		try {
			agreementService.creAgreement(agreement);
			return ResponseBo.ok("生成协议成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("生成协议失败，请联系网站管理员！");
		}
	}
	/**
	 * 预览
	 * @param agreementId
	 * @return
	 */
	@RequestMapping("agreement/check")
	@ResponseBody
	public ResponseBo check(Integer id) {
		try {
			Agreement agreement = this.agreementService.findById(id);
			return ResponseBo.ok(agreement.getPath());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseBo.error("预览协议信息失败，请联系网站管理员！");
		}
	}
	/**
	 * 导出
	 * @param agreementId
	 * @return
	 */
	@RequestMapping("agreement/exportPDF")
	public void exportPDF(Integer id,HttpServletResponse response) {
		try {
			Agreement agreement = this.agreementService.findById(id);
			FileUtils.downloadFile(agreement.getFileName(), response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
