package cc.test.visibilidad;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.opencsv.exceptions.CsvValidationException;

import cc.test.visibilidad.domain.DomainData;
import cc.test.visibilidad.domain.Product;
import cc.test.visibilidad.domain.Size;
import cc.test.visibilidad.domain.Stock;
import cc.test.visibilidad.utils.DataSourceUtils;

/**
 * Prueba tecnica - Visibilidad 
 * @author augusto
 */
public class App {
	
	private final static String PRODUCT_FILE_PATH = "./assets/product.csv";
	private final static String SIZE_FILE_PATH = "./assets/size-1.csv";
	private final static String STOCK_FILE_PATH = "./assets/stock.csv";

	private static void showVisibleProducts() {
		Map<Integer,Product> visibleProducts;
		DomainData domainData;
		Map<Integer,Product> productMap = null;
		Map<Integer,Size> sizeMap = null;
		Map<Integer,Stock> stockMap = null;
		
		System.out.println("Cargando tabla de productos...");
		try {
			productMap = DataSourceUtils.loadProductCsvFile(PRODUCT_FILE_PATH);
		} catch (FileNotFoundException e) {
			System.out.println("No existe el archivo. Detalle: "+PRODUCT_FILE_PATH);
		} catch (CsvValidationException e) {
			System.out.println("El archivo no tiene un formato valido. Detalle: "+PRODUCT_FILE_PATH);
		} catch (IOException e) {
			System.out.println("Error de acceso al archivo especificado. Detalle: "+PRODUCT_FILE_PATH);
		} catch (Exception e) {
			System.out.println("Error inesperado. Detalle: "+PRODUCT_FILE_PATH+" / "+e);
		}
		System.out.println("Listo. Tabla de producto cargadas.");
		
		
		
		System.out.println("Cargando tabla de talles...");
		try {
			sizeMap = DataSourceUtils.loadSizeCsvFile(SIZE_FILE_PATH,productMap);
		} catch (FileNotFoundException e) {
			System.out.println("No existe el archivo. Detalle: "+SIZE_FILE_PATH);
		} catch (CsvValidationException e) {
			System.out.println("El archivo no tiene un formato valido. Detalle: "+SIZE_FILE_PATH);
		} catch (IOException e) {
			System.out.println("Error de acceso al archivo especificado. Detalle: "+SIZE_FILE_PATH);
		} catch (Exception e) {
			System.out.println("Error inesperado. Detalle: "+SIZE_FILE_PATH+" / "+e);
		}
		System.out.println("Listo. Tabla de talles cargadas.");
		
		
		System.out.println("Cargando tabla de stock...");
		try {
			stockMap = DataSourceUtils.loadStockCsvFile(STOCK_FILE_PATH,sizeMap);
		} catch (FileNotFoundException e) {
			System.out.println("No existe el archivo. Detalle: "+STOCK_FILE_PATH);
		} catch (CsvValidationException e) {
			System.out.println("El archivo no tiene un formato valido. Detalle: "+STOCK_FILE_PATH);
		} catch (IOException e) {
			System.out.println("Error de acceso al archivo especificado. Detalle: "+STOCK_FILE_PATH);
		} catch (Exception e) {
			System.out.println("Error inesperado. Detalle: "+STOCK_FILE_PATH+" / "+e);
		}
		System.out.println("Listo. Tabla de stock cargadas.");
		
		domainData = new DomainData(productMap,sizeMap,stockMap);
		
		visibleProducts = getVisibleProducts(domainData);
		
		System.out.println("RESULTADO: \n"+String.valueOf(visibleProducts).replaceAll("\\],", "\\],\n"));
	}
	
	private static Map<Integer,Product> getVisibleProducts(DomainData domainData){
		Map<Integer,Product> visibleProducts = new HashMap<Integer,Product>();
		
		visibleProducts = domainData.getProductMap().values().stream()
							.filter(
								e->isVisible(e,domainData)
							).collect(Collectors.toMap(
								e->e.getSequence(), e->e) // key: sequense / value: product
							);

		return visibleProducts;
	}
	
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
	
	private static boolean hasAvailableSize(Product product, DomainData domainData) {
		boolean result = false;
		
		result = hasAvailableNoSpecialSize(product, domainData)
				 ||hasAvailableSpecialSize(product, domainData)
				 ;
		
		return result;
	}
	
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

	private static boolean hasBackSoonSizeLinked(Product product, DomainData domainData) {
		boolean result = false;
		
		result =domainData.getSizeMap().values().stream().anyMatch(
					e-> e.getBackSoon()
				);
		
		return result;
	}
	
	private static boolean isCompoundProduct(Product product, DomainData domainData) {
		boolean result = false;
		
		result =hasSpecialSizeLinked(product, domainData)
				&&hasNoSpecialSizeLinked(product, domainData)
				;
		
		return result;
	}
	
	private static boolean hasSpecialSizeLinked(Product product, DomainData domainData) {
		boolean result = false;
		
		result =domainData.getSizeMap().values().stream().anyMatch(
					e-> e.getProductId()==product.getProductId()
						&& !e.getSpecial()
				);
		
		return result;
	} 
	
	private static boolean hasNoSpecialSizeLinked(Product product, DomainData domainData) {
		boolean result = false;
		
		result = domainData.getSizeMap().values().stream().anyMatch(
				 	e-> e.getProductId()==product.getProductId()
				 		&& e.getSpecial()
				 );
		
		return result;
	}
	
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
	
    public static void main(String[] args) {
    	System.out.println("Prueba tecnica - Visibilidad");
    	showVisibleProducts();
    }
    
    
}
