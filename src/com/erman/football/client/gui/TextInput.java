package com.erman.football.client.gui;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;

public class TextInput{

	private final TextBox box = new TextBox();
	private ParamUpdateHandler paramListener;
	private String oldText;

	public TextInput(ParamUpdateHandler paramListener){
		this.paramListener = paramListener;
		box.addFocusHandler(new FocusHandler(){
			public void onFocus(FocusEvent event) {
				oldText = box.getText();
			}

		});
		box.addKeyUpHandler(new KeyUpHandler(){
			public void onKeyUp(KeyUpEvent event) {
				if(event.getNativeKeyCode()==KeyCodes.KEY_ENTER){
					if(!(oldText.equals(box.getText()))){
						//value changed. update it
						TextInput.this.paramListener.updateData();
					}
				}else{
					if(oldText.equals(box.getText())){
						box.removeStyleDependentName("defaultInput");
						box.removeStyleDependentName("updatedInput");
					}else{
						box.removeStyleDependentName("defaultInput");
						box.addStyleDependentName("updatedInput");
					}
				}
			}
		});
	}

	public TextBox getTextBox(){
		return box;
	}

	public void setText(String text, boolean def){
		box.setText(text);
		if(def){
			box.removeStyleDependentName("updatedInput");
			box.addStyleDependentName("defaultInput");
		}else{
			box.removeStyleDependentName("updatedInput");
			box.removeStyleDependentName("defaultInput");
		}
	}

	public String getText(){
		return box.getText();
	}

	public void setEnabled(boolean enabled){
		box.setEnabled(enabled);
	}

}
