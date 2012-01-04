package com.erman.football.client.gui.player;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.gui.ParamUpdateHandler;
import com.erman.football.client.gui.TextInput;
import com.erman.football.shared.ClientPlayer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PlayerDialog implements ParamUpdateHandler{

	final VerticalPanel playerInfoPanel = new VerticalPanel();

	final TextInput playerNameText = new TextInput(this);
	final CheckBox playerNotifyBox = new CheckBox();
	final CheckBox playerAdminBox = new CheckBox();
	final TextInput playerEmailText = new TextInput(this);
	final Button updateButton = new Button("Apply"); 

	private Cache cache;
	private boolean add;
	private Grid playerInfos;
	private ClientPlayer player;
	private boolean admin;

	public PlayerDialog(Cache _cache){
		this.cache = _cache;
		admin = cache.getLoggedPlayer().isAdmin();

		updateButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				updateData();
			}

		});
		playerInfoPanel.setWidth("250px");
		playerInfoPanel.setBorderWidth(1);
	}

	public void render(boolean add, ClientPlayer player, Panel parent){
		this.add = add;
		this.player = player;
		playerInfoPanel.clear();
		if(admin){
			playerInfos = new Grid(4,2);	
			playerNameText.setEnabled(true);
			playerInfos.setWidget(0,0,playerNameText.getTextBox());
			playerEmailText.setEnabled(true);
			playerInfos.setWidget(1,0,playerEmailText.getTextBox());
			playerInfos.setWidget(2,0,new Label("Admin:"));
			playerAdminBox.setEnabled(true);
			playerInfos.setWidget(2,1,playerAdminBox);
			playerInfos.setWidget(3, 1, updateButton);
		}else if(player.getKey()==cache.getLoggedPlayer().getKey()){
			playerInfos = new Grid(4,2);	
			playerNameText.setEnabled(true);
			playerInfos.setWidget(0,0,playerNameText.getTextBox());
			playerEmailText.setEnabled(true);
			playerInfos.setWidget(1,0,playerEmailText.getTextBox());
			playerInfos.setWidget(2, 1, updateButton);
		}else{	
			playerInfos = new Grid(1,2);	
			playerNameText.setEnabled(false);
			playerInfos.setWidget(0,0,playerNameText.getTextBox());
			playerAdminBox.setEnabled(false);
			playerInfos.setWidget(1,0,new Label("Admin:"));
			playerInfos.setWidget(1,1,playerAdminBox);
		}
		
		playerInfoPanel.add(playerInfos);	
		if(add){
			playerNameText.setText(player.getName(),add);
			playerEmailText.setText(player.getEmail(),add);
		}else{
			playerNameText.setText(player.getName(),add);
			playerEmailText.setText(player.getEmail(),add);
		}		
		playerNotifyBox.setValue(player.isNotify());
		playerAdminBox.setValue(player.isAdmin());
		parent.clear();
		parent.add(playerInfoPanel);
		if(add){
			playerNameText.setFocus(add);
		}
	}

	private boolean retrieveData(){
		boolean result = false;
		if(!playerAdminBox.getValue().equals(player.isAdmin())){
			player.setAdmin(playerAdminBox.getValue());
			result = true;
		}
		if(!playerNotifyBox.getValue().equals(player.isNotify())){
			player.setNotify(playerNotifyBox.getValue());
			result = true;
		}
		if(!playerNameText.getText().equals(player.getName())){
			player.setName(playerNameText.getText());
			result = true;
		}
		if(!playerEmailText.getText().equals(player.getEmail())){
			player.setEmail(playerEmailText.getText());
			result = true;
		}
		return result;
	}

	public void updateData() {
		if(retrieveData()){
			if(add){
				PlayerDialog.this.cache.addPlayer(player);
				playerInfoPanel.removeFromParent();
				add=false;
			}else{
				PlayerDialog.this.cache.updatePlayer(player);
			}
		}
	}

}