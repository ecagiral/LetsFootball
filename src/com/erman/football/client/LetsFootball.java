package com.erman.football.client;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.gui.MainTab;
import com.erman.football.shared.ClientPlayer;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;


import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LetsFootball implements EntryPoint,LoginHandler{
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	 private Login loginPanel;
	 private MainTab playerTab;
	 private Label welcome;
	 private final Button logout = new Button("Cikis");
	 private final DockLayoutPanel mainLayout = new DockLayoutPanel(Unit.PX);
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		loginPanel = new Login(this);
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
				loginPanel.logout();
			}
			
		});
		headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		headerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		HorizontalPanel userPanel = new HorizontalPanel();
		welcome = new Label("");
		welcome.setStyleName("welcome");
		userPanel.add(welcome);
		logout.setVisible(false);
		userPanel.add(logout);
		headerPanel.add(userPanel);
		headerPanel.setStyleName("header");
		mainLayout.addNorth(headerPanel, 30);
		mainLayout.add(loginPanel);
		mainLayout.setVisible(false);
		//mainLayout.setStyleName("mainLayout");
		RootLayoutPanel.get().add(mainLayout);
	    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand () {
	        public void execute () {
	            loginPanel.setFocus();
	        }
	    });
	}
	
	public void init(){
		mainLayout.setVisible(true);
	}

	public void loggedIn(ClientPlayer player) {
		mainLayout.setVisible(true);
		Cache cache = new Cache();
		cache.setLoggedPlayer(player);
		welcome.setText(player.getName());
		logout.setVisible(true);
		loginPanel.removeFromParent();
		playerTab = new MainTab(cache);
		mainLayout.add(playerTab);
		cache.load();
	}
	
	public void loggedOut() {
		welcome.setText("");
		logout.setVisible(false);
		playerTab.removeFromParent();
		playerTab = null;
		loginPanel = new Login(this);
		mainLayout.add(loginPanel);
		loginPanel.setFocus();
	}

}
