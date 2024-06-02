package main;

import java.sql.ResultSet;

import db.Database;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class HelpersFunc {

	public static void errorAlert(String type, String message) {
		Alert error = new Alert(AlertType.ERROR);
		error.setTitle("Error");
		error.setHeaderText(String.format("Failed to %s", type));
		error.setContentText(message);
		error.showAndWait();
	}

	public static void informationAlert(String message, String title) {
		Alert information = new Alert(AlertType.INFORMATION);
		information.setTitle(title);
		information.setHeaderText(message);
		information.showAndWait();
	}

	public static boolean isAlphaNumeric(String password) {
		if (password == null || password.isEmpty()) {
			return false;
		}

		for (char c : password.toCharArray()) {
			if (!Character.isLetterOrDigit(c)) {
				return false;
			}
		}

		return true;
	}

	public static ResultSet getProductData() {
		Database db = new Database();
		ResultSet result = db.getProductData();
		return result;
	}

	public static ResultSet userInfo() {
		Database db = new Database();
		ResultSet result = db.getUserData();
		return result;
	}

	public static ResultSet cartInfo() {
		Database db = new Database();
		ResultSet result = db.getCartData();
		return result;
	}

}
