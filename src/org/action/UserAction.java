package org.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.dao.CellDao;
import org.dao.PhotoDao;
import org.dao.RoleDao;
import org.dao.UserDao;
import org.dao.UserDetailDao;
import org.dao.imp.CellDaoImp;
import org.dao.imp.PhotoDaoImp;
import org.dao.imp.RoleDaoImp;
import org.dao.imp.UserDaoImp;
import org.dao.imp.UserDetailDaoImp;
import org.imodel.VeUserinfoId;
import org.model.SuperUser;
import org.model.User;
import org.model.UserCell;
import org.model.UserRole;
import org.util.ChangeTime;
import org.util.MD5;
import org.util.MapMessage;
import org.util.ResultUtils;
import org.util.Utils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	private String phone;
	private String username;
	private String password;
	private String role;
	private String id;
	private String ids[];
	private Object result;
	private int available = 1;
	private int priority = 2;
	private Integer Page = 1;// 当前请求页,因为必须进行-1操作，所以初始化为0
	private Integer start = 0;// 数据起始位置
	private Integer limit = 15;// 每页显示的数据数目
	HttpServletRequest request = null;
	HttpServletResponse response = null;

	private RoleDao roleDao;

	/**
	 * 登录,超级用户的登录
	 */
	public String login() {
		UserDao uDao = new UserDaoImp();
		Map<String, String> message = new HashMap<>();
		SuperUser su = uDao.validate(username, password);
		if (su == null) {
			message.put("message", "error");
			message.put("description", "用户名或密码错误");
			result = message;
			return SUCCESS;
		} else {
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			session.put("sUser", su);
			message.put("message", "success");
			message.put("description", "登录成功");
			result = message;
			return SUCCESS;
		}

	}

	/**
	 * 获取用户总数目
	 */
	public String getUserCount() {
		List<Long> list = new ArrayList<>();
		UserDao uDao = new UserDaoImp();
		Long count = uDao.getCount();
		list.add(count);
		result = list;
		return SUCCESS;
	}

	/**
	 * 查看所有用户，显示用户列表
	 */
	public String execute() {
		UserDao uDao = new UserDaoImp();
		System.out.println(Page);
		if ((Page != null || start != null) && limit != null) {
			List<VeUserinfoId> list = uDao.getUserList1(Page - 1, start, limit);
			if (list != null && list.size() > 0) {
				result = Utils.setToResult(list, uDao.getCount());
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
	 * 统计每个月份的用户增长
	 */
	public String userIncrease() {
		UserDao uDao = new UserDaoImp();
		List<Object[]> list = uDao.getIncreaseNum();
		List<String> list2 = ChangeTime.current();
		List<String> list3 = new ArrayList<>();
		for (Object[] o : list) {
			list3.add(o[0] + "");
		}
		List<Map<String, String>> list1 = new ArrayList<>();
		for (String s : list2) {
			Map<String, String> map = new HashMap<>();
			if (list3.contains(s)) {
				map.put("month", "" + s);
				map.put("data", "" + list.get(list3.indexOf(s))[1]);
			} else {
				map.put("month", "" + s);
				map.put("data", "" + "0");
			}
			list1.add(map);
		}
		result = list1;

		return SUCCESS;
	}

	/**
	 * 后台新加新用户
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public String addUser() {
		Map<String, String> message = new HashMap<String, String>();
		UserDao userDao = new UserDaoImp();
		if (username != null && phone != null) {
			String current = ChangeTime.timeStamp();
			User user = new User(
					Long.parseLong(phone),
					new MD5().string2MD5(new MD5().string2MD5("123456" + "agp")),
					Integer.parseInt(current));
			User u = userDao.findUserByPhone(phone);
			if (u != null) {
				message.put("message", "error");
				message.put("description", "该手机号已注册，请登录");
			} else {
				Long userid = userDao.Register(user);
				if (userid > 0) {
					if (!Utils.isLong(role)) {
						role = "1";
						message.put("extral1", "需要传入role,默认role=1");
					}
					roleDao = new RoleDaoImp();
					UserRole userRole = new UserRole(userid,
							Long.parseLong(role));
					roleDao.insert1(userRole);
					UserDetailDao uDetailDao = new UserDetailDaoImp();
					if (uDetailDao.insert(username, userid)) {
					} else {
						message.put("extral", "注册成功但未保存详细信息");
					}
					message.put("message", "success");
					message.put("description", "注册成功");
				} else {
					message.put("message", "error");
					message.put("description", "注册失败");
				}
			}
		} else {
			message.put("message", "error");
			message.put("description", "缺少必要参数");
		}
		message.put("parms", "username=" + Utils.isNull(username) + "&phone="
				+ Utils.isNull(phone) + "&password" + Utils.isNull(password));
		result = message;
		return SUCCESS;
	}

	/**
	 * 新增超级用户
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public String addSuper() throws UnsupportedEncodingException {
		Map<String, String> message = new HashMap<String, String>();
		UserDao uDao = new UserDaoImp();

		String current = ChangeTime.timeStamp();
		username = new String(username.getBytes("ISO-8859-1"), "utf-8");
		SuperUser superUser = new SuperUser(username, password, available,
				priority, Integer.parseInt(current));
		SuperUser su = uDao.findSuperByName(username);
		if (su != null) {
			message.put("message", "error");
			message.put("description", "用户名重复!");
			result = message;
			return SUCCESS;
		} else if (uDao.AddSuper(superUser)) {
			message.put("message", "success");
			message.put("description", "添加成功");
			result = message;
			String JsonArry = JSONArray.fromObject(result).toString();
			System.out.println(JsonArry);
			return SUCCESS;
		} else {
			message.put("message", "error");
			message.put("description", "添加失败");
			result = message;
			return SUCCESS;
		}
	}

	/**
	 * 查找用户
	 */
	public String searchUser() {
		UserDao uDao = new UserDaoImp();
		User u = uDao.findUserByPhone(phone);
		if (u != null) {
			List<User> list = new ArrayList<User>();
			list.add(u);
			result = list;
			String JsonArry = JSONArray.fromObject(result).toString();
			System.out.println(JsonArry);
			return SUCCESS;
		} else {
			Map<String, String> message = new HashMap<String, String>();
			message.put("message", "error");
			message.put("description", "无该手机用户");
			result = message;
			return SUCCESS;
		}
	}

	/**
	 * 获取超级用户列表
	 */
	public String getSuperList() {
		UserDao uDao = new UserDaoImp();
		List<SuperUser> list = uDao.getSuperList();
		result = list;
		String JsonArry = JSONArray.fromObject(result).toString();
		System.out.println(JsonArry);
		return SUCCESS;
	}

	/**
	 * 删除用户
	 */
	public String delUser() {
		Map<String, String> message = new HashMap<String, String>();
		UserDao uDao = new UserDaoImp();
		User user = null;
		if (phone != null) {
			user = uDao.findUserByPhone(phone);
		}
		if (id != null) {
			if (Utils.isLong(id)) {
				user = uDao.findUserById(Long.parseLong(id));
			} else {
				message.put("message", "error");
				message.put("description", "请输入合法的id");
			}
		}
		if (user == null) {
			message.put("message", "error");
			message.put("description", "该用户不存在");
			result = message;
			return SUCCESS;
		}
		if (uDao.deleteUser(user)) {
			deleteUser(user, message);
		} else {
			message.put("message", "error");
			message.put("description", "删除用户失败");
			result = message;
		}
		return SUCCESS;
	}

	/**
	 * 删除多个用户
	 */
	public String delUsers() {
		UserDao uDao = new UserDaoImp();
		if (ids != null) {
			Map<String, String> message = new HashMap<String, String>();
			if (uDao.deleteUsers(ids)) {
				for (String id : ids) {
					deleteUser(id, message);
				}
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

	/**
	 * 删除用户的相关信息
	 */
	public void deleteUser(String id, Map<String, String> message) {
		PhotoDao pDao = new PhotoDaoImp();
		UserDetailDao uDetailDao = new UserDetailDaoImp();
		CellDao cDao = new CellDaoImp();
		roleDao = new RoleDaoImp();
		List<String> urlList = pDao.getUrlList(Long.parseLong(id));
		if (!Utils.delFile(urlList)) {
			System.out.println("删除图片失败");
		}
		if (pDao.delete(Long.parseLong(id))) {
			System.out.println("删除图片表成功");
		} else {
			System.out.println("删除用户成功,但删除图片表时失败了");
		}
		if (uDetailDao.delete(Long.parseLong(id))) {
			System.out.println("删除个人详细信息成功");
		} else {
			System.out.println("删除用户成功,但删除个人详细信息时失败了");
		}
		List<UserCell> userCells = cDao.getCellList(Long.parseLong(id));
		if (userCells.size() > 0) {
			for (UserCell userCell : userCells) {
				if (cDao.deleteCell(userCell.getCellid())) {
					System.out.println("删除用户细胞成功");
				} else {
					System.out.println("删除用户细胞失败");
				}
			}
		}
		if (cDao.deleteUserCell(Long.parseLong(id))) {
			System.out.println("删除用户细胞信息成功");
		} else {
			System.out.println("删除用户细胞信息失败");
		}
		if (roleDao.delete1(Long.parseLong(id))) {
			System.out.println("删除用户角色关联成功");
		} else {
			System.out.println("删除用户角色关联失败");
		}
		message.put("message", "success");
		message.put("description", "删除用户成功");
		result = message;
	}

	/**
	 * 删除该用户的相关信息
	 */
	public void deleteUser(User user, Map<String, String> message) {
		PhotoDao pDao = new PhotoDaoImp();
		UserDetailDao uDetailDao = new UserDetailDaoImp();
		CellDao cDao = new CellDaoImp();
		List<String> urlList = pDao.getUrlList(user.getId());
		if (!Utils.delFile(urlList)) {
			System.out.println("删除图片失败");
		}
		if (pDao.delete(user.getId())) {
			message.put("extral", "删除图片表成功");
			System.out.println("删除图片表成功");
		} else {
			message.put("extral", "删除用户成功,但删除图片表时失败了");
			System.out.println("删除用户成功,但删除图片表时失败了");
		}
		if (uDetailDao.delete(user.getId())) {
			message.put("extral1", "删除个人详细信息成功");
			System.out.println("删除个人详细信息成功");
		} else {
			message.put("extral1", "删除用户成功,但删除个人详细信息时失败了");
			System.out.println("删除用户成功,但删除个人详细信息时失败了");
		}
		List<UserCell> userCells = cDao.getCellList(user.getId());
		String[] cellids = new String[100];
		int i = 0;
		for (UserCell userCell : userCells) {
			cellids[i] = "" + userCell.getCellid();
			i++;
		}
		if (cellids != null && cellids.length > 0) {
			if (cDao.deleteCells(cellids)) {
				System.out.println("删除用户细胞关联成功");
			} else {
				System.out.println("删除用户细胞关联失败");
			}
		}
		if (cDao.deleteUserCell(user.getId())) {
			System.out.println("删除用户细胞信息成功");
		} else {
			System.out.println("删除用户细胞信息失败");
		}
		message.put("message", "success");
		message.put("description", "删除用户成功");
		result = message;
	}

	/**
	 * 删除超级用户
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public String delSuper() throws UnsupportedEncodingException {
		UserDao uDao = new UserDaoImp();
		username = new String(username.getBytes("ISO-8859-1"), "utf-8");
		SuperUser superUser = uDao.findSuperByName(username);
		Map<String, String> message = new HashMap<String, String>();
		if (superUser == null) {
			message.put("message", "error");
			message.put("description", "该用户不存在");
			result = message;

			return SUCCESS;
		}
		if (uDao.deleteSuper(superUser)) {
			message.put("message", "success");
			message.put("description", "删除成功");
			result = message;
			return SUCCESS;
		} else {
			message.put("message", "error");
			message.put("description", "删除失败");
			result = message;
			return SUCCESS;
		}
	}

	/**
	 * 编辑超级用户
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public String editSuper() throws UnsupportedEncodingException {
		UserDao uDao = new UserDaoImp();
		username = new String(username.getBytes("ISO-8859-1"), "utf-8");
		SuperUser superUser = uDao.findSuperByName(username);
		superUser.setAvailable(available);
		superUser.setPriority(priority);
		Map<String, String> message = new HashMap<String, String>();
		if (uDao.editSuper(superUser)) {
			message.put("message", "success");
			message.put("description", "更新成功");
			result = message;
			return SUCCESS;
		} else {
			message.put("message", "error");
			message.put("description", "更新失败");
			result = message;
			return SUCCESS;
		}
	}

	/**
	 * 
	 */

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

	public int getPage() {
		return Page;
	}

	public void setPage(int Page) {
		this.Page = Page;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public void setPage(Integer page) {
		Page = page;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
