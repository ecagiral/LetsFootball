package com.erman.football.client.cache;

import java.util.List;

import com.erman.football.shared.Pitch;

public interface CachePitchHandler {

	public void pitchAdded(List<Pitch> pitch);	
	public void pitchUpdated(Pitch pitch);	
	public void pitchRemoved(Long pitch);
}
