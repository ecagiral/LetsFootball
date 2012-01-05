package com.erman.football.client.gui.pitch;

import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePitchHandler;
import com.erman.football.client.gui.ParamUpdateHandler;
import com.erman.football.client.gui.TextInput;
import com.erman.football.shared.Pitch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PitchDialog implements ParamUpdateHandler, CachePitchHandler{

	private final TextInput pitchNameText = new TextInput(this);
	private final TextInput pitchCapacityText = new TextInput(this);
	private final Button updateButton = new Button("Apply");
	private final Button deleteButton = new Button("Sil");
	private final HorizontalPanel pitchDialogPanel = new HorizontalPanel();
	private final Image laodImg = new Image("loader.gif");
	
	private Cache cache;
	private boolean add;
	private Pitch pitch;
	private boolean admin;
	private final Marker marker;
	private boolean inProgress;
	 
	public PitchDialog(Cache cache, Marker _marker){
		this.cache = cache;
		cache.regiserPitch(this);
		this.marker = _marker;
		admin = cache.getLoggedPlayer().isAdmin();
		
		updateButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				updateData();
			}

		});
		deleteButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				deleteData();
			}

		});
		
		VerticalPanel pitchInfoPanel = new VerticalPanel();
		pitchInfoPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		if(admin){
			pitchInfoPanel.add(pitchNameText.getTextBox());
			pitchInfoPanel.add(pitchCapacityText.getTextBox());
			pitchNameText.setEnabled(true);
			pitchCapacityText.setEnabled(true);
		}else{
			pitchInfoPanel.add(pitchNameText.getTextBox());
			pitchInfoPanel.add(pitchCapacityText.getTextBox());
			pitchNameText.setEnabled(false);
			pitchCapacityText.setEnabled(false);
		}
	    if(admin){
	    	pitchInfoPanel.add(updateButton);
	    	pitchInfoPanel.add(deleteButton);
	    	laodImg.setVisible(false);
	    	pitchInfoPanel.add(laodImg);
	    }
	    pitchDialogPanel.add(pitchInfoPanel);    		
	}
	
	public void render(boolean add, Pitch pitch,Panel basePanel){
		this.add = add;
		laodImg.setVisible(false);
		deleteButton.setVisible(!add);
		this.pitch = pitch;	
		pitchNameText.setText(pitch.getName(),add );
		pitchCapacityText.setText(Integer.toString(pitch.getCapacity()), add);
		marker.setPosition(pitch.getLocation());
		basePanel.clear();
		basePanel.add(pitchDialogPanel);
	}
	
	private boolean retrieveData(){
		boolean result = false;
		if(!pitchNameText.getText().equals(pitch.getName())){
			pitch.setName(pitchNameText.getText());
			result = true;
		}
		if(!marker.getPosition().equals(pitch.getLocation())){
			pitch.setLocation(marker.getPosition());
			result = true;
		}
		if(!pitchCapacityText.getText().equals(pitch.getCapacity())){
			pitch.setCapacity(Integer.parseInt(pitchCapacityText.getText()));
			result = true;
		}
		return result;
	}
	
	public void updateData() {
		if(retrieveData()){
			inProgress = true;
			
			if(add){
				PitchDialog.this.cache.addPitch(pitch);
				pitchDialogPanel.removeFromParent();
				
			}else{
				PitchDialog.this.cache.updatePitch(pitch);
			}
			add=false;
			updateButton.setEnabled(false);
			laodImg.setVisible(true);
			marker.setVisible(false);
		}

	}
	
	private void deleteData(){
		cache.removePitch(pitch);
		deleteButton.setEnabled(false);
		inProgress = true;
	}

	public void pitchAdded(List<Pitch> pitch) {
		if(inProgress){
			Pitch aPitch = pitch.get(0);
			this.add = false;
			this.pitch = aPitch;	
			pitchNameText.setText(aPitch.getName(),add );
			pitchCapacityText.setText(Integer.toString(aPitch.getCapacity()), add);
			marker.setPosition(aPitch.getLocation());
			updateButton.setEnabled(true);
			laodImg.setVisible(false);
			inProgress = false;
		}

	}

	public void pitchUpdated(Pitch pitch) {
		if(inProgress){
			this.add = false;
			this.pitch = pitch;	
			pitchNameText.setText(pitch.getName(),add );
			pitchCapacityText.setText(Integer.toString(pitch.getCapacity()), add);
			marker.setPosition(pitch.getLocation());
			updateButton.setEnabled(true);
			laodImg.setVisible(false);
			inProgress = false;
		}
	}

	public void pitchRemoved(Long pitch) {
		if(inProgress){
			pitchDialogPanel.setVisible(false);
			deleteButton.setEnabled(true);
			laodImg.setVisible(false);
			inProgress = false;
			marker.setVisible(false);
		}
	}
	
    void doubleClick(HasLatLng latlgn){
    	if(add){
    		marker.setPosition(latlgn);
    	}
    }

}
