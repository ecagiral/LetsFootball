package com.erman.football.client;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.gui.MainTab;
import com.erman.football.shared.ClientPlayer;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;


import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LetsFootball implements EntryPoint,LoginHandler{

	 private Login loginPanel;
	 private MainTab mainTab;
	 private Label welcome;
	 private FacebookUtil face;
	 private final Button logout = new Button("Cikis");
	 private final VerticalPanel mainLayout = new VerticalPanel();
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		loginPanel = new Login(this);
	    face = new FacebookUtil(this,loginPanel);
		
		
		Window.addResizeHandler(new ResizeHandler(){
			public void onResize(ResizeEvent event) {

			}	
		});
		HorizontalPanel headerPanel = new HorizontalPanel();
		headerPanel.setWidth("100%");
		Label logo = new Label("TEST");
		logo.setStyleName("logo");
		headerPanel.add(logo);
				
		logout.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				face.logout();
			}
			
		});
		headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		headerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		HorizontalPanel userPanel = new HorizontalPanel();
		welcome = new Label("");
		welcome.setStyleName("welcome");
		userPanel.add(welcome);
		logout.setVisible(true);
		userPanel.add(logout);
		headerPanel.add(userPanel);
		headerPanel.setStyleName("header");
		mainLayout.add(headerPanel);
		mainLayout.add(loginPanel);
		mainLayout.setVisible(false);
		mainLayout.setStyleName("mainLayout");
		RootPanel.get("soccer").add(mainLayout);
	    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	            loginPanel.setFocus();
	        }
	    });
	    mainLayout.setVisible(true);
	}
	
	public void init(){
		mainLayout.setVisible(true);
	}

	public void loggedIn(ClientPlayer player) {
		if(player == null){
			face.getMe();
			return;
		}
		mainLayout.setVisible(true);
		Cache cache = new Cache();
		cache.setLoggedPlayer(player);
		welcome.setText(player.getName());
		logout.setVisible(true);
		loginPanel.setVisible(false);
		mainTab = new MainTab(cache);
		mainLayout.add(mainTab);
		cache.load();
	}
	
	public void loggedOut() {
		welcome.setText("");
		logout.setVisible(true);
		mainTab.removeFromParent();
		mainTab = null;
		loginPanel = new Login(this);
		mainLayout.add(loginPanel);
		loginPanel.setFocus();
	}
	
}
