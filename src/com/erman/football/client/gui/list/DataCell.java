package com.erman.football.client.gui.list;

import com.erman.football.shared.DataObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class DataCell extends VerticalPanel{
	
	protected DataObject data;
	protected ListPanelListener listener;
	protected boolean admin;
	
	public DataCell(ListPanelListener _listener){
		listener = _listener;
	}

	protected abstract DataCell generateCell(DataObject _data,boolean isAdmin);
	protected abstract void update(DataObject data);
	
	protected void setData(DataObject _data){
		this.data = _data;
	}
	
	protected void setListener(ListPanelListener _listener){
		this.listener = _listener;
	}
	
	public DataObject getData(){
		return data;
	}
	
	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	protected class CellDeleteHandler implements ClickHandler{

		private DataCell cell;
		
		public CellDeleteHandler(DataCell _cell){
			this.cell = _cell;
		}

		public void onClick(ClickEvent event) {
			listener.removeClicked(cell);
			event.stopPropagation();
		}
	}
	
	protected class CellModifyHandler implements ClickHandler{

		private DataCell cell;
		
		public CellModifyHandler(DataCell _cell){
			this.cell = _cell;
		}

		public void onClick(ClickEvent event) {
			listener.modifyClicked(cell);
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
