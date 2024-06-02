package NavBar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import view.Home;
import view.Login;
import view.ManageProduct;

public class AdminNav {
	public static MenuBar adminNav(Stage stage, String userID) {
		MenuBar menuBar;
		Menu home, manage_Product, account;
		MenuItem homePage, manageP, logOut;

		menuBar = new MenuBar();

		// Menu
		home = new Menu("Home");
		manage_Product = new Menu("Manage Products");
		account = new Menu("Account");

		// Items
		homePage = new MenuItem("Home Page");
		manageP = new MenuItem("Manage Products");
		logOut = new MenuItem("Log Out");

		// Arrange items and menu
		home.getItems().add(homePage);
		manage_Product.getItems().add(manageP);
		account.getItems().add(logOut);

		menuBar.getMenus().addAll(home, manage_Product, account);

		// Handler

		homePage.setOnAction(event -> {
			Home hm = new Home(stage, userID);
		});
		manageP.setOnAction(event -> {
			ManageProduct mp = new ManageProduct(stage, userID);
		});
		logOut.setOnAction(event -> {
			Login lg = new Login(stage);
		});

		return menuBar;
	}
}
