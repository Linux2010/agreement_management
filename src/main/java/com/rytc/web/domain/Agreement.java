package com.rytc.web.domain;

import javax.persistence.*;

import com.rytc.common.annotation.ExportConfig;

@Table(name = "tb_agreement")
public class Agreement {
	
	
	@Transient
	private String type;
	@Transient
	private String val;
	@Transient
	private String timeField;
	@Transient
	private Double totalFee;
    /**
     * 主键
     */
    @Id
    @Column(name = "agreement_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer agreementId;

    /**
     * 协议编号
     */
    @Column(name = "agreement_no")
    @ExportConfig(value = "协议编号")
    private String agreementNo;

    @Transient
	@ExportConfig(value = "姓名")
	private String userName;
	@Transient
	@ExportConfig(value = "手机号")
	private String phone;
	@Transient
	@ExportConfig(value = "身份证号")
	private String idNum;
    
    /**
     * 借款金额
     */
    @Column(name = "borrow_amount")
    @ExportConfig(value = "借款金额")
    private Double borrowAmount;

    /**
     * 借款年利率
     */
    private String rate;

    /**
     * 借款期限
     */
    private Integer term;

    /**
     * 管理费
     */
    @Column(name = "manage_fee")
    private Double manageFee;

    /**
     * 融资服务
     */
    @Column(name = "financ_fee")
    private Double financFee;

    /**
     * 借款用途
     */
    @Column(name = "borrow_use")
    private String borrowUse;

    /**
     * 共同借款人ID
     */
    @Column(name = "borrow_user_id2")
    private Integer borrowUserId2;

    /**
     * 借款人ID
     */
    @Column(name = "borrow_user_id")
    private Integer borrowUserId;

    /**
     * 还款账号开户名
     */
    @Column(name = "repay_open_acct_name")
    private String repayOpenAcctName;

    /**
     * 还款账号
     */
    @Column(name = "repay_acct_no")
    private String repayAcctNo;

    /**
     * 还款账号开户行
     */
    @Column(name = "repay_open_acct_bank")
    private String repayOpenAcctBank;

    /**
     * 指定还款人身份证号
     */
    @Column(name = "repay_card")
    private String repayCard;

    /**
     * 签订日期
     */
    @Column(name = "sign_date")
    private String signDate;

    /**
     * 创建时间
     */
    @Column(name = "cre_time")
    @ExportConfig(value = "创建时间", convert = "c:com.rytc.common.util.poi.convert.TimeConvert")
    private String creTime;

    /**
     * 状态：1：已提交2：未提交
     */
    private String state;

    /**
     * 融资服务费率
     */
    @Column(name = "financ_rate")
    private Double financRate;

    /**
     * 协议保存路径
     */
    private String path;

    /**
     * 文件名称
     */
    @Column(name = "file_name")
    private String fileName;
    /**
     * 审批金额
     */
    @Column(name = "approval_amount")
    @ExportConfig(value = "审批金额")
    private Double approvalAmount;
    /**
     * 综合费率
     */
    @Column(name = "general_rate")
    private String generalRate;

    /**
     * 获取主键
     *
     * @return agreement_id - 主键
     */
    public Integer getAgreementId() {
        return agreementId;
    }

    /**
     * 设置主键
     *
     * @param agreementId 主键
     */
    public void setAgreementId(Integer agreementId) {
        this.agreementId = agreementId;
    }

    /**
     * 获取协议编号
     *
     * @return agreement_no - 协议编号
     */
    public String getAgreementNo() {
        return agreementNo;
    }

    /**
     * 设置协议编号
     *
     * @param agreementNo 协议编号
     */
    public void setAgreementNo(String agreementNo) {
        this.agreementNo = agreementNo == null ? null : agreementNo.trim();
    }

    /**
     * 获取借款金额
     *
     * @return borrow_amount - 借款金额
     */
    public Double getBorrowAmount() {
        return borrowAmount;
    }

    /**
     * 设置借款金额
     *
     * @param borrowAmount 借款金额
     */
    public void setBorrowAmount(Double borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    /**
     * 获取借款年利率
     *
     * @return rate - 借款年利率
     */
    public String getRate() {
        return rate;
    }

    /**
     * 设置借款年利率
     *
     * @param rate 借款年利率
     */
    public void setRate(String rate) {
        this.rate = rate == null ? null : rate.trim();
    }

    /**
     * 获取借款期限
     *
     * @return term - 借款期限
     */
    public Integer getTerm() {
        return term;
    }

    /**
     * 设置借款期限
     *
     * @param term 借款期限
     */
    public void setTerm(Integer term) {
        this.term = term;
    }

    /**
     * 获取管理费
     *
     * @return manage_fee - 管理费
     */
    public Double getManageFee() {
        return manageFee;
    }

    /**
     * 设置管理费
     *
     * @param manageFee 管理费
     */
    public void setManageFee(Double manageFee) {
        this.manageFee = manageFee;
    }

    /**
     * 获取融资服务
     *
     * @return financ_fee - 融资服务
     */
    public Double getFinancFee() {
        return financFee;
    }

    /**
     * 设置融资服务
     *
     * @param financFee 融资服务
     */
    public void setFinancFee(Double financFee) {
        this.financFee = financFee;
    }

    /**
     * 获取借款用途
     *
     * @return borrow_use - 借款用途
     */
    public String getBorrowUse() {
        return borrowUse;
    }

    /**
     * 设置借款用途
     *
     * @param borrowUse 借款用途
     */
    public void setBorrowUse(String borrowUse) {
        this.borrowUse = borrowUse == null ? null : borrowUse.trim();
    }

    /**
     * 获取共同借款人ID
     *
     * @return borrow_user_id2 - 共同借款人ID
     */
    public Integer getBorrowUserId2() {
        return borrowUserId2;
    }

    /**
     * 设置共同借款人ID
     *
     * @param borrowUserId2 共同借款人ID
     */
    public void setBorrowUserId2(Integer borrowUserId2) {
        this.borrowUserId2 = borrowUserId2;
    }

    /**
     * 获取借款人ID
     *
     * @return borrow_user_id - 借款人ID
     */
    public Integer getBorrowUserId() {
        return borrowUserId;
    }

    /**
     * 设置借款人ID
     *
     * @param borrowUserId 借款人ID
     */
    public void setBorrowUserId(Integer borrowUserId) {
        this.borrowUserId = borrowUserId;
    }

    /**
     * 获取还款账号开户名
     *
     * @return repay_open_acct_name - 还款账号开户名
     */
    public String getRepayOpenAcctName() {
        return repayOpenAcctName;
    }

    /**
     * 设置还款账号开户名
     *
     * @param repayOpenAcctName 还款账号开户名
     */
    public void setRepayOpenAcctName(String repayOpenAcctName) {
        this.repayOpenAcctName = repayOpenAcctName == null ? null : repayOpenAcctName.trim();
    }

    /**
     * 获取还款账号
     *
     * @return repay_acct_no - 还款账号
     */
    public String getRepayAcctNo() {
        return repayAcctNo;
    }

    /**
     * 设置还款账号
     *
     * @param repayAcctNo 还款账号
     */
    public void setRepayAcctNo(String repayAcctNo) {
        this.repayAcctNo = repayAcctNo;
    }

    /**
     * 获取还款账号开户行
     *
     * @return repay_open_acct_bank - 还款账号开户行
     */
    public String getRepayOpenAcctBank() {
        return repayOpenAcctBank;
    }

    /**
     * 设置还款账号开户行
     *
     * @param repayOpenAcctBank 还款账号开户行
     */
    public void setRepayOpenAcctBank(String repayOpenAcctBank) {
        this.repayOpenAcctBank = repayOpenAcctBank == null ? null : repayOpenAcctBank.trim();
    }

    /**
     * 获取指定还款人身份证号
     *
     * @return repay_card - 指定还款人身份证号
     */
    public String getRepayCard() {
        return repayCard;
    }

    /**
     * 设置指定还款人身份证号
     *
     * @param repayCard 指定还款人身份证号
     */
    public void setRepayCard(String repayCard) {
        this.repayCard = repayCard;
    }

    /**
     * 获取签订日期
     *
     * @return sign_date - 签订日期
     */
    public String getSignDate() {
        return signDate;
    }

    /**
     * 设置签订日期
     *
     * @param signDate 签订日期
     */
    public void setSignDate(String signDate) {
        this.signDate = signDate == null ? null : signDate.trim();
    }

    /**
     * 获取创建时间
     *
     * @return cre_time - 创建时间
     */
    public String getCreTime() {
        return creTime;
    }

    /**
     * 设置创建时间
     *
     * @param creTime 创建时间
     */
    public void setCreTime(String creTime) {
        this.creTime = creTime == null ? null : creTime.trim();
    }

    /**
     * 获取状态：1：已提交2：未提交
     *
     * @return state - 状态：1：已提交2：未提交
     */
    public String getState() {
        return state;
    }

    /**
     * 设置状态：1：已提交2：未提交
     *
     * @param state 状态：1：已提交2：未提交
     */
    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    /**
     * 获取融资服务费率
     *
     * @return financ_rate - 融资服务费率
     */
    public Double getFinancRate() {
        return financRate;
    }

    /**
     * 设置融资服务费率
     *
     * @param financRate 融资服务费率
     */
    public void setFinancRate(Double financRate) {
        this.financRate = financRate;
    }

    /**
     * 获取协议保存路径
     *
     * @return path - 协议保存路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置协议保存路径
     *
     * @param path 协议保存路径
     */
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    /**
     * 获取文件名称
     *
     * @return file_name - 文件名称
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置文件名称
     *
     * @param fileName 文件名称
     */
    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getTimeField() {
		return timeField;
	}

	public void setTimeField(String timeField) {
		this.timeField = timeField;
	}

	public Double getApprovalAmount() {
		return approvalAmount;
	}

	public void setApprovalAmount(Double approvalAmount) {
		this.approvalAmount = approvalAmount;
	}

	public String getGeneralRate() {
		return generalRate;
	}

	public void setGeneralRate(String generalRate) {
		this.generalRate = generalRate;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}
    
}