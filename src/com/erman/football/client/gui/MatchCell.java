package com.erman.football.client.gui;

import com.erman.football.client.gui.list.DataCell;
import com.erman.football.client.gui.list.ListPanelListener;
import com.erman.football.shared.ClientMatch;
import com.erman.football.shared.DataObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class MatchCell extends DataCell{
	
	private Label dateTime = new Label();
	final private DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd MMM yyyy - HH:mm");

	public MatchCell(ListPanelListener _listener) {
		super(_listener);
	}

	protected DataCell generateCell(){
		return new MatchCell(listener);
	}

	protected HorizontalPanel generateCard(DataObject data){
		ClientMatch match = (ClientMatch)data;
		HorizontalPanel result = new HorizontalPanel();
		result.setWidth("100%");
		dateTime.setText(dateFormat.format(match.getDate()));
		result.add(dateTime);
		boolean admin = true;
		if(admin){
			Button delete = new Button("-");
			delete.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					listener.removeClicked(MatchCell.this);
				}
			});
			result.setHorizontalAlignment(ALIGN_RIGHT);
			result.add(delete);
		}
		return result;
	}
	
	protected void update(DataObject data){
		ClientMatch match = (ClientMatch)data;
		this.data = match;
		dateTime.setText(dateFormat.format(match.getDate()));
		
	}
	
	public ClientMatch getMatch(){
		return (ClientMatch)data;
	}
}
