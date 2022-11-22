package cc.test.visibilidad.domain;

import java.util.Map;

public class Stock {
	private Integer sizeId;
	private Integer quantity;
	private Size size;
	private Map<Integer,Size> sizeMap;
	
	public Integer getSizeId() {
		return sizeId;
	}
	public void setSizeId(Integer sizeId) {
		this.sizeId = sizeId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Size getSize() {
		if(size!=null) {
			return size;
		}else {
			if(sizeMap!=null) {
				this.size=sizeMap.get(sizeId);
			}
			return size;
		}
	}
	public void setSize(Size size) {
		this.size = size;
	}
	public Map<Integer, Size> getSizeMap() {
		return sizeMap;
	}
	public void setSizeMap(Map<Integer, Size> sizeMap) {
		this.sizeMap = sizeMap;
	}
	@Override
	public String toString() {
		return "Stock [sizeId=" + sizeId + ", quantity=" + quantity + ", size=" + size + "]";
	}
}
