package com.erman.football.client.gui.pitch;

import java.util.ArrayList;
import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePitchHandler;
import com.erman.football.client.gui.list.DataCell;
import com.erman.football.client.gui.list.ListPanelListener;
import com.erman.football.shared.DataObject;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PitchPanel extends HorizontalPanel implements CachePitchHandler ,ListPanelListener{

	final private PitchDialog pitchDialog;	
	final private SimplePanel infoPanel = new SimplePanel();
	final private SimplePanel mapPanel = new SimplePanel();
	final private Cache cache;
	final private ArrayList<Marker> markers = new ArrayList<Marker>();
	
	private MapWidget mapWidget;
	
	public PitchPanel(Cache _cache){
		this.cache = _cache;
		cache.regiserPitch(this);
		pitchDialog = new PitchDialog(cache);
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
				mapPanel.setVisible(true);
				//listMainPanel.setVisible(true);
			}
		});
		buttonPanel.add(searchMatch);
		buttonPanel.setWidth("100px");
		
		
	    final MapOptions options = new MapOptions();
	    // Zoom level. Required
	    options.setZoom(13);
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
	    options.setScrollwheel(true);
	    mapWidget = new MapWidget(options);
	    mapWidget.setSize("500px", "500px"); 
	    Event.addListener(mapWidget.getMap(), "dblclick", new MouseEventCallback(){
			public void callback(HasMouseEvent event) {
				retrievePitches();
			}
	    });
	    mapPanel.add(mapWidget);
		this.add(buttonPanel);
		this.add(mapPanel);
		this.add(infoPanel);
		//retrievePitches();
	}
	
	private void displayPitch(Pitch pitch){
		if(pitch == null){
			//listMainPanel.setVisible(false);
			mapPanel.setVisible(false);
			pitchDialog.render(true,new Pitch(),infoPanel);	
		}else{
			pitchDialog.render(false,pitch,infoPanel);	
		}
		
	}
	
	private void retrievePitches(){
		for(Marker marker:markers){
			marker.setMap(null);
		}
		markers.clear();
		double NELat = mapWidget.getMap().getBounds().getNorthEast().getLatitude();
		double NELon = mapWidget.getMap().getBounds().getNorthEast().getLongitude();
		double SWLat = mapWidget.getMap().getBounds().getSouthWest().getLatitude();
		double SWLon = mapWidget.getMap().getBounds().getSouthWest().getLongitude();
		cache.getPitches(NELat, NELon,SWLat,SWLon);
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
		    Event.addListener(marker, "click", new MarkerClick(pitch));
		    markers.add(marker);
		}
		//ArrayList<DataObject> data = new ArrayList<DataObject>();
		//data.addAll(pitch);
		//listMainPanel.dataAdded(data);
	}

	public void pitchUpdated(Pitch pitch) {
		ArrayList<DataObject> data = new ArrayList<DataObject>();
		data.add(pitch);
		//listMainPanel.dataUpdated(data);
	}

	public void pitchRemoved(Long pitch) {
		ArrayList<Long> data = new ArrayList<Long>();
		data.add(pitch);
		//listMainPanel.dataRemoved(data);
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

}
