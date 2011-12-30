package com.erman.football.client.gui;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CacheMatchHandler;
import com.erman.football.shared.ClientMatch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class MatchPanel extends HorizontalPanel implements CacheMatchHandler{
	
	private static final int PAGINATION_NUM = 5;
	private MatchDialog matchDialog;	
	final VerticalPanel matchTimePanel = new VerticalPanel();
	final LinkedHashMap<Long,MatchCell> matches = new LinkedHashMap<Long,MatchCell>();
	final FilterPanel filter = new FilterPanel();
	final DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yy HH:mm");
	final UpdateListCell updateListCell = new UpdateListCell();
	final StatusCell status = new StatusCell();
	
	
	private boolean admin;
	private Cache cache;
	private MatchCell currentMatch;
	private SimplePanel other;
	
	
	public MatchPanel(Cache cache, SimplePanel other){
		this.cache = cache;
		this.other = other;
		cache.regiserMatch(this);
		admin = cache.getLoggedPlayer().isAdmin();
		matchDialog = new MatchDialog(cache);
		
		VerticalPanel buttonPanel = new VerticalPanel();
		Label addMatch = new Label("Mac Ekle");
		addMatch.setStyleName("leftButton");
		addMatch.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				displayMatch(null);
			}
		});
		buttonPanel.add(addMatch);
		buttonPanel.add(filter);
		this.add(buttonPanel);
		ScrollPanel listPanel = new ScrollPanel();
		listPanel.setStyleName("listPanel");
		listPanel.add(matchTimePanel);
		matchTimePanel.setWidth("100%");
		matchTimePanel.add(status);
		this.add(listPanel);
	}

	public void matchAdded(List<ClientMatch> matches) {
		status.removeFromParent();
		for(ClientMatch match:matches){
			new MatchCell(match);
		}
		if(matches.size() == PAGINATION_NUM){
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
		if(currentMatch!=null){
			currentMatch.setStyleName("matchCard");
		}
		if(cell == null){
			matchDialog.render(new ClientMatch(),true,other);	
		}else{
			cell.setStyleName("selectedMatchCard");
			currentMatch = cell;
			matchDialog.render(cell.getMatch(),false,other);	
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
					filter.applyFilter(false);
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
	
	private class FilterPanel extends VerticalPanel{
		
		final DialogBox dateDialog = new DialogBox();
		private Date startDate;
		private boolean attend;
		private int startIndex = 5;
		
		public FilterPanel(){

			Label attendButton = new Label("Katildiklarim ");
			attendButton.setStyleName("leftButton");
			attendButton.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					attend = !attend;
					applyFilter(true);
				}	
			});
			this.add(attendButton);
			
			DatePicker datePicker = new DatePicker();
			datePicker.addValueChangeHandler(new ValueChangeHandler<Date>(){

				public void onValueChange(ValueChangeEvent<Date> event) {
					startDate = event.getValue();
					dateDialog.hide();
					applyFilter(true);
				}
				
			});
			dateDialog.add(datePicker);
			Label startDate = new Label("Baslangic");
			startDate.setStyleName("leftButton");
			startDate.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					dateDialog.setPopupPosition(event.getClientX()+10, event.getClientY()+10);
					dateDialog.show();
				}	
			});
			this.add(startDate);
			this.setWidth("150px");	
		}
		
		public void applyFilter(boolean init){
			if(init){
				startIndex = 0;
				matchTimePanel.clear();
				matches.clear();
			}
			updateListCell.removeFromParent();
			matchTimePanel.add(status);
			cache.getMatches(startDate, startIndex, startIndex+PAGINATION_NUM ,attend);
			startIndex = startIndex+PAGINATION_NUM;
		}
	}
	

}

