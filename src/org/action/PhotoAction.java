package org.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.util.ChangeTime;
import org.util.MapMessage;
import org.util.PhotoUtils;
import org.util.Utils;

import com.opensymphony.xwork2.ActionSupport;

public class PhotoAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	HttpServletRequest request = null;
	HttpServletResponse response = null;

	private static final long serialVersionUID = 1L;
	private List<File> upload = new ArrayList<>();
	private List<String> uploadFileName = new ArrayList<>();
	private Object result;

	public String photoUp() {
		List<String> urlList = new ArrayList<>();
		List<String> srcList = new ArrayList<>();
		for (int i = 0; i < upload.size(); i++) {
			String fileName = ChangeTime.timeStamp() + "_"
					+ uploadFileName.get(i);
			urlList.add(Utils.PICURL + fileName);
			srcList.add(Utils.PICSRC + fileName);
		}
		if (PhotoUtils.photoUp(upload, srcList)) {
			result = urlList;
		}else {
			result = new MapMessage(3);
		}
		return SUCCESS;
	}

	public String articalUp() {

		return SUCCESS;

	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
	}

	public List<File> getUpload() {
		return upload;
	}

	public void setUpload(List<File> upload) {
		this.upload = upload;
	}

	public List<String> getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(List<String> uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
