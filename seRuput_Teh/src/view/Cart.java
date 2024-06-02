package view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import NavBar.CustomerNav;
import db.Database;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.HelpersFunc;
import model.TransactionEntry;

public class Cart {
	private Stage stage;
	private Scene scene;
	private BorderPane bp;
	private GridPane gp;

	private Label cartLbl, productName, productDetail, priceLbl, totalCartLbl, infoLbl, usernameLbl, phoneLbl,
			addressLbl, quantityLbl, totalPrice;
	private Button updateCart, removeFromCart, makePurchase;
	private HBox buttonHB, quantityHB;
	private VBox detailContainer, infoContainer;
	private Spinner<Integer> quantity;
	private ListView<String> cartView;
	private MenuBar navbar;
	private String username, userID, pNUm, address, productID;
	private int quantitySpinner, price;
	private MenuBar navBar;
	int sumPrice, updateQuantity, curQuantity;
	boolean cartIsEmpty = true;

	Database db = new Database();

	private void init() {
		getUserInfo();

		bp = new BorderPane();
		scene = new Scene(bp, 800, 600);
		gp = new GridPane();

		// 0,0
		cartLbl = new Label(String.format("%s's Cart", username));
		// 1,0
		cartView = new ListView<String>();

		// 1,2
		detailContainer = new VBox(10);
		productName = new Label(String.format("Welcome, %s", username));
		productDetail = new Label("Select a product to add and remove");
		priceLbl = new Label("");

		quantityLbl = new Label("Quantity :");
		quantity = new Spinner<>(-100, 100, 1);
		totalPrice = new Label("");
		totalPrice.setVisible(false);
		quantityHB = new HBox(10);

		updateCart = new Button("Update Cart");
		removeFromCart = new Button("Remove From Cart");
		buttonHB = new HBox(10);
		// 2,0
		sumCartPrice();
		totalCartLbl = new Label(String.format("Total : Rp.%d", sumPrice));
		infoLbl = new Label("Order Information");
		usernameLbl = new Label(String.format("Username : %s", username));
		phoneLbl = new Label(String.format("Phone Number : %s", pNUm));
		addressLbl = new Label(String.format("Address : %s", address));
		makePurchase = new Button("Make Purchase");
		infoContainer = new VBox(7);

		navBar = CustomerNav.custNav(stage, userID);

		showCart();
	}

	private void arrangeScene() {
		quantityHB.getChildren().addAll(quantityLbl, quantity, totalPrice);
		buttonHB.getChildren().addAll(updateCart, removeFromCart);

		priceLbl.setVisible(false);
		quantityHB.setVisible(false);
		buttonHB.setVisible(false);

		detailContainer.getChildren().addAll(productName, productDetail, priceLbl, quantityHB, buttonHB);
		infoContainer.getChildren().addAll(totalCartLbl, infoLbl, usernameLbl, phoneLbl, addressLbl, makePurchase);

		gp.add(cartLbl, 0, 0);
		gp.add(cartView, 0, 1);
		gp.add(detailContainer, 1, 1);
		gp.add(infoContainer, 0, 2);

		gp.setHgap(10);
		gp.setVgap(5);
		gp.setPadding(new Insets(10));

		bp.setTop(navBar);
		bp.setCenter(gp);

		gp.setAlignment(Pos.TOP_LEFT);
	}

	private void setStyle() {
		cartLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 24; -fx-font-family: \"Arial\"");
		infoLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-font-family: \"Arial\"");
		productName.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-font-family: \"Arial\"");

		productDetail.setWrapText(true);
		productDetail.setMaxWidth(scene.getWidth() / 2);

		navBar.prefWidthProperty().bind(scene.widthProperty());
	}

	public Cart(Stage stage, String userid) {
		// TODO Auto-generated constructor stub
		this.userID = userid;
		this.stage = stage;

		init();
		arrangeScene();
		setStyle();
		showDetail();
		sumTotalPrice();
		btnEvent();

		this.stage.setTitle("myCart");
		this.stage.setScene(scene);
		this.stage.show();
	}

	private void getUserInfo() {
		ResultSet result = HelpersFunc.userInfo();

		try {
			while (result.next()) {
				if (this.userID.contentEquals(result.getString("userID"))) {
					this.username = result.getString("username");
					this.address = result.getString("address");
					this.pNUm = result.getString("phone_num");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void showCart() {
		ResultSet result = HelpersFunc.cartInfo();

		try {
			while (result.next()) {
				if (result.getString("userID").contentEquals(this.userID)) {
					ResultSet result1 = HelpersFunc.getProductData();

					while (result1.next()) {
						if (result.getString("productID").contentEquals(result1.getString("productID"))) {
							int quantityToAdd = result.getInt("quantity");
							int priceToAdd = result1.getInt("product_price") * quantityToAdd;
							String productToAdd = result1.getString("product_name");
							String toAdd = String.format("%dx %s (%d)", quantityToAdd, productToAdd, priceToAdd);

							cartView.getItems().add(toAdd);
							sumPrice += priceToAdd;
							cartIsEmpty = false;
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (cartIsEmpty) {
			productName.setText("No Items in Cart");
			productDetail.setText("Consider Adding one!");
		}
		cartView.setPrefSize(380, 200);
		cartView.setMaxWidth(scene.getWidth() / 2);
	}

	private void sumTotalPrice() {
		quantity.valueProperty().addListener((observable, oldValue, newValue) -> {
			int tprice = price * newValue;

			totalPrice.setText("Total: Rp." + tprice);
			if (newValue != oldValue) {
				totalPrice.setVisible(true);
			} else {
				totalPrice.setVisible(false);
			}

		});
	}

	private void sumCartPrice() {
		sumPrice = 0;

		ResultSet result = HelpersFunc.cartInfo();
		try {
			while (result.next()) {
				if (userID.contentEquals(result.getString("userID"))) {
					String productID = result.getString("productID");
					int quantity = result.getInt("quantity");

					// Fetch product data outside the loop over cart items
					ResultSet result1 = HelpersFunc.getProductData();
					while (result1.next()) {
						if (productID.contentEquals(result1.getString("productID"))) {
							sumPrice += result1.getInt("product_price") * quantity;
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void showDetail() {
		cartView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			Platform.runLater(() -> {
				// Extract product name from the selected cart item

				String productNameFromCart = extractProductName(newValue);
				productName.setText(productNameFromCart);

				price = 0;

				ResultSet result = HelpersFunc.getProductData();
				try {
					while (result.next()) {
						if (result.getString("product_name").contentEquals(productNameFromCart)) {
							price = result.getInt("product_price");
							productID = result.getString("productID");
							productDetail.setText(result.getString("product_des"));
							priceLbl.setText("Price: Rp." + price);

							// Reset Spinner value to 1 when selecting another item
							quantity.getValueFactory().setValue(1);

							productName.setVisible(true);
							productDetail.setVisible(true);
							priceLbl.setVisible(true);
							quantityHB.setVisible(true);
							buttonHB.setVisible(true);

							break;
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});

		});
	}

	// Helper method to extract product name from the cart item
	private String extractProductName(String cartItem) {
		if (cartItem == null) {
			return "";
		}

		int endIndex = cartItem.lastIndexOf("(") - 1;

		if (endIndex >= 0 && cartItem.indexOf("x") + 2 <= endIndex) {
			return cartItem.substring(cartItem.indexOf("x") + 2, endIndex);
		} else {
			return "";
		}
	}

	private void btnEvent() {
		updateCart.setOnMouseClicked(event -> {
			ResultSet result = HelpersFunc.cartInfo();
			updateQuantity = quantity.getValue();

			if (updateQuantity == 0) {
				HelpersFunc.errorAlert("Not a Valid Amount", "");
				return;
			}

			try {
				while (result.next()) {
					if (userID.contentEquals(result.getString("userID"))
							&& productID.contentEquals(result.getString("productID"))) {
						curQuantity = result.getInt("quantity");
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			int finalQuantity = curQuantity + updateQuantity;
			Platform.runLater(() -> {
				if (finalQuantity > 0) {
					db.updateCartQuantity(productID, userID, curQuantity + updateQuantity);
					cartView.getItems().clear();
					showCart();
					sumCartPrice();
					totalCartLbl.setText(String.format("Total : Rp.%d", sumPrice));
					HelpersFunc.informationAlert("Updated From Cart", "Message");
				} else if (finalQuantity == 0) {
					db.deleteCartItem(productID, userID);
					cartView.getItems().clear();
					showCart();
					sumCartPrice();
					totalCartLbl.setText(String.format("Total : Rp.%d", sumPrice));
					HelpersFunc.informationAlert("Deleted From Cart", "Message");
				} else {
					Alert Error = new Alert(AlertType.ERROR);
					Error.setTitle("Error");
					Error.setHeaderText("Not a Valid Amount");
					Error.showAndWait();
					return;
				}
			});

			productDetail.setVisible(false);

			productName.setVisible(false);
			priceLbl.setVisible(false);
			quantityHB.setVisible(false);
			buttonHB.setVisible(false);
		});

		removeFromCart.setOnMouseClicked(event -> {
			Platform.runLater(() -> {
				db.deleteCartItem(productID, userID);
				cartView.getItems().clear();
				showCart();
				sumCartPrice();
				totalCartLbl.setText(String.format("Total : Rp.%d", sumPrice));
				HelpersFunc.informationAlert("Deleted From Cart", "Message");
			});

			productDetail.setVisible(false);
			productName.setVisible(false);
			priceLbl.setVisible(false);
			quantityHB.setVisible(false);
			buttonHB.setVisible(false);
		});

		makePurchase.setOnMouseClicked(event -> {
			showConfirmationDialog();
		});

	}

	private void showConfirmationDialog() {
		Dialog<ButtonType> confirmationDialog = new Dialog<>();
		confirmationDialog.initOwner(stage);
		confirmationDialog.setTitle("Order Confirmation");

		VBox layout = new VBox(10);
		layout.getChildren().add(new Label("Are you sure you want to make a purchase?"));

		ButtonType yesButtonType = new ButtonType("Yes");
		ButtonType noButtonType = new ButtonType("No");
		confirmationDialog.getDialogPane().getButtonTypes().addAll(yesButtonType, noButtonType);

		confirmationDialog.getDialogPane().setContent(layout);

		confirmationDialog.showAndWait().ifPresent(response -> {
			if (response == yesButtonType) {
				handlePurchaseConfirmation();
			}
		});
	}

	private void handlePurchaseConfirmation() {
		if (cartIsEmpty) {
			// Display an error message
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("Failed To Make Transactions");
			error.showAndWait();
		} else {
			// Perform the purchase action
			List<TransactionEntry> transactionEntries = new ArrayList<>();
			ResultSet cartResult = Database.cartInfo();

			try {
				while (cartResult.next()) {
					if (cartResult.getString("userID").contentEquals(userID)) {
						TransactionEntry entry = new TransactionEntry(cartResult.getString("productID"),
								cartResult.getInt("quantity"));
						transactionEntries.add(entry);

						// Delete the cart entry from the database
						db.deleteCartItem(cartResult.getString("productID"), userID);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			showCart();
			db.newTransaction(userID, transactionEntries);
			HelpersFunc.informationAlert("Successfully Purchased", "Message");
		}
	}

}