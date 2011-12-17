package com.erman.football.client.gui;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePitchHandler;
import com.erman.football.shared.Pitch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PitchDialog implements ParamUpdateHandler,CachePitchHandler{

	final VerticalPanel pitchInfoPanel = new VerticalPanel();
	final TextInput pitchNameText = new TextInput(this);
	final TextInput pitchLocationText = new TextInput(this);
	final TextInput pitchCapacityText = new TextInput(this);
	final Button updateButton = new Button("Apply");
	
	private Cache cache;
	private boolean add;
	private Grid pitchInfos;
	private Pitch pitch;
	private Panel basePanel;
	private boolean admin;
	
	public PitchDialog(Cache cache, Panel basePanel){
		this.cache = cache;
		this.basePanel = basePanel;
		cache.regiserPitch(this);
		admin = cache.getLoggedPlayer().isAdmin();
		
		updateButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				updateData();
			}

		});
		pitchInfoPanel.setWidth("250px");
	}
	
	public void render(boolean add, Pitch pitch){
		this.add = add;
		this.pitch = pitch;
		pitchInfoPanel.clear();
		if(admin){
			pitchInfos = new Grid(4,2);
			pitchNameText.setEnabled(true);
			pitchInfos.setWidget(0, 0, pitchNameText.getTextBox());
			pitchLocationText.setEnabled(true);
			pitchInfos.setWidget(1, 0, pitchLocationText.getTextBox());
			pitchCapacityText.setEnabled(true);
			pitchInfos.setWidget(2, 0, pitchCapacityText.getTextBox());
			pitchInfos.setWidget(3, 1, updateButton);
		}else{
			pitchInfos = new Grid(3,2);
			pitchNameText.setEnabled(false);
			pitchInfos.setWidget(0, 0, pitchNameText.getTextBox());
			pitchLocationText.setEnabled(false);
			pitchInfos.setWidget(1, 0, pitchLocationText.getTextBox());
			pitchCapacityText.setEnabled(false);
			pitchInfos.setWidget(2, 0, pitchCapacityText.getTextBox());
		}
		
		pitchNameText.setText(pitch.getName(),add );
		pitchLocationText.setText(pitch.getLocation(), add);
		pitchCapacityText.setText(Integer.toString(pitch.getCapacity()), add);

		pitchInfoPanel.add(pitchInfos);
		
		basePanel.clear();
		basePanel.add(pitchInfoPanel);
	}
	
	private boolean retrieveData(){
		boolean result = false;
		if(!pitchNameText.getText().equals(pitch.getName())){
			pitch.setName(pitchNameText.getText());
			result = true;
		}
		if(!pitchLocationText.getText().equals(pitch.getLocation())){
			pitch.setLocation(pitchLocationText.getText());
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
			if(add){
				PitchDialog.this.cache.addPitch(pitch);
				pitchInfoPanel.removeFromParent();
				add=false;
			}else{
				PitchDialog.this.cache.updatePitch(pitch);
			}
		}
	}

	public void pitchLoaded() {
		// TODO Auto-generated method stub
	}

	public void pitchAdded(Pitch pitch) {
		if(this.pitch.getKey()==0L){
			render(false,pitch);
		}
	}

	public void pitchUpdated(Pitch pitch) {
		if(this.pitch.getKey()==pitch.getKey()){
			render(false,pitch);
		}
	}

	public void pitchRemoved(Long pitch) {
		if(this.pitch.getKey()==pitch){
			pitchInfoPanel.removeFromParent();
		}
	}

}
