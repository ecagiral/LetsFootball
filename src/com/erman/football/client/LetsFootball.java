package com.erman.football.client;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.gui.MainTab;
import com.erman.football.shared.ClientPlayer;
import com.google.gwt.core.client.EntryPoint;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;


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
	 private final DockLayoutPanel mainLayout = new DockLayoutPanel(Unit.CM);
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		loginPanel = new Login(this);
		
		HorizontalPanel headerPanel = new HorizontalPanel();
		headerPanel.setWidth("100%");
		welcome = new Label("Lutfen login olun");
		welcome.setStyleName("welcome");
		headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		headerPanel.add(welcome);
		Label logo = new Label("Mac Yapiyoruz!");
		logo.setStyleName("logo");
		headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		headerPanel.add(logo);
		headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		headerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		
		logout.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				reset();
			}
			
		});
		logout.setVisible(false);
		headerPanel.add(logout);
		headerPanel.setStyleName("header");
		mainLayout.addNorth(headerPanel, 2);
		mainLayout.add(loginPanel);
		RootLayoutPanel.get().add(mainLayout);
		
	}
	
	public void reset(){
		welcome.setText("Lutfen login olun");
		logout.setVisible(false);
		playerTab.removeFromParent();
		playerTab = null;
		loginPanel = new Login(this);
		mainLayout.add(loginPanel);
	}

	public void LoggedIn(ClientPlayer player) {
		Cache cache = new Cache();
		cache.setLoggedPlayer(player);
		welcome.setText("Merhaba "+player.getName());
		logout.setVisible(true);
		loginPanel.removeFromParent();
		playerTab = new MainTab(cache);
		mainLayout.add(playerTab);
		cache.load();
	}

}
