package org.util;

import java.io.File;

import org.apache.struts2.ServletActionContext;

public class Constants {
	/**
	 * 适应任何系统的文件分隔符
	 */
	public static final String dot = File.separator;
	/**
	 * 存放临时图片的目录
	 */
	public static final String tempDir = "ueditor" + dot + "jsp" + dot
			+ "upload" + dot + "temp" + dot;
	/**
	 * 存放上传文章及图片的目录
	 */
	public static final String UsedDir = "ueditor" + dot + "jsp" + dot
			+ "upload" + dot + "article" + dot;

	public static String articleUrl = "http://"+ServletActionContext.getRequest()
			.getLocalAddr()
			+ ":"
			+ ServletActionContext.getRequest().getLocalPort()
			+ ServletActionContext.getRequest().getContextPath()
			+ "/"
			+ "ueditor/jsp/upload/article/";
}
