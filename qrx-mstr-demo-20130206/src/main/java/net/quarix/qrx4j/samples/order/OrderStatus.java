package net.quarix.qrx4j.samples.order;

public enum OrderStatus {
	
	ORDERED("Ordered"),
	SHIPPED("Shipped"),
	PARTIALLY_SHIPPED("PartiallyShipped");
	
	private String statusText;
	
	private OrderStatus(String statusText) {
		this.statusText = statusText;	
	}

	public String getStatusText() {
		return statusText;
	}
	
	public static String [] getOrderStatusTextArray() {
		return new String [] {ORDERED.getStatusText(), 
				SHIPPED.getStatusText(), PARTIALLY_SHIPPED.getStatusText()};
		
	}
}
