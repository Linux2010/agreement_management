package com.rytc.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import application.dcs.Convert;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.rytc.common.util.doc.DcsPool;
import com.rytc.common.util.doc.PoolFactory;

public class DcsUtils {
	private static DcsPool dcsPool = PoolFactory.createDcsPool();

	/**
	 * 
	 * <b>方法：</b>MSToPDF<br>
	 * <b>描述：</b>WORD、EXCEL、PPT转PDF<br>
	 * <b>作者：</b>xudi<br>
	 * <b>时间：</b>2017-01-21 23:23:43<br>
	 * 
	 * @param sourcePath
	 * @param targetPath
	 * @throws Exception
	 * 
	 */
	public static void MSToPDF(String sourcePath, String targetPath) throws Exception {
		Convert convert = dcsPool.borrowObject();
		File file = new File(sourcePath);
		String fileTile = file.getName().replace("." + FileUtils.getExtension(file), "");
		//发布环境
//		String tempPdfUrl ="d:\\temp\\dcs\\pdf\\"+ UUID.randomUUID().toString().replaceAll("-", "") + ".pdf";
		String tempPdfUrl =Constant.FILEPATH+UUID.randomUUID().toString().replaceAll("-", "") + ".pdf";
		convert.setFilePermission(true);
		convert.convertMStoPDF(sourcePath, tempPdfUrl);
		dcsPool.returnObject(convert);

		// 修改PDF文档属性
		PdfReader reader = new PdfReader(tempPdfUrl);
		Document document = new Document(reader.getPageSize(1));
		PdfCopy copy = new PdfCopy(document, new FileOutputStream(targetPath));
		document.addTitle(fileTile);
		document.addAuthor("应用系统");
		document.addCreator("应用系统");
		document.open();
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			document.newPage();
			PdfImportedPage page = copy.getImportedPage(reader, i);
			copy.addPage(page);
		}
		document.close();
		reader.close();
		System.out.println(tempPdfUrl);
		boolean pdfFile = FileUtils.deleteFile(tempPdfUrl);
		System.out.println(pdfFile);
		//删除word
		boolean wordFile = FileUtils.deleteFile(sourcePath);
		System.out.println(wordFile);
	}
}
