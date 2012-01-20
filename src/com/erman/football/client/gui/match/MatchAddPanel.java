package com.erman.football.client.gui.match;

import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CacheMatchHandler;
import com.erman.football.client.gui.pitch.PitchMapPanel;
import com.erman.football.client.gui.pitch.PitchMapPanelHandler;
import com.erman.football.shared.Match;
import com.erman.football.shared.Pitch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class MatchAddPanel extends DialogBox implements CacheMatchHandler,PitchMapPanelHandler{

	private static enum stage {pitch,date,player,summary};

	private final PitchMapPanel pitchMap;
	private final VerticalPanel mapPanel = new VerticalPanel();
	private final SimplePanel playerPanel = new SimplePanel();
	private final SummaryPanel summaryPanel = new SummaryPanel();
	private final Label selectPitch = new Label("Saha");
	private final Label selectDate = new Label("Tarih");
	private final Label selectPlayer = new Label("Takimlar");
	private final Label matchSummary = new Label("Onay");
	private final Image nextButton = new Image("arrow_right.png");
	private final Image backButton = new Image("arrow_left.png");
	private final Button applyButton = new Button("Gonder");
	private final Label pitchName = new Label("Haritadan seciniz");
	private final TextBox teamAName = new TextBox();
	private final TextBox teamBName = new TextBox();
	private final Cache cache;
	private final Image laodImg = new Image("loader.gif");
	private final Image successImg = new Image("success.jpg");
	private final DatePicker datePicker = new DatePicker();
	private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd MMM yyyy:HH.mm");
	private final WeekPanel datePanel = new WeekPanel();

	private stage currentStage;
	private Match match;
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
		matchSummary.setStylePrimaryName("addStage");

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
		stepPanel.add(matchSummary);
		stepPanel.add(nextButton);

		HorizontalPanel pitchNamePanel = new HorizontalPanel();
		pitchNamePanel.add(new Label("Secilen Saha: "));
		pitchNamePanel.add(pitchName);
		mapPanel.add(pitchNamePanel);
		
		VerticalPanel teamPanel = new VerticalPanel();
		teamPanel.add(new Label("Takim Isimleri"));
		teamPanel.add(teamAName);
		teamPanel.add(teamBName);
		playerPanel.add(teamPanel);

		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.add(mapPanel);
		mainPanel.add(datePanel);
		mainPanel.add(playerPanel);
		mainPanel.add(summaryPanel);
		
		VerticalPanel bottomPanel = new VerticalPanel();
		bottomPanel.setWidth("100%");
		bottomPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		HorizontalPanel nextPanel = new HorizontalPanel();
		
		applyButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				if(!inProgress){
					applyClicked();
				}
			}
		});

		laodImg.setVisible(false);
		nextPanel.add(applyButton);
		nextPanel.add(laodImg);
		nextPanel.add(successImg);
		bottomPanel.add(nextPanel);

		VerticalPanel dialogPanel = new VerticalPanel();
		dialogPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		dialogPanel.setHeight("500px");
		dialogPanel.setWidth("500px");
		dialogPanel.add(stepPanel);
		dialogPanel.add(mainPanel);
		dialogPanel.add(bottomPanel);
		dialogPanel.setCellVerticalAlignment(bottomPanel, HasVerticalAlignment.ALIGN_BOTTOM);
		
		this.setPopupPosition(400, 50);
		this.setGlassEnabled(true);
		this.add(dialogPanel);
		this.setAutoHideEnabled(true);
		
	}
	
	public void load(Match _match){
		inProgress = false;
		successImg.setVisible(false);
		mapPanel.setVisible(true);
		playerPanel.setVisible(false);
		datePanel.setVisible(false);
		summaryPanel.setVisible(false);
		selectPitch.setStyleDependentName("selected", true);
		selectDate.setStyleDependentName("selected", false);
		selectPlayer.setStyleDependentName("selected", false);
		matchSummary.setStyleDependentName("selected", false);
		currentStage = stage.pitch;
		applyButton.setEnabled(false);
		pitchName.setText("Haritadan seciniz");
		
		this.setVisible(true);
		this.show();
		pitchMap.show(mapPanel, this);
		if(_match == null){
			this.setText("Mac Ekle");
			modify = false;
			match = new Match();			
		}else{
			this.setText("Mac Duzenle");
			modify = true;
			match = _match;
			Pitch pitch = new Pitch();
			pitch.setKey(match.getLocation().getKey());
			if(pitch.getKey()!=0){
				pitchMap.selectMarker(pitch);
			}
		}
		
		datePicker.setValue(match.getDate());
		teamAName.setText(match.getTeamAName());
		teamBName.setText(match.getTeamBName());
	}

	private void backClicked(){
		switch(currentStage){
		case pitch:
			break;
		case date:
			mapPanel.setVisible(true);
			playerPanel.setVisible(false);
			datePanel.setVisible(false);
			summaryPanel.setVisible(false);
			selectPitch.setStyleDependentName("selected", true);
			selectDate.setStyleDependentName("selected", false);
			selectPlayer.setStyleDependentName("selected", false);
			matchSummary.setStyleDependentName("selected", false);
			currentStage = stage.pitch;
			break;
		case player:
			mapPanel.setVisible(false);
			playerPanel.setVisible(false);
			datePanel.setVisible(true);
			summaryPanel.setVisible(false);
			selectPitch.setStyleDependentName("selected", false);
			selectDate.setStyleDependentName("selected", true);
			selectPlayer.setStyleDependentName("selected", false);
			matchSummary.setStyleDependentName("selected", false);
			currentStage = stage.date;
			break;
		case summary:
			mapPanel.setVisible(false);
			playerPanel.setVisible(true);
			datePanel.setVisible(false);
			summaryPanel.setVisible(false);
			selectPitch.setStyleDependentName("selected", false);
			selectDate.setStyleDependentName("selected", false);
			selectPlayer.setStyleDependentName("selected", true);
			matchSummary.setStyleDependentName("selected", false);
			applyButton.setEnabled(false);
			currentStage = stage.player;
			break;
		}
	}

	private void nextClicked(){
		switch(currentStage){
		case pitch:
			mapPanel.setVisible(false);
			playerPanel.setVisible(false);
			datePanel.load(match.getLocation());
			summaryPanel.setVisible(false);
			selectPitch.setStyleDependentName("selected", false);
			selectDate.setStyleDependentName("selected", true);
			selectPlayer.setStyleDependentName("selected", false);
			matchSummary.setStyleDependentName("selected", false);
			currentStage = stage.date;
			break;
		case date:
			match.setDate(datePanel.getSelectedDate());
			mapPanel.setVisible(false);
			playerPanel.setVisible(true);
			datePanel.setVisible(false);
			summaryPanel.setVisible(false);
			selectPitch.setStyleDependentName("selected", false);
			selectDate.setStyleDependentName("selected", false);
			selectPlayer.setStyleDependentName("selected", true);
			matchSummary.setStyleDependentName("selected", false);
			currentStage = stage.player;
			break;
		case player:
			mapPanel.setVisible(false);
			playerPanel.setVisible(false);
			datePanel.setVisible(false);
			summaryPanel.update();
			summaryPanel.setVisible(true);
			selectPitch.setStyleDependentName("selected", false);
			selectDate.setStyleDependentName("selected", false);
			selectPlayer.setStyleDependentName("selected", false);
			matchSummary.setStyleDependentName("selected", true);
			applyButton.setEnabled(true);
			currentStage = stage.summary;
			break;
		case summary:
			break;
		}
	};
	
	

	private void applyClicked(){
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

	public void markerClicked(Pitch pitch) {
		match.setLocation(pitch);
		pitchName.setText(pitch.getName());
	}

	public void markerAdded(Pitch pitch) {
		//Should not be called. Do nothing
	}

	public void matchAdded(List<Match> matches) {
		inProgress = false;
		laodImg.setVisible(false);
		successImg.setVisible(true);
	}

	public void matchUpdated(Match match) {
		inProgress = false;
		laodImg.setVisible(false);
		successImg.setVisible(true);
	}

	public void matchRemoved(Long match) {
		//Not interested
	}
	
	private class SummaryPanel extends VerticalPanel{
		Label date = new Label();
		Label time = new Label();
		Label location = new Label();
		Label teamA = new Label();
		Label teamB = new Label();
		
		public SummaryPanel(){
			this.setHorizontalAlignment(ALIGN_CENTER);
			HorizontalPanel headerPanel = new HorizontalPanel();
			headerPanel.setVerticalAlignment(ALIGN_MIDDLE);
			teamA.setStyleName("matchAName");
			headerPanel.add(teamA);
			headerPanel.add(new Label("vs"));
			teamB.setStyleName("matchBName");
			headerPanel.add(teamB);
			this.add(headerPanel);
			HorizontalPanel datePanel = new HorizontalPanel();
			datePanel.add(new Label("Tarih: "));
			datePanel.add(date);
			this.add(datePanel);
			HorizontalPanel timePanel = new HorizontalPanel();
			timePanel.add(new Label("Saat: "));
			timePanel.add(time);
			this.add(timePanel);
			HorizontalPanel locationPanel = new HorizontalPanel();
			locationPanel.add(new Label("Saha: "));
			locationPanel.add(location);
			this.add(locationPanel);
		}
		
		public void update(){
			teamA.setText(teamAName.getText());
			teamB.setText(teamBName.getText());
			String dateTime[] = dateFormat.format(match.getDate()).split(":");
			date.setText(dateTime[0]);
			time.setText(dateTime[1]);
			location.setText(pitchName.getText());
		}
		
	}

}
