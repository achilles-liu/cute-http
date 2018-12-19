package org.johnny.http.model;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RequestInfo {
	private final StringProperty request;
    private final StringProperty response;
    private final ObjectProperty<LocalDate> requestDate;
    private String requestId;
    private String method;
    
    public RequestInfo() {
        this(null, null);
    }
    
    public RequestInfo(String requestStr, String responseStr) {
    	this.request = new SimpleStringProperty(requestStr);
    	this.response = new SimpleStringProperty(responseStr);
    	this.requestDate = new SimpleObjectProperty<LocalDate>(LocalDate.of(2017, 12, 3));
    }
    
    public String getRequest() {
    	return this.request.get();
    }
    
    public void setRequest(String request) {
    	this.request.set(request);
    }
    
    public String getResponse() {
    	return this.response.get();
    }
    
    public void setResponse(String response) {
    	this.response.set(response);
    }

	public StringProperty request() {
		return request;
	}

	public StringProperty response() {
		return response;
	}

	public ObjectProperty<LocalDate> requestDate() {
		return requestDate;
	}
    
	public void setRequestDate(LocalDate requestDate) {
		this.requestDate.set(requestDate);
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
}
