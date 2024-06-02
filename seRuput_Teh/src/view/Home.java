package view;

import java.sql.ResultSet;
import java.sql.SQLException;

import NavBar.AdminNav;
import NavBar.CustomerNav;
import db.Database;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class Home {
	private Stage stage;
	private Scene scene;
	private BorderPane bp;
	private GridPane gp;
	private ListView<String> productView;
	private Spinner<Integer> quantity;
	private Button addToCartBtn;
	private Label brandLbl, productDetail, productPrice, totalPrice, quantityLbl, welcome_ProductName;
	private VBox detailContainer;
	private HBox priceContainer, navContainer;
	private String userID, userRole, username, productID;
	private MenuBar navBar;
	private Integer numQuantity;

	private void getUserInfo() {
		ResultSet result = HelpersFunc.userInfo();

		try {
			while (result.next()) {
				if (this.userID.contentEquals(result.getString("userID"))) {
					this.userRole = result.getString("role");
					if (this.userRole.contentEquals("Admin")) {
						this.username = "Admin";
					} else {
						this.username = result.getString("username");
					}

				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void init() {
		getUserInfo();
		bp = new BorderPane();
		gp = new GridPane();
		scene = new Scene(bp, 800, 600);

		// Row 0
		brandLbl = new Label("SeRuput Teh");
		productView = new ListView<>();
		showProduct();

		// Row 1
		welcome_ProductName = new Label(String.format("Welcome, %s", this.username));
		productDetail = new Label("Select a product to view");
		productPrice = new Label("");
		priceContainer = new HBox(10);
		quantity = new Spinner<>(1, 100, 1);
		quantityLbl = new Label("Quantity:");
		totalPrice = new Label("");
		totalPrice.setVisible(false);
		addToCartBtn = new Button("Add To Cart");

		detailContainer = new VBox(10);
		if (userRole.contentEquals("Admin")) {
			navBar = AdminNav.adminNav(stage, userID);
		} else {
			navBar = CustomerNav.custNav(stage, userID);
		}
		navContainer = new HBox(navBar);
	}

	public void setStyle() {
		brandLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 34; -fx-font-family: \"Arial\"");
		welcome_ProductName.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-font-family: \"Arial\"");
		productDetail.setWrapText(true);
		productDetail.setMaxWidth(scene.getWidth() / 2);
		priceContainer.setAlignment(Pos.TOP_LEFT);

		navBar.prefWidthProperty().bind(scene.widthProperty());

	}

	public void arrangeScene() {
		productPrice.setVisible(false);
		priceContainer.setVisible(false);
		addToCartBtn.setVisible(false);

		priceContainer.getChildren().addAll(quantityLbl, quantity, totalPrice);
		detailContainer.getChildren().addAll(welcome_ProductName, productDetail, productPrice, priceContainer,
				addToCartBtn);

		gp.add(brandLbl, 0, 0);
		gp.add(productView, 0, 1);
		gp.add(detailContainer, 1, 1);

		gp.setHgap(10);
		gp.setVgap(5);
		gp.setPadding(new Insets(10));

		bp.setTop(navContainer);
		bp.setCenter(gp);

		gp.setAlignment(Pos.CENTER_LEFT);

	}

	public void showDetail() {
		productView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			Platform.runLater(() -> {
				welcome_ProductName.setText(newValue);
				int price = 0;

				ResultSet result = HelpersFunc.getProductData();
				try {
					while (result.next()) {
						if (result.getString("product_name").contentEquals(newValue)) {
							price = result.getInt("product_price");
							productID = result.getString("productID");
							productDetail.setText(result.getString("product_des"));
							productPrice.setText("Price: Rp." + price);

							quantity.getValueFactory().setValue(1);

							if (userRole.contentEquals("Admin")) {
								productPrice.setVisible(true);
							} else if (userRole.contentEquals("User")) {
								productPrice.setVisible(true);
								priceContainer.setVisible(true);
								addToCartBtn.setVisible(true);

								sumTotalPrice(price);
							}
							break;
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		});
	}

	public void addToCart() {
		addToCartBtn.setOnMouseClicked(event -> {
			numQuantity = quantity.getValue();
			Database db = Database.getDatabase();

			ResultSet result = db.getCartData();

			boolean addQuantity = false;

			try {
				while (result.next()) {
					if (result.getString("productID").contentEquals(productID)
							&& result.getString("userID").contentEquals(userID)) {
						numQuantity = +result.getInt("quantity");
						addQuantity = true;
						break;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (numQuantity > 0 && addQuantity == false) {
				db.addCart(productID, userID, numQuantity);
				HelpersFunc.informationAlert("Added to Cart", "Message");
			} else if (addQuantity == true) {
				db.updateCartQuantity(productID, userID, numQuantity);
				HelpersFunc.informationAlert("Added to Cart", "Message");
			} else {
				HelpersFunc.errorAlert("Add to Cart", "Quantity must be > 0");
			}

		});
	}

	public Home(Stage stage, String userid) {
		// TODO Auto-generated constructor stub
		this.userID = userid;
		this.stage = stage;

		init();
		arrangeScene();
		setStyle();
		showDetail();
		addToCart();

		this.stage.setTitle("Home");
		this.stage.setScene(scene);
		this.stage.show();
	}

	private void showProduct() {
		ResultSet result = HelpersFunc.getProductData();

		try {
			while (result.next()) {
				productView.getItems().add(result.getString("product_name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		productView.setMinSize(380, 500);
		productView.setMaxWidth(scene.getWidth() / 2);

	}

	private void sumTotalPrice(Integer price) {
		quantity.valueProperty().addListener((observable, oldValue, newValue) -> {
			int tprice = price * newValue;

			totalPrice.setText("Total: Rp." + tprice);
			if (newValue > 1) {
				totalPrice.setVisible(true);
			} else {
				totalPrice.setVisible(false);
			}

		});
	}

}
