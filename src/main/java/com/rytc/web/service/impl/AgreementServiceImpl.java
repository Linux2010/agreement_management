package com.rytc.web.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rytc.common.service.impl.BaseService;
import com.rytc.common.util.Constant;
import com.rytc.common.util.DcsUtils;
import com.rytc.common.util.MoneyUtils;
import com.rytc.common.util.PriceUtil;
import com.rytc.common.util.word.PactWord;
import com.rytc.system.dao.DeptMapper;
import com.rytc.system.domain.Dept;
import com.rytc.system.domain.User;
import com.rytc.web.dao.AgreementMapper;
import com.rytc.web.domain.Agreement;
import com.rytc.web.domain.BorrowUser;
import com.rytc.web.service.AgreementService;
import com.rytc.web.service.BorrowUserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("agreementService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AgreementServiceImpl extends BaseService<Agreement> implements AgreementService{
	private static Logger logger = LoggerFactory.getLogger(AgreementServiceImpl.class);

	@Autowired
	private BorrowUserService borrowUserService;
	@Autowired
	private AgreementMapper agreementMapper;
	@Autowired
	private DeptMapper deptMapper;
	@Autowired
	private ServletContext application;
	
	@Override
	public List<Agreement> findAllAgreement(Agreement agreement) {
		try {
			Example example = new Example(Agreement.class);
			Criteria criteria = example.createCriteria();
			
			if (StringUtils.isNotBlank(agreement.getType())) {
				if(StringUtils.isNotBlank(agreement.getVal())) {
					if(agreement.getType().equals("1")) {
						criteria.andCondition("agreement_no=", agreement.getVal());
					}else if(agreement.getType().equals("2")) {
						List<BorrowUser> bList = borrowUserService.findbyUserName(agreement.getVal());
						List<Integer> list = new ArrayList<>(); 
						for (BorrowUser borrowUser : bList) {
							list.add(borrowUser.getBorrowUserId());
						}
						if(list.size() > 0) {
							criteria.andIn("borrowUserId", list);
						}else {
							list.add(0);
							criteria.andIn("borrowUserId", list);
						}
					}else if(agreement.getType().equals("3")) {
						BorrowUser borrowUser = borrowUserService.findByPhone(agreement.getVal());
						if(null != borrowUser) {
							criteria.andCondition("borrow_user_id=", borrowUser.getBorrowUserId());
						}else {
							criteria.andCondition("borrow_user_id=", 0);
						}
					}if(agreement.getType().equals("4")) {
						BorrowUser borrowUser = borrowUserService.findByIdNum(agreement.getVal());
						if(null != borrowUser) {
							
							criteria.andCondition("borrow_user_id=", borrowUser.getBorrowUserId());
						}else {
							criteria.andCondition("borrow_user_id=", 0);
						}
					}
				}
			}
			if (StringUtils.isNotBlank(agreement.getTimeField())) {
				String[] timeArr = agreement.getTimeField().split("~");
				criteria.andCondition("date_format(cre_time,'%Y-%m-%d') >=", timeArr[0]);
				criteria.andCondition("date_format(cre_time,'%Y-%m-%d') <=", timeArr[1]);
			}
			return this.selectByExample(example);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Agreement>();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void addAgreement(Agreement agreement) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		agreement.setCreTime(sdf.format(new Date()));
		//计算借款金额与相关服务费
		Double borrowAmount = MoneyUtils.computeContractMoney(agreement.getApprovalAmount().toString(), agreement.getGeneralRate(), agreement.getTerm());
		agreement.setBorrowAmount(Math.floor(borrowAmount));
		this.agreementMapper.insert(agreement);
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteAgreement(List<String> list) {
		
		this.batchDelete(list, "agreementId", Agreement.class);
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void updateAgreement(Agreement agreement) {
		this.updateNotNull(agreement);
		
	}

	@Override
	public Agreement findById(Integer agreementId) {
		return this.selectByKey(agreementId);
	}

	@Override
	public void onSubmit(Integer agreementId) {
		Agreement agreement = this.selectByKey(agreementId);
		agreement.setState("1");
		
		this.updateNotNull(agreement);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void creAgreement(Agreement agreement) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		agreement.setCreTime(sdf.format(new Date()));
		//计算借款金额和相关服务费
		Double borrowAmount = MoneyUtils.computeContractMoney(agreement.getApprovalAmount().toString(), agreement.getGeneralRate(), agreement.getTerm());
		BigDecimal ba = new BigDecimal(Math.floor(borrowAmount));//借款金额
		BigDecimal aa = new BigDecimal(agreement.getApprovalAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);//审批金额
		BigDecimal totalFee = ba.subtract(aa).setScale(2, BigDecimal.ROUND_HALF_UP);//服务总费用
		//融资服务费
		BigDecimal financRate = new BigDecimal(agreement.getFinancRate());
		BigDecimal fa = financRate.divide(new BigDecimal(100));
		BigDecimal financFee = ba.multiply(fa).setScale(2, BigDecimal.ROUND_HALF_UP);
		//管理费
		BigDecimal manageFee = totalFee.subtract(financFee).setScale(2, BigDecimal.ROUND_HALF_UP);
		//获取年化利率
		double rate = MoneyUtils.getRate(agreement.getTerm());
		
		agreement.setBorrowAmount(Math.floor(borrowAmount));
		agreement.setFinancFee(financFee.doubleValue());
		agreement.setManageFee(manageFee.doubleValue());
		agreement.setRate(String.valueOf(rate));
		
		
		if(agreement.getAgreementId()!=null) {
			this.updateNotNull(agreement);
		}else {
			this.agreementMapper.insert(agreement);
		}
		//生成合同编号(服务协议编号+yyyyMMdd+协议ID4位)
		User user = (User)SecurityUtils.getSubject().getPrincipal();//获取当前登录用户信息
		Dept dept = deptMapper.selectByPrimaryKey(user.getDeptId());
		String date=sdf2.format(new Date());
		String agreementId = agreement.getAgreementId().toString();
		
		StringBuffer sb = new StringBuffer();
		sb.append(dept.getContractNumber()).append(date);
		if(agreementId.length()==1) {
			sb.append("000").append(agreementId);
		}else if(agreementId.length()==2) {
			sb.append("00").append(agreementId);
		}else if(agreementId.length()==3) {
			sb.append("0").append(agreementId);
		}else if(agreementId.length()==4) {
			sb.append(agreementId);
		}else {
			sb.append(agreementId);
		}
		agreement.setAgreementNo(sb.toString());
		this.updateNotNull(agreement);
		
		BorrowUser borrowUser = borrowUserService.findById(agreement.getBorrowUserId());
		BorrowUser borrowUser2 = borrowUserService.findById(agreement.getBorrowUserId2());
		
		String signDate = agreement.getSignDate();
    	String[] sDate = signDate.split("-");
//    	BigDecimal manageFee = new BigDecimal(agreement.getManageFee()).setScale(2, BigDecimal.ROUND_HALF_UP);
//    	BigDecimal financFee = new BigDecimal(agreement.getFinancFee()).setScale(2, BigDecimal.ROUND_HALF_UP);
//    	
//    	BigDecimal totalFee = manageFee.add(financFee).setScale(2, BigDecimal.ROUND_HALF_UP);
    	
		Map<String, Object> data = new HashMap<String, Object>();
		//TODO
		data.put("${AGREEMENT_NO}", agreement.getAgreementNo());
		data.put("${YEAR}", sDate[0]);
		data.put("${MONTH}", sDate[1]);
		data.put("${DAY}", sDate[2]);
		data.put("${ADDRESS}", dept.getAddress());
		data.put("${NAME1}", borrowUser.getUserName());
		data.put("${ID_NUM1}", borrowUser.getIdNum());
		data.put("${USER_ADDRESS1}", borrowUser.getProvince()+borrowUser.getCity()+borrowUser.getCounty()+borrowUser.getAddress());
		data.put("${PHONE1}", borrowUser.getPhone());
		if(borrowUser2 != null) {
			data.put("${NAME2}", borrowUser2.getUserName());
			data.put("${ID_NUM2}", borrowUser2.getIdNum());
			data.put("${USER_ADDRESS2}", borrowUser2.getProvince()+borrowUser2.getCity()+borrowUser2.getCounty()+borrowUser2.getAddress());
			data.put("${PHONE2}", borrowUser2.getPhone());
		}
		data.put("${UPPERCASE_AMOUNT}", PriceUtil.change(agreement.getBorrowAmount()));
		data.put("${AMOUNT}",agreement.getBorrowAmount());
		data.put("${TERM}",agreement.getTerm());
		data.put("${RATE}",agreement.getRate());
		data.put("${BORROW_USE}",agreement.getBorrowUse());
		data.put("${REPAY_OPEN_ACCT_NAME}",agreement.getRepayOpenAcctName());
		data.put("${REPAY_CARD}",agreement.getRepayCard());
		data.put("${UPPERCASE_TOTAL}",PriceUtil.change(totalFee.doubleValue()));
		data.put("${TOTAL}",totalFee);
		data.put("${MANAGER_FEE}",PriceUtil.change(manageFee.doubleValue()));
		data.put("${manager_fee}",manageFee);
		data.put("${FINANC_RATE}",financRate);
		data.put("${FINANC_FEE}",PriceUtil.change(financFee.doubleValue()));
		data.put("${financ_fee}",financFee);
		data.put("${REPAY _ACCT_NO}",agreement.getRepayAcctNo());
		data.put("${REPAY_OPEN_ACCT_BANK}",agreement.getRepayOpenAcctBank());
		
		try {
			/**
			 * 利用word模板生成word
			 */
			logger.info("协议上传路径："+Constant.FILEPATH);
			File docDir = new File(Constant.FILEPATH);
			if (!docDir.exists()) {
				docDir.mkdirs();
			}
			StringBuilder docSb = new StringBuilder();
			docSb.append(Constant.FILEPATH);
			StringBuilder docRelative = new StringBuilder();
			docRelative.append(UUID.randomUUID().toString());
			docRelative.append(".docx");
			String docPath = docSb.append(docRelative).toString();
			// String tplPath = application.getRealPath("/") + "/tpl/pact/loan.docx";
			String tplPath="";
			if(borrowUser2 == null) {
				tplPath="/doc/loan_rytc2.docx";
			}else {
				
				tplPath="/doc/loan_rytc.docx";
			}
			System.out.println("-------------------"+tplPath);
//			tplPath= AgreementServiceImpl.class. getClass().getResource("/").getPath() + tplPath;
			tplPath =application.getRealPath("/")+tplPath;
			System.out.println("-------------------"+tplPath);
			
			InputStream is = new FileInputStream(tplPath);
			FileOutputStream out = new FileOutputStream(docPath);
			PactWord.createLoanDoc2(is, out, data);
			/**
			 * word转pdf
			 */
			File pdfDir = new File(Constant.FILEPATH);
			if (!pdfDir.exists()) {
				pdfDir.mkdirs();
			}
			StringBuffer pdfSb = new StringBuffer();
			pdfSb.append(Constant.FILEPATH);
			StringBuilder pdfRelative = new StringBuilder();
			pdfRelative.append(agreement.getAgreementNo());
			pdfRelative.append(".pdf");
			String pdfPath = pdfSb.append(pdfRelative).toString();
			DcsUtils.MSToPDF(docPath, pdfPath);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		/*ByteArrayOutputStream byteArrayOutputStream = NewLoanContractUtil.getPdfIO(agreement,dept,borrowUser,borrowUser2);
		byte[] pdfFileStream = byteArrayOutputStream.toByteArray();
		
		FileUtils.uploadFile(pdfFileStream, Constant.FILEPATH, agreement.getAgreementNo()+"(2).pdf");
		String filePath="/static/img/sign/rysd/rysd.png";
        filePath= AgreementServiceImpl.class. getClass().getResource("/").getPath() + filePath;
        try {
			PDFStamperCheckMark.stamperCheckMarkPDF(Constant.FILEPATH+agreement.getAgreementNo()+"(2).pdf",Constant.FILEPATH+agreement.getAgreementNo()+".pdf",filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		agreement.setPath(Constant.SAVEFILEPATH+agreement.getAgreementNo()+".pdf");
		agreement.setFileName(agreement.getAgreementNo()+".pdf");
		this.updateNotNull(agreement);
	}
}
