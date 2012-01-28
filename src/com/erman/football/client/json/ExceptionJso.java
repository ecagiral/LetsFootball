package com.erman.football.client.json;

import com.google.gwt.core.client.JavaScriptObject;

public class ExceptionJso extends JavaScriptObject {
	  // Overlay types always have protected, zero-arg constructors
	  protected ExceptionJso() { }

	  // Typically, methods on overlay types are JSNI
	  public final native String getType() /*-{ return this.type; }-*/;
	  public final native String getMessage()  /*-{ return this.message;  }-*/;
}
