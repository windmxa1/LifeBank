package org.dao.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dao.IndustryDynamicDao;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.model.IndustryDynamic;
import org.util.HibernateSessionFactory;

@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class IndustryDynamicDaoImp implements IndustryDynamicDao {

	private Session session = null;

	/**
	 * app根据状态查询数据
	 */
	public List query(Integer states) {

		try {
			Session session = HibernateSessionFactory.getSession();
			Transaction ts = session.beginTransaction();
			String sql = "select * from industry_dynamic where  states = "
					+ "?";// sql语句查询
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setParameter(0, states);
			sqlQuery.addEntity(IndustryDynamic.class);
			List<IndustryDynamic> industryDynamic = sqlQuery.list();
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			// 循环遍历数据库中数据
			for (IndustryDynamic industryDynamic1 : industryDynamic) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", "" + industryDynamic1.getId());
				map.put("picture", "" + industryDynamic1.getPicture());
				map.put("content", "" + industryDynamic1.getContent());
				map.put("url", "" + industryDynamic1.getUrl());
				map.put("title", "" + industryDynamic1.getTitle());
				map.put("content", "" + industryDynamic1.getContent());
				map.put("clock", "" + industryDynamic1.getClock());
				map.put("guide", "" + industryDynamic1.getGuide());
				list.add(map);
			}
			HibernateSessionFactory.closeSession();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			HibernateSessionFactory.closeSession();
			return null;
		}
	}

	/**
	 * 6.行业新闻动态
	 * 
	 * */
	public List industryNews() {
		try {
			Session session = HibernateSessionFactory.getSession();
			Transaction ts = session.beginTransaction();
			String sql = "select * from industry_dynamic where states = 6 ORDER BY clock desc";// sql语句查询
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.addEntity(IndustryDynamic.class);
			List<IndustryDynamic> industryDynamic = sqlQuery.list();
			ts.commit();
			return industryDynamic;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			HibernateSessionFactory.closeSession();
		}
	}

	/*
	 * 
	 * industry_dynamic表中添加数据
	 */
	public boolean insert(IndustryDynamic industryDynamic) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Transaction ts = session.beginTransaction();
			session.save(industryDynamic);
			ts.commit();
			HibernateSessionFactory.closeSession();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			HibernateSessionFactory.closeSession();
			return false;
		}
	}

	/*
	 * 
	 * industry_dynamic表中删除数据
	 */
	public int delete(int id) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Transaction ts = session.beginTransaction();
			String sql = "delete from industry_dynamic where id = " + "?";
			SQLQuery query = session.createSQLQuery(sql);
			query.setParameter(0, id);
			int count = query.executeUpdate();
			ts.commit();
			HibernateSessionFactory.closeSession();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			HibernateSessionFactory.closeSession();
			return 0;
		}
	}

	/*
	 * 
	 * industry_dynamic表中修改数据
	 */
	public int update(IndustryDynamic industryDynamic) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Transaction ts = session.beginTransaction();
			session = HibernateSessionFactory.getSession();
			session.update(industryDynamic);
			ts.commit();
			HibernateSessionFactory.closeSession();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			HibernateSessionFactory.closeSession();
			return 0;
		}
	}

	public int count(Integer states) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Transaction ts = session.beginTransaction();
			int count = 0;
			SQLQuery sqlQuery = session
					.createSQLQuery("select * from industry_dynamic where states = "
							+ "?");
			sqlQuery.setParameter(0, states);
			sqlQuery.addEntity(IndustryDynamic.class);
			List cats = sqlQuery.list();
			count = cats.size();
			ts.commit();
			HibernateSessionFactory.closeSession();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			HibernateSessionFactory.closeSession();
			return 0;
		}
	}

	public IndustryDynamic getDataById(Integer id) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Transaction ts = session.beginTransaction();

			Query query = session
					.createQuery("from IndustryDynamic where id = ?");
			query.setParameter(0, id);
			query.setMaxResults(1);
			IndustryDynamic idDynamic = (IndustryDynamic) query.uniqueResult();

			ts.commit();
			HibernateSessionFactory.closeSession();
			return idDynamic;

		} catch (Exception e) {
			// TODO: handle exception
			HibernateSessionFactory.closeSession();
			return null;
		}
	}

	public List queryId(Integer id) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Transaction ts = session.beginTransaction();
			String sql = "select * from industry_dynamic where id = " + "?";// sql语句查询
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setParameter(0, id);
			sqlQuery.addEntity(IndustryDynamic.class);
			List<IndustryDynamic> industryDynamic = sqlQuery.list();
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			// 循环遍历数据库中数据
			for (IndustryDynamic industryDynamic1 : industryDynamic) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", "" + industryDynamic1.getId());
				map.put("picture", "" + industryDynamic1.getPicture());
				map.put("content", "" + industryDynamic1.getContent());
				map.put("url", "" + industryDynamic1.getUrl());
				map.put("title", "" + industryDynamic1.getTitle());
				map.put("content", "" + industryDynamic1.getContent());
				map.put("clock", "" + industryDynamic1.getClock());
				map.put("guide", "" + industryDynamic1.getGuide());
				list.add(map);
			}
			ts.commit();
			HibernateSessionFactory.closeSession();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			HibernateSessionFactory.closeSession();
			return null;
		}
	}

	@Override
	public List query(Integer states, Integer start, Integer limit) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Transaction ts = session.beginTransaction();
			String sql = "select * from industry_dynamic where  states = "
					+ "?";// sql语句查询
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setParameter(0, states);
			sqlQuery.setFirstResult(start);
			sqlQuery.setMaxResults(limit);
			sqlQuery.addEntity(IndustryDynamic.class);
			List<IndustryDynamic> industryDynamic = sqlQuery.list();
			return industryDynamic;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
}
