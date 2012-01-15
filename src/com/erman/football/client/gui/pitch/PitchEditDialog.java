package com.erman.football.client.gui.pitch;

import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CachePitchHandler;
import com.erman.football.client.gui.ParamUpdateHandler;
import com.erman.football.client.gui.TextInput;
import com.erman.football.shared.DataObject;
import com.erman.football.shared.Pitch;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PitchEditDialog extends VerticalPanel implements DialogIf, ParamUpdateHandler, CachePitchHandler{

	private final TextInput pitchNameText = new TextInput(this);
	private final TextInput pitchCapacityText = new TextInput(this);
	private final TextInput pitchPhoneText = new TextInput(this);
	private final Button updateButton = new Button("Guncelle");
	private final Button deleteButton = new Button("Sil");
	private final Image laodImg = new Image("loader.gif");
	private final Image successImg = new Image("success.jpg");
	private final PitchEditDialogHandler handler;
	
	private Cache cache;
	private Pitch pitch;
	private boolean inProgress;
	 
	public PitchEditDialog(Cache cache,PitchEditDialogHandler _handler){
		this.cache = cache;
		this.handler = _handler;
		cache.regiserPitch(this);
		
		Grid infoGrid = new Grid(4,2);
		infoGrid.setWidget(0, 0,new Label("Isim:"));
		infoGrid.setWidget(0, 1,pitchNameText.getTextBox());
		pitchNameText.getTextBox().setWidth("194px");
		pitchNameText.getTextBox().setMaxLength(24);
		infoGrid.setWidget(1, 0,new Label("Kapasite:"));
		infoGrid.setWidget(1, 1,pitchCapacityText.getTextBox());
		pitchCapacityText.getTextBox().setWidth("16px");
		pitchCapacityText.getTextBox().setMaxLength(2);
		infoGrid.setWidget(2, 0,new Label("Telefon"));
		infoGrid.setWidget(2, 1,pitchPhoneText.getTextBox());
		pitchPhoneText.getTextBox().setWidth("80px");
		pitchPhoneText.getTextBox().setMaxLength(10);
		updateButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				updateData();
			}

		});
		deleteButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				deleteData();
			}

		});
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(updateButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(laodImg);
		buttonPanel.add(successImg);		
		infoGrid.setWidget(3,1,buttonPanel);
		laodImg.setVisible(false);
		successImg.setVisible(false);
		
		this.add(infoGrid);	
	}
	
	public void render(DataObject data,Panel basePanel){
		boolean add;
		pitch = (Pitch)data;
		if(pitch.getKey()==0){
			add = true;
		}else{
			add = false;
		}
		laodImg.setVisible(false);
		deleteButton.setVisible(!add);
		deleteButton.setEnabled(!add);
		if(add){
			updateButton.setText("Ekle");
		}else{
			updateButton.setText("Guncelle");
		}
		updateButton.setEnabled(true);
		pitchNameText.setText(pitch.getName(),add );
		pitchCapacityText.setText(Integer.toString(pitch.getCapacity()), add);
		pitchPhoneText.setText(pitch.getPhone(),add);
		inProgress = false;
		if(basePanel!=null){
			basePanel.clear();
			basePanel.add(this);
			successImg.setVisible(false);
		}
	}
	
	private boolean retrieveData(){
		pitch.setName(pitchNameText.getText());
		pitch.setCapacity(Integer.parseInt(pitchCapacityText.getText()));
		pitch.setPhone(pitchPhoneText.getText());
		return true;
	}
	
	public void updateData() {
		if(retrieveData()){
			inProgress = true;
			handler.handleModify(pitch);
			updateButton.setEnabled(false);
			laodImg.setVisible(true);
		}

	}
	
	private void deleteData(){
		cache.removePitch(pitch);
		deleteButton.setEnabled(false);
		laodImg.setVisible(true);
		inProgress = true;
	}

	public void pitchAdded(List<Pitch> pitch) {
		if(inProgress){
			successImg.setVisible(true);
			render(pitch.get(0),null);
		}

	}

	public void pitchUpdated(Pitch pitch) {
		if(inProgress){
			successImg.setVisible(true);
			render(pitch,null);
		}
	}

	public void pitchRemoved(Long pitch) {
		if(inProgress){
			this.removeFromParent();
			inProgress = false;
		}
	}

}
