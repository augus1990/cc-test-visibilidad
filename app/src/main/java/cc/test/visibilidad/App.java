package cc.test.visibilidad;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import com.opencsv.exceptions.CsvValidationException;

import cc.test.visibilidad.domain.Product;
import cc.test.visibilidad.domain.Size;
import cc.test.visibilidad.domain.Stock;
import cc.test.visibilidad.utils.DataSourceUtils;

/**
 * Prueba tecnica - Visibilidad 
 * @author augusto
 */
public class App {
	
	private static String PRODUCT_FILE_PATH = "./assets/product.csv";
	private static String SIZE_FILE_PATH = "./assets/size-1.csv";
	private static String STOCK_FILE_PATH = "./assets/stock.csv";

	public static void showVisibleProducts() {
		Map<Integer,Product> productTable = null;
		Map<Integer,Size> sizeTable = null;
		Map<Integer,Stock> stockTable = null;
		
		System.out.println("Cargando tabla de productos...");
		try {
			productTable = DataSourceUtils.loadProductCsvFile(PRODUCT_FILE_PATH);
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
			sizeTable = DataSourceUtils.loadSizeCsvFile(SIZE_FILE_PATH,productTable);
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
			stockTable = DataSourceUtils.loadStockCsvFile(STOCK_FILE_PATH,sizeTable);
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
		
		
	}
	
    public static void main(String[] args) {
    	System.out.println("Prueba tecnica - Visibilidad");
    	showVisibleProducts();
    }
    
    
}
