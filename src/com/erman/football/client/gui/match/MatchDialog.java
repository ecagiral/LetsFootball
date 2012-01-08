package com.erman.football.client.gui.match;

import com.erman.football.client.cache.Cache;
import com.erman.football.shared.ClientMatch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MatchDialog{

	final VerticalPanel matchBoxPanel = new VerticalPanel();
	final Button matchUpdateButton = new Button("OK");
	final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd.MM.yy HH.mm");
	final Label matchLocationText = new Label();
	final Label matchDateText = new Label();
	final Label matchTimeText = new Label();

	private MatchDetailPanel detailPanel;
	private ClientMatch match;	
	private Cache cache; 

	public MatchDialog(Cache cache){
		this.cache = cache;
		detailPanel = new MatchDetailPanel(cache);

		Grid matchInfos = new Grid(3,2);	
		matchInfos.setWidget(0,0,new Label("Tarih: "));
		matchInfos.setWidget(0,1,matchDateText);
		matchInfos.setWidget(1,0,new Label("Saat: "));
		matchInfos.setWidget(1,1,matchTimeText);
		matchInfos.setWidget(2,0,new Label("Saha: "));
		matchInfos.setWidget(2,1,matchLocationText);

		matchUpdateButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				retrieveData();
				MatchDialog.this.cache.updateMatch(match);
			}
		});	
		matchBoxPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		matchBoxPanel.add(matchInfos);
		matchBoxPanel.add(detailPanel);
		matchBoxPanel.add(matchUpdateButton);
	}

	public void derender(){
		matchBoxPanel.setVisible(false);
	}

	public void render(ClientMatch match,Panel parent){
		detailPanel.render(match);
		String dateTime[] =  dateTimeFormat.format(match.getDate()).split("\\s+");
		matchDateText.setText(dateTime[0]);
		matchTimeText.setText(dateTime[1]);
		matchLocationText.setText(match.getLocation());
		this.match = match;
		parent.clear();
		parent.add(matchBoxPanel);
		matchBoxPanel.setVisible(true);
	}

	private void retrieveData(){
		match.setTeamA(detailPanel.getTeamA());
		match.setTeamB(detailPanel.getTeamB());
	}


}
