package model;

public class TransactionEntry {
	private String productID;
	private int quantity;

	public TransactionEntry(String productID, int quantity) {
		this.productID = productID;
		this.quantity = quantity;
	}

	public String getProductID() {
		return productID;
	}

	public int getQuantity() {
		return quantity;
	}
}
