package org.dao;

import java.util.List;

import org.imodel.VeUserinfoId;
import org.model.SuperUser;
import org.model.User;

public interface UserDao {
	/**
	 * 注册，手机号、名字、密码、时间
	 */
	public Long Register(User user);

	/**
	 * 验证密码是否正确
	 */
	public User validateUser(Long userid, String password);

	/**
	 * 登录
	 */
	public User Login(String phone, String password);

	/**
	 * 更新用户信息
	 */
	public boolean update(User user);

	/**
	 * 通过手机号找到用户
	 */
	public User findUserByPhone(String phone);

	/**
	 * 通过id找到用户
	 */
	public User findUserById(Long id);

	/**
	 * 获取用户总数
	 */
	public Long getCount();

	/**
	 * 获取用户列表
	 */
	public List<User> getUserList(Integer Page);

	/**
	 * 获取用户信息列表
	 */
	public List<VeUserinfoId> getUserList1(Integer Page, Integer start,
			Integer limit);

	/**
	 * 添加新的超级用户
	 */
	public boolean AddSuper(SuperUser superUser);

	/**
	 * 获取超级用户列表
	 */
	public List<SuperUser> getSuperList();

	/**
	 * 删除超级用户
	 */
	public boolean deleteSuper(SuperUser superUser);

	/**
	 * 删除用户
	 */
	public boolean deleteUser(User user);

	/**
	 * 批量删除多用户
	 */
	public boolean deleteUsers(String ids[]);

	/**
	 * 编辑超级用户
	 */
	public boolean editSuper(SuperUser superUser);

	/**
	 * 通过用户名找到超级用户
	 */
	public SuperUser findSuperByName(String username);

	/**
	 * 用户名密码确认
	 */
	public SuperUser validate(String username, String password);

	/**
	 * 统计每个月份的用户增长数
	 */
	public List<Object[]> getIncreaseNum();
}
