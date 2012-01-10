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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class MatchAddPanel extends VerticalPanel implements CacheMatchHandler,PitchMapPanelHandler{

	private static enum stage {pitch,date,player};

	private final PitchMapPanel pitchMap;
	private final VerticalPanel mapPanel = new VerticalPanel();
	private final VerticalPanel datePanel = new VerticalPanel();
	private final SimplePanel playerPanel = new SimplePanel();
	private final Label selectPitch = new Label("Saha");
	private final Label selectDate = new Label("Tarih");
	private final Label selectPlayer = new Label("Takimlar");
	private final Image nextButton = new Image("arrow_right.png");
	private final Image backButton = new Image("arrow_left.png");
	private final Label applyButton = new Label("Ekle");
	private final Label pitchName = new Label("Haritadan seciniz");
	private final TextBox teamAName = new TextBox();
	private final TextBox teamBName = new TextBox();
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

		backButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				backClicked();
			}
		});
		backButton.setStyleName("nextButton");
		nextButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				nextClicked();
			}
		});
		nextButton.setStyleName("nextButton");
		stepPanel.add(backButton);
		stepPanel.add(selectPitch);
		stepPanel.add(selectDate);
		stepPanel.add(selectPlayer);
		stepPanel.add(nextButton);

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

		VerticalPanel teamPanel = new VerticalPanel();
		teamPanel.add(new Label("Takim Isimleri"));
		teamPanel.add(teamAName);
		teamPanel.add(teamBName);
		playerPanel.add(teamPanel);

		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.add(mapPanel);
		mainPanel.add(datePanel);
		mainPanel.add(playerPanel);

		VerticalPanel bottomPanel = new VerticalPanel();
		bottomPanel.setWidth("100%");
		bottomPanel.setHorizontalAlignment(ALIGN_CENTER);
		HorizontalPanel nextPanel = new HorizontalPanel();
		
		applyButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				if(!inProgress){
					applyClicked();
				}
			}
		});
		applyButton.setStyleName("filterButton");
		laodImg.setVisible(false);
		nextPanel.add(applyButton);
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
		applyButton.setVisible(false);
		pitchName.setText("Haritadan seciniz");
		this.setVisible(true);
		pitchMap.show(mapPanel, this);
		if(_match == null){
			modify = false;
			match = new ClientMatch();			
		}else{
			modify = true;
			match = _match;
			Pitch pitch = new Pitch();
			pitch.setKey(Long.parseLong(match.getLocation()));
			pitchMap.selectMarker(pitch);
		}
		datePicker.setValue(match.getDate());
		teamAName.setText(match.getTeamAName());
		teamBName.setText(match.getTeamBName());
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
			nextButton.setVisible(true);
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
			nextButton.setVisible(true);
			backButton.setVisible(true);
			applyButton.setVisible(false);
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
			nextButton.setVisible(true);
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
				applyButton.setText("Guncelle");
			}else{
				applyButton.setText("Ekle");
			}
			applyButton.setVisible(true);
			backButton.setVisible(true);
			nextButton.setVisible(false);
			currentStage = stage.player;
			break;
		case player:
			//should not be here
			nextButton.setVisible(false);
			break;
		}
	};
	
	

	private void applyClicked(){
		backButton.setVisible(false);
		inProgress = true;
		laodImg.setVisible(true);
		match.setTeamAName(teamAName.getText());
		match.setTeamBName(teamBName.getText());
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
