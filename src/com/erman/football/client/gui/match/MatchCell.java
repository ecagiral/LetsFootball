package com.erman.football.client.gui.match;

import com.erman.football.client.gui.list.DataCell;
import com.erman.football.client.gui.list.ListPanelListener;
import com.erman.football.shared.ClientMatch;
import com.erman.football.shared.DataObject;
import com.google.gwt.event.dom.client.ClickEvent;
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

	protected DataCell generateCell(DataObject _data){
		MatchCell result = new MatchCell(listener);
		result.setData(_data);
		result.setListener(listener);
		HorizontalPanel dateDel = generateCard(result);
		result.add(dateDel);
		result.setStyleName("matchCard");
		result.addDomHandler(new CellClickHandler(result), ClickEvent.getType());
		return result;
	}

	private HorizontalPanel generateCard(MatchCell cell){
		HorizontalPanel result = new HorizontalPanel();
		result.setWidth("100%");
		cell.getDateTime().setText(dateFormat.format(cell.getMatch().getDate()));
		result.add(cell.getDateTime());
		boolean admin = true;
		if(admin){
			Button delete = new Button("-");
			delete.addClickHandler(new CellDeleteHandler(cell));
			result.setHorizontalAlignment(ALIGN_RIGHT);
			result.add(delete);
		}
		return result;
	}
	
	public Label getDateTime(){
		return dateTime;
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
