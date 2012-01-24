package com.erman.football.client.gui.match;


import java.util.ArrayList;
import java.util.Date;

import com.erman.football.shared.Match;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class WeekPanel extends HorizontalPanel{
	
	private final  DateTimeFormat dateFormat = DateTimeFormat.getFormat("d MMM, EEE");
	private final  DateTimeFormat timeFormat = DateTimeFormat.getFormat("d MMM, EEE H:mm");
	private final  DateTimeFormat hourFormat = DateTimeFormat.getFormat("H:mm");
	private ArrayList<String> dateList = new ArrayList<String>();
	private ArrayList<String> hourList= new ArrayList<String>();
	private Grid hourTable;
	private Cell selectedCell;
	private Date startDate;
	private Match match;
	
	public void load(Match _match,Date _startDate){
		match = _match;
		this.clear();
		if(match.getLocation().getKey()==0){
			this.add(new Label("Lutfen bir saha secin"));
			this.setVisible(true);
			return;
		}

		long matchTime = match.getLocation().getMatchTime()*60*1000; 
		if(_startDate == null){
			startDate = timeFormat.parse(dateFormat.format(new Date())+" "+match.getLocation().getOpenTime());
		}else{
			startDate = _startDate;
		}
		
		Date closeDate = timeFormat.parse(dateFormat.format(startDate)+" "+match.getLocation().getCloseTime());
		Date time = (Date)startDate.clone();
		if(closeDate.before(startDate)){
			CalendarUtil.addDaysToDate(closeDate,1);
		}
		hourList.clear();
		Date tmpTime = (Date)time.clone();
		do{
			hourList.add(hourFormat.format(tmpTime));
			tmpTime.setTime(tmpTime.getTime()+matchTime);
		}while(tmpTime.before(closeDate));
		//hourList = new ArrayList<String>(hourArray);
		dateList.clear();
		int dayCount = 0;
		while(true){
				dayCount++;
				if(dayCount>4){
					break;
				}
				CalendarUtil.addDaysToDate(startDate,1);
				dateList.add(dateFormat.format(startDate));
		}
		int maxRow = hourList.size()+1;
		int maxColumn = dateList.size()+2;
		hourTable = new Grid(maxRow,maxColumn);
		

		
		for(int column = 0;column<maxColumn;column++){
			for(int row = 0;row<maxRow;row++){
				if(column == 0 && row!=0){
						hourTable.setText(row, 0, hourList.get(row-1));
						hourTable.getCellFormatter().setStyleName(row, 0, "hourTableHoursLeft");
						hourTable.setText(row, 5, hourList.get(row-1));
						hourTable.getCellFormatter().setStyleName(row, 5, "hourTableHoursRight");
						continue;
				}
				if(row==0 && column!=0 && column != maxColumn-1){
					hourTable.setText(row, column, dateList.get(column-1));
					hourTable.getCellFormatter().setStyleName(row, column, "hourTableHeader");
					continue;
				}
				if(row!=0 && column!=0 && column != maxColumn-1){
					Date cellDate = timeFormat.parse(dateList.get(column-1)+" "+hourList.get(row-1));
					if(cellDate.equals(match.getDate())){
						hourTable.getCellFormatter().setStyleName(row, column, "hourTableSelected");
					}else{
						hourTable.getCellFormatter().setStyleName(row, column, "hourTable");
					}					
				}
			}
		}
		Label nextLabel = new Label("<");
		nextLabel.setHorizontalAlignment(ALIGN_CENTER);
		hourTable.setWidget(0, 0,nextLabel);
		hourTable.getCellFormatter().setStyleName(0, 0, "hourTableNext");
		Label rNextLabel = new Label(">");
		rNextLabel.setHorizontalAlignment(ALIGN_CENTER);
		hourTable.setWidget(0, 5, rNextLabel);
		hourTable.getCellFormatter().setStyleName(0, 5, "hourTableNext");
		
		hourTable.setCellSpacing(0);
		hourTable.addClickHandler(new TimeClickHandler());
		this.add(hourTable);
		this.setVisible(true);
	}
	
	private class TimeClickHandler implements ClickHandler{

		public void onClick(ClickEvent event) {
			Cell clickedCell = hourTable.getCellForEvent(event);
			if(clickedCell.getCellIndex()==0 && clickedCell.getRowIndex()==0){
				CalendarUtil.addDaysToDate(startDate,-7);
				load(match,startDate);
				return;
			}
			if(clickedCell.getCellIndex()==5 && clickedCell.getRowIndex()==0){
				load(match,startDate);
				return;
			}
			Date cellDate = timeFormat.parse(dateList.get(clickedCell.getCellIndex()-1)+" "+hourList.get(clickedCell.getRowIndex()-1));
			if(cellDate.equals(match.getDate())){
				return;
			}
			
			if(selectedCell!=null){
				hourTable.getCellFormatter().setStyleName(selectedCell.getRowIndex(), selectedCell.getCellIndex(),"hourTable");
			}
			selectedCell = clickedCell;
			hourTable.getCellFormatter().setStyleName(selectedCell.getRowIndex(), selectedCell.getCellIndex(),"hourTableSelected");
			match.setDate(cellDate);
		}
		
	}
	
	public Date getSelectedDate(){
		if(selectedCell!=null){
			String date = dateList.get(selectedCell.getCellIndex()-1);
			String time = hourList.get(selectedCell.getRowIndex()-1);
			return timeFormat.parse(date+" "+time);
		}else
			return new Date(0);
	}
	
}
