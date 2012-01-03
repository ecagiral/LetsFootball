package com.erman.football.client.service;

import java.util.List;

import com.erman.football.shared.Pitch;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PitchServiceAsync {

	void createPitch(Pitch pitch, AsyncCallback<Pitch> callback);

	void deletePitch(Pitch pitch, AsyncCallback<Long> callback);

	void getPitches(int start, int stop, AsyncCallback<List<Pitch>> callback);

	void updatePitch(Pitch pitch, AsyncCallback<Pitch> callback);

}
