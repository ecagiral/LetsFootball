package com.erman.football.client.service;

import java.util.List;

import com.erman.football.shared.Pitch;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("pitch")
public interface PitchService extends RemoteService{
	public Pitch createPitch(Pitch pitch);
	public List<Pitch> getPitches(int start, int stop);
	public Long deletePitch(Pitch pitch);
	public Pitch updatePitch(Pitch pitch);
}
