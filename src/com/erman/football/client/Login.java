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
import com.google.gwt.user.client.ui.Image;
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
	private final DialogBox warningBox = new DialogBox();
	private final Label loginResult = new Label();
	private final Label joinResult = new Label();
	private final Image loginStatus = new Image("loader.gif");
	private final Image joinStatus = new Image("loader.gif");
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
		HorizontalPanel loginButtonPanel = new HorizontalPanel();
		loginButtonPanel.add(loginButton);
		loginStatus.setVisible(false);
		loginButtonPanel.add(loginStatus);
		loginPanel.add(loginButtonPanel);
		loginPanel.add(loginResult);
		
		VerticalPanel signupPanel = new VerticalPanel();
		signupPanel.add(new Label("Yeni Oyuncu"));
		signupPanel.add(nameBox);
		signupPanel.add(newEmailBox);
		HorizontalPanel signButtonPanel = new HorizontalPanel();
		signButtonPanel.add(joinButton);
		joinStatus.setVisible(false);
		signButtonPanel.add(joinStatus);
		signupPanel.add(signButtonPanel);
		signupPanel.add(joinResult);
		
		SimplePanel space1 = new SimplePanel();
		space1.setWidth("190px");
		SimplePanel space2 = new SimplePanel();
		space2.setWidth("50px");
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.add(space1);
		mainPanel.add(signupPanel);
		mainPanel.add(space2);
		mainPanel.add(loginPanel);

		this.setWidth("100%");
		this.add(mainPanel);
	}

	private void login(String email){
		loginStatus.setVisible(true);
		loginService.login(email, new  LoginCallback());
	}
	
	public void setFocus(){
		emailBox.setFocus(true);
	}
	
	public void join(){
		ClientPlayer newPlayer = new ClientPlayer();
		newPlayer.setEmail(newEmailBox.getText());
		newPlayer.setName(nameBox.getText());
		joinStatus.setVisible(true);
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
			loginStatus.setVisible(false);
			if(notFirst){
				loginResult.setText("Bilinmeyen kullanici");
			}else{
				handler.init();
			}
			notFirst = true;
		}

		public void onSuccess(ClientPlayer result) {
			loginStatus.setVisible(false);
			if(result == null){
				if(notFirst){
					loginResult.setText("Bilinmeyen kullanici");
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
			joinStatus.setVisible(false);
			if(caught instanceof PlayerException){
				Label error = new Label(((PlayerException)caught).getMessage());
				warningBox.add(error);
				warningBox.center();
				joinResult.setText("");
				
			}
		}

		public void onSuccess(ClientPlayer result) {
			joinStatus.setVisible(false);
			joinResult.setText("Oyuncu eklendi");
			login(result.getEmail());
		}
		
	}
}
