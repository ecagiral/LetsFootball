package com.erman.football.client.gui.pitch;

import com.erman.football.client.gui.list.DataCell;
import com.erman.football.client.gui.list.ListPanelListener;
import com.erman.football.shared.DataObject;
import com.erman.football.shared.Pitch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class PitchCell extends DataCell{

	private Label name = new Label();
	
	public PitchCell(ListPanelListener _listener){
		super(_listener);
	}
	
	protected DataCell generateCell(DataObject _data,boolean isAdmin){
		PitchCell result = new PitchCell(listener);
		result.setData(_data);
		result.setListener(listener);
		HorizontalPanel dateDel = generateCard(result);
		result.add(dateDel);
		result.setStyleName("matchCard");
		result.addDomHandler(new CellClickHandler(result), ClickEvent.getType());
		return result;
	}

	private HorizontalPanel generateCard(PitchCell cell){
		HorizontalPanel result = new HorizontalPanel();
		result.setWidth("100%");
		cell.getName().setText(cell.getPitch().getName());
		result.add(cell.getName());
		boolean admin = true;
		if(admin){
			Image delete = new Image("delete.jpg");
			delete.addClickHandler(new CellDeleteHandler(cell));
			result.setHorizontalAlignment(ALIGN_RIGHT);
			result.add(delete);
		}
		return result;
	}
	
	public Label getName(){
		return name;
	}
	
	protected void update(DataObject data){
		Pitch pitch = (Pitch)data;
		this.data = pitch;
		name.setText(pitch.getName());
		
	}
	
	public Pitch getPitch(){
		return (Pitch)data;
	}
	
}
