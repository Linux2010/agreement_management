package com.rytc.common.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.rytc.web.domain.RateConstants;
/**
 * 借款金额相关计算
 * 
 *@author lzp
 * 2018年7月24日
 */
public class MoneyUtils {
	private static final Map<Integer, RateConstants> 	RATEMAP=new HashMap<>();
	static {
//		RATEMAP.put(6, new RateConstants(6,"0.085","0.00415684"));
//		RATEMAP.put(7, new RateConstants(7,"0.085","0.00407631"));
//		RATEMAP.put(8, new RateConstants(8,"0.085","0.00401718"));
//		RATEMAP.put(9, new RateConstants(9,"0.085","0.00397231"));
//		RATEMAP.put(10, new RateConstants(10,"0.085","0.0393708"));
//		RATEMAP.put(11, new RateConstants(11,"0.085","0.00390916"));
//		RATEMAP.put(12, new RateConstants(12,"0.1","0.00458282"));
//		RATEMAP.put(13, new RateConstants(13,"0.1","0.00456170"));
//		RATEMAP.put(14, new RateConstants(14,"0.1","0.00454499"));
//		RATEMAP.put(15, new RateConstants(15,"0.1","0.00453100"));
//		RATEMAP.put(16, new RateConstants(16,"0.1","0.00451890"));
//		RATEMAP.put(17, new RateConstants(17,"0.1","0.00450977"));
//		RATEMAP.put(18, new RateConstants(18,"0.1","0.00450194"));
//		RATEMAP.put(24, new RateConstants(24,"0.114","0.00512757"));
//		RATEMAP.put(36, new RateConstants(36,"0.13","0.00591667"));
		RATEMAP.put(6, new RateConstants(6,"0.07","0.00341927"));
		RATEMAP.put(12, new RateConstants(12,"0.08","0.00365510"));
		RATEMAP.put(18, new RateConstants(18,"0.1","0.00450152"));
	}
	
	/**
	 * 借款金额
	 * @param auditMoney 审批金额
	 * @param generalRate 综合费率
	 * @param loanExpiry 期数
	 * @return
	 */
	public static double computeContractMoney(String auditMoney, String generalRate, int loanExpiry) {
		BigDecimal actRate=new BigDecimal(RATEMAP.get(loanExpiry).getActRate());
		//融资金额=（审批金额*(1+期数*综合费率)）/(1+活动费率*期数)
		BigDecimal money1=new BigDecimal(1).add(new BigDecimal(loanExpiry).multiply(new BigDecimal(generalRate)).divide(new BigDecimal(100)));
		money1=money1.multiply(new BigDecimal(auditMoney));
		
		BigDecimal money2=new BigDecimal(1).add(new BigDecimal(loanExpiry).multiply(actRate));
		return money1.divide(money2, 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
				
	}
	/**
	 * 获取年化利率
	 * @param loanExpiry 期数
	 * @return
	 */
	public static double getRate(int loanExpiry) {
		BigDecimal rate = new BigDecimal(RATEMAP.get(loanExpiry).getArp());
		BigDecimal r = rate.multiply(new BigDecimal(100));
		return r.doubleValue();
	}
}
