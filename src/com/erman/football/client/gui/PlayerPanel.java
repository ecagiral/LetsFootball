package com.erman.football.client.gui;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PlayerPanel extends HorizontalPanel implements CachePlayerHandler{


	private PlayerDialog infoDialogBox;

	final private VerticalPanel playerEmailPanel = new VerticalPanel();	
	final private HashMap<Long,PlayerCell> players;
	final private SimplePanel dialogPanel = new SimplePanel();
	private PlayerCell currentPlayer;
	private Cache cache;
	private boolean admin;

	public PlayerPanel(Cache cache){
		this.cache = cache;
		this.admin = cache.getLoggedPlayer().isAdmin();
		players = new HashMap<Long,PlayerCell>();
		cache.regiserPlayer(this);
		
		VerticalPanel buttonPanel = new VerticalPanel();
		Label addMatch = new Label("Oyuncu Ekle");
		addMatch.setStyleName("leftButton");
		buttonPanel.add(addMatch);
		Label searchMatch = new Label("Oyuncu Ara");
		searchMatch.setStyleName("leftButton");
		buttonPanel.add(searchMatch);
		buttonPanel.setWidth("100px");
		this.add(buttonPanel);
		
		infoDialogBox = new PlayerDialog(cache,dialogPanel);
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setStyleName("listPanel");
		scrollPanel.add(playerEmailPanel);
		playerEmailPanel.setWidth("100%");	
		this.add(scrollPanel);
		this.add(dialogPanel);
	}

	private class PlayerCell extends VerticalPanel{
		private final Label name = new Label();	
		private Long playerId;
		public PlayerCell(Long playerId){
			this.playerId = playerId;
			HorizontalPanel nameDel = new HorizontalPanel();
			nameDel.setWidth("100%");
			VerticalPanel over = new VerticalPanel();
			name.setText(cache.getPlayer(playerId).getName()+" "+cache.getPlayer(playerId).getSurname());
			over.add(name);
			nameDel.add(over);
			if(admin){
				Button delete = new Button("-");
				delete.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {
						cache.removePlayer(PlayerCell.this.playerId);
					}
				});
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
					infoDialogBox.render(false, cache.getPlayer(PlayerCell.this.playerId));
				}
			});
			this.setStyleName("userCard");
			players.put(playerId,this);
		}

		public HandlerRegistration addClickHandler(ClickHandler handler) {
			return addDomHandler(handler, ClickEvent.getType());
		}

		public void update(){
			name.setText(cache.getPlayer(playerId).getName()+" "+cache.getPlayer(playerId).getSurname());
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
			this.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					ClientPlayer player = new ClientPlayer();
					infoDialogBox.render(true, player);
				}
			});
			this.setStyleName("newUserCard");
		}

		public HandlerRegistration addClickHandler(ClickHandler handler) {
			return addDomHandler(handler, ClickEvent.getType());
		}

	}

	public void playerLoaded() {
		for(Long ply:cache.getAllPlayerIds()){	
			new PlayerCell(ply);
		}
		updateList();
	}

	public void playerAdded(ClientPlayer player) {
		if(currentPlayer!=null){
			currentPlayer.setStyleName("userCard");
		}
		PlayerCell playerCell = new PlayerCell(player.getKey());
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
		players.get(player.getKey()).update();	
	}

	public void playerRemoved(Long player) {
		players.remove(player).removeFromParent();
	}

	private void updateList(){
		playerEmailPanel.clear();
		if(admin){
			playerEmailPanel.insert(new NewPlayerCell(),0);
		}
		ValueComparator comp = new ValueComparator(players);
		TreeMap<Long,PlayerCell> ordPlayers = new TreeMap(comp);
		ordPlayers.putAll(players);
		for(PlayerCell playerCell: ordPlayers.values()){
			if(admin){
				//first one new player cell
				playerEmailPanel.insert(playerCell,1);
			}else{
				playerEmailPanel.insert(playerCell,0);
			}
		}
	}

	class ValueComparator implements Comparator<Long>{

		Map base;
		public ValueComparator(Map<Long,PlayerCell> base) {
			this.base = base;
		}

		public int compare(Long a, Long b) {
			String aName = cache.getPlayer(a).getFullName();
			String bName = cache.getPlayer(b).getFullName();
			return bName.compareToIgnoreCase(aName);

		}
	}

}
