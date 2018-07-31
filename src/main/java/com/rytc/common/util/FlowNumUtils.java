package com.rytc.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FlowNumUtils {

	public static String getFlowNum(String mac) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		String flownum = df.format(new Date())+mac+RandomUtils.createRandomString(8);
		return flownum;
	}
}
