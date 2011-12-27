package com.erman.football.client.gui;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MatchPanel extends ScrollPanel implements CacheMatchHandler{
	
	private MatchDialog matchDialog;
	
	final VerticalPanel matchTimePanel = new VerticalPanel();
	final HashMap<Long,MatchCell> matches = new HashMap<Long,MatchCell>();
	final FilterCell filter = new FilterCell();
	final DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yy HH:mm");
	
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
		
		this.setStyleName("listPanel");
		this.add(matchTimePanel);
		matchTimePanel.setWidth("100%");
	}
	
	public void matchLoaded() {
		for(ClientMatch match:cache.getAllMatches()){
			new MatchCell(match);
		}
		updateList();
	}

	public void matchAdded(ClientMatch match) {
		MatchCell matchCell = new MatchCell(match);
		matches.put(new Long(match.getKey()), matchCell);
		matchTimePanel.insert(matchCell,2);
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
			over.add(new Label("Mac Ekle"));
			over.setWidth("100%");
			this.add(over);
			this.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					matchDialog.render(new ClientMatch(),true,other);	
				}
			});
			this.setStyleName("newMatchCard");
		}
		
		public HandlerRegistration addClickHandler(ClickHandler handler) {
		    return addDomHandler(handler, ClickEvent.getType());
		}
	}
	
	private class FilterCell extends VerticalPanel{
		
		ListBox months = new ListBox(false);
		ListBox years = new ListBox(false);
		Date minDate;
		Date maxDate;
		
		public FilterCell(){
			HorizontalPanel data = new HorizontalPanel();
			data.add(new Label("Ay: "));
			months.addItem("Hepsi","0");
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
					if(months.getValue(months.getSelectedIndex()).equals("0")){
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
			years.addItem("Hepsi","0");
			years.addItem("2011","11");
			years.addItem("2012","12");
			years.addItem("2013","13");
			years.addChangeHandler(new ChangeHandler(){
				public void onChange(ChangeEvent event) {
					if(years.getValue(years.getSelectedIndex()).equals("0")){
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
				public void onClick(ClickEvent event) {
					updateList();
				}				
			});
			data.setHorizontalAlignment(ALIGN_RIGHT);
			data.add(apply);
			this.add(data);
		}

		private boolean isValidDate(String strDate){
			Date date = dateFormat.parse(strDate);
			if(date.after(minDate)||date.equals(minDate)){
				if(date.before(maxDate)||date.equals(maxDate)){
					return true;
				}
				//high than expected
				return false;
			}
			//low than expected
			return false;
		}
		
		private boolean isFilterOn(){
			boolean mNot = months.getValue(months.getSelectedIndex()).equalsIgnoreCase("0");
			boolean yNot = years.getValue(years.getSelectedIndex()).equalsIgnoreCase("0");
			if(mNot||yNot){
				return false;
			}
			String filterDate = months.getValue(months.getSelectedIndex())+"."+years.getValue(years.getSelectedIndex());
			maxDate = dateFormat.parse("31."+filterDate+" 23:59");
			minDate = dateFormat.parse("01."+filterDate+" 00:00");
			
			return true;
		}
		
	}
	
	private void updateList(){
		matchTimePanel.clear();
		matchTimePanel.insert(new NewMatchCell(),0);
		matchTimePanel.insert(filter,1);
		ValueComparator comp = new ValueComparator(matches);
		TreeMap<Long,MatchCell> ordMatches = new TreeMap(comp);
		ordMatches.putAll(matches);
		boolean filterOn = filter.isFilterOn();
		for(MatchCell matchCell: ordMatches.values()){
			if(filterOn){
				if(filter.isValidDate(matchCell.getMatch().getDate()+" "+matchCell.getMatch().getTime())){
					matchTimePanel.insert(matchCell,2);
				}
			}else{
				matchTimePanel.insert(matchCell,2);
			}
		}
	}

	class ValueComparator implements Comparator<Long>{

		Map<Long,MatchCell> base;
		public ValueComparator(Map<Long,MatchCell> base) {
			this.base = base;
		}

		public int compare(Long a, Long b) {
			Date aDate = dateFormat.parse(cache.getMatch(a).getDate()+" "+cache.getMatch(a).getTime());
			Date bDate = dateFormat.parse(cache.getMatch(b).getDate()+" "+cache.getMatch(b).getTime());
			return bDate.compareTo(aDate);

		}
	}
}

