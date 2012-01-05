package com.erman.football.server.service;

import java.util.ArrayList;
import java.util.List;

import com.erman.football.client.service.PitchService;
import com.erman.football.server.data.PitchDO;
import com.erman.football.server.data.Pitch_JDO_DB;
import com.erman.football.shared.Pitch;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class PitchServiceImpl extends RemoteServiceServlet implements PitchService{

	public Pitch createPitch(Pitch Pitch) {
		return Pitch_JDO_DB.addPitch(Pitch);
		
	}

	public List<Pitch> getPitches(int start, int stop) {
		List<Pitch> result = new ArrayList<Pitch>();
		for(PitchDO pitchDO:Pitch_JDO_DB.getPitches(start, stop)){
			result.add(pitchDO.convert());
		}
		return result;
	}
	
	public List<Pitch> getPitches() {
		List<Pitch> result = new ArrayList<Pitch>();
		for(PitchDO pitchDO:Pitch_JDO_DB.getPitches()){
			result.add(pitchDO.convert());
		}
		return result;
	}

	public Long deletePitch(Pitch Pitch) {
		Pitch_JDO_DB.deletePitch(Pitch.getKey());	
		return Pitch.getKey();
	}

	public Pitch updatePitch(Pitch pitch) {
		Pitch_JDO_DB.updatePitch(pitch);
		return pitch;
	}
}

