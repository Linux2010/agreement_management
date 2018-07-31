package com.rytc.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 
 * @author daichongqing
 *
 *         2018年7月12日
 */
public class PropertiesUtil {
	private static Map<String, Properties> propMap = null;

	private static void loadProperties(String filePath) {
		try {
			Properties properties = new Properties();
			filePath = Class.class.getClass().getResource("/").getPath() + filePath;
			
			File f = new File(filePath);
			if (f.exists()) {
				InputStream in = new BufferedInputStream(new FileInputStream(f));
				properties.load(new InputStreamReader(in, "utf-8"));
				if (propMap == null) {
					propMap = new HashMap<String, Properties>();
					propMap.put(getFileName(filePath), properties);
				}
			} else {
				throw new IOException("File not exists!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getFileName(String filePath) {
		File tempFile = new File(filePath.trim());
		String fileName = tempFile.getName();
		return fileName.substring(0, fileName.indexOf("."));
	}

	/**
	 * 获取项目中properties文档内容的静态方法
	 * 
	 * @param filePath
	 *            文档的相对路径（"/resources/xxx.properties"）
	 * @param refresh
	 *            是否强制刷新静态文档
	 * @return
	 */
	public static Properties getProperties(String filePath, boolean refresh) {
		if (propMap == null || !propMap.containsKey(filePath) || refresh) {
			loadProperties(filePath);
		}
		return propMap.get(getFileName(filePath));
	}

	/**
	 * 获取项目中properties文档的value值内容的静态方法
	 * 
	 * @param filePath
	 *            文档的相对路径（"/resources/xxx.properties"）
	 * @param key
	 *            Properties的key值
	 * @return
	 */
	public static String getVal(String filePath, String key) {
		if (propMap == null || !propMap.containsKey(getFileName(filePath))) {
			loadProperties(filePath);
		}
		return propMap.get(getFileName(filePath)).getProperty(key);
	}

	public static void main(String[] args) {
		String filePath = "/resources/Constant.properties";
		Properties properties = getProperties(filePath, false);
		Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();// 返回的属性键值对实体
		for (Map.Entry<Object, Object> entry : entrySet) {
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}
	}

}
