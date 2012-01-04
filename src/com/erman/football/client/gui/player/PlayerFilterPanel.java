package com.erman.football.client.gui.player;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.gui.list.ListFilter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

public class PlayerFilterPanel extends ListFilter{
	
	private Cache cache;
	private String startChar = "A";
	private int startIndex =PAGINATION_NUM-1;
	
	public PlayerFilterPanel(Cache _cache){
		this.cache = _cache;
		
		this.setHorizontalAlignment(ALIGN_CENTER);
		this.add(new CharCell("A"));
		this.add(new CharCell("E"));
		this.add(new CharCell("K"));
		this.add(new CharCell("P"));
		this.add(new CharCell("T"));
	}
	
	protected void applyFilter(boolean pagination) {
		if(!pagination){
			startIndex = 0;
		}
		handler.filterApplied(pagination);
		cache.getPlayers(startChar,startIndex, startIndex+PAGINATION_NUM);
		startIndex = startIndex+PAGINATION_NUM-1;// -1 is required for extra data to display more button
	}
	
	private class CharCell extends Label{

		private String value;
		
		public CharCell(String _value){
			this.value = _value;
			this.setStyleName("filterButton");
			this.setText(value);
			this.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					setStartChar(value);
					applyFilter(false);
				}	
			});
		}
	}
	
	private void setStartChar(String _char){
		startChar = _char;
	}

}
