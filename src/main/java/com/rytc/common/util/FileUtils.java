package com.rytc.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.util.ResourceUtils;

import com.rytc.common.domain.ResponseBo;

public class FileUtils {

	/**
	 * 文件名加UUID
	 * 
	 * @param filename
	 * @return
	 */
	public static String makeFileName(String filename) {
		return UUID.randomUUID().toString() + "_" + filename;
	}

	/**
	 * 文件名特殊字符过滤
	 * 
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static String StringFilter(String str) throws PatternSyntaxException {
		String regEx = "[`~!@#$%^&*+=|{}':; ',//[//]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * 生成Excel文件
	 * 
	 * @param filename
	 * @param list
	 * @param clazz
	 * @return
	 */
	public static ResponseBo createExcelByPOIKit(String filename, List<?> list, Class<?> clazz) {

		if (list.isEmpty()) {
			return ResponseBo.warn("导出数据为空！");
		} else {
			boolean operateSign = false;
			String fileName = filename + ".xlsx";
			fileName = makeFileName(fileName);
			try {
				String path = ResourceUtils.getURL("classpath:").getPath() + "static/file/" + fileName;
				FileOutputStream fos = null;
				fos = new FileOutputStream(path);
				operateSign = ExcelUtils.$Builder(clazz)
						// 设置每个sheet的最大记录数,默认为10000,可不设置
						// .setMaxSheetRecords(10000)
						.toExcel(list, "查询结果", fos);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (operateSign) {
				return ResponseBo.ok(fileName);
			} else {
				return ResponseBo.error("导出Excel失败，请联系网站管理员！");
			}
		}
	}
	
	/**
	 * 生成Csv文件
	 * @param filename
	 * @param list
	 * @param clazz
	 * @return
	 */
	public static ResponseBo createCsv(String filename, List<?> list, Class<?> clazz) {

		if (list.isEmpty()) {
			return ResponseBo.warn("导出数据为空！");
		} else {
			boolean operateSign = false;
			String fileName = filename + ".csv";
			fileName = makeFileName(fileName);
			try {
				String path = ResourceUtils.getURL("classpath:").getPath() + "static/file/" + fileName;
				operateSign = ExcelUtils.$Builder(clazz)
						.toCsv(list, path);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (operateSign) {
				return ResponseBo.ok(fileName);
			} else {
				return ResponseBo.error("导出Csv失败，请联系网站管理员！");
			}
		}
	}
	
    /**
     * 上传图片
     * @param file 文件数组
     * @param filePath 存储路径
     * @param fileName 图片名
     * @throws Exception
     */
    public static void uploadFile(byte[] file, String filePath, String fileName){
    	try {
    		File targetFile = new File(filePath); 
        	if(!targetFile.exists()){ 
        	targetFile.mkdirs(); 
        	} 
        	FileOutputStream out = new FileOutputStream(filePath+fileName);
        	out.write(file);
        	out.flush();
        	out.close();
    	}catch (Exception e) {
    		e.printStackTrace();
    		ResponseBo.error("上传图片失败，请联系网站管理员！");
		}
    	
    }
    /**
     * 文件下载
     * @param fileName 文件名
     * @param response
     * @param request
     */
    public static void downloadFile(String fileName, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition","attachment;filename=" + java.net.URLEncoder.encode(fileName, "utf-8"));
			File file = new File(Constant.FILEPATH+fileName);
			InputStream inputStream = new FileInputStream(file);
			OutputStream os = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}
			os.close();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    /**
	 * 
	 * 删除单个文件
	 * 
	 * @param fileName 被删除的文件名
	 * @return 如果删除成功，则返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	public static String getExtension(File file) {
		String extension = "";
		if (file.isDirectory()) {
			extension = "dir";
		} else {
			String lowerFileName = file.getName().toLowerCase();
			extension = lowerFileName.substring(lowerFileName.lastIndexOf(".") + 1);
			if (lowerFileName.equals(extension)) {
				extension = "";
			} else if (lowerFileName.endsWith("tar.gz")) {
				extension = "tar.gz";
			} else if (lowerFileName.endsWith("tar.bz2")) {
				extension = "tar.bz2";
			}
		}

		return extension;
	}
}
