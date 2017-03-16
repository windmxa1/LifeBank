package org.test;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.dao.CanDao;
import org.dao.ItemsDao;
import org.dao.TopicsDao;
import org.dao.UserDao;
import org.dao.imp.CanDaoImp;
import org.dao.imp.IndustryDynamicDaoImp;
import org.dao.imp.ItemsDaoImp;
import org.dao.imp.TopicsDaoImp;
import org.dao.imp.UserDaoImp;
import org.hibernate.Query;
import org.hibernate.Session;
import org.imodel.VeItemsId;
import org.imodel.VeTopics;
import org.model.Can;
import org.util.ChangeTime;
import org.util.Constants;
import org.util.HibernateSessionFactory;
import org.util.MD5;
import org.util.MapMessage;
import org.util.Utils;

public class Demo01 {
	public static void main(String[] args) {
		// System.out.println(new MD5().string2MD5(new
		// MD5().string2MD5("111111"+"agp")));
		try {
			Session session = HibernateSessionFactory.getSession();
			Query query = session.createQuery("from VeTopics");
			query.setMaxResults(1);
			VeTopics veTopics = (VeTopics) query.uniqueResult();
			System.out.println(veTopics.getId().getUsername());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
