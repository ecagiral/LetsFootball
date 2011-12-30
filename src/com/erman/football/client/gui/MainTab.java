package com.erman.football.client.gui;

import com.erman.football.client.cache.Cache;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainTab extends VerticalPanel{
	
	private final SimplePanel listPanel = new SimplePanel();
	private final SimplePanel detailPanel = new SimplePanel();
	
	private final PlayerPanel playerPanel;
	private final MatchPanel matchPanel;
	private final PitchPanel pitchPanel;
	
	public MainTab(Cache cache){

		playerPanel = new PlayerPanel(cache,detailPanel);
		matchPanel = new MatchPanel(cache,detailPanel);
		pitchPanel = new PitchPanel(cache,detailPanel);
		
		SimplePanel topPanel = new SimplePanel();
		topPanel.setStyleName("topPanel");
		HorizontalPanel buttonPanel = new HorizontalPanel();
		//buttonPanel.setStyleName("topButtonPanel");
		Label matchButton = new Label("Maclar");
		matchButton.setStyleName("topButton");
		matchButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				listPanel.clear();
				detailPanel.clear();
				listPanel.add(matchPanel);
			}
			
		});
		buttonPanel.add(matchButton);
		Label playerButton = new Label("Oyuncular");
		playerButton.setStyleName("topButton");
		playerButton.addClickHandler(new ClickHandler(){
		
			public void onClick(ClickEvent event) {
				listPanel.clear();
				detailPanel.clear();
				listPanel.add(playerPanel);
			}
			
		});
		buttonPanel.add(playerButton);
		Label pitchButton = new Label("Sahalar");
		pitchButton.setStyleName("topButton");
		pitchButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				listPanel.clear();
				detailPanel.clear();
				listPanel.add(pitchPanel);
			}
			
		});
		buttonPanel.add(pitchButton);
		topPanel.add(buttonPanel);
		this.add(topPanel);
		HorizontalPanel centerPanel = new HorizontalPanel();
		listPanel.add(matchPanel);
		centerPanel.add(listPanel);
		centerPanel.add(detailPanel);

		this.add(centerPanel);
		this.setWidth("100%");
	}

}
