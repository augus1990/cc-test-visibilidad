package cc.test.visibilidad;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import cc.test.visibilidad.domain.DomainData;
import cc.test.visibilidad.domain.Product;
import cc.test.visibilidad.domain.Size;
import cc.test.visibilidad.domain.Stock;
import cc.test.visibilidad.utils.DataSourceUtils;

/**
 * Prueba tecnica - Visibilidad 
 * @author Augusto Mendoza
 */
public class App {
	
	private final static String PRODUCT_FILE_PATH = "./assets/product.csv";
	private final static String SIZE_FILE_PATH = "./assets/size-1.csv";
	private final static String STOCK_FILE_PATH = "./assets/stock.csv";

	/**
	 * Muestra en la termina los ID de los productos visibles.
	 * @throws Exception
	 */
	private static void showVisibleProducts() throws Exception {
		Map<Integer,Product> visibleProducts;
		DomainData domainData;
		
		domainData = loadData(PRODUCT_FILE_PATH,SIZE_FILE_PATH,STOCK_FILE_PATH);
		
		System.out.println("Obteniendo los productos visibles...");
		visibleProducts = getVisibleProducts(domainData);
		System.out.println("Listo. Productos visibles listos.");
		
		System.out.print("\nRESULTADO: ");
		System.out.println(
				visibleProducts.values().stream().map(e->String.valueOf(e.getProductId())).collect(Collectors.joining(", ")) 
		);
	}
	
	/**
	 * Carga los datos desde la fuente.
	 * @param productFilePath
	 * @param sizeFilePath
	 * @param stockFilePath
	 * @return
	 * @throws Exception
	 */
	private static DomainData loadData(String productFilePath, String sizeFilePath, String stockFilePath) throws Exception{
		Map<Integer,Product> productMap = null;
		Map<Integer,Size> sizeMap = null;
		Map<Integer,Stock> stockMap = null;
		
		System.out.println("Cargando tabla de productos...");
		productMap = DataSourceUtils.loadProductCsvFile(productFilePath);
		System.out.println("Listo. Tabla de producto cargadas.");
		
		System.out.println("Cargando tabla de talles...");
		sizeMap = DataSourceUtils.loadSizeCsvFile(sizeFilePath,productMap);
		System.out.println("Listo. Tabla de talles cargadas.");
		
		System.out.println("Cargando tabla de stock...");
		stockMap = DataSourceUtils.loadStockCsvFile(stockFilePath,sizeMap);
		System.out.println("Listo. Tabla de stock cargadas.");
		
		return new DomainData(productMap,sizeMap,stockMap);
	}
	
	/**
	 * Obtiene un map ordenado con los productos visibles.
	 * @param domainData
	 * @return
	 */
	private static Map<Integer,Product> getVisibleProducts(DomainData domainData){
		Map<Integer,Product> visibleProducts = new TreeMap<Integer,Product>();
		
		visibleProducts = domainData.getProductMap().values().stream()
							.filter(
								e->isVisible(e,domainData)
							).collect(Collectors.toMap(
								e->e.getSequence(), e->e) // key: sequense / value: product
							);

		return visibleProducts;
	}
	
	/**
	 * Determina si un producto es visible o no.
	 * @param product
	 * @param domainData
	 * @return
	 */
	private static boolean isVisible(Product product, DomainData domainData) {
		boolean isVisible = false;
		
		if(isCompoundProduct(product, domainData)) {
			isVisible = hasAvailableNoSpecialSize(product, domainData)
						&&hasAvailableSpecialSize(product, domainData)
						;
		}else {
			// un producto simple es visible si tiene stock o tiene asociado un talle "backsoon" 
			isVisible = hasAvailableSize(product, domainData)
						||hasBackSoonSizeLinked(product, domainData)
						;
		}
		
		return isVisible;
	}
	
	/**
	 * Indica si el producto especificado tiene un talla disponible. La talla esta disponible si tiene stock o es backsoon.
	 */
	private static boolean hasAvailableSize(Product product, DomainData domainData) {
		boolean result = false;
		
		result = hasAvailableNoSpecialSize(product, domainData)
				 ||hasAvailableSpecialSize(product, domainData)
				 ;
		
		return result;
	}
	
	/**
	 * Indica si el producto tiene disponible algun talle no especial.
	 * @param product
	 * @param domainData
	 * @return
	 */
	private static boolean hasAvailableNoSpecialSize(Product product, DomainData domainData) {
		boolean result = false;
		
		result =domainData.getStockMap().values().stream().filter(
					e-> isAvailableSize(e.getSize(),domainData) // en stock o backsoon
				).anyMatch(
					e-> e.getSize().getProduct().getProductId()==product.getProductId()
						&&!e.getSize().getSpecial()
				);
		
		return result;
	}
	
	/**
	 * Indica si el producto tiene disponible algun talle especial.
	 * @param product
	 * @param domainData
	 * @return
	 */
	private static boolean hasAvailableSpecialSize(Product product, DomainData domainData) {
		boolean result = false;
		
		result =domainData.getStockMap().values().stream().filter(
					e-> isAvailableSize(e.getSize(),domainData) // en stock o backsoon
				).anyMatch(
					e-> e.getSize().getProduct().getProductId()==product.getProductId()
						&&e.getSize().getSpecial()
				);
		
		return result;
	}

	/**
	 * Indica si el producto especificado tiene algun talle backsoon asociado.
	 * @param product
	 * @param domainData
	 * @return
	 */
	private static boolean hasBackSoonSizeLinked(Product product, DomainData domainData) {
		boolean result = false;
		
		result =domainData.getSizeMap().values().stream().anyMatch(
					e-> e.getBackSoon()
				);
		
		return result;
	}
	
	/**
	 * Indica si un producto es compuesto o no.
	 * @param product
	 * @param domainData
	 * @return
	 */
	private static boolean isCompoundProduct(Product product, DomainData domainData) {
		boolean result = false;
		
		result =hasSpecialSizeLinked(product, domainData)
				&&hasNoSpecialSizeLinked(product, domainData)
				;
		
		return result;
	}
	
	/**
	 * Indica si el producto especificado tiene un talle especial asociado.
	 * @param product
	 * @param domainData
	 * @return
	 */
	private static boolean hasSpecialSizeLinked(Product product, DomainData domainData) {
		boolean result = false;
		
		result =domainData.getSizeMap().values().stream().anyMatch(
					e-> e.getProductId()==product.getProductId()
						&& !e.getSpecial()
				);
		
		return result;
	} 
	
	/**
	 * Indica si el producto especificado tiene un talle no especial asociado.
	 * @param product
	 * @param domainData
	 * @return
	 */
	private static boolean hasNoSpecialSizeLinked(Product product, DomainData domainData) {
		boolean result = false;
		
		result = domainData.getSizeMap().values().stream().anyMatch(
				 	e-> e.getProductId()==product.getProductId()
				 		&& e.getSpecial()
				 );
		
		return result;
	}
	
	/**
	 * Indica si el talle especificado esta disponible. La talla esta disponible si tiene stock o es backsoon.
	 * @param size
	 * @param domainData
	 * @return
	 */
	private static boolean isAvailableSize(Size size, DomainData domainData) {
		boolean result = false;
		boolean hasStock = false;
		boolean hasBackSoon = false;
		
		hasStock = domainData.getStockMap().get(size.getSizeId()).getQuantity()>0;
		hasBackSoon = size.getBackSoon();
		
		result	= ( hasStock 
					&& hasBackSoon
				  );
		
		return result;
	}
	
	/**
	 * @author Augusto Mendoza
	 */
    public static void main(String[] args) throws Exception {
    	System.out.println("Prueba tecnica - Visibilidad");
    	showVisibleProducts();
    }
    
    
}
