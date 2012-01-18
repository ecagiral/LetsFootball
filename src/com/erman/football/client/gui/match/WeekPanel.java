package com.erman.football.client.gui.match;


import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class WeekPanel {
	
	private final  DateTimeFormat dateFormat = DateTimeFormat.getFormat("d MMM, EEE");
	private final  DateTimeFormat timeFormat = DateTimeFormat.getFormat("d MMM, EEE h");
	private final Grid hourTable = new Grid(12,5);
	private Date[][] dateTable = new Date[12][5];
	
	private Cell selectedCell;
	
	public WeekPanel(){
		
		Date date = new Date();
		for(int day = 0;day<5;day++){
			CalendarUtil.addDaysToDate(date,1);
			hourTable.setText(0, day, dateFormat.format(date));
			hourTable.getCellFormatter().setStyleName(0, day, "hourTableHeader");
			for(int hour = 1;hour<12;hour++){
				hourTable.setText(hour, day, hour+":00");
				dateTable[hour][day] = timeFormat.parse(dateFormat.format(date)+" "+hour);
				hourTable.getCellFormatter().setStyleName(hour, day, "hourTable");
			}
		}
		hourTable.setCellSpacing(1);
		hourTable.addClickHandler(new TimeClickHandler());
	}
	
	public Panel getPanel(){
		return hourTable;
	}
	
	private class TimeClickHandler implements ClickHandler{

		public void onClick(ClickEvent event) {
			if(selectedCell!=null){
				hourTable.getCellFormatter().setStyleName(selectedCell.getRowIndex(), selectedCell.getCellIndex(),"hourTable");
			}
			selectedCell = hourTable.getCellForEvent(event);
			hourTable.getCellFormatter().setStyleName(selectedCell.getRowIndex(), selectedCell.getCellIndex(),"hourTableSelected");
			//System.out.println(timeFormat.format(dateTable[selectedCell.getRowIndex()][selectedCell.getCellIndex()]));
		}
		
	}
	
	public Date getSelectedDate(){
		if(selectedCell!=null){
			return dateTable[selectedCell.getRowIndex()][selectedCell.getCellIndex()];
		}else
			return new Date();
	}

}
