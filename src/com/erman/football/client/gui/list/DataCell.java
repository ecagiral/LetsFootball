package com.erman.football.client.gui.list;

import com.erman.football.shared.DataObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DataCell extends VerticalPanel{
	
	private DataObject data;
	
	public DataCell(){
		HorizontalPanel dateDel = generateCard();
		this.add(dateDel);
		this.setStyleName("matchCard");
		this.addDomHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {

			}
			
		}, ClickEvent.getType());
	}
	
	/**
	 * Implement in subclass
	 * @return
	 */
	protected HorizontalPanel generateCard(){
		HorizontalPanel result = new HorizontalPanel();
		result.add(new Label("Empty"));
		return result;
	}

}
