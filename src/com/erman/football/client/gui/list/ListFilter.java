package com.erman.football.client.gui.list;

import com.google.gwt.user.client.ui.HorizontalPanel;

public abstract class ListFilter extends HorizontalPanel{
	
	public static int PAGINATION_NUM = 6;

	protected abstract void applyFilter(boolean pagination);
	protected FilterHandler handler;
	
	public void setHandler(FilterHandler _handler){
		this.handler = _handler;
	}
}
