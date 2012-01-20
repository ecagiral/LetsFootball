package com.erman.football.client.gui.match;


import java.util.ArrayList;
import java.util.Date;

import com.erman.football.shared.Pitch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class WeekPanel extends HorizontalPanel{
	
	private final  DateTimeFormat dateFormat = DateTimeFormat.getFormat("d MMM, EEE");
	private final  DateTimeFormat timeFormat = DateTimeFormat.getFormat("d MMM, EEE H:mm");
	private final  DateTimeFormat dayFormat = DateTimeFormat.getFormat("d");
	private final  DateTimeFormat hourFormat = DateTimeFormat.getFormat("H:mm");
	private ArrayList<ArrayList<Date>> dateList;
	final private VerticalPanel hourList= new VerticalPanel();
	private Grid hourTable;
	private Cell selectedCell;
	
	public void load(Pitch pitch){
		this.clear();
		if(pitch==null){
			this.add(new Label("Lutfen bir mac secin"));
			return;
		}

		long matchTime = pitch.getMatchTime()*60*1000; 
		Date startDate = timeFormat.parse(dateFormat.format(new Date())+" "+pitch.getOpenTime());
		Date time = (Date)startDate.clone();
		Date closeDate = timeFormat.parse(dateFormat.format(new Date())+" "+pitch.getCloseTime());
		if(closeDate.before(startDate)){
			CalendarUtil.addDaysToDate(closeDate,1);
		}
		
		dateList = new ArrayList<ArrayList<Date>>();
		hourList.clear();
		int dayCount = 0;
		String day = "0"; 
		ArrayList<Date> dateColumn = null;
		while(true){
			if(!dayFormat.format(time).equals(day)){
				//day updated
				day = dayFormat.format(time);
				dayCount++;
				if(dayCount>4){
					break;
				}
				dateColumn = new ArrayList<Date>();
				dateList.add(dateColumn);
				dateColumn.add(time);
			}
			dateColumn.add((Date)time.clone());
			time.setTime(time.getTime()+matchTime);
			if(time.getTime()+matchTime > closeDate.getTime()){
				//new session for pitch
				CalendarUtil.addDaysToDate(startDate,1);
				CalendarUtil.addDaysToDate(closeDate,1);
				time = (Date)startDate.clone();
			}			
		}
		hourTable = new Grid(dateList.get(0).size(),dateList.size()+1);
		int column = 1;
		for(ArrayList<Date> columnList:dateList){
			int row = 0;
			for(Date cellDate:columnList){
				if(column == 1){
					if(row!=0){
						hourTable.setText(row, 0, hourFormat.format(cellDate));
					}
				}
				if(row==0){
					hourTable.setText(row, column, dateFormat.format(cellDate));
					hourTable.getCellFormatter().setStyleName(row, column, "hourTableHeader");
				}else{
					//hourTable.setText(row, column, hourFormat.format(cellDate));
					hourTable.getCellFormatter().setStyleName(row, column, "hourTable");
				}

				row++;
			}
			column++;
		}
		hourTable.setCellSpacing(0);
		hourTable.addClickHandler(new TimeClickHandler());
		this.add(hourList);
		this.add(hourTable);
		this.setVisible(true);
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
			return dateList.get(selectedCell.getCellIndex()).get(selectedCell.getRowIndex());
		}else
			return new Date();
	}
}
