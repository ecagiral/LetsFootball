package com.erman.football.client.gui.player;

import java.util.ArrayList;
import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePlayerHandler;
import com.erman.football.client.gui.list.DataCell;
import com.erman.football.client.gui.list.ListPanel;
import com.erman.football.client.gui.list.ListPanelListener;
import com.erman.football.shared.ClientPlayer;
import com.erman.football.shared.DataObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PlayerPanel extends HorizontalPanel implements CachePlayerHandler ,ListPanelListener, PlayerEditDialogHandler{

	final private PlayerEditDialog playerEditDialog;
	final private PlayerDialog playerDialog;	
	final private ListPanel listMainPanel;
	final private SimplePanel infoPanel = new SimplePanel();
	final private Cache cache;

	private PlayerCell currentPlayer;

	public PlayerPanel(Cache _cache){
		this.cache = _cache;
		cache.regiserPlayer(this);
		playerEditDialog = new PlayerEditDialog(cache,this);
		playerDialog = new PlayerDialog(cache);
		PlayerFilterPanel filter = new PlayerFilterPanel(cache);
		listMainPanel = new ListPanel(filter, new PlayerCell(this));
		filter.setHandler(listMainPanel);
		VerticalPanel buttonPanel = new VerticalPanel();
		buttonPanel.setStyleName("leftPanel");
		if(_cache.getLoggedPlayer().isAdmin()){
			Label addMatch = new Label("Oyuncu Ekle");
			addMatch.setStyleName("leftButton");
			addMatch.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					displayPlayer(null);
				}
			});
			buttonPanel.add(addMatch);

			Label searchMatch = new Label("Oyuncu Ara");
			searchMatch.setStyleName("leftButton");
			searchMatch.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					infoPanel.clear();
					listMainPanel.setVisible(true);
				}
			});
			buttonPanel.add(searchMatch);
		}
		SimplePanel summaryPanel = new SimplePanel();
		Label summaryLabel = new Label("Bu paneli kullanarak oyuncu listesini gorebilir, kendi bilgilerinizi guncelleyebilirsiniz.");
		summaryPanel.add(summaryLabel);
		summaryPanel.setStyleName("summaryLabel");
		buttonPanel.add(summaryPanel);

		this.add(buttonPanel);
		this.add(listMainPanel);
		this.add(infoPanel);
	}

	private void displayPlayer(PlayerCell cell){
		if(cell == null){
			listMainPanel.setVisible(false);
			playerEditDialog.render(new ClientPlayer(),infoPanel);	
		}else{
			if(currentPlayer!=null){
				currentPlayer.setStyleName("matchCard");
			}
			cell.setStyleName("selectedMatchCard");
			currentPlayer = cell;
			if(cache.getLoggedPlayer().isAdmin() || cache.getLoggedPlayer().getKey() == cell.getPlayer().getKey()){
				playerEditDialog.render(cell.getPlayer(),infoPanel);
			}else{
				playerDialog.render(cell.getPlayer(),infoPanel);	
			}
			
		}

	}

	public void CellClicked(DataCell dataCell) {
		displayPlayer((PlayerCell)dataCell);
	}

	public void removeClicked(DataCell dataCell) {
		cache.removePlayer(((PlayerCell)dataCell).getPlayer().getKey());
	}

	public void playerAdded(List<ClientPlayer> player) {
		ArrayList<DataObject> data = new ArrayList<DataObject>();
		data.addAll(player);
		listMainPanel.dataAdded(data,cache.getLoggedPlayer().getKey());
	}

	public void playerUpdated(ClientPlayer player) {
		ArrayList<DataObject> data = new ArrayList<DataObject>();
		data.add(player);
		listMainPanel.dataUpdated(data);
	}

	public void playerRemoved(Long player) {
		ArrayList<Long> data = new ArrayList<Long>();
		data.add(player);
		listMainPanel.dataRemoved(data);
	}

	@Override
	public void modifyClicked(DataCell dataCell) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endClicked(DataCell dataCell, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleModify(ClientPlayer player) {
		cache.updatePlayer(player);
	}

}
