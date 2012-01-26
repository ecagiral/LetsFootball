package com.erman.football.client.service;

import java.util.List;

import com.erman.football.shared.Schedule;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("schedule")
public interface ScheduleService extends RemoteService{
	
	public Boolean addSchedule(Schedule schedule);
	
	/**
	 * Returns schedule list starting from start date for a pitch
	 * @param startDate 
	 * @param from
	 * @param to
	 * @return
	 */
	public List<Schedule> getSchedules(Long pitchId, Long startDate);
}
