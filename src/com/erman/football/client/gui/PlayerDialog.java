package com.erman.football.client.gui;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePlayerHandler;
import com.erman.football.shared.ClientPlayer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PlayerDialog implements ParamUpdateHandler, CachePlayerHandler{

	final VerticalPanel playerInfoPanel = new VerticalPanel();

	final TextInput playerNameText = new TextInput(this);
	final TextInput playerSurnameText = new TextInput(this);
	final CheckBox playerNotifyBox = new CheckBox();
	final CheckBox playerAdminBox = new CheckBox();
	final TextInput playerEmailText = new TextInput(this);
	final Button updateButton = new Button("Apply"); 

	private Cache cache;
	private boolean add;
	private Grid playerInfos;
	private ClientPlayer player;
	private Panel basePanel;
	private boolean admin;

	public PlayerDialog(Cache _cache, Panel basePanel){
		this.cache = _cache;
		this.basePanel = basePanel;
		cache.regiserPlayer(this);
		admin = cache.getLoggedPlayer().isAdmin();

		updateButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				updateData();
			}

		});
		playerInfoPanel.setWidth("250px");
		playerInfoPanel.setBorderWidth(1);
	}

	public void render(boolean add, ClientPlayer player){
		this.add = add;
		this.player = player;
		playerInfoPanel.clear();
		if(admin){
			playerInfos = new Grid(5,2);	
			playerNameText.setEnabled(true);
			playerInfos.setWidget(0,0,playerNameText.getTextBox());
			playerSurnameText.setEnabled(true);
			playerInfos.setWidget(1,0,playerSurnameText.getTextBox());
			playerEmailText.setEnabled(true);
			playerInfos.setWidget(2,0,playerEmailText.getTextBox());
			playerInfos.setWidget(3,0,new Label("Admin:"));
			playerAdminBox.setEnabled(true);
			playerInfos.setWidget(3,1,playerAdminBox);
			playerInfos.setWidget(4, 1, updateButton);
		}else if(player.getKey()==cache.getLoggedPlayer().getKey()){
			playerInfos = new Grid(4,2);	
			playerNameText.setEnabled(true);
			playerInfos.setWidget(0,0,playerNameText.getTextBox());
			playerSurnameText.setEnabled(true);
			playerInfos.setWidget(1,0,playerSurnameText.getTextBox());
			playerEmailText.setEnabled(true);
			playerInfos.setWidget(2,0,playerEmailText.getTextBox());
			playerInfos.setWidget(3, 1, updateButton);
		}else{	
			playerInfos = new Grid(3,2);	
			playerNameText.setEnabled(false);
			playerInfos.setWidget(0,0,playerNameText.getTextBox());
			playerSurnameText.setEnabled(false);
			playerInfos.setWidget(1,0,playerSurnameText.getTextBox());
			playerAdminBox.setEnabled(false);
			playerInfos.setWidget(2,0,new Label("Admin:"));
			playerInfos.setWidget(2,1,playerAdminBox);
		}
		
		playerInfoPanel.add(playerInfos);	
		if(add){
			playerNameText.setText(player.getName(),true);
			playerSurnameText.setText(player.getSurname(),true);
			playerEmailText.setText(player.getEmail(),true);
		}else{
			playerNameText.setText(player.getName(),false);
			playerSurnameText.setText(player.getSurname(),false);
			playerEmailText.setText(player.getEmail(),false);
		}		
		playerNotifyBox.setValue(player.isNotify());
		playerAdminBox.setValue(player.isAdmin());

		basePanel.clear();
		basePanel.add(playerInfoPanel);
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
		if(!playerSurnameText.getText().equals(player.getSurname())){
			player.setSurname(playerSurnameText.getText());
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

	public void playerLoaded() {
		// TODO Auto-generated method stub
		
	}

	public void playerAdded(ClientPlayer player) {
		if(this.player.getKey()==0L){
			render(false,player);
		}
	}

	public void playerUpdated(ClientPlayer player) {
		if(this.player.getKey()==player.getKey()){
			render(false,player);
		}
	}

	public void playerRemoved(Long player) {
		if(this.player.getKey()==player){
			playerInfoPanel.removeFromParent();
		}
	}
}