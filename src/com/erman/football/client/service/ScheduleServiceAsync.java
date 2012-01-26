package com.erman.football.client.service;

import java.util.List;

import com.erman.football.shared.Schedule;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ScheduleServiceAsync {

	void addSchedule(Schedule schedule, AsyncCallback<Boolean> callback);

	void getSchedules(Long pitchId, Long startDate, AsyncCallback<List<Schedule>> callback);
	
}
