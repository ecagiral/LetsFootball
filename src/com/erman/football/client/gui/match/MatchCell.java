package com.erman.football.client.gui.match;

import com.erman.football.client.gui.list.DataCell;
import com.erman.football.client.gui.list.ListPanelListener;
import com.erman.football.shared.ClientMatch;
import com.erman.football.shared.DataObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class MatchCell extends DataCell{
	
	private Label summary = new Label();
	
	final private DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd MMM yyyy - HH:mm");

	public MatchCell(ListPanelListener _listener) {
		super(_listener);
	}

	protected DataCell generateCell(DataObject _data,boolean isAdmin){
		MatchCell result = new MatchCell(listener);
		result.setData(_data);
		result.setListener(listener);
		result.setAdmin(isAdmin);
		HorizontalPanel dateDel = generateCard(result);
		result.add(dateDel);
		result.setStyleName("matchCard");
		result.addDomHandler(new CellClickHandler(result), ClickEvent.getType());
		return result;
	}

	private HorizontalPanel generateCard(MatchCell cell){
		HorizontalPanel result = new HorizontalPanel();
		result.setWidth("100%");
		cell.getSummary().setText(dateFormat.format(cell.getMatch().getDate())+" "+cell.getMatch().getLocation());
		result.add(cell.getSummary());
		if(cell.isAdmin()){
			Image edit = new Image("modify.png");
			edit.addClickHandler(new CellModifyHandler(cell));
			result.setHorizontalAlignment(ALIGN_RIGHT);
			result.add(edit);
			Image delete = new Image("delete.png");
			delete.addClickHandler(new CellDeleteHandler(cell));
			result.setHorizontalAlignment(ALIGN_RIGHT);
			result.add(delete);
		}
		return result;
	}
	
	public Label getSummary(){
		return summary;
	}
	
	protected void update(DataObject data){
		ClientMatch match = (ClientMatch)data;
		this.data = match;
		summary.setText(dateFormat.format(match.getDate())+" "+match.getLocation());
		
	}
	
	public ClientMatch getMatch(){
		return (ClientMatch)data;
	}
	
}
