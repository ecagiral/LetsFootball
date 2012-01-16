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
import com.google.gwt.maps.client.geocoder.Geocoder;
import com.google.gwt.maps.client.geocoder.GeocoderCallback;
import com.google.gwt.maps.client.geocoder.GeocoderRequest;
import com.google.gwt.maps.client.geocoder.HasAddressComponent;
import com.google.gwt.maps.client.geocoder.HasGeocoderResult;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerImage;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

public class PitchMapPanel extends SimplePanel implements CachePitchHandler, PitchEditDialogHandler{
	
	private final HashMap<Long,Marker> markers = new HashMap<Long,Marker>();
	private final MarkerImage.Builder greenFieldBuild = new MarkerImage.Builder("greenfield.png"); 
	private final MarkerImage.Builder yellowFieldBuild = new MarkerImage.Builder("yellowfield.png");
	private final Geocoder geocoder = new Geocoder();
	private final GeocodeHandler geoHandler = new GeocodeHandler();
	
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
	    options.setZoom(11);
	    // Open a map centered on Istanbul. Required
	    LatLng cor = new LatLng(41.0,29.0);
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
			marker.setDraggable(false);
			
		    Event.addListener(marker, "click", new MarkerClick(pitch));
		    markers.put(pitch.getKey(),marker);
		}
	}

	public void pitchUpdated(Pitch pitch) {
		markers.get(pitch.getKey()).setPosition(pitch.getLocation());
		markers.get(pitch.getKey()).setDraggable(false);
		markers.get(pitch.getKey()).setIcon(greenField);
	}

	public void pitchRemoved(Long pitch) {
		markers.remove(pitch).setVisible(false);
	}
	
	public void show(Panel panel, PitchMapPanelHandler _handler){
		restoreCurrent();
		removeIdle();	
		this.mapWidget.getMap().setCenter(new LatLng(41.0,29.0));
		this.mapWidget.getMap().setZoom(11);
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
		marker.setDraggable(true);
		marker.setMap(getMap());
		Pitch pitch = new Pitch();
		handler.markerAdded(pitch);
	}
	
	public void selectMarker(Pitch pitch){
		restoreCurrent();
		currentMarker =markers.get(pitch.getKey());
		currentMarker.setIcon(yellowField);
		currentMarker.setDraggable(true);
		getMap().setCenter(currentMarker.getPosition());
		handler.markerClicked(pitch);
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

	@Override
	public void handleModify(Pitch pitch) {
		if(pitch.getKey()==0){
			pitch.setLocation(idleMarker.getPosition());
			geoHandler.retrieveGeocode(pitch);
			removeIdle();
		}else{
			if(currentMarker.getPosition().equalsTo(pitch.getLocation())){
				cache.updatePitch(pitch);
			}else{
				pitch.setLocation(currentMarker.getPosition());
				geoHandler.retrieveGeocode(pitch);
			}	
		}
	}
	
	private class GeocodeHandler extends GeocoderCallback{
		
		private Pitch pitch;
		
		void retrieveGeocode(Pitch _pitch){
			this.pitch = _pitch;
			GeocoderRequest request = new GeocoderRequest();
			request.setLatLng(pitch.getLocation());
			geocoder.geocode(request,this);
		}
 
		public void callback(List<HasGeocoderResult> responses, String status) {
			for(HasAddressComponent addr:responses.get(0).getAddressComponents()){
				for(String type:addr.getTypes()){
					if(type.equals("administrative_area_level_1")){
						pitch.setCity(addr.getShortName());
					}else if(type.equals("sublocality")){
						pitch.setTown(addr.getShortName());
					}
				}
			}
			if(pitch.getKey()==0){
				cache.addPitch(pitch);
			}else{
				cache.updatePitch(pitch);
			}
			
		}
		
	}
	
}
