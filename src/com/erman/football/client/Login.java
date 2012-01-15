package com.erman.football.client;

import com.erman.football.client.service.LoginService;
import com.erman.football.client.service.LoginServiceAsync;
import com.erman.football.client.service.PlayerException;
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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Login extends VerticalPanel {

	private final LoginServiceAsync loginService = GWT.create(LoginService.class);
	private final PlayerServiceAsync playerService = GWT.create(PlayerService.class);
	private final TextBox emailBox = new TextBox();
	private final TextBox newEmailBox = new TextBox();
	private final TextBox nameBox = new TextBox();
	private final Label loginStatus = new Label();
	private final Label joinStatus = new Label();
	private final DialogBox warningBox = new DialogBox();
	private final LoginHandler handler;	
	private boolean notFirst = false;
	
	public Login(LoginHandler _handler){
		handler = _handler;
		login(null);
		
		warningBox.setAutoHideEnabled(true);
		warningBox.setText("Uyari");
		
		emailBox.setText("Email");
		emailBox.addKeyPressHandler(new KeyPressHandler(){
			public void onKeyPress(KeyPressEvent event) {
				loginStatus.setText("");
				int key = new Integer(event.getCharCode());
				if(key==13){
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
		newEmailBox.setText("Email");
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
		
		VerticalPanel loginPanel = new VerticalPanel();
		loginPanel.add(new Label("Kayitli Oyuncu"));
		loginPanel.add(emailBox);
		loginPanel.add(loginButton);
		loginStatus.setText("");
		loginPanel.add(loginStatus);
		
		VerticalPanel signupPanel = new VerticalPanel();
		signupPanel.add(new Label("Yeni Oyuncu"));
		signupPanel.add(nameBox);
		signupPanel.add(newEmailBox);
		signupPanel.add(joinButton);
		joinStatus.setText("");
		signupPanel.add(joinStatus);
		
		SimplePanel space = new SimplePanel();
		space.setWidth("50px");
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.add(signupPanel);
		mainPanel.add(space);
		mainPanel.add(loginPanel);

		
		this.setHorizontalAlignment(ALIGN_CENTER);
		this.setWidth("100%");
		this.add(mainPanel);
	}

	private void login(String email){
		loginStatus.setText(new String("Isleniyor..."));
		loginService.login(email, new  LoginCallback());
	}
	
	public void setFocus(){
		emailBox.setFocus(true);
	}
	
	public void join(){
		ClientPlayer newPlayer = new ClientPlayer();
		newPlayer.setEmail(newEmailBox.getText());
		newPlayer.setName(nameBox.getText());
		joinStatus.setText("Oyuncu ekleniyor...");
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
			if(caught instanceof PlayerException){
				Label error = new Label(((PlayerException)caught).getMessage());
				warningBox.add(error);
				warningBox.center();
				joinStatus.setText("");
			}
		}

		public void onSuccess(ClientPlayer result) {
			joinStatus.setText("Oyuncu eklendi");
			login(result.getEmail());
		}
		
	}
}
