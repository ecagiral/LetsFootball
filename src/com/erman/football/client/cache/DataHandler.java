package com.erman.football.client.cache;

import java.util.List;

import com.erman.football.shared.DataObject;

public interface DataHandler {
	
	public void dataAdded(List<DataObject> data,long loggedUser);
	public void dataRemoved(List<Long> dataId);
	public void dataUpdated(List<DataObject> data);

}
