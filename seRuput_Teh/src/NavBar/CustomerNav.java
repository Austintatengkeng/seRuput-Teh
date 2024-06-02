package NavBar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import view.Cart;
import view.Home;
import view.Login;
import view.PurchaseHistory;

public class CustomerNav {

	public static MenuBar custNav(Stage stage, String userID) {
		MenuBar menuBar;
		Menu home, cart, account;
		MenuItem homePage, myCart, purchaseHistory, logOut;

		menuBar = new MenuBar();

		// Menu
		home = new Menu("Home");
		cart = new Menu("Cart");
		account = new Menu("Account");

		// Items
		homePage = new MenuItem("Home Page");
		myCart = new MenuItem("My Cart");
		purchaseHistory = new MenuItem("Purchase History");
		logOut = new MenuItem("Log Out");

		// Arrange items and menu
		home.getItems().add(homePage);
		cart.getItems().addAll(myCart, purchaseHistory);
		account.getItems().add(logOut);

		menuBar.getMenus().addAll(home, cart, account);

		// Handler
		homePage.setOnAction(event -> {
			Home homeS = new Home(stage, userID);
		});
		myCart.setOnAction(event -> {
			Cart cartS = new Cart(stage, userID);
		});
		purchaseHistory.setOnAction(event -> {
			PurchaseHistory pH = new PurchaseHistory(stage, userID);
		});
		logOut.setOnAction(event -> {
			Login loginS = new Login(stage);
		});

		return menuBar;
	}
}
