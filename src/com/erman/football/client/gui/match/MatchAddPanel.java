package com.erman.football.client.gui.match;

import java.util.Date;
import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CacheMatchHandler;
import com.erman.football.client.gui.pitch.PitchMapPanel;
import com.erman.football.client.gui.pitch.PitchMapPanelHandler;
import com.erman.football.shared.ClientMatch;
import com.erman.football.shared.Pitch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class MatchAddPanel extends VerticalPanel implements CacheMatchHandler,PitchMapPanelHandler{

	private static enum stage {pitch,date,player};

	private final PitchMapPanel pitchMap;
	private final VerticalPanel mapPanel = new VerticalPanel();
	private final VerticalPanel datePanel = new VerticalPanel();
	private final SimplePanel playerPanel = new SimplePanel();
	private final Label selectPitch = new Label("Saha Sec");
	private final Label selectDate = new Label("Gun Sec");
	private final Label selectPlayer = new Label("Oyuncu Sec");
	private final Label nextButton = new Label("Ileri");
	private final Label backButton = new Label("Geri");
	private final Label pitchName = new Label("Haritadan seciniz");
	private final Cache cache;
	private final Image laodImg = new Image("loader.gif");
	private final Image successImg = new Image("success.jpg");
	private final DatePicker datePicker = new DatePicker();

	private stage currentStage;
	private ClientMatch match;
	private boolean inProgress;
	private boolean modify;

	public MatchAddPanel(Cache _cache,PitchMapPanel _pitchMap){
		cache = _cache;
		pitchMap = _pitchMap;
		cache.regiserMatch(this);



		HorizontalPanel stepPanel = new HorizontalPanel();

		selectPitch.setStylePrimaryName("addStage");
		selectDate.setStylePrimaryName("addStage");
		selectPlayer.setStylePrimaryName("addStage");

		stepPanel.add(selectPitch);
		stepPanel.add(selectDate);
		stepPanel.add(selectPlayer);

		HorizontalPanel pitchNamePanel = new HorizontalPanel();
		pitchNamePanel.add(new Label("Secilen Saha: "));
		pitchNamePanel.add(pitchName);
		mapPanel.add(pitchNamePanel);

		datePicker.addValueChangeHandler(new ValueChangeHandler<Date>(){
			public void onValueChange(ValueChangeEvent<Date> event) {
				match.setDate(event.getValue());
			}

		});
		datePanel.setHorizontalAlignment(ALIGN_CENTER);
		datePanel.add(datePicker);
		datePanel.setVisible(false);

		playerPanel.add(new Label("Oyuncu sec"));

		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.add(mapPanel);
		mainPanel.add(datePanel);
		mainPanel.add(playerPanel);

		VerticalPanel bottomPanel = new VerticalPanel();
		bottomPanel.setWidth("100%");
		bottomPanel.setHorizontalAlignment(ALIGN_CENTER);
		HorizontalPanel nextPanel = new HorizontalPanel();
		backButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				backClicked();
			}
		});
		backButton.setStyleName("filterButton");
		nextPanel.add(backButton);
		nextButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				nextClicked();
			}
		});
		nextButton.setStyleName("filterButton");
		nextPanel.add(nextButton);
		laodImg.setVisible(false);
		nextPanel.add(laodImg);

		nextPanel.add(successImg);
		bottomPanel.add(nextPanel);

		this.setStyleName("listMainPanel");
		this.setHorizontalAlignment(ALIGN_CENTER);
		this.add(stepPanel);
		this.add(mainPanel);
		this.add(bottomPanel);
	}
	
	public void load(ClientMatch _match){
		inProgress = false;
		successImg.setVisible(false);
		mapPanel.setVisible(true);
		backButton.setVisible(false);
		playerPanel.setVisible(false);
		datePanel.setVisible(false);
		selectPitch.setStyleDependentName("selected", true);
		selectDate.setStyleDependentName("selected", false);
		selectPlayer.setStyleDependentName("selected", false);
		currentStage = stage.pitch;
		nextButton.setVisible(true);
		nextButton.setText("Ileri");
		pitchName.setText("Haritadan seciniz");
		this.setVisible(true);
		pitchMap.show(mapPanel, this);
		if(_match == null){
			modify = false;
			match = new ClientMatch();			
		}else{
			modify = true;
			match = _match;
			datePicker.setValue(match.getDate());
			Pitch pitch = new Pitch();
			pitch.setKey(Long.parseLong(match.getLocation()));
			pitchMap.selectMarker(pitch);
		}
	}

	private void backClicked(){
		switch(currentStage){
		case pitch:
			//should not be here
			backButton.setVisible(false);
			break;
		case date:
			mapPanel.setVisible(true);
			playerPanel.setVisible(false);
			datePanel.setVisible(false);
			selectPitch.setStyleDependentName("selected", true);
			selectDate.setStyleDependentName("selected", false);
			selectPlayer.setStyleDependentName("selected", false);
			nextButton.setText("Ileri");
			backButton.setVisible(false);
			currentStage = stage.pitch;
			break;
		case player:
			mapPanel.setVisible(false);
			playerPanel.setVisible(false);
			datePanel.setVisible(true);
			selectPitch.setStyleDependentName("selected", false);
			selectDate.setStyleDependentName("selected", true);
			selectPlayer.setStyleDependentName("selected", false);
			nextButton.setText("Ileri");
			backButton.setVisible(true);
			currentStage = stage.date;
			break;
		}
	}

	private void nextClicked(){
		switch(currentStage){
		case pitch:
			mapPanel.setVisible(false);
			playerPanel.setVisible(false);
			datePanel.setVisible(true);
			selectPitch.setStyleDependentName("selected", false);
			selectDate.setStyleDependentName("selected", true);
			selectPlayer.setStyleDependentName("selected", false);
			nextButton.setText("Ileri");
			backButton.setVisible(true);
			currentStage = stage.date;
			break;
		case date:
			mapPanel.setVisible(false);
			playerPanel.setVisible(true);
			datePanel.setVisible(false);
			selectPitch.setStyleDependentName("selected", false);
			selectDate.setStyleDependentName("selected", false);
			selectPlayer.setStyleDependentName("selected", true);
			if(modify){
				nextButton.setText("Guncelle");
			}else{
				nextButton.setText("Ekle");
			}
			backButton.setVisible(true);
			currentStage = stage.player;
			break;
		case player:
			if(!inProgress){
				backButton.setVisible(false);
				applyMatch();
			}
			break;
		}
	};

	private void applyMatch(){
		inProgress = true;
		laodImg.setVisible(true);
		if(modify){
			cache.updateMatch(match);
		}else{
			cache.addMatch(match);
		}
	}

	public void markerClicked(Pitch pitch, Marker marker) {
		match.setLocation(Long.toString(pitch.getKey()));
		pitchName.setText(Long.toString(pitch.getKey()));
	}

	public void markerAdded(Pitch pitch, Marker marker) {
		//Should not be called. Do nothing
	}

	public void matchAdded(List<ClientMatch> matches) {
		inProgress = false;
		nextButton.setVisible(false);
		laodImg.setVisible(false);
		successImg.setVisible(true);
	}

	public void matchUpdated(ClientMatch match) {
		inProgress = false;
		laodImg.setVisible(false);
		successImg.setVisible(true);
	}

	public void matchRemoved(Long match) {
		//Not interested
	}

}
