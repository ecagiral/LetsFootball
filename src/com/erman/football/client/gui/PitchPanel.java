package com.erman.football.client.gui;

import java.util.HashMap;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePitchHandler;
import com.erman.football.shared.Pitch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PitchPanel extends HorizontalPanel implements CachePitchHandler{
	
	final private VerticalPanel pitchListPanel = new VerticalPanel();
	final private HashMap<Long,PitchCell> pitches = new HashMap<Long,PitchCell>();
	final private SimplePanel infoPanel = new SimplePanel();
	
	private PitchDialog pitchDialog;
	private Cache cache;
	private boolean admin;
	private PitchCell currentPitch;
	
	public PitchPanel(Cache cache){
		this.cache = cache;
		this.admin = cache.getLoggedPlayer().isAdmin();
		cache.regiserPitch(this);
			
		VerticalPanel buttonPanel = new VerticalPanel();
		Label addMatch = new Label("Saha Ekle");
		addMatch.setStyleName("leftButton");
		buttonPanel.add(addMatch);
		Label searchMatch = new Label("Saha Ara");
		searchMatch.setStyleName("leftButton");
		buttonPanel.add(searchMatch);
		buttonPanel.setWidth("100px");
		this.add(buttonPanel);
		
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setStyleName("listPanel");
		scrollPanel.add(pitchListPanel);
		pitchListPanel.setWidth("100%");
		if(admin){
			pitchListPanel.insert(new NewPitchCell(), 0);
		}
		this.add(scrollPanel);
		pitchDialog = new PitchDialog(cache,infoPanel);
		this.add(infoPanel);

	}
	
	private class PitchCell extends VerticalPanel{
		
		private Pitch pitch;
		private Label pitchName = new Label();
		
		public PitchCell(Pitch pitch){
			this.pitch = pitch;
			HorizontalPanel nameDel = new HorizontalPanel();
			nameDel.setWidth("100%");
			pitchName.setText(pitch.getName());
			nameDel.add(pitchName);
			if(admin){
				Button delete = new Button("-");
				delete.addClickHandler(new PitchDeleteHandler(this));
				nameDel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
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
	
	private class NewPitchCell extends VerticalPanel{
		
		public NewPitchCell(){
			VerticalPanel over = new VerticalPanel();
			over.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			over.add(new Label("Saha Ekle"));
			over.setWidth("100%");
			this.add(over);
			this.addClickHandler(new NewPitchHandler());
			this.setStyleName("newMatchCard");
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
