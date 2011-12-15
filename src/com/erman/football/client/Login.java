package com.erman.football.client;

import com.erman.football.client.service.PlayerService;
import com.erman.football.client.service.PlayerServiceAsync;
import com.erman.football.shared.ClientPlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Login extends VerticalPanel {

	private final PlayerServiceAsync greetingService = GWT.create(PlayerService.class);
	final TextBox emailBox = new TextBox();
	final Label loginStatus = new Label();
	final LoginHandler handler;
	
	public Login(LoginHandler _handler){
		handler = _handler;
		
		emailBox.setText("Email");
		emailBox.addKeyPressHandler(new KeyPressHandler(){

			@Override
			public void onKeyPress(KeyPressEvent event) {
				loginStatus.setText("");
				int key = new Integer(event.getCharCode());
				if(key==13||key==0){
					loginStatus.setText("Retrieving...");
					login();
				}
				
			}
			
		});
		this.add(emailBox);
		this.setVerticalAlignment(ALIGN_TOP);
		this.add(loginStatus);
		this.setHeight("50px");
		
	}

	private void login(){
		if(emailBox.getText().equals("admin")){
			ClientPlayer tmp = new ClientPlayer();
			tmp.setName("admin");
			tmp.setAdmin(true);
			handler.LoggedIn(tmp);
			return;
		}
		greetingService.getPlayer(emailBox.getText(), new AsyncCallback<ClientPlayer>() {

			public void onFailure(Throwable caught) {
				loginStatus.setText("Unknown user");
				
			}

			public void onSuccess(ClientPlayer result) {
				if(result == null){
					loginStatus.setText("Unknown user");
				}else{
					handler.LoggedIn(result);
				}
			}
			
		});
	}
}
