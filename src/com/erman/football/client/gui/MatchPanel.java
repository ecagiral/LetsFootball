package com.erman.football.client.gui;

import java.util.HashMap;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CacheMatchHandler;
import com.erman.football.shared.ClientMatch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MatchPanel extends ScrollPanel implements CacheMatchHandler{
	
	private MatchDialog matchDialog;
	
	final VerticalPanel matchTimePanel = new VerticalPanel();
	final DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yy");
	final HashMap<Long,MatchCell> matches = new HashMap<Long,MatchCell>();
	
	private boolean admin;
	private Cache cache;
	private MatchCell currentMatch;
	private OtherPanel other;
	
	public MatchPanel(Cache cache, OtherPanel other){
		this.cache = cache;
		this.other = other;
		cache.regiserMatch(this);
		admin = cache.getLoggedPlayer().isAdmin();
		matchDialog = new MatchDialog(cache);
		
		//matchTimePanel.setWidth("210px");
		
		//this.setWidth("250px");
		//this.setHeight("500px");
		
		this.setStyleName("listPanel");
		this.add(matchTimePanel);
		matchTimePanel.setWidth("100%");
		matchTimePanel.insert(new NewMatchCell(),0);
	}
	
	public void matchLoaded() {
		for(ClientMatch match:cache.getAllMatches()){
			MatchCell matchCell = new MatchCell(match);
			matches.put(new Long(match.getKey()), matchCell);
			matchTimePanel.insert(matchCell,1);
		}
	}

	public void matchAdded(ClientMatch match) {
		MatchCell matchCell = new MatchCell(match);
		matches.put(new Long(match.getKey()), matchCell);
		matchTimePanel.insert(matchCell,1);
	}

	public void matchUpdated(ClientMatch match) {
		matches.get(new Long(match.getKey())).update(match);
	}

	public void matchRemoved(Long match) {
		matches.remove(match).removeFromParent();
	}

	private class MatchCell extends VerticalPanel{
		
		private ClientMatch match;
		private Label dateTime = new Label();
		private Label location = new Label();
		
		public MatchCell(ClientMatch match){
			this.match = match;
			HorizontalPanel dateDel = new HorizontalPanel();
			dateDel.setWidth("100%");
			dateTime.setText(match.getDate()+"-"+match.getTime());
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
				delete.addClickHandler(new MatchDeleteHandler(this));
				dateDel.setHorizontalAlignment(ALIGN_RIGHT);
				dateDel.add(delete);
			}
			this.add(dateDel);
			this.add(location);
			this.setStyleName("matchCard");
			this.addClickHandler(new MatchInfoHandler(this));
		}
		public HandlerRegistration addClickHandler(ClickHandler handler) {
		    return addDomHandler(handler, ClickEvent.getType());
		}
		
		public ClientMatch getMatch(){
			return match;
		}
		
		public void update(ClientMatch _match){
			this.match = _match;
			dateTime.setText(match.getDate()+" - "+match.getTime());
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
	
	private class NewMatchCell extends VerticalPanel{
		
		public NewMatchCell(){
			VerticalPanel over = new VerticalPanel();
			over.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			Label label = new Label("Mac Ekle");
			over.add(label);
			over.setWidth("100%");
			this.add(over);
			this.addClickHandler(new NewMatchHandler());
			this.setStyleName("newMatchCard");
		}
		
		public HandlerRegistration addClickHandler(ClickHandler handler) {
		    return addDomHandler(handler, ClickEvent.getType());
		}
	}
	
	private class NewMatchHandler implements ClickHandler{

		public void onClick(ClickEvent event) {
			ClientMatch match = new ClientMatch();
			matchDialog.render(match,true,other);	
		}	
	}
	
	private class MatchInfoHandler implements ClickHandler{
		
		private MatchCell matchCell;
		public MatchInfoHandler(MatchCell matchCell){
			this.matchCell = matchCell;
		}

		public void onClick(ClickEvent event) {
			if(currentMatch!=null){
				currentMatch.setStyleName("matchCard");
			}
			matchCell.setStyleName("selectedMatchCard");
			currentMatch = matchCell;
			ClientMatch match = matchCell.getMatch();
			matchDialog.render(match,false,other);	
		}
		
	}
	
	private class MatchDeleteHandler implements ClickHandler{
		
		private MatchCell matchCell;
		public MatchDeleteHandler(MatchCell matchCell){
			this.matchCell = matchCell;
		}

		public void onClick(ClickEvent event) {
			cache.removeMatch(matchCell.getMatch());
		}
		
	}
}

