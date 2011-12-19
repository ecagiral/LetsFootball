package com.erman.football.client.gui;

import java.util.HashMap;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePitchHandler;
import com.erman.football.shared.Pitch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PitchPanel extends VerticalPanel implements CachePitchHandler{
	
	final ScrollPanel scrollPitchPanel = new ScrollPanel();
	final VerticalPanel pitchListPanel = new VerticalPanel();
	final HashMap<Long,PitchCell> pitches = new HashMap<Long,PitchCell>();
	
	private PitchDialog pitchDialog;
	private Cache cache;
	private boolean admin;
	private PitchCell currentPitch;
	
	public PitchPanel(Cache cache, OtherPanel other){
		this.cache = cache;
		this.admin = cache.getLoggedPlayer().isAdmin();
		cache.regiserPitch(this);
		this.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		this.add(new Label("Saha Paneli"));
		pitchDialog = new PitchDialog(cache,other);
		pitchListPanel.setWidth("210px");
		scrollPitchPanel.add(pitchListPanel);
		scrollPitchPanel.setWidth("250px");
		scrollPitchPanel.setHeight("500px");
		if(admin){
			pitchListPanel.insert(new NewPitchCell(), 0);
		}
		this.add(scrollPitchPanel);
		
	}
	
	private class PitchCell extends SimplePanel{
		
		private Pitch pitch;
		private Label pitchName = new Label();
		
		public PitchCell(Pitch pitch){
			this.pitch = pitch;
			this.setWidth("100%");
			pitchName.setText(pitch.getName());
			HorizontalPanel nameDel = new HorizontalPanel();
			nameDel.add(pitchName);
			nameDel.setWidth("100%");
			if(admin){
				Button delete = new Button("-");
				delete.addClickHandler(new PitchDeleteHandler(this));
				nameDel.setHorizontalAlignment(ALIGN_RIGHT);
				nameDel.add(delete);
			}
			this.add(nameDel);
			this.setStyleName("matchCard");
			this.addClickHandler(new PitchInfoHandler(this));
		}
		public HandlerRegistration addClickHandler(ClickHandler handler) {
		    return addDomHandler(handler, ClickEvent.getType());
		}
		
		public Pitch getPitch(){
			return pitch;
		}
		
		public void update(Pitch pitch){
			this.pitch = pitch;
			pitchName.setText(pitch.getName());
		}
	}
	
	private class NewPitchCell extends SimplePanel{
		
		public NewPitchCell(){
			this.setWidth("100%");
			VerticalPanel over = new VerticalPanel();
			over.add(new Label("Saha Ekle"));
			over.setWidth("100%");
			this.add(over);
			this.addClickHandler(new NewPitchHandler());
			this.setStyleName("matchCard");
		}
		
		public HandlerRegistration addClickHandler(ClickHandler handler) {
		    return addDomHandler(handler, ClickEvent.getType());
		}
	}
	
	private class PitchDeleteHandler implements ClickHandler{
		
		private PitchCell pitchCell;
		public PitchDeleteHandler(PitchCell pitchCell){
			this.pitchCell = pitchCell;
		}

		public void onClick(ClickEvent event) {
			cache.removePitch(pitchCell.getPitch());
		}
		
	}
	
	
	private class PitchInfoHandler implements ClickHandler{
		
		private PitchCell pitchCell;
		public PitchInfoHandler(PitchCell pitchCell){
			this.pitchCell = pitchCell;
		}

		public void onClick(ClickEvent event) {
			if(currentPitch!=null){
				currentPitch.setStyleName("matchCard");
			}
			pitchCell.setStyleName("selectedMatchCard");
			currentPitch = pitchCell;
			Pitch pitch = pitchCell.getPitch();
			pitchDialog.render(false,pitch);	
		}
		
	}
	
	private class NewPitchHandler implements ClickHandler{

		public void onClick(ClickEvent event) {
			Pitch pitch = new Pitch();
			pitchDialog.render(true,pitch);	
		}	
	}

	public void pitchLoaded() {
		for(Pitch pitch:cache.getAllPitches()){
			PitchCell pitchCell = new PitchCell(pitch);
			pitches.put(new Long(pitch.getKey()), pitchCell);
			if(admin){
				pitchListPanel.insert(pitchCell,1);
			}else{
				pitchListPanel.insert(pitchCell,0);
			}
		}
	}

	public void pitchAdded(Pitch pitch) {
		PitchCell pitchCell = new PitchCell(pitch);
		pitches.put(new Long(pitch.getKey()), pitchCell);
		if(admin){
			pitchListPanel.insert(pitchCell,1);
		}else{
			pitchListPanel.insert(pitchCell,0);
		}
	}

	public void pitchUpdated(Pitch pitch) {
		pitches.get(new Long(pitch.getKey())).update(pitch);
	}

	public void pitchRemoved(Long pitch) {
		pitches.remove(pitch).removeFromParent();
		
	}

}
