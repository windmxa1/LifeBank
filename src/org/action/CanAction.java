package org.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dao.CanDao;
import org.dao.UserDao;
import org.dao.imp.CanDaoImp;
import org.dao.imp.UserDaoImp;
import org.model.Can;
import org.util.MapMessage;
import org.util.ResultUtils;
import org.util.Utils;

import com.opensymphony.xwork2.ActionSupport;

public class CanAction extends ActionSupport {
	private Object result;
	private String serial;
	private String address;
	private String canid;
	private Integer Page = 1;// 当前请求页,因为必须进行-1操作，所以初始化为0
	private Integer start = 0;// 数据起始位置
	private Integer limit = 15;// 每页显示的数据数目
	private String[] ids;

	/**
	 * 获取罐体的页数
	 */
	public String getCanPage() {
		CanDao canDao = new CanDaoImp();
		Long count = canDao.getCount();
		List<Long> list = new ArrayList<>();
		list.add(count);
		Long pageCount = (long) 0;
		if (count % 15 > 0) {
			pageCount = count / 15 + 1;
		} else {
			pageCount = count / 15;
		}
		if (pageCount == 0) {
			pageCount = (long) 1;
		}
		result = list;
		return SUCCESS;
	}

	public String getCanCount() {
		CanDao canDao = new CanDaoImp();
		Long count = canDao.getCount();
		List<Long> list = new ArrayList<>();
		list.add(count);
		result = list;
		return SUCCESS;
	}

	/**
	 * 查看存储的罐体信息
	 */
	public String execute() {
		CanDao canDao = new CanDaoImp();
		if ((Page != null || start != null) && limit != null) {
			List<Can> list = canDao.getList(Page - 1, start, limit);
			if (list != null && list.size() > 0) {
				result = Utils.setToResult(list, canDao.getCount());
			} else {
				result = ResultUtils.toResult(1);
			}
		} else {
			Map<String, String> message = new HashMap<>();
			message.put("message", "error");
			message.put("description", "参数错误");
			message.put("接收到的参数", "Page=" + Utils.isNull(Page) + "&start="
					+ Utils.isNull(start) + "&limit=" + Utils.isNull(limit));
			result = message;
		}
		return SUCCESS;
	}

	/**
	 * 添加罐体信息
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public String addCan() throws UnsupportedEncodingException {
		CanDao canDao = new CanDaoImp();
		// address = new String(address.getBytes("ISO-8859-1"), "utf-8");
		Can can = new Can(serial, address);
		Can can1 = canDao.getCanBySerial(serial);
		Map<String, String> message = new HashMap<>();

		if (can1 != null) {
			message.put("message", "error");
			message.put("description", "罐体序列号重复!");
			result = message;
			return SUCCESS;
		}
		if (canDao.insert(can)) {
			message.put("message", "success");
			message.put("description", "新增罐体成功!");
			result = message;
			return SUCCESS;
		} else {
			message.put("message", "error");
			message.put("description", "新增罐体失败!");
			result = message;
			return SUCCESS;
		}
	}

	/**
	 * 修改罐体信息
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public String updateCan() throws UnsupportedEncodingException {
		// address = new String(address.getBytes("ISO-8859-1"), "utf-8");
		CanDao canDao = new CanDaoImp();
		if (canid != null) {
			Map<String, String> message = new HashMap<>();
			Can can = canDao.findCanById(Long.parseLong(canid));
			if (address != null && !address.equals("")) {
				can.setAddress(address);
			}
			if (serial != null && !serial.equals("")) {
				can.setSerial(serial);
			}
			if (canDao.update(can)) {
				message.put("message", "success");
				message.put("description", "修改信息成功!");
				result = message;
				return SUCCESS;
			} else {
				message.put("message", "error");
				message.put("description", "修改信息失败!");
				result = message;
			}
		}
		result = new MapMessage(2);
		return SUCCESS;
	}

	/**
	 * 删除罐体
	 */
	public String delCan() {
		CanDao canDao = new CanDaoImp();
		Map<String, String> message = new HashMap<>();

		if (canDao.delCan(Long.parseLong(canid))) {
			message.put("message", "success");
			message.put("description", "删除罐体成功!");
			result = message;
			return SUCCESS;
		} else {
			message.put("message", "error");
			message.put("description", "删除罐体失败!");
			result = message;
			return SUCCESS;
		}
	}

	public String delCans() {
		CanDao cDao = new CanDaoImp();
		if (ids != null) {
			Map<String, String> message = new HashMap<String, String>();
			if (cDao.delCans(ids)) {
				message.put("message", "success");
				message.put("description", "删除成功");
				result = message;
			} else {
				message.put("message", "error");
				message.put("description", "删除失败");
				result = message;
			}
		} else {
			result = new MapMessage(2);
		}
		return SUCCESS;

	}

	public Integer getPage() {
		return Page;
	}

	public void setPage(Integer Page) {
		this.Page = Page;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCanid() {
		return canid;
	}

	public void setCanid(String canid) {
		this.canid = canid;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}
}
