package com.erman.football.client.gui.list;

import java.util.HashMap;
import java.util.List;

import com.erman.football.client.cache.DataHandler;
import com.erman.football.shared.DataObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ListPanel extends VerticalPanel implements DataHandler,FilterHandler{

	final private VerticalPanel cardListPanel = new VerticalPanel();
	final private StatusCell status = new StatusCell();
	final private ListFilter filter;
	final private UpdateListCell updateListCell = new UpdateListCell();
	final private HashMap<Long,DataCell> cellList = new HashMap<Long,DataCell>();	
	final private DataCell dataCell;
	
	public ListPanel(ListFilter _filter,DataCell _dataCell){
		this.dataCell = _dataCell;
		this.setStyleName("listMainPanel");
		this.filter = _filter;
		this.add(filter);
		this.setCellHorizontalAlignment(filter, ALIGN_CENTER);
		ScrollPanel scrollListPanel = new ScrollPanel();
		scrollListPanel.setStyleName("listPanel");
		cardListPanel.setWidth("100%");
		cardListPanel.add(status);
		scrollListPanel.add(cardListPanel);
		this.add(scrollListPanel);
	}
	
	private class StatusCell extends VerticalPanel{
		
		public StatusCell(){
			this.setHorizontalAlignment(ALIGN_CENTER);
			this.add(new Image("loader.gif"));
			this.setSpacing(10);
			this.setWidth("100%");
		}
	}
	
	private class UpdateListCell extends VerticalPanel{
		
		public UpdateListCell(){
			Label label = new Label("Daha Fazla");
			this.setHorizontalAlignment(ALIGN_CENTER);
			this.add(label);
			this.addDomHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					filter.applyFilter(true);
					UpdateListCell.this.removeFromParent();
				}
			}, ClickEvent.getType());
			this.setStyleName("matchCard");
		}

	}

	public void dataAdded(List<DataObject> data) {
		status.removeFromParent();
		int index = 1;
		for(DataObject aData:data){
			if(index==ListFilter.PAGINATION_NUM){
				break;
			}
			DataCell cell = dataCell.generateCell(aData);
			cellList.put(aData.getKey(), cell);
			cardListPanel.add(cell);
			index++;
		}
		if(data.size() == ListFilter.PAGINATION_NUM){
			cardListPanel.add(updateListCell);
		}
	}

	public void dataRemoved(List<Long> dataId) {
		cellList.remove(dataId).removeFromParent();
	}

	public void dataUpdated(List<DataObject> data) {
		cellList.get(data.get(0).getKey()).update(data.get(0));
		
	}
	

	public void filterApplied(boolean pagination) {
		if(!pagination){
			cardListPanel.clear();
			cellList.clear();
		}
		cardListPanel.add(status);
	}
}
