package com.erman.football.server.data;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class Schedule_JDO_DB {
	
	@SuppressWarnings("unchecked")
	public static List<ScheduleDO> getSchedule(long pitch,long time){
		List<ScheduleDO> scheduleDOs = new ArrayList<ScheduleDO>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(ScheduleDO.class);
		query.setOrdering("time ascending");
		query.setRange(0,10);
		query.declareParameters("long pitch, long startDate");
		query.setFilter("pitchId == pitch && time >= startDate");
		try {
			scheduleDOs = (List<ScheduleDO>)query.execute(pitch,time);
		}catch(Exception e){
			System.out.println(e.getMessage());
		} finally {
			query.closeAll();
		}
		return scheduleDOs;
	}

}
