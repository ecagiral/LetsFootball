package com.erman.football.client.gui.pitch;

import java.util.HashMap;
import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePitchHandler;
import com.erman.football.shared.Pitch;
import com.google.gwt.maps.client.HasMap;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.event.Event;
import com.google.gwt.maps.client.event.HasMouseEvent;
import com.google.gwt.maps.client.event.MouseEventCallback;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerImage;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

public class PitchMapPanel extends SimplePanel implements CachePitchHandler{
	
	private final HashMap<Long,Marker> markers = new HashMap<Long,Marker>();
	private final MarkerImage.Builder greenFieldBuild = new MarkerImage.Builder("greenfield.png"); 
	private final MarkerImage.Builder yellowFieldBuild = new MarkerImage.Builder("yellowfield.png");
	
	private Marker currentMarker;	
	private Marker idleMarker;	
	private MapWidget mapWidget;
	private MarkerImage greenField;
	private MarkerImage yellowField;
	private Cache cache;
	private PitchMapPanelHandler handler;
	
	public PitchMapPanel(Cache _cache){
		this.cache = _cache;
		cache.regiserPitch(this);
		greenField = greenFieldBuild.build();
		yellowField = yellowFieldBuild.build();
		
	    final MapOptions options = new MapOptions();
	    // Zoom level. Required
	    options.setZoom(13);
	    // Open a map centered on Istanbul. Required
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
	    options.setScrollwheel(true);
	    mapWidget = new MapWidget(options);
	    mapWidget.setSize("400px", "400px"); 
	    
	    this.add(mapWidget);
	    cache.getPitches();
	}
	
	public void pitchAdded(List<Pitch> pitches) {
		for(Pitch pitch:pitches){
			Marker marker = new Marker();
			marker.setMap(mapWidget.getMap());
			marker.setPosition(pitch.getLocation());
			
			marker.setIcon(greenField);
		    Event.addListener(marker, "click", new MarkerClick(pitch));
		    markers.put(pitch.getKey(),marker);
		}
	}

	public void pitchUpdated(Pitch pitch) {
		markers.get(pitch.getKey()).setPosition(pitch.getLocation());
	}

	public void pitchRemoved(Long pitch) {
		markers.remove(pitch).setVisible(false);
	}
	
	public void show(Panel panel, PitchMapPanelHandler _handler){
		restoreCurrent();
		this.removeFromParent();
		panel.add(this);
		this.handler = _handler;
	}
	
	public void restoreCurrent(){
		if(currentMarker!=null){
			currentMarker.setIcon(greenField);
		}
	}
	
	public void removeIdle(){
		if(idleMarker!=null){
			idleMarker.setVisible(false);
			idleMarker = null;
		}
	}
	
	public MarkerImage getGreen(){
		return greenField;
	}
	
	public MarkerImage getYellow(){
		return yellowField;
	}
	
	public HasMap getMap(){
		return mapWidget.getMap();
	}
	
	public void addMarker(){
		restoreCurrent();
		removeIdle();
		Marker marker = new Marker();
		idleMarker = marker;
		marker.setPosition(getMap().getCenter());
		marker.setIcon(yellowField);
		marker.setMap(getMap());
		Pitch pitch = new Pitch();
		handler.markerAdded(pitch,marker);
	}
	
	public void selectMarker(Pitch pitch){
		restoreCurrent();
		currentMarker =markers.get(pitch.getKey());
		currentMarker.setIcon(yellowField);
		getMap().setCenter(currentMarker.getPosition());
		handler.markerClicked(pitch,currentMarker);
	}
	
	private class MarkerClick extends MouseEventCallback{
		
		private Pitch pitch;

		public MarkerClick(Pitch _pitch){
			pitch = _pitch;
		}
		
		public void callback(HasMouseEvent event) {
			selectMarker(pitch);
		}
		
	}
}
