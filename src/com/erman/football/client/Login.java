package com.erman.football.client;

import com.erman.football.client.service.LoginService;
import com.erman.football.client.service.LoginServiceAsync;
import com.erman.football.client.service.PlayerException;
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
	private final VerticalPanel signupDataPanel = new VerticalPanel();
	private final VerticalPanel loginDataPanel = new VerticalPanel();
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
		
		//check if user is already has logged in
		login(null);
		
		warningBox.setAutoHideEnabled(true);
		warningBox.setText("Uyari");
		
		emailBox.setText("Email");
		emailBox.addKeyPressHandler(new KeyPressHandler(){
			public void onKeyPress(KeyPressEvent event) {
				int key = new Integer(event.getCharCode());
				if(key==13){
					ClientPlayer player = new ClientPlayer();
					player.setEmail(emailBox.getText());
					login(player);
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
				ClientPlayer player = new ClientPlayer();
				player.setEmail(emailBox.getText());
				login(player);
			}		
		});
		
		Button joinButton = new Button("Kat&#305l");
		joinButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				join();
			}		
		});
		
		VerticalPanel loginPanel = new VerticalPanel();
		Label loginPanelBtn = new Label("Gir");
		loginPanelBtn.setStyleName("loginPanelButton");
		loginPanelBtn.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				signupDataPanel.setVisible(false);
				loginDataPanel.setVisible(true);
			}
			
		});
		loginPanel.add(loginPanelBtn);		
		loginDataPanel.add(emailBox);
		HorizontalPanel loginButtonPanel = new HorizontalPanel();
		loginButtonPanel.add(loginButton);
		loginStatus.setVisible(false);
		loginButtonPanel.add(loginStatus);
		loginDataPanel.add(loginButtonPanel);
		loginDataPanel.add(loginResult);
		loginDataPanel.setVisible(false);
		loginPanel.add(loginDataPanel);
		
		VerticalPanel signupPanel = new VerticalPanel();
		Label joinPanelBtn = new Label("Kaydol");
		joinPanelBtn.setStyleName("loginPanelButton");
		joinPanelBtn.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				loginDataPanel.setVisible(false);
				signupDataPanel.setVisible(true);
			}
			
		});
		signupPanel.add(joinPanelBtn);
		signupDataPanel.add(nameBox);
		signupDataPanel.add(newEmailBox);
		HorizontalPanel signButtonPanel = new HorizontalPanel();
		signButtonPanel.add(joinButton);
		joinStatus.setVisible(false);
		signButtonPanel.add(joinStatus);
		signupDataPanel.add(signButtonPanel);
		signupDataPanel.add(joinResult);
		signupDataPanel.setVisible(false);
		signupPanel.add(signupDataPanel);
		
		Label facebookLogin = new Label("facebook");
		facebookLogin.setStyleName("loginPanelButton");
		facebookLogin.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				FacebookUtil.login();
				loginDataPanel.setVisible(false);
				signupDataPanel.setVisible(false);
			}	
		});
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.setStyleName("loginPanel");
		mainPanel.add(signupPanel);
		mainPanel.add(loginPanel);
		mainPanel.add(facebookLogin);

		this.setWidth("100%");
		this.add(mainPanel);
	}
	
	public void login(ClientPlayer player){
		loginStatus.setVisible(true);
		loginService.login(player, new  LoginCallback());
	}
	
	public void setFocus(){
		emailBox.setFocus(true);
	}
	
	public void join(){
		ClientPlayer player = new ClientPlayer();
		player.setEmail(newEmailBox.getText());
		player.setName(nameBox.getText());
		joinStatus.setVisible(true);
		login(player);
	}
	
	public void logout(){
		loginService.logout(new  AsyncCallback<ClientPlayer>(){

			public void onFailure(Throwable caught) {
				handler.loggedOut();
			}

			public void onSuccess(ClientPlayer result) {
				System.out.println("logout result received "+result.getFacebookId());
				if(result != null && result.getFacebookId() != 0){
					System.out.println("for facebook");
					//facebook user. logout from facebook
					FacebookUtil.logout();
				}
				loginDataPanel.setVisible(false);
				signupDataPanel.setVisible(false);
				handler.loggedOut();
			}
			
		});
	}
	
	private class LoginCallback implements AsyncCallback<ClientPlayer>{

		public void onFailure(Throwable caught) {
			loginStatus.setVisible(false);
			joinStatus.setVisible(false);
			if(caught instanceof PlayerException){
				Label error = new Label(((PlayerException)caught).getMessage());
				warningBox.add(error);
				warningBox.center();
				joinResult.setText("");
				return;
			}
			if(notFirst){
				loginResult.setText("Bilinmeyen kullanici");
			}else{
				handler.init();
			}
			notFirst = true;
		}

		public void onSuccess(ClientPlayer result) {
			loginStatus.setVisible(false);
			joinStatus.setVisible(false);
			loginDataPanel.setVisible(false);
			signupDataPanel.setVisible(false);
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
	
}
