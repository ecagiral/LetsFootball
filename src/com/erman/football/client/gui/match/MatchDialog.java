package com.erman.football.client.gui.match;

import java.util.Date;
import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePitchHandler;
import com.erman.football.shared.ClientMatch;
import com.erman.football.shared.Pitch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class MatchDialog implements CachePitchHandler {
	
	final VerticalPanel matchInfoPanel = new VerticalPanel();
	final VerticalPanel matchBoxPanel = new VerticalPanel();
	final Button matchUpdateButton = new Button("OK");
	final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd.MM.yy HH.mm");
	final DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yy");
	
	final Label matchLocationLabel = new Label("Yer: ");
	final Label matchLocationText = new Label();
	final ListBox matchLocationList = new ListBox();
	final Label matchDateLabel = new Label("Tarih: ");
	final TextBox matchDateText = new TextBox();
	final Label matchDateDetail = new Label();
	final Label matchTimeLabel = new Label("Saat: ");
	final TextBox matchTimeText = new TextBox();
	final Label matchTimeDetail = new Label();
	final Label matchPlayedLabel = new Label("Oynandi: ");	
	final Button gamePlayedButton = new Button("Oynandi");
	final Label teamAScoreLabel = new Label("TeamA .");
	final TextBox teamAScore = new TextBox();
	final Label teamBScoreLabel = new Label("TeamB");
	final TextBox teamBScore = new TextBox();
	
	final DialogBox dateDialog = new DialogBox();
	
	private boolean matchPlayed;
	
	private MatchDetailPanel detailPanel;
	private ClientMatch match;
	private boolean add;
	
	private Cache cache; 
	
	public MatchDialog(Cache cache){
		this.cache = cache;
		cache.regiserPitch(this);
		detailPanel = new MatchDetailPanel(cache);
		boolean admin = cache.getLoggedPlayer().isAdmin();
		matchDateText.setWidth("52px");
		matchTimeText.setWidth("36px");
		teamAScore.setWidth("18px");
		teamAScore.setMaxLength(2);
		teamBScore.setWidth("18px");
		teamBScore.setMaxLength(2);
		
		DatePicker date = new DatePicker();
		date.addValueChangeHandler(new ValueChangeHandler<Date>(){

			public void onValueChange(ValueChangeEvent<Date> event) {
				String date = dateFormat.format(event.getValue());
				matchDateText.setText(date);
				dateDialog.hide();
			}
			
		});
		dateDialog.setAutoHideEnabled(true);
		dateDialog.add(date);
		matchDateText.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				dateDialog.setPopupPosition(event.getClientX()+10, event.getClientY()+10);
				dateDialog.show();
			}			
		});
		HorizontalPanel matchScorePanel = new HorizontalPanel();
		matchScorePanel.add(teamAScoreLabel);
		matchScorePanel.add(teamAScore);
		matchScorePanel.add(new Label(":"));
		matchScorePanel.add(teamBScore);
		matchScorePanel.add(teamBScoreLabel);
		Grid matchInfos = null;
		if(admin){
			matchInfos = new Grid(4,2);	
			matchInfos.setWidget(0,0,matchDateLabel);
			matchInfos.setWidget(0,1,matchDateText);
			matchInfos.setWidget(1,0,matchTimeLabel);
			matchInfos.setWidget(1,1,matchTimeText);
			matchInfos.setWidget(2,0,matchLocationLabel);
			matchInfos.setWidget(2,1,matchLocationList);
			gamePlayedButton.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					if(matchPlayed){
						matchPlayed = false;
						teamAScore.setEnabled(false);
						teamAScore.setText("-");
						teamBScore.setEnabled(false);
						teamBScore.setText("-");
						gamePlayedButton.setText("Oynandi");
					}else{
						matchPlayed = true;
						teamAScore.setEnabled(true);
						teamAScore.setText("");
						teamBScore.setEnabled(true);
						teamBScore.setText("");
						gamePlayedButton.setText("Oynanmadi");
					}
				}			
			});
			matchInfos.setWidget(3,1,gamePlayedButton);
		}else{
			matchInfos = new Grid(3,2);	
			matchInfos.setWidget(0,0,matchDateLabel);
			matchInfos.setWidget(0,1,matchDateDetail);
			matchDateText.setEnabled(false);
			matchInfos.setWidget(1,0,matchTimeLabel);
			matchInfos.setWidget(1,1,matchTimeDetail);
			matchTimeText.setEnabled(false);
			matchInfos.setWidget(2,0,matchLocationLabel);
			matchInfos.setWidget(2,1,matchLocationText);
			matchLocationList.setEnabled(false);
			teamAScore.setEnabled(false);
			teamBScore.setEnabled(false);
		}
		matchInfoPanel.add(matchInfos);
		matchInfoPanel.add(matchScorePanel);
		
		matchUpdateButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				retrieveData();
				if(add){
					MatchDialog.this.cache.addMatch(match);
				}else{
					MatchDialog.this.cache.updateMatch(match);
					
				}
			}
			
		});	
		matchBoxPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		matchBoxPanel.add(matchInfoPanel);
		matchBoxPanel.add(detailPanel);
		matchBoxPanel.add(matchUpdateButton);
	}
	
	public void derender(){
		matchBoxPanel.setVisible(false);
	}
	
	public void render(ClientMatch match, Boolean _add,Panel parent){
		this.add = _add;
		detailPanel.render(match);
		String dateTime[] =  dateTimeFormat.format(match.getDate()).split("\\s+");
		matchDateText.setText(dateTime[0]);
		matchTimeText.setText(dateTime[1]);
		for(int in = 0; in < matchLocationList.getItemCount();in++){
			if(matchLocationList.getValue(in).equals(match.getLocation())){
				matchLocationList.setSelectedIndex(in);
				break;
			}
		}
		Pitch pitch = cache.getPitch(Long.valueOf(match.getLocation()));
		if(pitch==null){
			matchLocationText.setText("Bilinmiyor");
		}else{
			matchLocationText.setText(pitch.getName());
		}
		matchTimeDetail.setText(dateTime[1]);
		matchDateDetail.setText(dateTime[0]);
		matchPlayed = match.isPlayed();
		if(matchPlayed){
			teamAScore.setEnabled(true);
			teamAScore.setText("3");
			teamBScore.setEnabled(true);
			teamBScore.setText("2");
			gamePlayedButton.setText("Oynanmadi");
		}else{
			teamAScore.setEnabled(false);
			teamBScore.setEnabled(false);
			gamePlayedButton.setText("Oynandi");
		}
		this.match = match;
		parent.clear();
		parent.add(matchBoxPanel);
		matchBoxPanel.setVisible(true);
	}
	
	
	
	private void retrieveData(){
		match.setDate(dateTimeFormat.parseStrict(matchDateText.getText()+" "+matchTimeText.getText()));
		match.setLocation(matchLocationList.getValue(matchLocationList.getSelectedIndex()));
		match.setTeamA(detailPanel.getTeamA());
		match.setTeamB(detailPanel.getTeamB());
	}

	public void pitchAdded(List<Pitch> pitch) {
		updateLocationList();
		
	}

	public void pitchUpdated(Pitch pitch) {
		updateLocationList();
		
	}

	public void pitchRemoved(Long pitch) {
		updateLocationList();
		
	}
	
	private void updateLocationList(){
		matchLocationList.clear();
		matchLocationList.addItem("Not Selected", "0");
		//TODO Implement location listing
			
	}

}
