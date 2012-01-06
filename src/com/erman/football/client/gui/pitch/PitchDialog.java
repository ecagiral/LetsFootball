package com.erman.football.client.gui.pitch;

import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePitchHandler;
import com.erman.football.client.gui.ParamUpdateHandler;
import com.erman.football.client.gui.TextInput;
import com.erman.football.shared.Pitch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerImage;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PitchDialog implements ParamUpdateHandler, CachePitchHandler{

	private final TextInput pitchNameText = new TextInput(this);
	private final TextInput pitchCapacityText = new TextInput(this);
	private final Button updateButton = new Button("Guncelle");
	private final Button deleteButton = new Button("Sil");
	private final HorizontalPanel pitchDialogPanel = new HorizontalPanel();
	private final Image laodImg = new Image("loader.gif");
	private final Image successImg = new Image("success.jpg");
	
	private Cache cache;
	private boolean add;
	private Pitch pitch;
	private boolean admin;
	private Marker marker;
	private boolean inProgress;
	private MarkerImage greenField;
	 
	public PitchDialog(Cache cache,MarkerImage _greenField){
		this.cache = cache;
		this.greenField = _greenField;
		cache.regiserPitch(this);
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
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(updateButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(laodImg);
		buttonPanel.add(successImg);
		laodImg.setVisible(false);
		successImg.setVisible(false);
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
	    	pitchInfoPanel.add(buttonPanel);
	    	
	    }
	    pitchDialogPanel.add(pitchInfoPanel);    		
	}
	
	public void render(boolean add, Pitch pitch,Panel basePanel,Marker _marker){
		//remove old idle marker
		if(this.add && marker!=null){
			this.marker.setVisible(false);
		}
		this.add = add;
		this.marker = _marker;
		marker.setDraggable(true);
		this.pitch = pitch;	
		laodImg.setVisible(false);
		deleteButton.setVisible(!add);
		deleteButton.setEnabled(!add);
		if(add){
			updateButton.setText("Ekle");
		}else{
			updateButton.setText("Guncelle");
		}
		updateButton.setEnabled(true);
		pitchNameText.setText(pitch.getName(),add );
		pitchCapacityText.setText(Integer.toString(pitch.getCapacity()), add);
		inProgress = false;
		if(basePanel!=null){
			basePanel.clear();
			basePanel.add(pitchDialogPanel);
			successImg.setVisible(false);
		}
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
			marker.setDraggable(false);
			if(add){
				PitchDialog.this.cache.addPitch(pitch);
				marker.setVisible(false);
			}else{
				PitchDialog.this.cache.updatePitch(pitch);
			}
			add=false;
			updateButton.setEnabled(false);
			laodImg.setVisible(true);
			
		}

	}
	
	private void deleteData(){
		cache.removePitch(pitch);
		deleteButton.setEnabled(false);
		laodImg.setVisible(true);
		inProgress = true;
	}

	public void pitchAdded(List<Pitch> pitch) {
		if(inProgress){
			successImg.setVisible(true);
			render(false,pitch.get(0),null,marker);
		}

	}

	public void pitchUpdated(Pitch pitch) {
		if(inProgress){
			successImg.setVisible(true);
			render(false,pitch,null,marker);
			marker.setIcon(greenField);
		}
	}

	public void pitchRemoved(Long pitch) {
		if(inProgress){
			pitchDialogPanel.removeFromParent();
			inProgress = false;
		}
	}

}
