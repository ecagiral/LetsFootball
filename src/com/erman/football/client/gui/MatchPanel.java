package com.erman.football.client.gui;

import java.util.LinkedHashMap;
import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CacheMatchHandler;
import com.erman.football.shared.ClientMatch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MatchPanel extends HorizontalPanel implements CacheMatchHandler, FilterHandler{
	
	
	private MatchDialog matchDialog;	
	final private VerticalPanel matchTimePanel = new VerticalPanel();
	final private LinkedHashMap<Long,MatchCell> matches = new LinkedHashMap<Long,MatchCell>();
	final private MatchFilterPanel filter;
	final private DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd MMM yyyy - HH:mm");
	final private UpdateListCell updateListCell = new UpdateListCell();
	final private StatusCell status = new StatusCell();
	final private VerticalPanel listMainPanel = new VerticalPanel();
	final private SimplePanel infoPanel = new SimplePanel();
	
	private boolean admin;
	private Cache cache;
	private MatchCell currentMatch;
	
	public MatchPanel(Cache cache){
		this.cache = cache;
		cache.regiserMatch(this);
		admin = cache.getLoggedPlayer().isAdmin();
		matchDialog = new MatchDialog(cache);
		filter =  new MatchFilterPanel(cache,this);
		VerticalPanel buttonPanel = new VerticalPanel();
		Label addMatch = new Label("Mac Ekle");
		addMatch.setStyleName("leftButton");
		addMatch.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				displayMatch(null);
			}
		});
		buttonPanel.add(addMatch);
		Label searchMatch = new Label("Mac Ara");
		searchMatch.setStyleName("leftButton");
		searchMatch.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				infoPanel.clear();
				listMainPanel.setVisible(true);
			}
		});
		buttonPanel.add(searchMatch);
		buttonPanel.setWidth("100px");
		this.add(buttonPanel);
		
		listMainPanel.setStyleName("listMainPanel");
		listMainPanel.add(filter);
		listMainPanel.setCellHorizontalAlignment(filter, ALIGN_CENTER);
		ScrollPanel listPanel = new ScrollPanel();
		listPanel.setStyleName("listPanel");
		listPanel.add(matchTimePanel);
		matchTimePanel.setWidth("100%");
		matchTimePanel.add(status);
		listMainPanel.add(listPanel);
		this.add(listMainPanel);
		this.add(infoPanel);
	}

	public void matchAdded(List<ClientMatch> matches) {
		status.removeFromParent();
		int index = 1;
		for(ClientMatch match:matches){
			if(index==MatchFilterPanel.PAGINATION_NUM){
				break;
			}
			new MatchCell(match);
			index++;
		}
		if(matches.size() == MatchFilterPanel.PAGINATION_NUM){
			matchTimePanel.add(updateListCell);
		}
	}

	public void matchUpdated(ClientMatch match) {
		matches.get(new Long(match.getKey())).update(match);
	}

	public void matchRemoved(Long match) {
		matches.remove(match).removeFromParent();
	}
	
	private void displayMatch(MatchCell cell){
		if(cell == null){
			listMainPanel.setVisible(false);
			matchDialog.render(new ClientMatch(),true,infoPanel);	
		}else{
			if(currentMatch!=null){
				currentMatch.setStyleName("matchCard");
			}
			cell.setStyleName("selectedMatchCard");
			currentMatch = cell;
			matchDialog.render(cell.getMatch(),false,infoPanel);	
		}
		
	}

	private class MatchCell extends VerticalPanel{
		
		private ClientMatch match;
		private Label dateTime = new Label();
		private Label location = new Label();
		
		public MatchCell(ClientMatch match){
			this.match = match;
			HorizontalPanel dateDel = new HorizontalPanel();
			dateDel.setWidth("100%");
			dateTime.setText(dateFormat.format(match.getDate()));
			String locationName;
			try{
				Long locationId = Long.parseLong(match.getLocation());
				locationName = cache.getPitch(locationId).getName(); 
			}catch(Exception e){
				locationName = "Bilinmiyor";
			}
			
			location.setText(locationName);
			dateDel.add(dateTime);
			if(admin){
				Button delete = new Button("-");
				delete.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {
						cache.removeMatch(MatchCell.this.getMatch());
					}
				});
				dateDel.setHorizontalAlignment(ALIGN_RIGHT);
				dateDel.add(delete);
			}
			this.add(dateDel);
			this.add(location);
			this.setStyleName("matchCard");
			this.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					displayMatch(MatchCell.this);
				}
			});
			matches.put(match.getKey(),this);
			matchTimePanel.add(this);
		}
		public HandlerRegistration addClickHandler(ClickHandler handler) {
		    return addDomHandler(handler, ClickEvent.getType());
		}
		
		public ClientMatch getMatch(){
			return match;
		}
		
		public void update(ClientMatch _match){
			this.match = _match;
			dateTime.setText(dateFormat.format(match.getDate()));
			String locationName;
			try{
				Long locationId = Long.parseLong(match.getLocation());
				locationName = cache.getPitch(locationId).getName(); 
			}catch(Exception e){
				locationName = "Bilinmiyor";
			}
			location.setText(locationName);
		}
	}
	
	private class UpdateListCell extends VerticalPanel{
		
		public UpdateListCell(){
			Label label = new Label("Daha Fazla");
			this.setHorizontalAlignment(ALIGN_CENTER);
			this.add(label);
			this.addDomHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					filter.applyFilter(true);
					UpdateListCell.this.removeFromParent();
				}
			}, ClickEvent.getType());
			this.setStyleName("matchCard");
		}

	}
	
	private class StatusCell extends VerticalPanel{
		
		public StatusCell(){
			this.setHorizontalAlignment(ALIGN_CENTER);
			this.add(new Image("loader.gif"));
			this.setSpacing(10);
			this.setWidth("100%");
		}
	}

	public void filterApplied(boolean pagination) {
		if(!pagination){
			matchTimePanel.clear();
			matches.clear();
		}
		matchTimePanel.add(status);
	}
	
	
	

}

