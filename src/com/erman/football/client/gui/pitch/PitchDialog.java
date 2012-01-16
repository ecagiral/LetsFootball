package com.erman.football.client.gui.pitch;

import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePitchHandler;
import com.erman.football.shared.DataObject;
import com.erman.football.shared.Pitch;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PitchDialog extends VerticalPanel implements DialogIf, CachePitchHandler{

	private final Label pitchNameText = new Label();
	private final Label pitchCapacityText = new Label();
	private final Label pitchPhoneText = new Label();
	private final Label pitchLocationText = new Label();

	private Pitch pitch;
	 
	public PitchDialog(Cache cache,PitchEditDialogHandler _handler){
		cache.regiserPitch(this);

		HorizontalPanel namePanel = new HorizontalPanel();
		namePanel.add(new Label("Isim:"));
		namePanel.add(pitchNameText);
		
		HorizontalPanel capacityPanel = new HorizontalPanel();
		capacityPanel.add(new Label("Kapasite:"));
		capacityPanel.add(pitchCapacityText);
		pitchCapacityText.setWidth("10px");
		
		HorizontalPanel locationPanel = new HorizontalPanel();
		locationPanel.add(new Label("Lokasyon:"));
		locationPanel.add(pitchLocationText);
		pitchLocationText.setWidth("10px");
		
		HorizontalPanel phonePanel = new HorizontalPanel();
		phonePanel.add(new Label("Telefon:"));
		phonePanel.add(pitchPhoneText);
		pitchPhoneText.setWidth("30px");
		
		this.add(namePanel);
		this.add(locationPanel);
		this.add(capacityPanel);
		this.add(phonePanel);
  		
	}
	
	public void render(DataObject data,Panel basePanel){
		pitch = (Pitch)data;
		pitchNameText.setText(pitch.getName());
		pitchCapacityText.setText(Integer.toString(pitch.getCapacity()));
		pitchPhoneText.setText(pitch.getPhone());
		pitchLocationText.setText(pitch.getTown()+", "+pitch.getCity());
		if(basePanel!=null){
			basePanel.clear();
			basePanel.add(this);
		}
		
	}
	
	public void pitchAdded(List<Pitch> pitch) {

	}

	public void pitchUpdated(Pitch pitch) {
		if(pitch.getKey()==this.pitch.getKey()){
			render(pitch,null);
		}
	}

	public void pitchRemoved(Long pitch) {
		if(pitch==this.pitch.getKey()){
			this.removeFromParent();
		}
	}

}
