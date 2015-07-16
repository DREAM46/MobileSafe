package com.dream.mobilesafe.test;

import java.util.Iterator;
import java.util.List;

import android.test.AndroidTestCase;

import com.dream.mobilesafe.domin.TaskInfo;
import com.dream.mobilesafe.engine.TaskInfoProvider;

public class TestTaskProvider extends AndroidTestCase {

	public void test() {
		List<TaskInfo> infos;
		try {
			infos = TaskInfoProvider.getTaskInfos(getContext());
			if (infos == null)
				System.out.println("null");

			for (Iterator<TaskInfo> iterator = infos.iterator(); iterator
					.hasNext();) {
				System.out.println(iterator.next());
				System.out.println("======================================");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("exception");
		}

	}

}
