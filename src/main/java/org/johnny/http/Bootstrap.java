package org.johnny.http;

import org.johnny.http.resource.R;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Bootstrap extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception {
		StageContainer sc = StageMgr.INSTANCE.container();
		Stage stage = sc.load(R.id.MAIN, R.layout.MAIN);
		stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/png/cute-http-64x64.png")));
		sc.show(R.id.MAIN);
	}

}
