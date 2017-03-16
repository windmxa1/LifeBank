package org.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.dispatcher.multipart.MultiPartRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.dao.IndustryDynamicDao;
import org.dao.imp.IndustryDynamicDaoImp;
import org.model.IndustryDynamic;
import org.util.ChangeTime;
import org.util.Constants;
import org.util.MapMessage;
import org.util.PhotoUtils;
import org.util.Utils;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.org.apache.bcel.internal.generic.NEW;

@SuppressWarnings({ "serial", "unused", "unchecked" })
public class IndustryDynamicAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	HttpServletRequest request = null;

	HttpServletResponse response = null;

	private Object result;
	private String[] ids;
	private File pic1;
	private File pic2;
	private File pic3;
	private String pic1FileName;
	private String pic2FileName;
	private String pic3FileName;
	private String[] title;
	private String[] url;
	private static String fileDir = "";// 日期作为文件目录 ，只是根据提交时间生成的年月日数字
	private static String targetDir = ""; // 目标目录，只是一串随机生成的数字
	private Integer start;
	private Integer limit;

	/**
	 * 1.APP首页上的几张轮换图片替换
	 */
	public void replaceApp() {
		Map<String, String> message = new HashMap<String, String>();
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		List<IndustryDynamic> list = industryDynamicDao.query(1);
		JSONArray jsonArray = JSONArray.fromObject(list);
		responseMS(jsonArray);
	}

	/**
	 * 2.引导页图片替换
	 */
	public void guideReplaceApp() {
		Map<String, String> message = new HashMap<String, String>();
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		List<IndustryDynamic> list = industryDynamicDao.query(2);
		JSONArray jsonArray = JSONArray.fromObject(list);
		responseMS(jsonArray);
	}

	/**
	 * 4.专家团队： 图片头像替换，内容替换
	 */
	public void expertTeamApp() {
		Map<String, String> message = new HashMap<String, String>();
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		List<IndustryDynamic> list = industryDynamicDao.query(4);
		JSONArray jsonArray = JSONArray.fromObject(list);
		responseMS(jsonArray);
	}

	/**
	 * 5.产品介绍： 内容替换，允许插入图片
	 */
	public void productIntroductionApp() {
		Map<String, String> message = new HashMap<String, String>();
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		List<IndustryDynamic> list = industryDynamicDao.query(5);
		JSONArray jsonArray = JSONArray.fromObject(list);
		responseMS(jsonArray);
	}

	/**
	 * 6.动态读取行业动态
	 */
	public void industryNewsApp() {
		Map<String, String> message = new HashMap<String, String>();
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		List<IndustryDynamic> list = industryDynamicDao.query(6);
		JSONArray jsonArray = JSONArray.fromObject(list);
		responseMS(jsonArray);
	}

	/**
	 * 7.动态读取行业动态
	 */
	public void theScrollBarApp() {
		Map<String, String> message = new HashMap<String, String>();
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		List<IndustryDynamic> list = industryDynamicDao.query(7);
		JSONArray jsonArray = JSONArray.fromObject(list);
		responseMS(jsonArray);
	}

	/**
	 * 9.临床案例
	 */
	public void clinicalCaseApp() {
		Map<String, String> message = new HashMap<String, String>();
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		List<IndustryDynamic> list = industryDynamicDao.query(9);
		JSONArray jsonArray = JSONArray.fromObject(list);
		responseMS(jsonArray);
	}

	/**
	 * 10.公开课
	 */
	public void publicClassApp() {
		Map<String, String> message = new HashMap<String, String>();
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		List<IndustryDynamic> list = industryDynamicDao.query(10);
		JSONArray jsonArray = JSONArray.fromObject(list);
		responseMS(jsonArray);
	}

	/**
	 * 11.健康小知识
	 */
	public void healthKnowledgeApp() {
		Map<String, String> message = new HashMap<String, String>();
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		List<IndustryDynamic> list = industryDynamicDao.query(11);
		JSONArray jsonArray = JSONArray.fromObject(list);
		responseMS(jsonArray);
	}

	/**
	 * responseMS 响应请求 返回JsonArray
	 */
	private void responseMS(JSONArray JsonArry) {
		try {
			JSONObject json = new JSONObject();
			json.element("JsonArry", JsonArry);
			response.setContentType("text/html;charset=utf-8");
			byte[] jsonBytes = json.toString().getBytes("utf-8");
			response.setContentLength(jsonBytes.length);
			response.getOutputStream().write(jsonBytes);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除数据
	 * 
	 * */
	public String delete() {
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		List<Map<String, String>> list = new ArrayList<>();
		if (ids != null) {
			try {
				for (String id : ids) {
					IndustryDynamic industryDynamic = industryDynamicDao
							.getDataById(Integer.parseInt(id));
					if (industryDynamicDao.delete(Integer.parseInt(id)) > 0) {
						if (Utils.delFile(industryDynamic.getPicture().replace(
								Utils.PICURL, Utils.PICSRC))) {
							System.out.println("删除数据图片成功");
						}
						/********* 删除文章目录 ********/
						String realPath = request.getRealPath("/");
						String filesrc = industryDynamic
								.getUrl()
								.replace(Constants.articleUrl,
										realPath + Constants.UsedDir)
								.replace("/", File.separator);
						File file1 = new File(filesrc);
						try {
							FileUtils.deleteDirectory(file1.getParentFile());
						} catch (IOException e) {
							System.out.println("删除文章目录失败");
							e.printStackTrace();
						}
						/*************************/
						Map<String, String> message = new HashMap<>();
						message.put("message", "success");
						message.put("description", "删除成功!");
						result = message;
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				Map<String, String> message = new HashMap<>();
				message.put("message", "false");
				message.put("description", "删除失败!");
				result = message;
				result = message;
			}
		}
		return SUCCESS;
	}

	/**
	 * 添加数据 states: 1.轮播图片 4.专家团队 7.滚动条
	 * 
	 * */
	public String saveActive() {
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		List<Map<String, String>> list = new ArrayList<>();
		// Map<String, String> message;
		JSONObject message;
		if (request.getParameter("states") != null) {
			Integer states = Integer.parseInt(request.getParameter("states"));
			switch (states) {
			case 1:// pic1 , 2 ,3
				int count = 0;
				for (int i = 1; i <= 3; i++) {
					if (request.getAttribute("pic" + i) != null) {
						String filename = request.getAttribute(
								"pic" + i + "FileName").toString();
						Integer clock = Integer
								.parseInt(ChangeTime.timeStamp());
						String PicSrc = Utils.PICSRC + states + "_" + clock
								+ "_" + filename;
						String PicUrl = Utils.PICURL + states + "_" + clock
								+ "_" + filename;
						// 保存图片
						PhotoUtils.photoUp(
								(File) request.getAttribute("pic" + i), PicSrc);
						// 保存记录
						IndustryDynamic industryDynamic = new IndustryDynamic();
						industryDynamic.setStates(states);
						industryDynamic.setClock(clock);
						industryDynamic.setPicture(PicUrl);
						if (title[i] != null && !title[i].equals("")) {
							industryDynamic.setTitle(title[i]);
						}
						if (url[i] != null && !url[i].equals("")) {
							industryDynamic.setUrl(url[i]);
						}
						if (industryDynamicDao.insert(industryDynamic)) {
							count++;
						}
					}
				}
				if (count == 3) {
					message = new JSONObject();
					message.put("success", true);
					message.put("msg", "上传成功");
					result = message;
				} else {
					message = new JSONObject();
					message.put("success", false);
					message.put("msg", "部分图片可能上传失败");
					result = message;
				}
				break;
			case 4:// pic1 content 4.专家团队：图片头像替换，内容替换
				String content1 = request.getParameter("content");
				if (pic1 != null && content1 != null) {
					IndustryDynamic industryDynamic = new IndustryDynamic();
					industryDynamic.setContent(content1);
					Integer clock = Integer.parseInt(ChangeTime.timeStamp());
					industryDynamic.setClock(clock);
					industryDynamic.setStates(states);
					String filename = states + "_" + clock + "_" + pic1FileName;
					PhotoUtils.photoUp(pic1, Utils.PICSRC + filename);
					industryDynamic.setPicture(Utils.PICURL + filename);
					if (industryDynamicDao.insert(industryDynamic)) {
						message = new JSONObject();
						message.put("success", true);
						message.put("msg", "上传成功");
						result = message;
					}
				} else {
					message = new JSONObject();
					message.put("success", false);
					message.put("msg", "缺少必要参数，请检查您的输入");
					result = message;
				}
				break;

			case 7:// guide 7.滚动条
				if (request.getParameter("guide") != null) {
					Integer clock = Integer.parseInt(ChangeTime.timeStamp());
					IndustryDynamic industryDynamic = new IndustryDynamic();
					industryDynamic.setGuide(request.getParameter("guide"));
					industryDynamic.setStates(states);
					industryDynamic.setClock(clock);
					industryDynamicDao.insert(industryDynamic);
					message = new JSONObject();
					message.put("success", true);
					message.put("msg", "上传成功");
					result = message;
				} else {
					message = new JSONObject();
					message.put("success", false);
					message.put("msg", "缺少必要参数，请检查您的输入");
					result = message;
				}
				break;
			default:
				break;
			}
		}

		return SUCCESS;
	}

	/**
	 * 发表文章 6.行业新闻动态 9.临床案例 10.公开课 11.健康小知识
	 */
	public String publishArticle() {
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		Integer states = Utils.parseInt(request.getParameter("states"));
		String digest = request.getParameter("digest");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		Map<String, Object> message = new HashMap<>();
		if (digest != null && title != null && pic1 != null && states != -1) {
			/*** 文件操作 ***/
			// 发表文章并返回Url
			String url = publish(content);
			// 删除之前留下的临时文件
			System.out.println("删除失败的临时文件夹数目为:" + deleteTemp());
			/*** 数据操作 ***/
			IndustryDynamic industryDynamic = new IndustryDynamic();
			industryDynamic.setStates(states);
			industryDynamic.setContent(digest);
			industryDynamic.setTitle(title);
			Integer clock = Integer.parseInt(ChangeTime.timeStamp());
			industryDynamic.setClock(clock);
			String filename = states + "_" + clock + "_" + pic1FileName;
			PhotoUtils.photoUp(pic1, Utils.PICSRC + filename);
			industryDynamic.setPicture(Utils.PICURL + filename);
			industryDynamic.setUrl(url);
			if (industryDynamicDao.insert(industryDynamic)) {
				message.put("success", true);
				message.put("msg", "文章上传成功！");
				message.put("ArticleUrl", url);
				result = message;
			}else {
				message.put("success", false);
				message.put("msg", "文章上传失败，请重试");
				message.put("ArticleUrl", url);
				result = message;
			}
		} else {
			message.put("success", false);
			message.put("msg", "缺少必要参数，请检查您的输入");
			result = message;
		}

		return SUCCESS;
	}

	/**
	 * 删除今天以前的所有临时文件夹
	 * 
	 * @return 返回未成功删除的文件夹个数
	 */
	private int deleteTemp() {
		String realPath = request.getRealPath("/");
		File file = new File(realPath + Constants.tempDir);
		int i = 0;
		for (File f : file.listFiles()) {
			if (!f.getName().equals("" + ChangeTime.currentDate())) {
				System.out.println(f);
				try {
					if (f.isDirectory()) {
						FileUtils.deleteDirectory(f);
					} else {
						f.delete();
					}
				} catch (IOException e) {
					e.printStackTrace();
					i++;
				}
			}
		}
		return i;
	}

	/**
	 * 将上传内容写成html文件，并返回url地址
	 */
	private String publish(String content) {
		String timeStamp = ChangeTime.timeStamp();
		String realPath = request.getRealPath("/");
		String dot = Constants.dot;
		String usedDir = realPath + Constants.UsedDir;
		String tempDir = realPath + Constants.tempDir;
		content = movePic(content, timeStamp, tempDir, usedDir, dot);

		if (fileDir.equals("")) {
			fileDir = ChangeTime.currentDate() + "";
		}

		String fileName = realPath + Constants.UsedDir + fileDir + dot
				+ targetDir + dot + "1.html";
		File file = new File(fileName);
		try {
			if (!file.getParentFile().exists()) {
				System.out.println("目标文件的目录不存在，准备创建目录...");
				if (!file.getParentFile().mkdirs()) {
					System.out.println("创建目录失败");
				}
				System.out.println(file.createNewFile());
			}
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.flush();
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/************* 上面是文件相关操作 *******************/
		/************* 下面是数据相关操作 *******************/
		String ArticleUrl = Constants.articleUrl + fileDir + "/" + targetDir
				+ "/" + "1.html";
		return ArticleUrl;
	}

	/**
	 * 识别图片地址，移动图片位置，修改图片url
	 * 
	 * @param content
	 *            内容
	 */
	private String movePic(String content, String timeStamp, String tempDir,
			String usedDir, String dot) {
		System.out.println("Moving picture...");
		String pattern = "/jsp/upload/temp/.*?(.jpg|.png|.gif)";
		String dir = "";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(content);
		targetDir = timeStamp + Utils.ran6();
		while (m.find()) {
			String used_file = m.group().replace("/jsp/upload/temp/", "")
					.replace("/", "\\");
			String[] a = used_file.split("\\\\");
			dir = a[0];
			fileDir = dir;
			String filename = a[1];
			String orginalFile = tempDir + dir + dot + filename;
			String targetFile = usedDir + dir + dot + targetDir + dot
					+ filename;
			System.out.println("orginalFile:" + orginalFile);
			System.out.println("targetFile:" + targetFile);

			File file = new File(orginalFile);

			File file2 = new File(targetFile);
			if (!file2.getParentFile().exists()) {
				System.out.println("目标文件的目录不存在，准备创建目录...");
				if (!file2.getParentFile().mkdirs()) {
					System.out.println("创建目录失败");
				}
			}
			try {
				System.out.println(file.renameTo(file2));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		System.out.println("Moving Complete...");
		content = content.replaceAll("/jsp/upload/temp/[0-9]*/?",
				"/jsp/upload/article/" + dir + "/" + targetDir + "/");
		return content;
	}

	/**
	 * 根据id查询数据
	 */
	public String queryId() {
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		int id = Integer.valueOf(request.getParameter("id"));
		List<IndustryDynamic> list = industryDynamicDao.query(id);
		result = Utils.setToResult1(list);
		return SUCCESS;
	}

	/**
	 * 1.APP首页上的几张轮换图片替换
	 */
	public String replaceWeb() {
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		if (start == null) {
			start = 0;
		}
		if (limit == null) {
			limit = 15;
		}

		List<IndustryDynamic> list = industryDynamicDao.query(1, start, limit);
		int count = industryDynamicDao.count(1);
		result = Utils.setToResult(list, count);
		return SUCCESS;
	}

	/**
	 * 2.引导页图片替换
	 */
	public String guideReplaceWeb() {
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		List<IndustryDynamic> list = industryDynamicDao.query(2);
		int count = industryDynamicDao.count(2);
		result = Utils.setToResult(list, count);
		return SUCCESS;
	}

	/**
	 * 4.专家团队：图片头像替换，内容替换
	 */
	public String expertTeamWeb() {
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		if (start == null) {
			start = 0;
		}
		if (limit == null) {
			limit = 15;
		}
		List<IndustryDynamic> list = industryDynamicDao.query(4,start,limit);
		int count = industryDynamicDao.count(4);
		result = Utils.setToResult(list, count);
		return SUCCESS;
	}

	/**
	 * 5.产品介绍：内容替换，允许插入图片
	 */
	public String productIntroductionWeb() {
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		List<IndustryDynamic> list = industryDynamicDao.query(5);
		int count = industryDynamicDao.count(5);
		result = Utils.setToResult(list, count);
		return SUCCESS;
	}

	/**
	 * 6.行业新闻动态
	 */
	public String industryNewsWeb() {
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		if (start == null) {
			start = 0;
		}
		if (limit == null) {
			limit = 15;
		}
		List<IndustryDynamic> list = industryDynamicDao.query(6,start,limit);
		int count = industryDynamicDao.count(6);
		result = Utils.setToResult(list, count);
		return SUCCESS;
	}

	/**
	 * 7滚动条
	 */
	public String theScrollBarWeb() {
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		if (start == null) {
			start = 0;
		}
		if (limit == null) {
			limit = 15;
		}
		List<IndustryDynamic> list = industryDynamicDao.query(7,start,limit);
		int count = industryDynamicDao.count(7);
		result = Utils.setToResult(list, count);
		return SUCCESS;
	}

	/**
	 * 9.临床案例
	 */
	public String clinicalCaseWeb() {
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		if (start == null) {
			start = 0;
		}
		if (limit == null) {
			limit = 15;
		}
		List<IndustryDynamic> list = industryDynamicDao.query(9,start,limit);
		int count = industryDynamicDao.count(9);
		result = Utils.setToResult(list, count);
		return SUCCESS;
	}

	/**
	 * 10.公开课
	 */
	public String publicClassWeb() {
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		if (start == null) {
			start = 0;
		}
		if (limit == null) {
			limit = 15;
		}
		List<IndustryDynamic> list = industryDynamicDao.query(10,start,limit);
		int count = industryDynamicDao.count(10);
		result = Utils.setToResult(list, count);
		return SUCCESS;
	}

	/**
	 * 11.健康小知识
	 */
	public String healthKnowledgeWeb() {
		IndustryDynamicDao industryDynamicDao = new IndustryDynamicDaoImp();
		if (start == null) {
			start = 0;
		}
		if (limit == null) {
			limit = 15;
		}
		List<IndustryDynamic> list = industryDynamicDao.query(11,start,limit);
		int count = industryDynamicDao.count(11);
		result = Utils.setToResult(list, count);
		return SUCCESS;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		// TODO Auto-generated method stub
		response = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		request = arg0;
	}

	public File getPic1() {
		return pic1;
	}

	public void setPic1(File pic1) {
		this.pic1 = pic1;
	}

	public File getPic2() {
		return pic2;
	}

	public void setPic2(File pic2) {
		this.pic2 = pic2;
	}

	public File getPic3() {
		return pic3;
	}

	public void setPic3(File pic3) {
		this.pic3 = pic3;
	}

	public String getPic1FileName() {
		return pic1FileName;
	}

	public void setPic1FileName(String pic1FileName) {
		this.pic1FileName = pic1FileName;
	}

	public String getPic2FileName() {
		return pic2FileName;
	}

	public void setPic2FileName(String pic2FileName) {
		this.pic2FileName = pic2FileName;
	}

	public String getPic3FileName() {
		return pic3FileName;
	}

	public void setPic3FileName(String pic3FileName) {
		this.pic3FileName = pic3FileName;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String[] getUrl() {
		return url;
	}

	public void setUrl(String[] url) {
		this.url = url;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

}
