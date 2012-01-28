package com.erman.football.client.json;

import com.google.gwt.core.client.JavaScriptObject;

public class UserJso extends JavaScriptObject {
	  // Overlay types always have protected, zero-arg constructors
	  protected UserJso() { }

	  public final native String getFirstName() /*-{ return this.first_name; }-*/;
	  public final native String getLastName()  /*-{ return this.last_name;  }-*/;
	  public final native String getPicture()  /*-{ return this.picture;  }-*/;
	  public final native String getId()  /*-{ return this.id;  }-*/;

	  public final String getFullName() {
	    return getFirstName() + " " + getLastName();
	  }
}
