package com.rytc.common.util.word;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.PositionInParagraph;
import org.apache.poi.xwpf.usermodel.TextSegement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;


/**
 * 合同生成
 *
 * @author zhangkang
 */
public class PactWord {


	/**
	 * 替换表格条目
	 *
	 * @param table
	 */
	private static void replaceTableItem(XWPFTable table,
	                                     Map<String, Object> param) {
		List<XWPFTableRow> rows = table.getRows();
		for (XWPFTableRow row : rows) {
			List<XWPFTableCell> cells = row.getTableCells();
			for (XWPFTableCell cell : cells) {
				// 替换表格
				String text = cell.getText();

				for (Map.Entry<String, Object> entry : param.entrySet()) {
					String key = entry.getKey();
					if (text.indexOf(key) != -1) {
						text = text.replace(key, entry.getValue() + "");
						cell.removeParagraph(0);
						cell.setText(text);
					}
				}

			}
		}
	}

	/**
	 * 将map里的参数替换到doc里
	 *
	 * @param doc
	 * @param param 参数
	 */
	public static void replace(XWPFDocument doc, Map<String, Object> param)
		throws Exception {
		Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
		while (iterator.hasNext()) {
			XWPFParagraph para = iterator.next();
			String text = para.getText();
			// 遍历参数进行替换
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				String key = entry.getKey();
				if (text.indexOf(key) != -1) {
					// 删掉原来的Run
					List<XWPFRun> runs = para.getRuns();
					int size = runs.size();
					for (int i = 0; i < size; i++) {
						para.removeRun(0);
					}
					// 替换处理
					if (entry.getValue() instanceof DocPicture) {
						// 替换图片
						text = text.replace(key, "");
						DocPicture value = (DocPicture) entry.getValue();
						addPic(para, value.in, value.pictureType, value.width,
							value.height);
					} else {
						// 替换文本
						text = text.replace(key, entry.getValue() + "");
					}
					// 重新设置Run
					XWPFRun newRun = para.insertNewRun(0);
					newRun.setText(text, 0);
				}
			}

		}
	}

	/**
	 * word图片
	 *
	 * @author zhangkang
	 */
	public static class DocPicture {
		InputStream in;
		int width;
		int height;
		int pictureType = XWPFDocument.PICTURE_TYPE_PNG;

		public DocPicture(InputStream in, int width, int height) {
			this.in = in;
			this.width = width;
			this.height = height;
		}
	}

	private static void addPic(XWPFParagraph para, InputStream pictureData,
	                           int pictureType, int width, int height)
		throws InvalidFormatException {
		XWPFDocument doc = para.getDocument();
		String relationId = doc.addPictureData(pictureData, pictureType);
		final int EMU = 9525;
		width *= EMU;
		height *= EMU;

		CTInline inline = para.createRun().getCTR().addNewDrawing()
			.addNewInline();
		para.createRun().setText("");
		String picXml = ""
			+ "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
			+ "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
			+ "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
			+ "         <pic:nvPicPr>" + "            <pic:cNvPr id=\""
			+ 0
			+ "\" name=\"Generated\"/>"
			+ "            <pic:cNvPicPr/>"
			+ "         </pic:nvPicPr>"
			+ "         <pic:blipFill>"
			+ "            <a:blip r:embed=\""
			+ relationId
			+ "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"
			+ "            <a:stretch>"
			+ "               <a:fillRect/>"
			+ "            </a:stretch>"
			+ "         </pic:blipFill>"
			+ "         <pic:spPr>"
			+ "            <a:xfrm>"
			+ "               <a:off x=\"0\" y=\"0\"/>"
			+ "               <a:ext cx=\""
			+ width
			+ "\" cy=\""
			+ height
			+ "\"/>"
			+ "            </a:xfrm>"
			+ "            <a:prstGeom prst=\"rect\">"
			+ "               <a:avLst/>"
			+ "            </a:prstGeom>"
			+ "         </pic:spPr>"
			+ "      </pic:pic>"
			+ "   </a:graphicData>" + "</a:graphic>";

		inline.addNewGraphic().addNewGraphicData();
		XmlToken xmlToken = null;
		try {
			xmlToken = XmlToken.Factory.parse(picXml);
		} catch (XmlException xe) {
			xe.printStackTrace();
		}
		inline.set(xmlToken);

		inline.setDistT(0);
		inline.setDistB(0);
		inline.setDistL(0);
		inline.setDistR(0);

		CTPositiveSize2D extent = inline.addNewExtent();
		extent.setCx(width);
		extent.setCy(height);

		CTNonVisualDrawingProps docPr = inline.addNewDocPr();
		docPr.setId(pictureType);
		docPr.setName("图片" + pictureType);
		docPr.setDescr("");
	}
/************************************************************华丽的分割线（2.0版本）start******************************************************************/
	/**
	 * 创建借款合同
	 *
	 * @param is
	 * @param os
	 * @param param 合同所需数据
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void createLoanDoc2(InputStream is, OutputStream os,
	                                  Map<String, Object> param) throws Exception {
		XWPFDocument doc = new XWPFDocument(is);

		replace2(doc, param);

		List<XWPFTable> tables = doc.getTables();
		for (int i = 0; i < tables.size(); i++) {
			XWPFTable table = tables.get(i);
			if (i == 1) {
				replaceTableItem2(table, param);
			}
		}

		doc.write(os);
		is.close();
		os.flush();
		os.close();
	}

	private static void handleParagraph(List<XWPFParagraph> docParagraphs, Map<String, Object> paragraphData) {
		for (XWPFParagraph xwpfParagraph : docParagraphs) {
			for (Entry<String, Object> entry : paragraphData.entrySet()) {
				if (entry.getValue() instanceof WordPicture) {
					handleImage(xwpfParagraph, entry);
				} else {
					handleText(xwpfParagraph, entry);
				}
			}
		}
	}

	/**
	 * 替换表格条目
	 *
	 * @param table
	 */
	private static void replaceTableItem2(XWPFTable table,
	                                      Map<String, Object> param) {
		List<XWPFTableRow> rows = table.getRows();
		for (XWPFTableRow row : rows) {
			List<XWPFTableCell> cells = row.getTableCells();
			for (XWPFTableCell cell : cells) {
				// 替换表格
				handleParagraph(cell.getParagraphs(), param);
			}
		}
	}

	/**
	 * 将map里的参数替换到doc里
	 *
	 * @param doc
	 * @param param 参数
	 */
	public static void replace2(XWPFDocument doc, Map<String, Object> param)
		throws Exception {
		Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
		
		List<XWPFHeader>headers=doc.getHeaderList();
		headers.stream().forEach(t->{
			t.getListParagraph().forEach(s->{
				param.entrySet().forEach(e->{
					handleText(s,e);
				});
			});
		});

		
		while (iterator.hasNext()) {
			XWPFParagraph para = iterator.next();
			String text = para.getText();
			// 遍历参数进行替换
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				String key = entry.getKey();
				if (text.indexOf(key) != -1) {
					// 替换处理
					if (entry.getValue() instanceof WordPicture) {
						// 替换图片
						handleImage(para, entry);
					} else {
						// 替换文本
						handleText(para, entry);
					}
				}
			}

		}
	}

	private static void handleText(XWPFParagraph xwpfParagraph, Entry<String, Object> entry) {

		TextSegement textSegement = xwpfParagraph.searchText(entry.getKey(), new PositionInParagraph());
		if (textSegement != null) {
			List<XWPFRun> runs = xwpfParagraph.getRuns();
			if (textSegement.getBeginRun() == textSegement.getEndRun()) {
				XWPFRun run = runs.get(textSegement.getBeginRun());
				String runText = run.getText(run.getTextPosition());
				String replaced = runText.replace(entry.getKey(), entry.getValue().toString());
				run.setText(replaced, 0);
			} else {
				StringBuilder sb = new StringBuilder();
				for (int runPos = textSegement.getBeginRun(); runPos <= textSegement.getEndRun(); runPos++) {
					XWPFRun run = runs.get(runPos);
					sb.append(run.getText((run.getTextPosition())));
				}
				String connectedRuns = sb.toString();
				String replaced = connectedRuns.replace(entry.getKey(), entry.getValue().toString());
				XWPFRun firstRun = runs.get(textSegement.getBeginRun());
				firstRun.setText(replaced, 0);

				for (int runPos = textSegement.getBeginRun() + 1; runPos <= textSegement.getEndRun(); runPos++) {
					XWPFRun partNext = runs.get(runPos);
					partNext.setText("", 0);
				}
			}
		}
	}

	private static void handleImage(XWPFParagraph xwpfParagraph, Entry<String, Object> entry) {

		TextSegement textSegement = xwpfParagraph.searchText(entry.getKey(), new PositionInParagraph());
		if (textSegement != null) {
			List<XWPFRun> runs = xwpfParagraph.getRuns();
			WordPicture pic = (WordPicture) entry.getValue();
			int width = pic.getWidth();
			int height = pic.getHeight();
			int picType = pic.getPictureType();
			InputStream inputStream = pic.getInputStream();
			if (textSegement.getBeginRun() == textSegement.getEndRun()) {
				XWPFRun run = runs.get(textSegement.getBeginRun());
				String runText = run.getText(run.getTextPosition());
				String replaced = runText.replace(entry.getKey(), "");
				createPicture(picType, inputStream, width, height, xwpfParagraph);
				run.setText(replaced, 0);
			} else {
				StringBuilder sb = new StringBuilder();
				for (int runPos = textSegement.getBeginRun(); runPos <= textSegement.getEndRun(); runPos++) {
					XWPFRun run = runs.get(runPos);
					sb.append(run.getText((run.getTextPosition())));
				}
				String connectedRuns = sb.toString();
				String replaced = connectedRuns.replace(entry.getKey(), "");
				createPicture(picType, inputStream, width, height, xwpfParagraph);
				XWPFRun firstRun = runs.get(textSegement.getBeginRun());
				firstRun.setText(replaced, 0);

				for (int runPos = textSegement.getBeginRun() + 1; runPos <= textSegement.getEndRun(); runPos++) {
					XWPFRun partNext = runs.get(runPos);
					partNext.setText("", 0);
				}
			}
		}
	}

	private static void createPicture(int format, InputStream pictureData, int width, int height, XWPFParagraph paragraph) {
		String blipId = null;
		try {
			blipId = paragraph.getDocument().addPictureData(pictureData, format);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		int id = paragraph.getDocument().getAllPictures().size() - 1;
		final int EMU = 9525;
		width *= EMU;
		height *= EMU;
		CTInline inline = paragraph.createRun().getCTR().addNewDrawing().addNewInline();
		String picXml = "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
			+ "	<a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
			+ "		<pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" + "			<pic:nvPicPr>" + "				<pic:cNvPr id=\""
			+ id
			+ "\" name=\"Generated\"/>"
			+ "				<pic:cNvPicPr/>"
			+ "			</pic:nvPicPr>"
			+ "			<pic:blipFill>"
			+ "				<a:blip r:embed=\""
			+ blipId
			+ "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"
			+ "				<a:stretch>"
			+ "					<a:fillRect/>"
			+ "				</a:stretch>"
			+ "			</pic:blipFill>"
			+ "			<pic:spPr>"
			+ "				<a:xfrm>"
			+ "					<a:off x=\"0\" y=\"0\"/>"
			+ "					<a:ext cx=\""
			+ width
			+ "\" cy=\""
			+ height
			+ "\"/>"
			+ "				</a:xfrm>"
			+ "				<a:prstGeom prst=\"rect\">"
			+ "					<a:avLst/>"
			+ "				</a:prstGeom>"
			+ "			</pic:spPr>"
			+ "		</pic:pic>"
			+ "	</a:graphicData>"
			+ "</a:graphic>";

		inline.addNewGraphic().addNewGraphicData();
		XmlToken xmlToken = null;
		try {
			xmlToken = XmlToken.Factory.parse(picXml);
		} catch (XmlException xe) {
			xe.printStackTrace();
		}
		inline.set(xmlToken);

		inline.setDistT(0);
		inline.setDistB(0);
		inline.setDistL(0);
		inline.setDistR(0);

		CTPositiveSize2D extent = inline.addNewExtent();
		extent.setCx(width);
		extent.setCy(height);

		CTNonVisualDrawingProps docPr = inline.addNewDocPr();
		docPr.setId(id);
		docPr.setName("img" + id);
		docPr.setDescr("word" + id);
	}

	/************************************************************华丽的分割线（2.0版本）end******************************************************************/

	private static  String hideMiddle(String in) {
		  String regex = "(\\w{2})(.*)(\\w{2})";  
	      Matcher m = Pattern.compile(regex).matcher(in);  
	      if (m.find()) {  
	            String rep = m.group(2);  
	            StringBuilder sb = new StringBuilder();  
	            for (int i = 0; i < rep.length(); i++) {  
	                sb.append("*");  
	            }
	            return (in.replaceAll(rep, sb.toString()));
	        }
		    return in;
	}
	private static  String hideEnd(String in) {
		if(in.length()>4) {
			return in.substring(0, in.length()-4)+"****";
		}
		return in;
	}
	
}
