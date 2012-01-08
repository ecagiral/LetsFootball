package com.erman.football.client.gui.pitch;

import com.erman.football.shared.Pitch;
import com.google.gwt.maps.client.overlay.Marker;

public interface PitchMapPanelHandler {
	
	public void markerClicked(Pitch pitch,Marker marker);
	public void markerAdded(Pitch pitch,Marker marker);

}
