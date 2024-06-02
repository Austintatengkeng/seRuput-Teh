package view;

import java.sql.ResultSet;
import java.sql.SQLException;

import NavBar.AdminNav;
import db.Database;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.HelpersFunc;

public class ManageProduct {
	private Stage stage;
	private Scene scene;
	private BorderPane bp;
	private GridPane gp;
	private ListView<String> productView;
	private Button addProduct, updateProduct, removeProduct, addBtnA, backBtnA, updateBtn, backBtnU, removeBtn,
			backBtnR;
	private Label brandLbl, productDetail, productPrice, productNameLbl, inputName, inputPrice, inputDes, updateP,
			removeCon;
	private VBox detailContainer, addEventContainer, updateEventContainer, removeEventContainer;
	private HBox btnContainer, addBtnContainer, updateBtnContainer, removeBtnContainer;
	private String productID, userID;
	private MenuBar navBar;

	private TextField pName, pPrice, updatePrice;
	private TextArea pDes;

	private Database db = new Database();

	public void init() {
		bp = new BorderPane();
		gp = new GridPane();
		scene = new Scene(bp, 800, 600);

		// Row 0
		brandLbl = new Label("Manage Product");
		productView = new ListView<>();
		showProduct();

		// Row 1
		productNameLbl = new Label("Welcome, admin");
		productDetail = new Label("Select a product to update");
		productPrice = new Label("");
		productPrice.setVisible(false);
		addProduct = new Button("Add Product");
		detailContainer = new VBox(10);

		navBar = AdminNav.adminNav(stage, userID);

		updateProduct = new Button("Update Product");
		removeProduct = new Button("Remove Product");
		btnContainer = new HBox(10);

		inputName = new Label("Input product Name");
		pName = new TextField();
		pName.setPromptText("input product name..");
		inputPrice = new Label("Input Product Price");
		pPrice = new TextField();
		pPrice.setPromptText("input product price..");
		inputDes = new Label("Input product description");
		pDes = new TextArea();
		pDes.setPromptText("input product descrption..");
		addBtnA = new Button("Add Product");
		backBtnA = new Button("Back");
		addBtnContainer = new HBox(10);
		addEventContainer = new VBox(10);
		addBtnContainer.getChildren().addAll(addBtnA, backBtnA);
		addEventContainer.getChildren().addAll(inputName, pName, inputPrice, pPrice, inputDes, pDes, addBtnContainer);

		updateP = new Label("Update Product");
		updatePrice = new TextField();
		updatePrice.setPromptText("Input new price..");
		updateBtn = new Button("Update");
		backBtnU = new Button("Back");
		updateBtnContainer = new HBox(10);
		updateEventContainer = new VBox(10);
		updateBtnContainer.getChildren().addAll(updateBtn, backBtnU);
		updateEventContainer.getChildren().addAll(updateP, updatePrice, updateBtnContainer);

		removeCon = new Label("Are you sure, you want to remove this product?");
		removeBtn = new Button("Remove Product");
		backBtnR = new Button("Back");
		removeBtnContainer = new HBox(10);
		removeBtnContainer.getChildren().addAll(removeBtn, backBtnR);
		removeEventContainer = new VBox(10);
		removeEventContainer.getChildren().addAll(removeCon, removeBtnContainer);

	}

	public void setStyle() {
		brandLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 34; -fx-font-family: \"Arial\"");
		productNameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-font-family: \"Arial\"");
		productDetail.setWrapText(true);
		productDetail.setMaxWidth(scene.getWidth() / 2);

		navBar.prefWidthProperty().bind(scene.widthProperty());

		removeCon.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-font-family: \"Arial\"");
		removeCon.setWrapText(true);

	}

	public void arrangeScene() {
		productPrice.setVisible(false);

		detailContainer.getChildren().addAll(productNameLbl, productDetail, productPrice, addProduct);

		btnContainer.getChildren().addAll(updateProduct, removeProduct);

		gp.add(brandLbl, 0, 0);
		gp.add(productView, 0, 1);
		gp.add(detailContainer, 1, 1);

		gp.setHgap(10);
		gp.setVgap(5);
		gp.setPadding(new Insets(10));

		bp.setTop(navBar);
		bp.setCenter(gp);

		gp.setAlignment(Pos.TOP_LEFT);

	}

	public void showDetail() {
		productView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (!detailContainer.getChildren().contains(btnContainer)) {
				detailContainer.getChildren().add(btnContainer);
			}
			productNameLbl.setText(newValue);
			int price = 0;

			ResultSet result = HelpersFunc.getProductData();
			try {
				while (result.next()) {
					if (result.getString("product_name").contentEquals(newValue)) {
						price = result.getInt("product_price");
						productID = result.getString("productID");
						productDetail.setText(result.getString("product_des"));
						productPrice.setText("Price: Rp." + price);
						productPrice.setVisible(true);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	public ManageProduct(Stage stage, String userid) {
		// TODO Auto-generated constructor stub
		this.userID = userid;
		this.stage = stage;

		init();
		arrangeScene();
		setStyle();
		showDetail();
		btnEvent();

		this.stage.setTitle("Manage Product");
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
		productView.setPrefSize(300, 180);

	}

	private void btnEvent() {
		addProduct.setOnMouseClicked(event -> {
			detailContainer.getChildren().removeAll(btnContainer);
			detailContainer.getChildren().addAll(addEventContainer);

			addBtnA.setOnMouseClicked(event1 -> {
				if (pName.getText().isEmpty() || pPrice.getText().isEmpty() || pDes.getText().isEmpty()) {
					HelpersFunc.errorAlert("Add Product", "All fields must be filled");
					return;
				}

				String newPName = pName.getText();
				int newPPrice = Integer.parseInt(pPrice.getText());
				String newPDes = pDes.getText();

				if (!uniquePname(newPName)) {
					HelpersFunc.errorAlert("Add Product", "Product Name already exists");
					return;
				}

				if (newPPrice <= 0) {
					HelpersFunc.errorAlert("Add Product", "Invalid Price Amount");
					return;
				}

				db.addProduct(newPName, newPPrice, newPDes);
				HelpersFunc.informationAlert("New Product Successfully Added", "Message");
				ManageProduct mp = new ManageProduct(stage, userID);
			});

			backBtnA.setOnMouseClicked(event2 -> {
				ManageProduct mp = new ManageProduct(stage, userID);
			});
		});

		updateProduct.setOnMouseClicked(event -> {
			detailContainer.getChildren().removeAll(addProduct, btnContainer);
			detailContainer.getChildren().addAll(updateEventContainer);

			updateBtn.setOnMouseClicked(event1 -> {
				// Check if the entered price is more than 0
				if (Integer.parseInt(updatePrice.getText()) <= 0) {
					HelpersFunc.errorAlert("Update Product", "Invalid amount");
					return;
				}

				db.updateProductPrice(productID, Integer.parseInt(updatePrice.getText()));
				HelpersFunc.informationAlert("Product Price Successfully Updated", "Message");

				ManageProduct mp = new ManageProduct(stage, userID);
			});

			backBtnU.setOnMouseClicked(event2 -> {
				ManageProduct mp = new ManageProduct(stage, userID);
			});
		});

		removeProduct.setOnMouseClicked(event -> {
			detailContainer.getChildren().removeAll(addProduct, btnContainer);
			detailContainer.getChildren().addAll(removeEventContainer);

			removeBtn.setOnMouseClicked(event1 -> {
				db.removeProduct(productID);
				HelpersFunc.informationAlert("Product Successfully Removed", "Message");

				ManageProduct mp = new ManageProduct(stage, userID);
			});

			backBtnR.setOnMouseClicked(event2 -> {
				ManageProduct mp = new ManageProduct(stage, userID);
			});
		});

	}

	private Boolean uniquePname(String pname) {
		ResultSet result = db.getProductData();
		Boolean isUnique = true;
		try {
			while (result.next()) {
				if (pname.contentEquals(result.getString("product_name"))) {
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

}
