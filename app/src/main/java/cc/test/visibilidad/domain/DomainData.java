package cc.test.visibilidad.domain;

import java.util.Map;

public class DomainData {

	private Map<Integer,Product> productMap = null;
	private Map<Integer,Size> sizeMap = null;
	private Map<Integer,Stock> stockMap = null;
	
	public DomainData(Map<Integer, Product> productMap, Map<Integer, Size> sizeMap, Map<Integer, Stock> stockMap) {
		super();
		this.productMap = productMap;
		this.sizeMap = sizeMap;
		this.stockMap = stockMap;
	}

	public Map<Integer, Product> getProductMap() {
		return productMap;
	}
	public void setProductMap(Map<Integer, Product> productMap) {
		this.productMap = productMap;
	}
	public Map<Integer, Size> getSizeMap() {
		return sizeMap;
	}
	public void setSizeMap(Map<Integer, Size> sizeMap) {
		this.sizeMap = sizeMap;
	}
	public Map<Integer, Stock> getStockMap() {
		return stockMap;
	}
	public void setStockMap(Map<Integer, Stock> stockMap) {
		this.stockMap = stockMap;
	}
	@Override
	public String toString() {
		return "DomainData [productMap=" + productMap + ", sizeMap=" + sizeMap + ", stockMap=" + stockMap + "]";
	}

}
