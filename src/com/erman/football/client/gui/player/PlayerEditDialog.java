package com.erman.football.client.gui.player;

import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePlayerHandler;
import com.erman.football.client.gui.ParamUpdateHandler;
import com.erman.football.client.gui.TextInput;
import com.erman.football.client.gui.pitch.DialogIf;
import com.erman.football.shared.ClientPlayer;
import com.erman.football.shared.DataObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PlayerEditDialog extends VerticalPanel implements DialogIf, ParamUpdateHandler, CachePlayerHandler{


	private final TextInput playerNameText = new TextInput(this);
	private final TextInput playerEmailText = new TextInput(this);
	private final CheckBox adminCheck = new CheckBox();
	private final Button updateButton = new Button("Guncelle");
	private final Image laodImg = new Image("loader.gif");
	private final Image successImg = new Image("success.jpg");
	private final PlayerEditDialogHandler handler;
	
	private Cache cache;
	private ClientPlayer player;
	private boolean inProgress;
	 
	public PlayerEditDialog(Cache cache,PlayerEditDialogHandler _handler){
		this.cache = cache;
		this.handler = _handler;
		cache.regiserPlayer(this);
		
		Grid infoGrid = new Grid(4,2);
		infoGrid.setWidget(0, 0,new Label("Isim:"));
		infoGrid.setWidget(0, 1,playerNameText.getTextBox());
		playerNameText.getTextBox().setWidth("194px");
		playerNameText.getTextBox().setMaxLength(24);
		
		infoGrid.setWidget(1, 0,new Label("Email:"));
		infoGrid.setWidget(1, 1,playerEmailText.getTextBox());
		playerEmailText.getTextBox().setWidth("194px");
		playerEmailText.getTextBox().setMaxLength(24);
		
		infoGrid.setWidget(2, 0,new Label("Admin"));
		infoGrid.setWidget(2, 1,adminCheck);
		
		updateButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				updateData();
			}

		});

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(updateButton);
		buttonPanel.add(laodImg);
		buttonPanel.add(successImg);		
		infoGrid.setWidget(3,1,buttonPanel);
		laodImg.setVisible(false);
		successImg.setVisible(false);
		
		this.add(infoGrid);	
	}
	
	public void render(DataObject data,Panel basePanel){
		boolean add;
		player = (ClientPlayer)data;
		if(player.getKey()==0){
			add = true;
		}else{
			add = false;
		}
		laodImg.setVisible(false);
		if(add){
			updateButton.setText("Ekle");
		}else{
			updateButton.setText("Guncelle");
		}
		updateButton.setEnabled(true);
		playerNameText.setText(player.getName(),add );
		playerEmailText.setText(player.getEmail(),add );
		adminCheck.setValue(player.isAdmin());
		inProgress = false;
		if(basePanel!=null){
			basePanel.clear();
			basePanel.add(this);
			successImg.setVisible(false);
		}
	}
	
	private boolean retrieveData(){
		player.setName(playerNameText.getText());
		player.setAdmin(adminCheck.getValue());
		player.setEmail(playerEmailText.getText());
		return true;
	}
	
	@Override
	public void playerAdded(List<ClientPlayer> player) {
		if(inProgress){
			successImg.setVisible(true);
			render(player.get(0),null);
		}
	}

	@Override
	public void playerUpdated(ClientPlayer player) {
		if(inProgress){
			successImg.setVisible(true);
			render(player,null);
		}
	}

	@Override
	public void playerRemoved(Long player) {
		if(inProgress){
			this.removeFromParent();
			inProgress = false;
		}
	}

	@Override
	public void updateData() {
		inProgress = true;
		retrieveData();
		handler.handleModify(player);
		updateButton.setEnabled(false);
		laodImg.setVisible(true);
	}


}
