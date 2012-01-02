package com.erman.football.client.gui.list;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ListPanel extends VerticalPanel{

	final private VerticalPanel cardListPanel = new VerticalPanel();
	final private StatusCell status = new StatusCell();
	final private ListFilter filter;
	final private UpdateListCell updateListCell = new UpdateListCell();
	
	public ListPanel(ListFilter _filter){
		this.setStyleName("listMainPanel");
		this.filter = _filter;
		this.add(filter);
		this.setCellHorizontalAlignment(filter, ALIGN_CENTER);
		ScrollPanel listPanel = new ScrollPanel();
		listPanel.setStyleName("listPanel");
		listPanel.add(cardListPanel);
		cardListPanel.setWidth("100%");
		cardListPanel.add(status);
		cardListPanel.add(listPanel);
		this.add(cardListPanel);
	}
	
	private class StatusCell extends VerticalPanel{
		
		public StatusCell(){
			this.setHorizontalAlignment(ALIGN_CENTER);
			this.add(new Image("loader.gif"));
			this.setSpacing(10);
			this.setWidth("100%");
		}
	}
	
	private class UpdateListCell extends VerticalPanel{
		
		public UpdateListCell(){
			Label label = new Label("Daha Fazla");
			this.setHorizontalAlignment(ALIGN_CENTER);
			this.add(label);
			this.addDomHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					filter.applyFilter(true);
					UpdateListCell.this.removeFromParent();
				}
			}, ClickEvent.getType());
			this.setStyleName("matchCard");
		}

	}
}
