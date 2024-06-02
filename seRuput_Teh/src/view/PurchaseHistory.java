package view;

import java.sql.ResultSet;
import java.sql.SQLException;

import NavBar.CustomerNav;
import db.Database;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.HelpersFunc;

public class PurchaseHistory {
	private Stage Stage;
	private Scene scene;
	private BorderPane bp;
	private GridPane gp;
	private VBox mainContainer, detailContainer;
	private HBox mainBox;
	private Label purchaseH, transactionID, usernameLbl, pNumLbl, addressLbl, totalPrice;
	private ListView<String> detail, transaction;

	private String userID, username, address, pNum;
	private int sumPrice;
	private MenuBar navBar;

	private Database db = new Database();

	private Boolean transactionIsEmpty = true;

	private void init() {
		navBar = CustomerNav.custNav(Stage, userID);
		getUserInfo();

		bp = new BorderPane();
		scene = new Scene(bp, 800, 600);
		gp = new GridPane();

		purchaseH = new Label(String.format("%s's Purchase History", username));
		mainContainer = new VBox(10);

		transaction = new ListView<>();
		showTransaction();
		mainBox = new HBox(10);

		transactionID = new Label("Select a Transaction to View Details");
		usernameLbl = new Label(String.format("Username : %s", username));
		pNumLbl = new Label(String.format("Phone Number : %s", pNum));
		addressLbl = new Label(String.format("Address : %s", address));
		totalPrice = new Label("");
		detail = new ListView<>();
		detailContainer = new VBox(10);

	}

	private void arrangeScene() {
		detailContainer.getChildren().addAll(transactionID, usernameLbl, pNumLbl, addressLbl, totalPrice, detail);
		mainBox.getChildren().addAll(transaction, detailContainer);

		mainContainer.getChildren().addAll(purchaseH, mainBox);

		detail.setVisible(false);
		totalPrice.setVisible(false);
		addressLbl.setVisible(false);
		pNumLbl.setVisible(false);
		usernameLbl.setVisible(false);

		bp.setTop(navBar);
		bp.setCenter(mainContainer);

		mainContainer.setPadding(new Insets(10));
		mainContainer.setAlignment(Pos.TOP_LEFT);
	}

	private void setStyle() {

		purchaseH.setStyle("-fx-font-weight: bold; -fx-font-size: 28; -fx-font-family: \"Arial\"");
		transactionID.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-font-family: \"Arial\"");
		transaction.setPrefSize(180, 300);
		detail.setPrefSize(400, 160);

		navBar.prefWidthProperty().bind(scene.widthProperty());
	}

	public PurchaseHistory(Stage stage, String userid) {
		// TODO Auto-generated constructor stub
		this.userID = userid;
		this.Stage = stage;

		init();
		arrangeScene();
		setStyle();
		showDetail();

		this.Stage.setTitle("Purchase History");
		this.Stage.setScene(scene);
		this.Stage.show();
	}

	private void getUserInfo() {
		ResultSet result = HelpersFunc.userInfo();

		try {
			while (result.next()) {
				if (this.userID.contentEquals(result.getString("userID"))) {
					this.username = result.getString("username");
					this.address = result.getString("address");
					this.pNum = result.getString("phone_num");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showDetail() {
		transaction.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			detail.getItems().clear();
			sumPrice = 0;

			ResultSet result = db.getDetailT();

			try {
				while (result.next()) {
					if (result.getString("TransactionID").contentEquals(newValue)) {
						ResultSet result1 = HelpersFunc.getProductData();

						while (result1.next()) {
							if (result.getString("productID").contentEquals(result1.getString("productID"))) {
								int quantityToAdd = result.getInt("quantity");
								int priceToAdd = result1.getInt("product_price") * quantityToAdd;
								String productToAdd = result1.getString("product_name");
								String toAdd = String.format("%dx %s (%d)", quantityToAdd, productToAdd, priceToAdd);

								detail.getItems().add(toAdd);
								sumPrice += priceToAdd;
							}
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			totalPrice.setText(String.format("Total : Rp.%d", sumPrice));
			totalPrice.setVisible(true);
			detail.setVisible(true);
			addressLbl.setVisible(true);
			pNumLbl.setVisible(true);
			usernameLbl.setVisible(true);
			usernameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 13; -fx-font-family: \"Arial\"");
		});
	}

	public void showTransaction() {
		ResultSet tResult = db.getTransaction();

		try {
			while (tResult.next()) {
				if (userID.contentEquals(tResult.getString("userID"))) {
					transaction.getItems().add(tResult.getString("transactionID"));
					transactionIsEmpty = false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (transactionIsEmpty) {
			transactionID.setText("There's No History");
			usernameLbl.setText("Considering Purchasing Our Products");
			usernameLbl.setVisible(true);
		}

	}

}
