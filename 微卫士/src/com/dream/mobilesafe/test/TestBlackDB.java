package com.dream.mobilesafe.test;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.test.AndroidTestCase;

import com.dream.mobilesafe.db.BlackNumberDBOpenHelper;
import com.dream.mobilesafe.db.dao.BlackNumDAO;
import com.dream.mobilesafe.domin.BlackNumInfo;

public class TestBlackDB extends AndroidTestCase {

	public void test() throws Exception {
		BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(
				getContext());
		helper.getWritableDatabase();
	}

	public void testInsert() throws Exception {
		Random random = new Random();
		for (int i = 0; i < 10000; i++) {
			BlackNumDAO dao = new BlackNumDAO(getContext());
			BlackNumInfo num = new BlackNumInfo("13427168447" + i,
					random.nextInt(3));
			System.out.println("id=" + dao.insert(num));
		}
	}

	public void testDelete() throws Exception {
		BlackNumDAO dao = new BlackNumDAO(getContext());
		dao.deleteAll();
	}

	public void testUpdate() throws Exception {

	}

	public void testQueryAll() throws Exception {
		BlackNumDAO dao = new BlackNumDAO(getContext());
		List<BlackNumInfo> list = dao.queryAll();
		for (Iterator<BlackNumInfo> iterator = list.iterator(); iterator
				.hasNext();) {
			System.out.println(iterator.next());
		}
	}

	public void testFind() throws Exception {

	}

}
