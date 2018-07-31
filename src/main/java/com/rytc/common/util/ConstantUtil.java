package com.rytc.common.util;
/**
 * 
 * @author daichongqing
 *
 *  2018年7月12日
 */

import java.util.Properties;

public class ConstantUtil {
	private static final String PPS_FILE = "/resources/Constant.properties";

	private static Properties pps;

	public static String get(String key, boolean refresh) {
		if (pps == null || refresh) {
			pps = PropertiesUtil.getProperties(PPS_FILE, true);
		}
		return pps.getProperty(key);
	}

	public static String get(String key) {
		return get(key, false);
	}
	
	public static void main(String[] args) {
		System.out.println(ConstantUtil.get("FILEPATH"));
		System.out.println(ConstantUtil.get("SAVEFILEPATH"));
	}
}
