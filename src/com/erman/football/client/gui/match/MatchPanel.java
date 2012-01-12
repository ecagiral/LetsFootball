package com.erman.football.client.gui.match;

import java.util.ArrayList;
import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CacheMatchHandler;
import com.erman.football.client.gui.list.DataCell;
import com.erman.football.client.gui.list.ListPanel;
import com.erman.football.client.gui.list.ListPanelListener;
import com.erman.football.client.gui.pitch.PitchMapPanel;
import com.erman.football.shared.Match;
import com.erman.football.shared.DataObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MatchPanel extends HorizontalPanel implements CacheMatchHandler ,ListPanelListener{

	final private MatchDialog matchDialog;	
	final private ListPanel listMainPanel;
	final private SimplePanel infoPanel = new SimplePanel();
	final private Cache cache;
	final private MatchAddPanel matchAddPanel;

	private MatchCell currentMatch;

	public MatchPanel(Cache _cache,PitchMapPanel _pitchMap){
		this.cache = _cache;
		matchAddPanel = new MatchAddPanel(_cache,_pitchMap);
		cache.regiserMatch(this);
		matchDialog = new MatchDialog(cache);
		MatchFilterPanel filter =  new MatchFilterPanel(cache);
		listMainPanel = new ListPanel(filter,new MatchCell(this));
		filter.setHandler(listMainPanel);
		VerticalPanel buttonPanel = new VerticalPanel();
		Label addMatch = new Label("Mac Ekle");
		addMatch.setStyleName("leftButton");
		addMatch.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				infoPanel.clear();
				listMainPanel.setVisible(false);
				matchAddPanel.load(null);
			}
		});
		buttonPanel.add(addMatch);
		Label searchMatch = new Label("Mac Ara");
		searchMatch.setStyleName("leftButton");
		searchMatch.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				infoPanel.clear();
				matchAddPanel.setVisible(false);
				listMainPanel.setVisible(true);
			}
		});
		buttonPanel.add(searchMatch);
		buttonPanel.setWidth("100px");
		this.add(buttonPanel);
		matchAddPanel.setVisible(false);
		this.add(matchAddPanel);
		this.add(listMainPanel);
		this.add(infoPanel);
	}

	public void matchAdded(List<Match> matches) {
		ArrayList<DataObject> data = new ArrayList<DataObject>();
		data.addAll(matches);
		listMainPanel.dataAdded(data,cache.getLoggedPlayer().getKey());
	}

	public void matchUpdated(Match match) {
		ArrayList<DataObject> data = new ArrayList<DataObject>();
		data.add(match);
		listMainPanel.dataUpdated(data);
	}

	public void matchRemoved(Long match) {
		ArrayList<Long> data = new ArrayList<Long>();
		data.add(match);
		listMainPanel.dataRemoved(data);
	}

	private void displayMatch(MatchCell cell){
		if(currentMatch!=null){
			currentMatch.setStyleName("matchCard");
		}
		cell.setStyleName("selectedMatchCard");
		currentMatch = cell;
		matchDialog.render(cell.getMatch(),infoPanel);	
	}

	public void CellClicked(DataCell dataCell) {
		displayMatch((MatchCell)dataCell);

	}

	@Override
	public void removeClicked(DataCell dataCell) {
		cache.removeMatch(((MatchCell)dataCell).getMatch());

	}

	public void modifyClicked(DataCell dataCell) {
		infoPanel.clear();
		listMainPanel.setVisible(false);
		matchAddPanel.load((Match)dataCell.getData());
	}

	public void load(){
		matchAddPanel.setVisible(false);
		infoPanel.clear();
		listMainPanel.setVisible(true);
	}

}

