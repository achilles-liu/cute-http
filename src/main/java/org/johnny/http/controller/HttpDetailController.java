package org.johnny.http.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class HttpDetailController implements Initializable,IBaseController{
	@FXML private TextArea txHttpDetail;
	private Object data;

	public void initialize(URL location, ResourceBundle resources) { /**initialize*/ }

	public void inject(Object data) {
		this.data = data;
		this.txHttpDetail.setText(String.valueOf(this.data));
	}
}
