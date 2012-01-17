package com.erman.football.client.gui.player;

import com.erman.football.client.gui.list.DataCell;
import com.erman.football.client.gui.list.ListPanelListener;
import com.erman.football.shared.ClientPlayer;
import com.erman.football.shared.DataObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class PlayerCell extends DataCell{
	
	private Label name = new Label();

	public PlayerCell(ListPanelListener _listener) {
		super(_listener);
	}

	protected DataCell generateCell(DataObject _data,boolean isAdmin){
		PlayerCell result = new PlayerCell(listener);
		result.setData(_data);
		result.setAdmin(isAdmin);
		result.setListener(listener);
		HorizontalPanel dateDel = generateCard(result);
		result.add(dateDel);
		result.setStyleName("matchCard");
		result.addDomHandler(new CellClickHandler(result), ClickEvent.getType());
		return result;
	}

	private HorizontalPanel generateCard(PlayerCell cell){
		HorizontalPanel result = new HorizontalPanel();
		result.setWidth("100%");
		cell.getName().setText(cell.getPlayer().getName());
		result.add(cell.getName());
		if(cell.isAdmin()){
			Image delete = new Image("delete.png");
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
		ClientPlayer player = (ClientPlayer)data;
		this.data = player;
		name.setText(player.getName());
		
	}
	
	public ClientPlayer getPlayer(){
		return (ClientPlayer)data;
	}
	
}
