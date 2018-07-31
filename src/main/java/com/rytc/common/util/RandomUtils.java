package com.rytc.common.util;
/**
 * 获取随机数工具类
 *@author lzp
 * 2018年6月6日
 */
public class RandomUtils {
	private static final String charlist = "0123456789abcdefghigklmnopqrstuvwxyz";
	 
    public static String createRandomString(int len) {
        String str = new String();
        for (int i = 0; i < len; i++) {
            str += charlist.charAt(getRandom(charlist.length()));
        }
        return str;
    }
 
    public static int getRandom(int mod) {
        if (mod < 1) {
            return 0;
        }
        int ret = getInt() % mod;
        return ret;
    }
 
    private static int getInt() {
        int ret = Math.abs(Long.valueOf(getRandomNumString()).intValue());
        return ret;
    }
 
    private static String getRandomNumString() {
        double d = Math.random();
        String dStr = String.valueOf(d).replaceAll("[^\\d]", "");
        if (dStr.length() > 1) {
            dStr = dStr.substring(0, dStr.length() - 1);
        }
        return dStr;
    }
    public static void main(String[] args) {
    	String s = RandomUtils.createRandomString(36);
    	System.out.println(s);
    	System.out.println(s.length());
	}
}
