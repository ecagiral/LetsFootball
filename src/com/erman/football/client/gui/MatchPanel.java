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
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MatchPanel extends VerticalPanel implements CacheMatchHandler{
	
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
				matchDialog.render(new ClientMatch(),true,MatchPanel.this.other);	
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

		if(currentMatch!=null){
			currentMatch.setStyleName("matchCard");
		}
		if(matchCell!=null){
			matchCell.setStyleName("selectedMatchCard");
			currentMatch = matchCell;
			matchDialog.render(matchCell.getMatch(),false,other);	
		}
		if(matches.size()>0){
			matchTimePanel.insert(updateListCell,0);
		}

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
					if(currentMatch!=null){
						currentMatch.setStyleName("matchCard");
					}
					MatchCell.this.setStyleName("selectedMatchCard");
					currentMatch = MatchCell.this;
					ClientMatch match = MatchCell.this.getMatch();
					matchDialog.render(match,false,other);	
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
		
		private Date startDate = new Date();
		private int startIndex = 5;
		
		public UpdateListCell(){
			Label label = new Label("Daha Fazla");
			this.setHorizontalAlignment(ALIGN_CENTER);
			this.add(label);
			this.addDomHandler(new ClickHandler(){

				public void onClick(ClickEvent event) {
					cache.getMatches(startDate, startIndex, startIndex+5);
					startIndex = startIndex+5;
					updateListCell.removeFromParent();
				}
				
			}, ClickEvent.getType());
			this.setStyleName("matchCard");
		}
		
		public void setStartDate(Date date,int index){
			startDate = date;
			startIndex = index;
		}
	}
	
	private class FilterPanel extends VerticalPanel{
		
		ListBox months = new ListBox(false);
		ListBox years = new ListBox(false);
		
		public FilterPanel(){
			HorizontalPanel data = new HorizontalPanel();
			data.add(new Label("Ay: "));
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
			data.add(months);
			data.add(new Label("Yil: "));
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
			data.add(years);
			
			Button apply = new Button("Uygula");
			apply.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event){
					String strDate = months.getValue(months.getSelectedIndex())+"."+years.getValue(years.getSelectedIndex());
					Date date = dateFormat.parse("01."+strDate+" 00:00");
					cache.getMatches(date, 0, 5);
					updateListCell.setStartDate(date, 5);
					matchTimePanel.clear();
					matches.clear();
					matchDialog.derender();
				}				
			});
			data.setHorizontalAlignment(ALIGN_RIGHT);
			data.add(apply);
			this.add(data);
		}
		
	}
}

