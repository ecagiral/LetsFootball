package com.erman.football.client.gui.match;


import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class WeekPanel {
	
	private final  DateTimeFormat dateFormat = DateTimeFormat.getFormat("d MMM, EEE");
	private final Grid hourTable = new Grid(12,5);
	
	public WeekPanel(){
		
		Date date = new Date();
		for(int day = 0;day<5;day++){
			CalendarUtil.addDaysToDate(date,1);
			hourTable.setText(0, day, dateFormat.format(date));
			hourTable.getCellFormatter().setStyleName(0, day, "hourTableHeader");
			for(int hour = 1;hour<12;hour++){
				hourTable.setText(hour, day, hour+":00");
				hourTable.getCellFormatter().setStyleName(hour, day, "hourTable");
			}
		}
		hourTable.setCellSpacing(1);
	}
	
	public Panel getPanel(){
		return hourTable;
	}

}
