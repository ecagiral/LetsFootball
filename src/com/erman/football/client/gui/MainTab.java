package com.erman.football.client.gui;

import com.erman.football.client.cache.Cache;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class MainTab extends HorizontalPanel{
	
	private final OtherPanel leftPanel = new OtherPanel();
	private final OtherPanel listPanel = new OtherPanel();
	private final OtherPanel detailPanel = new OtherPanel();
	
	private final PlayerPanel playerPanel;
	private final MatchPanel matchPanel;
	private final PitchPanel pitchPanel;
	
	public MainTab(Cache cache){

		playerPanel = new PlayerPanel(cache,detailPanel);
		matchPanel = new MatchPanel(cache,detailPanel);
		pitchPanel = new PitchPanel(cache,detailPanel);
	
		this.setBorderWidth(1);
		
		Button matchButton = new Button("Maclar");
		matchButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				listPanel.clear();
				detailPanel.clear();
				listPanel.add(matchPanel);
			}
			
		});
		leftPanel.add(matchButton);
		Button playerButton = new Button("Oyuncular");
		playerButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				listPanel.clear();
				detailPanel.clear();
				listPanel.add(playerPanel);
			}
			
		});
		leftPanel.add(playerButton);
		Button pitchButton = new Button("Sahalar");
		pitchButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				listPanel.clear();
				detailPanel.clear();
				listPanel.add(pitchPanel);
			}
			
		});
		leftPanel.add(pitchButton);
		
		this.add(leftPanel );
		this.add(listPanel);
		this.add(detailPanel);
		
	}

}
