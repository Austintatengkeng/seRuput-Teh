package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.Login;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("seRuput Teh");

		Login login = new Login(primaryStage);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

}
