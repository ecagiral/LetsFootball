package com.erman.football.client.gui.match;

import com.erman.football.client.cache.Cache;
import com.erman.football.shared.Match;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MatchDialog{

	final VerticalPanel matchBoxPanel = new VerticalPanel();
	final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd.MM.yy HH.mm");
	final Label matchLocationText = new Label();
	final Label matchDateText = new Label();
	final Label matchTimeText = new Label();

	private MatchDetailPanel detailPanel;

	public MatchDialog(Cache cache){
		detailPanel = new MatchDetailPanel(cache);

		Grid matchInfos = new Grid(3,2);	
		matchInfos.setWidget(0,0,new Label("Tarih: "));
		matchInfos.setWidget(0,1,matchDateText);
		matchInfos.setWidget(1,0,new Label("Saat: "));
		matchInfos.setWidget(1,1,matchTimeText);
		matchInfos.setWidget(2,0,new Label("Saha: "));
		matchInfos.setWidget(2,1,matchLocationText);

		matchBoxPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		matchBoxPanel.add(matchInfos);
		matchBoxPanel.add(detailPanel);
	}

	public void derender(){
		matchBoxPanel.setVisible(false);
	}

	public void render(Match match,Panel parent){
		detailPanel.render(match);
		String dateTime[] =  dateTimeFormat.format(match.getDate()).split("\\s+");
		matchDateText.setText(dateTime[0]);
		matchTimeText.setText(dateTime[1]);
		matchLocationText.setText(match.getLocation().getName());
		parent.clear();
		parent.add(matchBoxPanel);
		matchBoxPanel.setVisible(true);
	}

}
