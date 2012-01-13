package com.erman.football.client.gui.pitch;

import com.erman.football.client.cache.Cache;
import com.erman.football.shared.Pitch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PitchPanel extends HorizontalPanel implements PitchMapPanelHandler{

	private final PitchDialog pitchDialog;	
	private final SimplePanel mapPanel = new SimplePanel();
	private final SimplePanel infoPanel = new SimplePanel();
	private final PitchMapPanel pitchMap;

	public PitchPanel(Cache _cache, PitchMapPanel _pitchMap){
		pitchMap = _pitchMap;
		pitchDialog = new PitchDialog(_cache,pitchMap.getGreen());
		VerticalPanel buttonPanel = new VerticalPanel();
		if(_cache.getLoggedPlayer().isAdmin()){
			Label addMatch = new Label("Saha Ekle");
			addMatch.setStyleName("leftButton");
			addMatch.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					pitchMap.addMarker();
				}
			});
			buttonPanel.add(addMatch);
		}
		Label searchMatch = new Label("Saha Ara");
		searchMatch.setStyleName("leftButton");
		searchMatch.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				infoPanel.clear();
				pitchMap.removeIdle();
			}
		});
		buttonPanel.add(searchMatch);
		SimplePanel summaryPanel = new SimplePanel();
		Label summaryLabel = new Label("Bu paneli kullanarak halisaha arayabilirsiniz.");
		summaryPanel.add(summaryLabel);
		summaryPanel.setStyleName("summaryLabel");
		buttonPanel.add(summaryPanel);
		buttonPanel.setWidth("120px");


		this.add(buttonPanel);
		this.add(mapPanel);
		this.add(infoPanel);
	}

	public void markerClicked(Pitch pitch,Marker marker) {
		pitchDialog.render(false,pitch,infoPanel,marker);
		infoPanel.setVisible(true);
	}

	public void markerAdded(Pitch pitch, Marker marker) {
		pitchDialog.render(true,pitch,infoPanel,marker);
		infoPanel.setVisible(true);
	}
	
	public void load(){
		pitchMap.show(mapPanel, this);
	}

}
