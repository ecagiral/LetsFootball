package com.erman.football.client;

import com.erman.football.client.service.LoginService;
import com.erman.football.client.service.LoginServiceAsync;
import com.erman.football.client.service.PlayerService;
import com.erman.football.client.service.PlayerServiceAsync;
import com.erman.football.shared.ClientPlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Login extends VerticalPanel {

	private final LoginServiceAsync loginService = GWT.create(LoginService.class);
	private final PlayerServiceAsync playerService = GWT.create(PlayerService.class);
	private final TextBox emailBox = new TextBox();
	private final TextBox nameBox = new TextBox();
	private final Label loginStatus = new Label();
	private final LoginHandler handler;	
	private boolean notFirst = false;
	
	public Login(LoginHandler _handler){
		handler = _handler;
		login(null);
		
		emailBox.setText("Email");
		emailBox.addKeyPressHandler(new KeyPressHandler(){
			public void onKeyPress(KeyPressEvent event) {
				loginStatus.setText("");
				int key = new Integer(event.getCharCode());
				if(key==13||key==0){
					login(emailBox.getText());
				}
			}
		});
		emailBox.addFocusHandler(new FocusHandler(){
			public void onFocus(FocusEvent event) {
				emailBox.selectAll();
			}
			
		});
		nameBox.setText("Isim");
		
		Button loginButton = new Button("Giri&#351");
		loginButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				login(emailBox.getText());
			}		
		});
		
		Button joinButton = new Button("Kat&#305l");
		joinButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				join();
			}		
		});
		
		VerticalPanel mainPanel = new VerticalPanel();
		HorizontalPanel loginPanel = new HorizontalPanel();
		loginPanel.add(emailBox);
		loginPanel.add(loginButton);
		loginStatus.setText("");
		loginPanel.add(loginStatus);
		mainPanel.add(loginPanel);
		mainPanel.add(nameBox);
		mainPanel.add(joinButton);

		this.setHorizontalAlignment(ALIGN_CENTER);
		this.setWidth("100%");
		this.add(mainPanel);
	}

	private void login(String email){
		loginStatus.setText(new String("&#304&#351leniyor..."));
		loginService.login(email, new  LoginCallback());
	}
	
	public void setFocus(){
		emailBox.setFocus(true);
	}
	
	public void join(){
		ClientPlayer newPlayer = new ClientPlayer();
		newPlayer.setEmail(emailBox.getText());
		newPlayer.setName(nameBox.getText());
		playerService.addPlayer(newPlayer, new AddCallback());
	}
	
	public void logout(){
		loginService.logout(new  AsyncCallback<Boolean>(){

			public void onFailure(Throwable caught) {
				handler.loggedOut();
			}

			public void onSuccess(Boolean result) {
				handler.loggedOut();
			}
			
		});
	}
	
	private class LoginCallback implements AsyncCallback<ClientPlayer>{

		public void onFailure(Throwable caught) {
			if(notFirst){
				loginStatus.setText("Unknown user");
			}else{
				handler.init();
			}
			notFirst = true;
		}

		public void onSuccess(ClientPlayer result) {
			if(result == null){
				if(notFirst){
					loginStatus.setText("Unknown user");
				}else{
					handler.init();
				}
				notFirst = true;
			}else{
				handler.loggedIn(result);
			}
		}
		
	}
	
	private class AddCallback implements AsyncCallback<ClientPlayer>{

		public void onFailure(Throwable caught) {
	
		}

		public void onSuccess(ClientPlayer result) {
			login(result.getEmail());
		}
		
	}
}
