package com.erman.football.client.gui.player;

import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePlayerHandler;
import com.erman.football.client.gui.pitch.DialogIf;
import com.erman.football.shared.ClientPlayer;
import com.erman.football.shared.DataObject;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PlayerDialog extends VerticalPanel implements DialogIf, CachePlayerHandler{

	private final Label playerNameText = new Label();
	private final Label playerScoreText = new Label();

	private ClientPlayer player;
	 
	public PlayerDialog(Cache cache){
		cache.regiserPlayer(this);

		HorizontalPanel namePanel = new HorizontalPanel();
		namePanel.add(new Label("Isim:"));
		namePanel.add(playerNameText);
		
		HorizontalPanel scorePanel = new HorizontalPanel();
		scorePanel.add(new Label("Puan:"));
		scorePanel.add(playerScoreText);
		playerScoreText.setWidth("30px");
		
		this.add(namePanel);
		this.add(scorePanel);
  		
	}
	
	public void render(DataObject data,Panel basePanel){
		player = (ClientPlayer)data;
		playerNameText.setText(player.getName());
		playerScoreText.setText("5.00");
		if(basePanel!=null){
			basePanel.clear();
			basePanel.add(this);
		}
		
	}

	@Override
	public void playerAdded(List<ClientPlayer> player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerUpdated(ClientPlayer player) {
		if(player.getKey()==this.player.getKey()){
			render(player,null);
		}
	}

	@Override
	public void playerRemoved(Long player) {
		if(player==this.player.getKey()){
			this.removeFromParent();
		}
	}

}