package view;

import java.sql.ResultSet;
import java.sql.SQLException;

import db.Database;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.HelpersFunc;

public class Register {
	private Label registrationLbl, usernameLbl, emailLbl, passwordLbl, confirmPwLbl, pNumberLbl, addressLbl, genderLbl,
			descriptionLbl, loginLbl;
	private TextField usernameTf, emailTf, pNumTf;
	private PasswordField passwordF, confirmPwF;
	private RadioButton maleR, femaleR;
	private TextArea addressTa;
	private CheckBox TnA;
	private Button registerBtn;
	private ToggleGroup genderTg;

	private GridPane formPane;
	private Stage stage;
	private Scene scene;
	private BorderPane bp;
	private HBox genderContainer, descContainer;

	public void init() {
		bp = new BorderPane();
		scene = new Scene(bp, 800, 800);
		formPane = new GridPane();

		registrationLbl = new Label("Registration");
		usernameLbl = new Label("Username :");
		emailLbl = new Label("Email :");
		passwordLbl = new Label("Password :");
		confirmPwLbl = new Label("Confirm Password :");
		pNumberLbl = new Label("Phone Number :");
		addressLbl = new Label("Address :");
		genderLbl = new Label("Gender :");

		descContainer = new HBox(10);
		descriptionLbl = new Label("Have an account?");
		loginLbl = new Label("login here");

		usernameTf = new TextField();
		usernameTf.setPromptText("input username..");
		emailTf = new TextField();
		emailTf.setPromptText("input email..");
		pNumTf = new TextField();
		pNumTf.setPromptText("input phone number..");
		addressTa = new TextArea();
		addressTa.setPromptText("input address..");

		passwordF = new PasswordField();
		passwordF.setPromptText("input password..");
		confirmPwF = new PasswordField();
		confirmPwF.setPromptText("input confirm password..");

		addressTa = new TextArea();
		addressTa.setPromptText("input address..");

		maleR = new RadioButton("Male");
		femaleR = new RadioButton("Female");
		genderTg = new ToggleGroup();
		maleR.setToggleGroup(genderTg);
		femaleR.setToggleGroup(genderTg);
		genderContainer = new HBox(15);

		TnA = new CheckBox("I agree to all terms and condition");

		registerBtn = new Button("Register");
	}

	public void arrangeScene() {
		genderContainer.getChildren().addAll(maleR, femaleR);
		descContainer.getChildren().addAll(descriptionLbl, loginLbl);

		formPane.add(registrationLbl, 1, 0);
		formPane.add(usernameLbl, 0, 1);
		formPane.add(usernameTf, 1, 1);
		formPane.add(emailLbl, 0, 2);
		formPane.add(emailTf, 1, 2);
		formPane.add(passwordLbl, 0, 3);
		formPane.add(passwordF, 1, 3);
		formPane.add(confirmPwLbl, 0, 4);
		formPane.add(confirmPwF, 1, 4);
		formPane.add(pNumberLbl, 0, 5);
		formPane.add(pNumTf, 1, 5);
		formPane.add(addressLbl, 0, 6);
		formPane.add(addressTa, 1, 6);
		formPane.add(genderLbl, 0, 7);
		formPane.add(genderContainer, 1, 7);
		formPane.add(TnA, 1, 8);
		formPane.add(descContainer, 1, 9);
		formPane.add(registerBtn, 1, 10);

		formPane.setHgap(10);
		formPane.setVgap(10);
		formPane.setPadding(new Insets(10));

		bp.setCenter(formPane);

		formPane.setAlignment(Pos.CENTER);

	}

	public void setStyle() {
		registrationLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 30; -fx-font-family: \"Arial\"");
		loginLbl.setStyle("-fx-text-fill: #0000FF");
	}

	public void loginEvent() {
		loginLbl.setOnMouseClicked(event -> {
			Login login = new Login(stage);
		});
		loginLbl.setOnMouseEntered(event -> {
			loginLbl.setStyle("-fx-underline: true; -fx-text-fill: #0000FF");
		});
		loginLbl.setOnMouseExited(event -> {
			loginLbl.setStyle("-fx-text-fill: #0000FF; ");
		});
	}

	public void registerHandler() {
		registerBtn.setOnMouseClicked(event -> {
			// Retrieve user input
			String username = usernameTf.getText();
			String email = emailTf.getText();
			String pw = passwordF.getText();
			String rePw = confirmPwF.getText();
			String pNum = pNumTf.getText();
			String address = addressTa.getText();
			String gender = "";
			RadioButton selectedButton = (RadioButton) genderTg.getSelectedToggle();

			// Check if gender is selected
			if (selectedButton != null) {
				gender = selectedButton.getText();
			} else {
				HelpersFunc.errorAlert("Register", "Gender must be selected");
				return;
			}

			// Validate and accumulate error messages
			StringBuilder errorMessage = new StringBuilder();

			if (username.length() < 5 || username.length() > 20) {
				errorMessage.append("Username must be 5-20 characters\n");
			}

			if (!uniqueUsername(username)) {
				errorMessage.append("Username already exists\n");
			}

			if (!email.endsWith("@gmail.com")) {
				errorMessage.append("Email must end with '@gmail.com'\n");
			}

			if (!HelpersFunc.isAlphaNumeric(pw)) {
				errorMessage.append("Password must be alphanumeric\n");
			}

			if (pw.length() < 5) {
				errorMessage.append("Password must be at least 5 characters\n");
			}

			if (!pw.equals(rePw)) {
				errorMessage.append("Password and Re-Password do not match\n");
			}

			if (!isNumeric(pNum)) {
				errorMessage.append("Phone number must be numeric\n");
			}

			if (!pNum.startsWith("+62")) {
				errorMessage.append("Phone number must start with \"+62\"\n");
			}

			if (!TnA.isSelected()) {
				errorMessage.append("Terms and Conditions must be checked\n");
			}

			if (username.isEmpty() || email.isEmpty() || pw.isEmpty() || rePw.isEmpty() || pNum.isEmpty()
					|| address.isEmpty() || gender.isEmpty()) {
				errorMessage.append("All fields must be filled\n");
			}

			// Show error alert if there are any validation errors
			if (errorMessage.length() > 0) {
				HelpersFunc.errorAlert("Register", errorMessage.toString().trim());
				return;
			}

			// If all requirements are met, create a new account
			Database db = Database.getDatabase();
			db.newUserData(username, pw, address, pNum, gender);
			HelpersFunc.informationAlert("Registered Successfully", "Success");
			new Login(stage);
		});
	}

	public Register(Stage stage) {
		// TODO Auto-generated constructor stub
		init();
		arrangeScene();
		setStyle();
		loginEvent();
		registerHandler();
		this.stage = stage;
		this.stage.setTitle("Register");
		this.stage.setScene(scene);
		this.stage.show();
	}

	// Helper function
	private Boolean uniqueUsername(String username) {
		Database db = Database.getDatabase();
		ResultSet result = db.getUserData();
		Boolean isUnique = true;
		try {
			while (result.next()) {
				if (username == result.getString("username")) {
					isUnique = false;
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isUnique;
	}

	public static boolean isNumeric(String phoneNumber) {
		if (phoneNumber == null || phoneNumber.isEmpty()) {
			return false;
		}
		for (int i = 3; i < phoneNumber.length(); i++) {
			if (!Character.isDigit(phoneNumber.charAt(i))) {
				return false;
			}
		}

		return true;
	}

}
