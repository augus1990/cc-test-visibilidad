package cc.test.visibilidad.domain;

import java.util.Map;

public class Size {
	private Integer sizeId;
	private Integer productId;
	private Boolean backSoon;
	private Boolean special;
	private Product product;
	private Map<Integer,Product> productMap;
	
	public Integer getSizeId() {
		return sizeId;
	}
	public void setSizeId(Integer sizeId) {
		this.sizeId = sizeId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Boolean getBackSoon() {
		return backSoon;
	}
	public void setBackSoon(Boolean backSoon) {
		this.backSoon = backSoon;
	}
	public Boolean getSpecial() {
		return special;
	}
	public void setSpecial(Boolean special) {
		this.special = special;
	}
	public Product getProduct() {
		if(product!=null) {
			return product;
		}else {
			if(productMap!=null) {
				this.product=productMap.get(productId);
			}
			return product;
		}
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Map<Integer, Product> getProductMap() {
		return productMap;
	}
	public void setProductMap(Map<Integer, Product> productMap) {
		this.productMap = productMap;
	}
	@Override
	public String toString() {
		return "Size [sizeId=" + sizeId + ", productId=" + productId + ", backSoon=" + backSoon + ", special=" + special
				+ ", product=" + product + "]";
	}
}
