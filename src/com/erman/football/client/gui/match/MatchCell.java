package com.erman.football.client.gui.match;

import com.erman.football.client.gui.list.DataCell;
import com.erman.football.client.gui.list.ListPanelListener;
import com.erman.football.shared.Match;
import com.erman.football.shared.DataObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MatchCell extends DataCell{
	
	private Label teamA = new Label();
	private Label teamB = new Label();
	private Label score = new Label();
	private Label date = new Label();
	private Label location = new Label();
	
	final private DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd MMM yyyy - HH:mm");

	public MatchCell(ListPanelListener _listener) {
		super(_listener);
	}

	protected DataCell generateCell(DataObject _data,boolean isAdmin){
		MatchCell result = new MatchCell(listener);
		result.setData(_data);
		result.setListener(listener);
		result.setAdmin(isAdmin);
		HorizontalPanel dateDel = generateCard(result);
		result.add(dateDel);
		result.setStyleName("matchCard");
		result.addDomHandler(new CellClickHandler(result), ClickEvent.getType());
		return result;
	}

	private HorizontalPanel generateCard(MatchCell cell){
		cell.teamA.setStyleName("matchAName");
		cell.teamB.setStyleName("matchBName");
		cell.teamA.setText(cell.getMatch().getTeamAName());
		cell.teamB.setText(cell.getMatch().getTeamBName());
		
		if(cell.getMatch().isPlayed()){
			cell.score.setStyleName("playedScore");
			cell.score.setText(cell.getMatch().getTeamAScore()+" - "+cell.getMatch().getTeamBScore());
		}else{
			cell.score.setStyleName("score");
			cell.score.setText("vs");
		}
		cell.date.setText(dateFormat.format(cell.getMatch().getDate()));
		cell.location.setText(cell.getMatch().getLocation().getName());
		HorizontalPanel result = new HorizontalPanel();
		result.setVerticalAlignment(ALIGN_BOTTOM);
		result.setWidth("100%");
		result.add(cell.getSummary(cell));
		if(cell.isAdmin()){
			HorizontalPanel buttonPanel = new HorizontalPanel();
			Image end = new Image("ball.png");
			end.addClickHandler(new CellEndHandler(cell));
			buttonPanel.add(end);
			Image edit = new Image("modify.png");
			edit.addClickHandler(new CellModifyHandler(cell));
			buttonPanel.add(edit);
			Image delete = new Image("delete.png");
			delete.addClickHandler(new CellDeleteHandler(cell));
			buttonPanel.add(delete);
			result.setHorizontalAlignment(ALIGN_RIGHT);
			result.add(buttonPanel);
			
		}
		return result;
	}
	
	public VerticalPanel getSummary(MatchCell cell){
		VerticalPanel result = new VerticalPanel();
		HorizontalPanel headerPanel = new HorizontalPanel();
		headerPanel.setVerticalAlignment(ALIGN_MIDDLE);
		headerPanel.add(teamA);
		headerPanel.add(score);
		headerPanel.add(teamB);
		result.add(headerPanel);
		HorizontalPanel datePanel = new HorizontalPanel();
		datePanel.add(new Label("Tarih: "));
		datePanel.add(date);
		result.add(datePanel);
		HorizontalPanel locationPanel = new HorizontalPanel();
		locationPanel.add(new Label("Saha: "));
		locationPanel.add(location);
		result.add(locationPanel);
		return result;
	}
	
	protected void update(DataObject data){
		Match match = (Match)data;
		this.data = match;
		teamA.setText(match.getTeamAName());
		teamB.setText(match.getTeamBName());
		if(match.isPlayed()){
			score.setStyleName("playedScore");
			score.setText(match.getTeamAScore()+" - "+match.getTeamBScore());
		}else{
			score.setStyleName("score");
			score.setText("vs");
		}
		date.setText(dateFormat.format(match.getDate()));
		location.setText(match.getLocation().getName());
	}
	
	public Match getMatch(){
		return (Match)data;
	}
	
}
