package com.erman.football.client.gui;

import com.erman.football.client.cache.Cache;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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
		
		leftPanel.setVerticalAlignment(ALIGN_TOP);
		leftPanel.setHeight("500px");
		
		VerticalPanel buttonPanel = new VerticalPanel();

		Button matchButton = new Button("Maclar");
		matchButton.setStyleName("leftButton");
		matchButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				listPanel.clear();
				detailPanel.clear();
				listPanel.add(matchPanel);
			}
			
		});
		buttonPanel.add(matchButton);
		Button playerButton = new Button("Oyuncular");
		playerButton.setStyleName("leftButton");
		playerButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				listPanel.clear();
				detailPanel.clear();
				listPanel.add(playerPanel);
			}
			
		});
		buttonPanel.add(playerButton);
		Button pitchButton = new Button("Sahalar");
		pitchButton.setStyleName("leftButton");
		pitchButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				listPanel.clear();
				detailPanel.clear();
				listPanel.add(pitchPanel);
			}
			
		});
		buttonPanel.add(pitchButton);
		leftPanel.add(buttonPanel);
		this.add(leftPanel );
		this.add(listPanel);
		this.add(detailPanel);
		
	}

}
