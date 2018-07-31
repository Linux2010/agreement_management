package com.rytc.web.domain;

import javax.persistence.*;

import com.rytc.common.annotation.ExportConfig;

@Table(name = "tb_borrow_user")
public class BorrowUser {
    /**
     * 主键
     */
    @Id
    @Column(name = "borrow_user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer borrowUserId;

    /**
     * 借款人姓名
     */
    @ExportConfig(value = "姓名")
    @Column(name = "user_name")
    private String userName;

    /**
     * 手机号
     */
    @ExportConfig(value = "手机号")
    private String phone;

    /**
     * 身份证号
     */
    @Column(name = "id_num")
    @ExportConfig(value = "身份证号")
    private String idNum;

    /**
     * 省
     */
    @Column(name = "province")
    @ExportConfig(value = "省")
    private String province;

    /**
     * 市
     */
    @Column(name = "city")
    @ExportConfig(value = "市")
    private String city;

    /**
     * 区/县
     */
    @Column(name = "county")
    @ExportConfig(value = "区/县")
    private String county;

    /**
     * 详细地址
     */
    @Column(name = "address")
    @ExportConfig(value = "详细地址")
    private String address;
    /**
     * 是否删除
     */
    @Column(name = "is_del")
    private String isDel;

    /**
     * 部门ID
     */
    @Column(name = "dept_id")
    private Long deptId;

    /**
     * 获取主键
     *
     * @return borrow_user_id - 主键
     */
    public Integer getBorrowUserId() {
        return borrowUserId;
    }

    /**
     * 设置主键
     *
     * @param borrowUserId 主键
     */
    public void setBorrowUserId(Integer borrowUserId) {
        this.borrowUserId = borrowUserId;
    }

    /**
     * 获取借款人姓名
     *
     * @return user_name - 借款人姓名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置借款人姓名
     *
     * @param userName 借款人姓名
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 获取手机号
     *
     * @return phone - 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号
     *
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取身份证号
     *
     * @return id_num - 身份证号
     */
    public String getIdNum() {
        return idNum;
    }

    /**
     * 设置身份证号
     *
     * @param idNum 身份证号
     */
    public void setIdNum(String idNum) {
        this.idNum = idNum == null ? null : idNum.trim();
    }

    /**
     * 获取省
     *
     * @return province - 省
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省
     *
     * @param province 省
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * 获取市
     *
     * @return city - 市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置市
     *
     * @param city 市
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * 获取区/县
     *
     * @return county - 区/县
     */
    public String getCounty() {
        return county;
    }

    /**
     * 设置区/县
     *
     * @param county 区/县
     */
    public void setCounty(String county) {
        this.county = county == null ? null : county.trim();
    }

    /**
     * 获取详细地址
     *
     * @return address - 详细地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置详细地址
     *
     * @param address 详细地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 获取部门ID
     *
     * @return dept_id - 部门ID
     */
    public Long getDeptId() {
        return deptId;
    }

    /**
     * 设置部门ID
     *
     * @param deptId 部门ID
     */
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}
    
}