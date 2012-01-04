package com.erman.football.client.gui.list;

import com.erman.football.shared.DataObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DataCell extends VerticalPanel{
	
	protected DataObject data;
	protected ListPanelListener listener;
	
	public DataCell(ListPanelListener _listener){
		listener = _listener;
	}
	
	/**
	 * Implement in subclass
	 * @return
	 */
	protected DataCell generateCell(DataObject _data){
		DataCell result = new DataCell(listener);
		return result;
	}
	
	protected void setData(DataObject _data){
		this.data = _data;
	}
	
	protected void setListener(ListPanelListener _listener){
		this.listener = _listener;
	}
	
	public DataObject getData(){
		return data;
	}
	
	/**
	 * Implement in subclass
	 * @return
	 */
	protected void update(DataObject data){
		
	}
	
	protected class CellDeleteHandler implements ClickHandler{

		private DataCell cell;
		
		public CellDeleteHandler(DataCell _cell){
			this.cell = _cell;
		}
		@Override
		public void onClick(ClickEvent event) {
			listener.removeClicked(cell);
			event.stopPropagation();
		}
	}
	
	protected class CellClickHandler implements ClickHandler{

		private DataCell cell;
		
		public CellClickHandler(DataCell _cell){
			this.cell = _cell;
		}
		@Override
		public void onClick(ClickEvent event) {
			listener.CellClicked(cell);
		}
		
	}

}
