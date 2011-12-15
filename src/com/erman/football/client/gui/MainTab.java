package com.erman.football.client.gui;

import com.erman.football.client.cache.Cache;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class MainTab extends HorizontalPanel{
	
	public MainTab(Cache cache){
		OtherPanel playerDetail = new OtherPanel();
		OtherPanel matchDetail = new OtherPanel();
		PlayerPanel playerPanel = new PlayerPanel(cache,playerDetail);
		MatchPanel matchPanel = new MatchPanel(cache,matchDetail);
	
		this.setBorderWidth(1);
		
		this.add(playerPanel);
		this.add(playerDetail);
		this.add(matchPanel);
		this.add(matchDetail);
		
	}

}
