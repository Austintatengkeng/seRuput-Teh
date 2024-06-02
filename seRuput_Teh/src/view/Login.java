package view;

import java.sql.ResultSet;
import java.sql.SQLException;

import db.Database;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.HelpersFunc;

public class Login {
	private Stage stage;
	private Scene scene;
	private BorderPane bp;
	private GridPane grid;
	private HBox descContainer;
	private Label loginLbl, usernameLbl, passwordLbl, descLbl, registerLbl;
	private TextField usernameTf;
	private PasswordField passPf;
	private Button loginBtn;

	public void init() {
		bp = new BorderPane();
		scene = new Scene(bp, 500, 500);
		grid = new GridPane();
		descContainer = new HBox(10);
		loginLbl = new Label("Login");
		usernameLbl = new Label("Username :");
		passwordLbl = new Label("Password :");
		descLbl = new Label("Don't have an account yet?");
		registerLbl = new Label("register here");
		usernameTf = new TextField();
		usernameTf.setPromptText("input username..");
		passPf = new PasswordField();
		passPf.setPromptText("input password..");
		loginBtn = new Button("Login");
	}

	public void arrangeScene() {
		descContainer.getChildren().addAll(descLbl, registerLbl);

		grid.add(loginLbl, 1, 0);
		grid.add(usernameLbl, 0, 1);
		grid.add(usernameTf, 1, 1);
		grid.add(passwordLbl, 0, 2);
		grid.add(passPf, 1, 2);
		grid.add(descContainer, 1, 3);
		grid.add(loginBtn, 1, 4);

		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10));

		bp.setCenter(grid);

		grid.setAlignment(Pos.CENTER);

	}

	public void setStyle() {
		loginLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 30; -fx-font-family: \"Arial\"");
		registerLbl.setStyle("-fx-text-fill: #0000FF");
	}

	public void regisAnchor() {
		registerLbl.setOnMouseClicked(event -> {
			Register register = new Register(stage);
		});
		registerLbl.setOnMouseEntered(event -> {
			registerLbl.setStyle("-fx-underline: true; -fx-text-fill: #0000FF");
		});
		registerLbl.setOnMouseExited(event -> {
			registerLbl.setStyle("-fx-text-fill: #0000FF; ");
		});
	}

	public void loginHandler() {
		loginBtn.setOnMouseClicked(event -> {
			String username = usernameTf.getText();
			String password = passPf.getText();
			String userID = "";
			if (username.isEmpty() || password.isEmpty()) {
				HelpersFunc.errorAlert("Login", "All fields must be filled ");
			}

			Database db = Database.getDatabase();
			ResultSet result = db.getUserData();
			Boolean credential = false;
			Boolean isAdmin = false;

			try {
				while (result.next()) {
					if (username.contentEquals(result.getString("username"))
							&& password.contentEquals(result.getString("password"))) {
						if (result.getString("role").contentEquals("Admin")) {
							isAdmin = true;
						}
						credential = true;
						userID = result.getString("userID");
						break;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (credential == false) {
				HelpersFunc.errorAlert("Login", "Invalid credentials");
			}
			if (credential == true) {
				Home home = new Home(stage, userID);
			}

		});

	}

	public Login(Stage stage) {
		// TODO Auto-generated constructor stub
		this.stage = stage;
		init();
		arrangeScene();
		setStyle();
		regisAnchor();
		loginHandler();
		this.stage.setTitle("Login");
		this.stage.setScene(scene);
		this.stage.show();
	}

}
