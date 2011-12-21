package com.erman.football.client.gui;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePitchHandler;
import com.erman.football.shared.Pitch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.event.HasMouseEvent;
import com.google.gwt.maps.client.event.MouseEventCallback;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PitchDialog implements ParamUpdateHandler,CachePitchHandler{

	private final TextInput pitchNameText = new TextInput(this);
	private final TextInput pitchCapacityText = new TextInput(this);
	private final Button updateButton = new Button("Apply");
	private final HorizontalPanel pitchDialogPanel = new HorizontalPanel();
	private final Marker marker = new Marker();
	
	private Cache cache;
	private boolean add;
	private Pitch pitch;
	private Panel basePanel;
	private boolean admin;
	private MapWidget mapWidget;
	 
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
	    }
	    
	    pitchDialogPanel.add(pitchInfoPanel);
	    
	    final MapOptions options = new MapOptions();
	    // Zoom level. Required
	    options.setZoom(11);
	    // Open a map centered on Cawker City, KS USA. Required
	    LatLng cor = new LatLng(41.010,28.970);
	    options.setCenter(cor);
	    // Map type. Required.
	    options.setMapTypeId(new MapTypeId().getSatellite());
	    
	    // Enable maps drag feature. Disabled by default.
	    options.setDraggable(true);
	    // Enable and add default navigation control. Disabled by default.
	    options.setNavigationControl(true);
	    // Enable and add map type control. Disabled by default.
	    options.setMapTypeControl(true);
	    options.setDisableDoubleClickZoom(true);
	    mapWidget = new MapWidget(options);
	    mapWidget.setSize("600px", "600px"); 
	    marker.setMap(mapWidget.getMap());
	    com.google.gwt.maps.client.event.Event.addListener(mapWidget.getMap(), "dblclick", new MouseEventCallback(){

			@Override
			public void callback(HasMouseEvent event) {
				marker.setPosition(event.getLatLng());
			}
	    	
	    });
	    pitchDialogPanel.add(mapWidget);
	    

	    
	    pitchDialogPanel.setBorderWidth(1);
		
	}
	
	public void render(boolean add, Pitch pitch){
		this.add = add;
		this.pitch = pitch;	
		pitchNameText.setText(pitch.getName(),add );
		pitchCapacityText.setText(Integer.toString(pitch.getCapacity()), add);
		marker.setPosition(pitch.getLocation());
		mapWidget.getMap().setCenter(pitch.getLocation());
		if(add){
			mapWidget.getMap().setZoom(13);
		}else{
			mapWidget.getMap().setZoom(16);
		}
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
			if(add){
				PitchDialog.this.cache.addPitch(pitch);
				pitchDialogPanel.removeFromParent();
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
			pitchDialogPanel.removeFromParent();
		}
	}

}
