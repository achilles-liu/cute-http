package org.johnny.http;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.johnny.http.controller.IBaseController;
import org.johnny.http.resource.R;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class StageContainer {
	private Map<String,Stage> stages = new HashMap<String, Stage>();
	private Map<String,IBaseController> controllers = new HashMap<>();
	
	public void addStage(String name,Stage stage) {
		this.stages.put(name, stage);
	}
	
	public Stage getStage(String name) {
		return this.stages.get(name);
	}
	
	public Stage load(String name,String resource) {
		try {
			Stage stage = new Stage();
			URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
			FXMLLoader loader = new FXMLLoader(url);
			Region tmp = loader.load();
			Scene scene = null;
			if(R.id.MAIN.equals(name)) {
				scene = new Scene(tmp,800,600);
			}else {
				scene = new Scene(tmp);
			}
			stage.setScene(scene);
			stage.setResizable(false);
			this.addStage(name, stage);
			this.controllers.put(name, loader.getController());
			return stage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Stage show(String name) {
		Stage stage = this.getStage(name);
		if(stage == null) return null;
		stage.show();
		return stage;
	}
	
	public Stage show(String name,Object data) {
		Stage stage = this.getStage(name);
		if(stage == null) return null;
		IBaseController bc = this.controllers.get(name);
		bc.inject(data);
		stage.show();
		return stage;
	}
	
	public boolean change(String from,String to) {
		this.getStage(from).close();
		this.show(to);
		return true;
	}
	
	public void close(String name) {
		this.getStage(name).close();
	}
	
	public boolean remove(String name) {
		return this.stages.remove(name) != null;
	}
}
