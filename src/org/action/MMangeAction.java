package org.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dao.CommentsDao;
import org.dao.PhotoDao;
import org.dao.ReplysDao;
import org.dao.TopicsDao;
import org.dao.UserDetailDao;
import org.dao.imp.CommentsDaoImp;
import org.dao.imp.PhotoDaoImp;
import org.dao.imp.ReplysDaoImp;
import org.dao.imp.TopicsDaoImp;
import org.dao.imp.UserDetailDaoImp;
import org.imodel.IComments;
import org.imodel.IReplys;
import org.imodel.VeCommentsId;
import org.imodel.VeTopicsId;
import org.model.Comments;
import org.model.Replys;
import org.model.Topics;
import org.util.MapMessage;
import org.util.ResultUtils;
import org.util.Utils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class MMangeAction extends ActionSupport {
	private Object result;
	private String topicId;
	private String commentId;
	private String replyId;
	private Integer Page = 1;// 当前请求页,因为必须进行-1操作，所以初始化为0
	private Integer start = 0;// 数据起始位置
	private Integer limit = 15;// 每页显示的数据数目

	/**
	 * 显示所有的话题
	 */
	public String execute() {
		TopicsDao tDao = new TopicsDaoImp();
		if ((Page != null || start != null) && limit != null) {
			List<VeTopicsId> list = tDao.getTopList1(Page, start, limit);
			if (list != null && list.size() > 0) {
				result = Utils.setToResult(list, tDao.getCount());
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

//	/**
//	 * 显示级联json,分级列出所有留言信息
//	 */
//	public String getAllInfo() {
//		TopicsDao tDao = new TopicsDaoImp();
//		List<Topics> list = tDao.getTopics(Page);
//		if (list.isEmpty() || list == null) {
//			return SUCCESS;
//		} else {
//			result = list;
//			return SUCCESS;
//		}
//	}

	/**
	 * 查看话题下的评论及回复
	 */
	public String getComRep() {
		CommentsDao cDao = new CommentsDaoImp();
		ReplysDao rDao = new ReplysDaoImp();
		List<Comments> comments = null;
		UserDetailDao uDao2 = new UserDetailDaoImp();
		System.out.println(topicId);
		Page = 0;
		System.out.println(Page);
		comments = cDao.getComments(topicId, Page);
		List<IComments> iComments = new ArrayList<>();
		for (Comments comment : comments) {
			IComments iComment = new IComments();
			iComment.setClock(comment.getClock());
			iComment.setContent(comment.getContent());
			iComment.setId(comment.getId());
			List<Replys> replys = rDao.getReplys1(comment.getId() + "");
			List<IReplys> iReplys = new ArrayList<IReplys>();
			if (replys != null) {
				for (Replys reply : replys) {
					IReplys iReply = new IReplys();
					iReply.setClock(reply.getClock());
					iReply.setCommentid(comment.getId());
					iReply.setId(reply.getId());
					iReply.setUserid(reply.getUserid());
					String Rusername = uDao2.getDetail(reply.getUserid())
							.getUsername();
					iReply.setUsername(Rusername);
					iReply.setContent(reply.getContent());
					iReplys.add(iReply);
				}
			}
			iComment.setReplys(iReplys);
			iComment.setTopicid(comment.getTopic().getId());
			iComment.setUserid(comment.getUserid());
			String Cusername = uDao2.getDetail(comment.getUserid())
					.getUsername();
			iComment.setUsername(Cusername);
			iComments.add(iComment);
		}
		if (iComments == null || iComments.isEmpty()) {
			// Map<String, String> map = new HashMap<>();
			// map.put("message", "error");
			// map.put("description", "无任何数据!");
			List<IComments> list = new ArrayList<>();
			result = list;
			return SUCCESS;
		} else {
			result = iComments;
			return SUCCESS;
		}
	}

	/**
	 * 点击话题查看评论
	 */
	public String getCommentsByTopic() {
		CommentsDao cDao = new CommentsDaoImp();
		Map<String, Object> session = ActionContext.getContext().getSession();
		if (topicId != null) {
			session.put("topicId", topicId);
		}
		if (session.get("topicId") != null
				&& !session.get("topicId").equals("")) {
			topicId = session.get("topicId").toString();
		}
		if ((Page != null || start != null) && limit != null && topicId != null) {
			List<VeCommentsId> list = cDao.getComList1(topicId, Page, start,
					limit);
			if (list != null && list.size() > 0) {
				result = Utils.setToResult(list, list.size());
			} else {
				result = ResultUtils.toResult(1);
			}
		} else {
			Map<String, String> message = new HashMap<>();
			message.put("message", "error");
			message.put("description", "参数错误");
			message.put("接收到的参数", "Page=" + Utils.isNull(Page) + "&start="
					+ Utils.isNull(start) + "&limit=" + Utils.isNull(limit)
					+ "&topicId=" + Utils.isNull(topicId));
			result = message;
		}
		return SUCCESS;
	}

	/**
	 * 点击评论查看回复
	 */
	public String getReplysByComment() {
		ReplysDao replysDao = new ReplysDaoImp();
		List<Map<String, String>> list = replysDao.getRepList1(commentId);
		if (list.isEmpty() || list == null) {
			Map<String, String> message = new HashMap<>();
			message.put("message", "error");
			message.put("description", "无任何回复!");
			result = message;
			return SUCCESS;
		} else {
			result = list;
			return SUCCESS;
		}
	}

	/**
	 * 删除话题
	 */
	public String delTopic() {
		Map<String, String> message = new HashMap<String, String>();
		if (topicId != null) {
			TopicsDao tDao = new TopicsDaoImp();
			PhotoDao pDao = new PhotoDaoImp();
			Topics topic = tDao.getTop(topicId);
			if (tDao.delTop(topicId)) {
				List<String> urlList = pDao.getTopAllUrl(Long
						.parseLong(topicId));
				if (topic.getComments() != null
						&& topic.getComments().size() > 0) {
					for (Comments comment : topic.getComments()) {
						if (!pDao.delByForeignId(comment.getId())) {
							System.out.println("话题删除成功，但删除评论图片记录失败了");
						}
					}
				}
				if (!pDao.delByForeignId(Long.parseLong(topicId))) {
					System.out.println("话题删除成功，但删除话题图片记录失败了");
				} else {
					System.out.println("话题删除成功且删除话题图片记录成功");
				}
				if (!Utils.delFile(urlList)) {
					System.out.println("话题删除成功,但删除话题及评论图片失败");
				} else {
					System.out.println("话题删除成功,但删除话题及评论图片成功");
				}
				message.put("message", "success");
				message.put("description", "删除话题成功");
				result = message;
			} else {
				message.put("message", "error");
				message.put("description", "删除话题失败");
				result = message;
			}
		} else {
			MapMessage message2 = new MapMessage(2);
			message2.put("parms", "topicId=" + Utils.isNull(topicId));
			result = message2;
		}
		return SUCCESS;
	}

	/**
	 * 删除评论
	 */
	public String delComment() {
		CommentsDao cDao = new CommentsDaoImp();
		if (commentId != null) {
			Map<String, String> message = new HashMap<String, String>();
			PhotoDao pDao = new PhotoDaoImp();
			if (cDao.delComment(commentId)) {
				List<String> urlList = pDao.getUrlByForeignId(Long
						.parseLong(commentId));
				if (!Utils.delFile(urlList)) {
					System.out.println("删除成功，但删除评论图片失败");
				} else {
					System.out.println("删除成功且删除评论图片成功");
				}
				if (!pDao.delByForeignId(Long.parseLong(commentId))) {
					System.out.println("评论删除成功，但删除评论图片记录失败了");
				} else {
					System.out.println("删除成功且删除评论图片记录成功");
				}
				message.put("message", "success");
				message.put("description", "删除评论成功");
			} else {
				message.put("message", "error");
				message.put("description", "删除评论失败");
			}
			result = message;
		} else {
			MapMessage message2 = new MapMessage(2);
			message2.put("parms", "commentId=" + Utils.isNull(commentId));
			result = message2;
		}
		return SUCCESS;
	}

	/**
	 * 删除回复
	 */
	public String delReply() {
		ReplysDao rDao = new ReplysDaoImp();
		Map<String, String> message = new HashMap<>();

		System.out.println("replyId:" + replyId);
		if (rDao.delReply(replyId)) {
			message.put("message", "success");
			message.put("description", "删除回复成功");
			result = message;
			return SUCCESS;
		} else {
			message.put("message", "error");
			message.put("description", "删除回复失败");
			result = message;
			return SUCCESS;
		}
	}

	/**
	 * 按关键词搜索所有包含关键词的信息,暂时不做
	 */
	public String getMessageByKey() {

		return SUCCESS;
	}

	public Object getResult() {
		return result;
	}

	public String getReplyId() {
		return replyId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public Integer getPage() {
		return Page;
	}

	public void setPage(Integer Page) {
		this.Page = Page;
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
}
