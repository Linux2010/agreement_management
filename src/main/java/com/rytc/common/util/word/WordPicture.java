package com.rytc.common.util.word;

import java.io.InputStream;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class WordPicture {
	private int pictureType;
	private InputStream inputStream;
	private int width;
	private int height;

	public WordPicture(String pictureType, InputStream inputStream, int width, int height) {
		this.pictureType = getPictureType(pictureType);
		this.inputStream = inputStream;
		this.width = width;
		this.height = height;

	}

	public WordPicture(int pictureType, InputStream inputStream, int width, int height) {
		this.pictureType = pictureType;
		this.inputStream = inputStream;
		this.width = width;
		this.height = height;

	}

	private static int getPictureType(String picType) {
		int res = XWPFDocument.PICTURE_TYPE_PICT;
		if (picType != null) {
			if (picType.equalsIgnoreCase("png")) {
				res = XWPFDocument.PICTURE_TYPE_PNG;
			} else if (picType.equalsIgnoreCase("dib")) {
				res = XWPFDocument.PICTURE_TYPE_DIB;
			} else if (picType.equalsIgnoreCase("emf")) {
				res = XWPFDocument.PICTURE_TYPE_EMF;
			} else if (picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")) {
				res = XWPFDocument.PICTURE_TYPE_JPEG;
			} else if (picType.equalsIgnoreCase("wmf")) {
				res = XWPFDocument.PICTURE_TYPE_WMF;
			}
		}
		return res;
	}

	public int getPictureType() {
		return pictureType;
	}

	public void setPictureType(int pictureType) {
		this.pictureType = pictureType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
