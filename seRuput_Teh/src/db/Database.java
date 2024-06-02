package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import model.TransactionEntry;

public class Database {
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String HOST = "localhost:3306";
	private final String DBNAME = "seruput_teh";
	private final String URL = String.format("jdbc:mysql://%s/%s", HOST, DBNAME);
	private static Database db;

	private Connection connect;
	private Statement state;

	public Database() {
		// TODO Auto-generated constructor stub
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			connect = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			state = connect.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Database getDatabase() {
		if (db == null) {
			db = new Database();
		}
		return db;
	}

	public ResultSet getUserData() {
		String query = "Select * FROM user";

		try {
			return state.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public ResultSet getProductData() {
		String query = "Select * FROM product";

		try {
			return state.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public ResultSet getCartData() {
		String query = "Select * FROM cart";

		try {
			return state.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void newUserData(String uname, String password, String address, String phoneNumber, String Gender) {
		int nextIdInt = (getLastOfCustindex() + 1);
		String custID = String.format("CU%03d", nextIdInt);

		String query = "INSERT INTO user (`userID`, `username`, `password`, `role`, `address`, `phone_num`, `gender`) VALUES (?, ?, ?, 'User', ?, ?, ?)";

		try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
			preparedStatement.setString(1, custID);
			preparedStatement.setString(2, uname);
			preparedStatement.setString(3, password);
			preparedStatement.setString(4, address);
			preparedStatement.setString(5, phoneNumber);
			preparedStatement.setString(6, Gender);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void addCart(String productID, String userID, int quantity) {
		String query = "INSERT INTO cart (`productID`, `userID`, `quantity`) VALUES (?, ?, ?)";

		try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
			preparedStatement.setString(1, productID);
			preparedStatement.setString(2, userID);
			preparedStatement.setInt(3, quantity);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void newTransaction(String userID, List<TransactionEntry> transactionEntries) {
		// Generate a new transaction ID
		int nextTransactionIndex = getLastTransactionIndex() + 1;
		String transactionID = String.format("TR%03d", nextTransactionIndex);

		try {
			// Insert into transaction_header
			String headerQuery = "INSERT INTO transaction_header (`transactionID`, `userID`) VALUES (?, ?)";
			try (PreparedStatement headerStatement = connect.prepareStatement(headerQuery)) {
				headerStatement.setString(1, transactionID);
				headerStatement.setString(2, userID);
				headerStatement.executeUpdate();
			}

			// Insert into transaction_detail for each product
			String detailQuery = "INSERT INTO transaction_detail (`transactionID`, `productID`, `quantity`) VALUES (?, ?, ?)";
			try (PreparedStatement detailStatement = connect.prepareStatement(detailQuery)) {
				for (TransactionEntry entry : transactionEntries) {
					detailStatement.setString(1, transactionID);
					detailStatement.setString(2, entry.getProductID());
					detailStatement.setInt(3, entry.getQuantity());
					detailStatement.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateCartQuantity(String productID, String userID, int newQuantity) {
		String query = "UPDATE cart SET quantity = ? WHERE productID = ? AND userID = ?";

		try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
			preparedStatement.setInt(1, newQuantity);
			preparedStatement.setString(2, productID);
			preparedStatement.setString(3, userID);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteCartItem(String productID, String userID) {
		String query = "DELETE FROM cart WHERE productID = ? AND userID = ?";

		try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
			preparedStatement.setString(1, productID);
			preparedStatement.setString(2, userID);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Helpers Function to generate customerID
	private int getLastOfCustindex() {
		String sql = "SELECT MAX(CAST(SUBSTRING(userID, 3) AS SIGNED)) AS latestCustomerIndex FROM user WHERE role LIKE '%user%'";
		// Execute the SQL statement
		ResultSet resultSet = null;
		try {
			resultSet = state.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get the latest customer index
		int latestCustomerIndex = 0;
		try {
			if (resultSet.next()) {
				latestCustomerIndex = resultSet.getInt("latestCustomerIndex");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return latestCustomerIndex;
	}

	private int getLastTransactionIndex() {
		String sql = "SELECT MAX(CAST(SUBSTRING(transactionID, 3) AS SIGNED)) AS latestTransactionIndex FROM transaction_header";
		ResultSet resultSet = null;

		try {
			resultSet = state.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int latestTransactionIndex = 0;
		try {
			if (resultSet.next()) {
				latestTransactionIndex = resultSet.getInt("latestTransactionIndex");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return latestTransactionIndex;
	}

	// Get ResultSet of Product
	public static ResultSet productData() {
		Database db = new Database();
		ResultSet result = db.getProductData();
		return result;
	}

	// Get ResultSet of User
	public static ResultSet userInfo() {
		Database db = new Database();
		ResultSet result = db.getUserData();
		return result;
	}

	// get ResultSet of Cart
	public static ResultSet cartInfo() {
		Database db = new Database();
		ResultSet result = db.getCartData();
		return result;
	}

	public ResultSet getTransaction() {
		String query = "Select * FROM transaction_header";

		try {
			return state.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public ResultSet getDetailT() {
		String query = "Select * FROM transaction_detail";

		try {
			return state.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private String generateNewProductID() {
		// Generate a new product ID
		int nextProductIndex = getLastProductIndex() + 1;
		return String.format("TEX%03d", nextProductIndex);
	}

	private int getLastProductIndex() {
		// Similar to getLastTransactionIndex but for the product table
		String sql = "SELECT MAX(CAST(SUBSTRING(productID, 4) AS SIGNED)) AS latestProductIndex FROM product";
		ResultSet resultSet = null;

		try {
			resultSet = state.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int latestProductIndex = 0;
		try {
			if (resultSet.next()) {
				latestProductIndex = resultSet.getInt("latestProductIndex");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return latestProductIndex;
	}

	public void addProduct(String productName, int productPrice, String productDescription) {
		// Generate a new product ID
		String productID = generateNewProductID();

		try {
			// Insert into product table
			String productQuery = "INSERT INTO product (`productID`, `product_name`, `product_price`, `product_des`) VALUES (?, ?, ?, ?)";
			try (PreparedStatement productStatement = connect.prepareStatement(productQuery)) {
				productStatement.setString(1, productID);
				productStatement.setString(2, productName);
				productStatement.setDouble(3, productPrice);
				productStatement.setString(4, productDescription);
				productStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateProductPrice(String productID, int newPrice) {
		try {
			// Update product price in the product table
			String updateQuery = "UPDATE product SET product_price = ? WHERE productID = ?";
			try (PreparedStatement updateStatement = connect.prepareStatement(updateQuery)) {
				updateStatement.setDouble(1, newPrice);
				updateStatement.setString(2, productID);
				updateStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeProduct(String productID) {
		try {
			// Remove product from the product table
			String removeQuery = "DELETE FROM product WHERE productID = ?";
			try (PreparedStatement removeStatement = connect.prepareStatement(removeQuery)) {
				removeStatement.setString(1, productID);
				removeStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
