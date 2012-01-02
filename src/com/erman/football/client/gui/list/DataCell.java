package com.erman.football.client.gui.list;

import com.erman.football.shared.DataObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DataCell extends VerticalPanel{
	
	protected DataObject data;
	protected ListPanelListener listener;
	
	public DataCell(ListPanelListener _listener){
		listener = _listener;
	}
	
	public DataCell generateCell(DataObject _data){
		DataCell result = generateCell();
		result.setData(_data);
		result.setListener(listener);
		HorizontalPanel dateDel = generateCard(_data);
		result.add(dateDel);
		result.setStyleName("matchCard");
		result.addDomHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				listener.CellClicked(DataCell.this);
			}
			
		}, ClickEvent.getType());
		return result;
	}
	
	private void setData(DataObject _data){
		this.data = _data;
	}
	
	private void setListener(ListPanelListener _listener){
		this.listener = _listener;
	}
	/**
	 * Implement in subclass
	 * @return
	 */
	protected DataCell generateCell(){
		return new DataCell(listener);
	}
	
	public DataObject getData(){
		return data;
	}
	
	/**
	 * Implement in subclass
	 * @return
	 */
	protected HorizontalPanel generateCard(DataObject data){
		HorizontalPanel result = new HorizontalPanel();
		result.add(new Label("Empty"));
		return result;
	}
	
	/**
	 * Implement in subclass
	 * @return
	 */
	protected void update(DataObject data){
		
	}

}
