package com.erman.football.client.gui;

import java.util.HashMap;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePlayerHandler;
import com.erman.football.shared.ClientPlayer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PlayerPanel extends ScrollPanel implements CachePlayerHandler{

	
	private PlayerDialog infoDialogBox;
	
	final VerticalPanel playerEmailPanel = new VerticalPanel();	
	final HashMap<Long,PlayerCell> players = new HashMap<Long,PlayerCell>();
	
	private PlayerCell currentPlayer;
	private Cache cache;
	private boolean admin;
	
	public PlayerPanel(Cache cache, OtherPanel other){
		this.cache = cache;
		this.admin = cache.getLoggedPlayer().isAdmin();
				
		cache.regiserPlayer(this);
		infoDialogBox = new PlayerDialog(cache,other);
	
		this.setStyleName("listPanel");
		this.add(playerEmailPanel);
		playerEmailPanel.setWidth("100%");
		if(admin){
			playerEmailPanel.insert(new NewPlayerCell(),0);
		}		
	}
	
	private class PlayerCell extends VerticalPanel{
		
		private ClientPlayer player;
		private final Label name = new Label();
		
		public PlayerCell(ClientPlayer player){
			this.player = player;
			HorizontalPanel nameDel = new HorizontalPanel();
			nameDel.setWidth("100%");
			VerticalPanel over = new VerticalPanel();
			name.setText(player.getName()+" "+player.getSurname());
			over.add(name);
			nameDel.add(over);
			if(admin){
				Button delete = new Button("-");
				delete.addClickHandler(new PlayerDeleteHandler(this));
				nameDel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				nameDel.add(delete);
			}
			this.add(nameDel);
			this.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					if(currentPlayer!=null){
						currentPlayer.setStyleName("userCard");
					}
					PlayerCell.this.setStyleName("selectedUserCard");
					currentPlayer = PlayerCell.this;
					infoDialogBox.render(false, PlayerCell.this.getPlayer());
				}
			});
			this.setStyleName("userCard");
			players.put(player.getKey(),this);
		}
		
		public ClientPlayer getPlayer(){
			return player;
		}
		
		public HandlerRegistration addClickHandler(ClickHandler handler) {
		    return addDomHandler(handler, ClickEvent.getType());
		}
		
		public void update(ClientPlayer player){
			name.setText(player.getName()+" "+player.getSurname());
		}
	}
	
	private class NewPlayerCell extends VerticalPanel{
		
		public NewPlayerCell(){
			this.setWidth("100%");
			VerticalPanel over = new VerticalPanel();
			over.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			over.add(new Label("Oyuncu Ekle"));
			over.setWidth("100%");
			this.add(over);
			this.addClickHandler(new NewPlayerHandler());
			this.setStyleName("newUserCard");
		}
		
		public HandlerRegistration addClickHandler(ClickHandler handler) {
		    return addDomHandler(handler, ClickEvent.getType());
		}

	}
	
	private class PlayerDeleteHandler implements ClickHandler{

		private PlayerCell player;
		
		public PlayerDeleteHandler(PlayerCell player){
			this.player = player;
		}
		
		public void onClick(ClickEvent event) {
			cache.removePlayer(player.getPlayer().getKey());
			player.removeFromParent();
		}
		
	}
	
	private class NewPlayerHandler implements ClickHandler{

		public void onClick(ClickEvent event) {
			ClientPlayer player = new ClientPlayer();
			infoDialogBox.render(true, player);
		}
	}

	public void playerLoaded() {
		for(Long ply:cache.getAllPlayerIds()){	
			PlayerCell playerCell = new PlayerCell(cache.getPlayer(ply));
			if(admin){
				//first one new player cell
				playerEmailPanel.insert(playerCell,1);
			}else{
				playerEmailPanel.insert(playerCell,0);
			}
		}
	}

	public void playerAdded(ClientPlayer player) {
		if(currentPlayer!=null){
			currentPlayer.setStyleName("userCard");
		}
		PlayerCell playerCell = new PlayerCell(player);
		currentPlayer = playerCell;
		currentPlayer.setStyleName("selectedUserCard");
		if(admin){
			//first one new player cell
			playerEmailPanel.insert(playerCell,1);
		}else{
			playerEmailPanel.insert(playerCell,0);
		}
	}

	public void playerUpdated(ClientPlayer player) {
		players.get(player.getKey()).update(player);	
	}

	public void playerRemoved(Long player) {
		players.remove(player).removeFromParent();
	}

}
