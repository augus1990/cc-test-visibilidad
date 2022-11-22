package cc.test.visibilidad.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import cc.test.visibilidad.domain.Product;
import cc.test.visibilidad.domain.Size;
import cc.test.visibilidad.domain.Stock;

public class DataSourceUtils {
	
	/**
	 * Posiciones de las columnas de las tablas.
	 */
	private static enum Field{
		PRODUCT_PRODUCT_ID(0)
		, PRODUCT_SEQUENCE(1)
		, SIZE_SIZE_ID(0)
		, SIZE_PRODUCT_ID(1)
		, SIZE_BACK_SOON(2)
		, SIZE_SPECIAL(3)
		, STOCK_SIZE_ID(0)
		, STOCK_QUANTITY(1)
		;
		
		private int field;

		Field(int field){this.field=field;}
		
		public int getField() {
			return field;
		}
		
	}
	
	/**
	 * Carga los datos de los productos.
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer,Product> loadProductCsvFile(String filePath) throws Exception{
		Map<Integer,Product> table;
		CSVReader csvReader;
		String[] row;
		Product rowObject;
		
		try {
			csvReader = new CSVReader(new FileReader(filePath));
			table = new HashMap<Integer,Product>();
			
			while( (row=csvReader.readNext())!=null ) {
				rowObject = new Product();
				rowObject.setProductId( Integer.parseInt(row[Field.PRODUCT_PRODUCT_ID.field].trim()) );
				rowObject.setSequence( Integer.parseInt(row[Field.PRODUCT_SEQUENCE.field].trim()) );
				table.put(rowObject.getProductId(),rowObject);
			}
			
			if(table.isEmpty()) {
				throw new Exception("El archivo no contiene datos.");
			}

			System.out.println(String.valueOf(table).replaceAll("\\],", "\\],\n"));
			
		} catch (FileNotFoundException e) {
			throw new Exception("No existe el archivo. Detalle: "+filePath);
		} catch (CsvValidationException e) {
			throw new Exception("El archivo no tiene un formato valido. Detalle: "+filePath);
		} catch (IOException e) {
			throw new Exception("Error de acceso al archivo especificado. Detalle: "+filePath);
		} catch (Exception e) {
			throw new Exception("Error inesperado. Detalle: "+filePath,e);
		}
		
		return table;
	}
	
	/**
	 * Carga los datos de los talles.
	 * @param filePath
	 * @param productMap
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer,Size> loadSizeCsvFile(String filePath, Map<Integer,Product> productMap) throws Exception{
		Map<Integer,Size> table;
		CSVReader csvReader;
		String[] row;
		Size rowObject;
		
		try {
			csvReader = new CSVReader(new FileReader(filePath));
			table = new HashMap<Integer,Size>();
			
			while( (row=csvReader.readNext())!=null ) {
				rowObject = new Size();
				rowObject.setSizeId( Integer.parseInt(row[Field.SIZE_SIZE_ID.field].trim()) );
				rowObject.setProductId( Integer.parseInt(row[Field.SIZE_PRODUCT_ID.field].trim()) );
				rowObject.setBackSoon( Boolean.parseBoolean(row[Field.SIZE_BACK_SOON.field].trim()) );
				rowObject.setSpecial( Boolean.parseBoolean(row[Field.SIZE_SPECIAL.field].trim()) );
				rowObject.setProductMap(productMap);
				table.put(rowObject.getSizeId(),rowObject);
			}
			
			if(table.isEmpty()) {
				throw new Exception("El archivo no contiene datos.");
			}
			
			System.out.println(String.valueOf(table).replaceAll("\\],", "\\],\n"));
			
		} catch (FileNotFoundException e) {
			throw new Exception("No existe el archivo. Detalle: "+filePath);
		} catch (CsvValidationException e) {
			throw new Exception("El archivo no tiene un formato valido. Detalle: "+filePath);
		} catch (IOException e) {
			throw new Exception("Error de acceso al archivo especificado. Detalle: "+filePath);
		} catch (Exception e) {
			throw new Exception("Error inesperado. Detalle: "+filePath,e);
		}
		
		return table;
	}
	
	/**
	 * Carga los datos de stock.
	 */
	public static Map<Integer,Stock> loadStockCsvFile(String filePath, Map<Integer,Size> sizeMap) throws Exception{
		Map<Integer,Stock> table;
		CSVReader csvReader;
		String[] row;
		Stock rowObject;
		
		try {
			csvReader = new CSVReader(new FileReader(filePath));
			table = new HashMap<Integer,Stock>();
			
			while( (row=csvReader.readNext())!=null ) {
				rowObject = new Stock();
				rowObject.setSizeId( Integer.parseInt(row[Field.STOCK_SIZE_ID.field].trim()) );
				rowObject.setQuantity( Integer.parseInt(row[Field.STOCK_QUANTITY.field].trim()) );
				rowObject.setSizeMap(sizeMap);
				table.put(rowObject.getSizeId(),rowObject);
			}
			
			if(table.isEmpty()) {
				throw new Exception("El archivo no contiene datos.");
			}
			
			System.out.println(String.valueOf(table).replaceAll("\\],", "\\],\n"));
			
		} catch (FileNotFoundException e) {
			throw new Exception("No existe el archivo. Detalle: "+filePath);
		} catch (CsvValidationException e) {
			throw new Exception("El archivo no tiene un formato valido. Detalle: "+filePath);
		} catch (IOException e) {
			throw new Exception("Error de acceso al archivo especificado. Detalle: "+filePath);
		} catch (Exception e) {
			throw new Exception("Error inesperado. Detalle: "+filePath+" / "+e);
		}
		
		return table;
	}
	
	public static List<List<String>> loadCsvFile(String filePath) throws Exception{
		List<List<String>> table;
		CSVReader csvReader;
		String[] row;
		
		csvReader = new CSVReader(new FileReader(filePath));
		table = new ArrayList<List<String>>();
		
		while( (row=csvReader.readNext())!=null ) {
			table.add(Arrays.asList(row));
		}
		
		if(table.isEmpty()) {
			throw new Exception("El archivo no contiene datos.");
		}
		
		System.out.println(String.valueOf(table).replaceAll("\\],", "\\],\n"));
		
		return table;
	}
}
