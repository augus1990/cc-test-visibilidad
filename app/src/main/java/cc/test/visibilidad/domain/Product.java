package cc.test.visibilidad.domain;

public class Product {
	private Integer productId;
	private Integer sequence;
	
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	@Override
	public String toString() {
		return "Product [productId=" + productId + ", sequence=" + sequence + "]";
	}
}
