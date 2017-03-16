package org.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 结果集工具类
 * @author marshall
 *
 */
public class ResultUtils {
	/**
	 * 将结果按格式输出
	 * 
	 * @param code
	 *            提示码
	 * @param msg
	 *            给用户的提示信息，没有则为空串
	 * @param jsonObject
	 *            返回给客户端的数据
	 * @return
	 */
	public static JSONObject toJson(int code, String msg, JSONObject jsonObject) {
		JSONObject json = new JSONObject();
		json.element("code", code);
		json.element("msg", msg);
		json.element("data", jsonObject);
		return json;
	}
	public static JSONObject toJson(int code, String msg,Object data) {
		JSONObject json = new JSONObject();
		json.element("code", code);
		json.element("msg", msg);
		json.element("data", data);
		return json;
	}
	/**
	 * 返回Map结果数组
	 * @param state 1：暂时没有数据
	 * @return
	 */
	public static Map<String, String> toResult(int state) {
		Map<String, String> message = new HashMap<String, String>();
		switch (state) {
		case 1:
			message.put("message", "error");
			message.put("description", "暂时没有数据!");
			break;
		case 2:
			break;
		default:
			break;
		}
		return message;
	}
}
