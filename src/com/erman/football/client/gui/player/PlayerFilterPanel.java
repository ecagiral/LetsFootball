package com.erman.football.client.gui.player;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.gui.list.ListFilter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

public class PlayerFilterPanel extends ListFilter{
	
	private Cache cache;
	private int startIndex =PAGINATION_NUM-1;
	final private Label filterButton = new Label("filter");
	
	public PlayerFilterPanel(Cache _cache){
		this.cache = _cache;
		
		this.setHorizontalAlignment(ALIGN_CENTER);
		filterButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				applyFilter(false);
			}		
		});
		filterButton.setStyleName("filterButton");
		this.add(filterButton);
	}
	
	protected void applyFilter(boolean pagination) {
		if(!pagination){
			startIndex = 0;
		}
		handler.filterApplied(pagination);
		cache.getPlayers("A",startIndex, startIndex+PAGINATION_NUM);
		startIndex = startIndex+PAGINATION_NUM-1;// -1 is required for extra data to display more button
	}

}
