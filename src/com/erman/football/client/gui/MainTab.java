package com.erman.football.client.gui;

import com.erman.football.client.cache.Cache;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class MainTab extends HorizontalPanel{
	
	public MainTab(Cache cache){
		OtherPanel playerDetail = new OtherPanel();
		OtherPanel matchDetail = new OtherPanel();
		OtherPanel pitchDetail = new OtherPanel();
		PlayerPanel playerPanel = new PlayerPanel(cache,playerDetail);
		MatchPanel matchPanel = new MatchPanel(cache,matchDetail);
		PitchPanel pitchPanel = new PitchPanel(cache,pitchDetail);
	
		this.setBorderWidth(1);
		
		this.add(playerPanel);
		this.add(playerDetail);
		this.add(matchPanel);
		this.add(matchDetail);
		this.add(pitchPanel);
		this.add(pitchDetail);
		
	}

}
