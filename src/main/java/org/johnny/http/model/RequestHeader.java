package org.johnny.http.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RequestHeader {
	private final StringProperty name;
	private final StringProperty value;
	
	public RequestHeader() {
		this(null, null);
	}
	
	public RequestHeader(String name,String value) {
		this.name = new SimpleStringProperty(name);
		this.value = new SimpleStringProperty(value);
	}
	
	public String getName() {
		return this.name.get();
	}
	
	public String getValue() {
		return this.value.get();
	}
	
	public StringProperty name() {
		return this.name;
	}
	
	public StringProperty value() {
		return this.value;
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public void setValue(String value) {
		this.value.set(value);
	}
}
