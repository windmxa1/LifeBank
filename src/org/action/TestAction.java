package org.action;

import com.opensymphony.xwork2.ActionSupport;

public class TestAction extends ActionSupport{
	private Object result; 
	
	public String test(){
		
		result ="a";
		return SUCCESS;
		
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}

}
