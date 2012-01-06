package com.erman.football.client.gui.pitch;

import java.util.HashMap;
import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePitchHandler;
import com.erman.football.client.gui.list.DataCell;
import com.erman.football.client.gui.list.ListPanelListener;
import com.erman.football.shared.Pitch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.event.Event;
import com.google.gwt.maps.client.event.HasMouseEvent;
import com.google.gwt.maps.client.event.MouseEventCallback;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerImage;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PitchPanel extends HorizontalPanel implements CachePitchHandler ,ListPanelListener{

	private final PitchDialog pitchDialog;	
	private final SimplePanel infoPanel = new SimplePanel();
	private final SimplePanel mapPanel = new SimplePanel();
	private final Cache cache;
	private final HashMap<Long,Marker> markers = new HashMap<Long,Marker>();
	private final MarkerImage.Builder greenFieldBuild = new MarkerImage.Builder("greenfield.png"); 
	private final MarkerImage.Builder yellowFieldBuild = new MarkerImage.Builder("yellowfield.png");
	
	private Marker currentMarker;	
	private MapWidget mapWidget;
	private MarkerImage greenField;
	private MarkerImage yellowField;
	
	public PitchPanel(Cache _cache){
		this.cache = _cache;
		cache.regiserPitch(this);
		greenField = greenFieldBuild.build();
		yellowField = yellowFieldBuild.build();
		pitchDialog = new PitchDialog(cache,greenField);
		VerticalPanel buttonPanel = new VerticalPanel();
		Label addMatch = new Label("Saha Ekle");
		addMatch.setStyleName("leftButton");
		addMatch.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				displayPitch(null);
			}
		});
		buttonPanel.add(addMatch);
		Label searchMatch = new Label("Saha Ara");
		searchMatch.setStyleName("leftButton");
		searchMatch.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				infoPanel.clear();
			}
		});
		buttonPanel.add(searchMatch);
		buttonPanel.setWidth("100px");
		
		
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
	    mapWidget.setSize("500px", "500px"); 
	    
	    mapPanel.add(mapWidget);
		this.add(buttonPanel);
		this.add(mapPanel);
		this.add(infoPanel);
		cache.getPitches();
	}
	
	private void displayPitch(Pitch pitch){
		Marker marker;
		if(pitch == null){
			marker = new Marker();
			marker.setPosition(mapWidget.getMap().getCenter());
			restoreCurrent();
			marker.setIcon(yellowField);
			marker.setMap(mapWidget.getMap());
			pitchDialog.render(true,new Pitch(),infoPanel,marker);
		}else{
			marker =markers.get(pitch.getKey());
			restoreCurrent();
			marker.setIcon(yellowField);
			currentMarker = marker;
			pitchDialog.render(false,pitch,infoPanel,marker);	
			mapWidget.getMap().setCenter(pitch.getLocation());
		}	
		infoPanel.setVisible(true);
		
	}

	public void CellClicked(DataCell dataCell) {
		//displayPitch((PitchCell)dataCell);
	}

	public void removeClicked(DataCell dataCell) {
		cache.removePitch(((PitchCell)dataCell).getPitch());
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
	
	private class MarkerClick extends MouseEventCallback{
		
		private Pitch pitch;

		public MarkerClick(Pitch _pitch){
			pitch = _pitch;
		}
		
		public void callback(HasMouseEvent event) {
			displayPitch(pitch);
		}
		
	}
	
	private void restoreCurrent(){
		if(currentMarker!=null){
			currentMarker.setIcon(greenField);
		}
	}

}
