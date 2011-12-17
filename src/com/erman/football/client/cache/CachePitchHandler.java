package com.erman.football.client.cache;

import com.erman.football.shared.Pitch;

public interface CachePitchHandler {
	
	public void pitchLoaded();
	public void pitchAdded(Pitch pitch);	
	public void pitchUpdated(Pitch pitch);	
	public void pitchRemoved(Long pitch);
}
