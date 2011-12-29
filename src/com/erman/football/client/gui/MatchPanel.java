package com.erman.football.client.gui;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CacheMatchHandler;
import com.erman.football.shared.ClientMatch;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MatchPanel extends VerticalPanel implements CacheMatchHandler{
	
	private static final int PAGINATION_NUM = 5;
	private MatchDialog matchDialog;	
	final VerticalPanel matchTimePanel = new VerticalPanel();
	final LinkedHashMap<Long,MatchCell> matches = new LinkedHashMap<Long,MatchCell>();
	final FilterPanel filter = new FilterPanel();
	final DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yy HH:mm");
	final UpdateListCell updateListCell = new UpdateListCell();
	
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
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);
		Label addMatch = new Label("Mac Ekle");
		addMatch.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				displayMatch(null);
			}
		});
		buttonPanel.add(addMatch);
		Label addFilter = new Label("Filtre");
		addFilter.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				filter.setVisible(!filter.isVisible());
			}
		});
		buttonPanel.add(addFilter);
		this.add(buttonPanel);
		filter.setVisible(false);
		this.add(filter);
		ScrollPanel listPanel = new ScrollPanel();
		listPanel.setStyleName("listPanel");
		listPanel.add(matchTimePanel);
		matchTimePanel.setWidth("100%");
		this.add(listPanel);
	}

	public void matchAdded(List<ClientMatch> matches) {
		MatchCell matchCell = null;
		for(ClientMatch match:matches){
			matchCell = new MatchCell(match);
		}
		if(matchCell!=null){
			displayMatch(matchCell);
		}
		if(matches.size() == PAGINATION_NUM){
			matchTimePanel.insert(updateListCell,0);
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
			matchTimePanel.insert(this,0);
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
	
	private class FilterPanel extends VerticalPanel{
		
		private final ListBox months = new ListBox(false);
		private final ListBox years = new ListBox(false);
		private final CheckBox attendOnly = new CheckBox();
		private final CheckBox startToday = new CheckBox();
		private final HorizontalPanel dateInfo = new HorizontalPanel();
		private int startIndex = 5;
		
		public FilterPanel(){
			this.setBorderWidth(1);
			HorizontalPanel startInfo = new HorizontalPanel();
			startInfo.add(new Label("Bugunden Basla"));
			startToday.addValueChangeHandler(new ValueChangeHandler<Boolean>(){
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					if(event.getValue()){
						dateInfo.setVisible(false);
					}else{
						dateInfo.setVisible(true);
					}
				}
				
			});
			startInfo.add(startToday);
			startToday.setValue(true);
			dateInfo.setVisible(false);
			this.add(startInfo);
			
			dateInfo.add(new Label("Ay: "));
			months.addItem("Hepsi","01");
			months.addItem("Ocak","01");
			months.addItem("Subat","02");
			months.addItem("Mart","03");
			months.addItem("Nisan","04");
			months.addItem("Mayis","05");
			months.addItem("Haziran","06");
			months.addItem("Temmuz","07");
			months.addItem("Agustos","08");
			months.addItem("Eylul","09");
			months.addItem("Ekim","10");
			months.addItem("Kasim","11");
			months.addItem("Aralik","12");
			months.addChangeHandler(new ChangeHandler(){
				public void onChange(ChangeEvent event) {
					if(months.getItemText(months.getSelectedIndex()).equals("Hepsi")){
						years.setItemSelected(0, true);
					}else{
						if(years.isItemSelected(0)){
							years.setItemSelected(1, true);
						}
					}
				}

			});
			dateInfo.add(months);
			dateInfo.add(new Label("Yil: "));
			years.addItem("Hepsi","00");
			years.addItem("2011","11");
			years.addItem("2012","12");
			years.addItem("2013","13");
			years.addChangeHandler(new ChangeHandler(){
				public void onChange(ChangeEvent event) {
					if(years.getItemText(years.getSelectedIndex()).equals("Hepsi")){
						months.setItemSelected(0, true);
					}else{
						if(months.isItemSelected(0)){
							months.setItemSelected(1, true);
						}
					}
				}

			});
			dateInfo.add(years);
			
			Button apply = new Button("Uygula");
			apply.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event){
					applyFilter(true);
				}				
			});
			this.add(dateInfo);
			HorizontalPanel attendInfo = new HorizontalPanel();
			attendInfo.add(new Label("Katildiklarim "));
			attendInfo.add(attendOnly);
			this.add(attendInfo);
			this.add(apply);
		}
		
		public void applyFilter(boolean init){
			Date date = new Date();
			if(!startToday.getValue()){
				String strDate = months.getValue(months.getSelectedIndex())+"."+years.getValue(years.getSelectedIndex());
				date = dateFormat.parse("01."+strDate+" 00:00");
			}
			if(init){
				startIndex = 0;
				matchTimePanel.clear();
				matches.clear();
			}
			updateListCell.removeFromParent();
			cache.getMatches(date, startIndex, startIndex+PAGINATION_NUM ,attendOnly.getValue());
			startIndex = startIndex+PAGINATION_NUM;
		}
	}
	

}

