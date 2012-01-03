package com.erman.football.client.gui.pitch;

import java.util.ArrayList;
import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePitchHandler;
import com.erman.football.client.gui.list.DataCell;
import com.erman.football.client.gui.list.ListPanel;
import com.erman.football.client.gui.list.ListPanelListener;
import com.erman.football.shared.DataObject;
import com.erman.football.shared.Pitch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PitchPanel extends HorizontalPanel implements CachePitchHandler ,ListPanelListener{

	final private PitchDialog pitchDialog;	
	final private ListPanel listMainPanel;
	final private SimplePanel infoPanel = new SimplePanel();
	final private Cache cache;
	
	private PitchCell currentPitch;
	
	public PitchPanel(Cache _cache){
		this.cache = _cache;
		cache.regiserPitch(this);
		pitchDialog = new PitchDialog(cache);
		PitchFilterPanel filter = new PitchFilterPanel(cache);
		listMainPanel = new ListPanel(filter, new PitchCell(this));
		filter.setHandler(listMainPanel);
		VerticalPanel buttonPanel = new VerticalPanel();
		Label addMatch = new Label("Saha Ekle");
		addMatch.setStyleName("leftButton");
		addMatch.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				displayPitch(null);
			}
		});
		buttonPanel.add(addMatch);
		Label searchMatch = new Label("Saha Ara");
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
		this.add(listMainPanel);
		this.add(infoPanel);
	}
	
	private void displayPitch(PitchCell cell){
		if(cell == null){
			listMainPanel.setVisible(false);
			pitchDialog.render(true,new Pitch(),infoPanel);	
		}else{
			if(currentPitch!=null){
				currentPitch.setStyleName("matchCard");
			}
			cell.setStyleName("selectedMatchCard");
			currentPitch = cell;
			pitchDialog.render(false,cell.getPitch(),infoPanel);	
		}
		
	}

	public void CellClicked(DataCell dataCell) {
		displayPitch((PitchCell)dataCell);
	}

	public void removeClicked(DataCell dataCell) {
		cache.removePitch(((PitchCell)dataCell).getPitch());
	}

	public void pitchAdded(List<Pitch> pitch) {
		ArrayList<DataObject> data = new ArrayList<DataObject>();
		data.addAll(pitch);
		listMainPanel.dataAdded(data);
	}

	public void pitchUpdated(Pitch pitch) {
		ArrayList<DataObject> data = new ArrayList<DataObject>();
		data.add(pitch);
		listMainPanel.dataUpdated(data);
	}

	public void pitchRemoved(Long pitch) {
		ArrayList<Long> data = new ArrayList<Long>();
		data.add(pitch);
		listMainPanel.dataRemoved(data);
	}

}
