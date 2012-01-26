package com.erman.football.server.service;

import java.util.ArrayList;
import java.util.List;

import com.erman.football.client.service.ScheduleService;
import com.erman.football.server.data.ScheduleDO;
import com.erman.football.server.data.Schedule_JDO_DB;
import com.erman.football.shared.Schedule;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ScheduleServiceImpl  extends RemoteServiceServlet implements ScheduleService{

	@Override
	public Boolean addSchedule(Schedule schedule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Schedule> getSchedules(Long pitchId, Long startDate) {
		List<Schedule> result = new ArrayList<Schedule>();
		for(ScheduleDO schDO:Schedule_JDO_DB.getSchedule(pitchId, startDate)){
			result.add(schDO.convert());
		}
		return result;
	}

}
