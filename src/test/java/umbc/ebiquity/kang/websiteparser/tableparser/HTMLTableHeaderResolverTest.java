package umbc.ebiquity.kang.websiteparser.tableparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.TableStatus;
import umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableHeaderResolver;
import umbc.ebiquity.kang.websiteparser.tableresolver.impl.Position;
import umbc.ebiquity.kang.websiteparser.tableresolver.impl.TableHeaderResolveResult;

public class HTMLTableHeaderResolverTest {
	
	@Test
	public void testResolveHorizontalTableWithHeaderTagInTableBody() throws IOException {
		
		HTMLTableHeaderResolver resolver = new HTMLTableHeaderResolver();
		File input = new File("///Users/yankang/Documents/Temp/HorizontalHeaderTableWithHeadIntheTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0); 
		
		TableHeaderResolveResult result = resolver.resolveHorizontalHeader(element.children(), false);
		Position position = result.getHorizontalHeaderPosition();
		assertEquals(result.getTableStatus(), TableStatus.RegularTable);
		assertEquals(result.getDataTableType(), DataTableHeaderType.HHT);
		assertEquals(0, position.getRowStart());
		assertEquals(0, position.getRowEnd());
		assertEquals(5, position.getRowBorderCount());
		assertEquals(4, position.getColumnBorderCount());
	}
	
	@Test
	public void testResolveHorizontalTableWithHeaderTagInTableHead() throws IOException {
		
		HTMLTableHeaderResolver resolver = new HTMLTableHeaderResolver();
		File input = new File("///Users/yankang/Documents/Temp/HorizontalHeaderTableWithHeadIntheThead.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("thead").get(0); 
		
		TableHeaderResolveResult result = resolver.resolveHorizontalHeader(element.children(), true);
		Position position = result.getHorizontalHeaderPosition();
		System.out.println(result.getMessage());
		assertEquals(TableStatus.RegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.HHT, result.getDataTableType());
		assertEquals(0, position.getRowStart());
		assertEquals(0, position.getRowEnd());
		assertEquals(1, position.getRowBorderCount());
		assertEquals(4, position.getColumnBorderCount());
	}
	
	@Test
	public void testResolveHorizontalTableWithEmptyTableHead() throws IOException {
		
		HTMLTableHeaderResolver resolver = new HTMLTableHeaderResolver();
		File input = new File("///Users/yankang/Documents/Temp/HorizontalHeaderTableWithoutHeadIntheThead.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("thead").get(0); 
		
		TableHeaderResolveResult result = resolver.resolveHorizontalHeader(element.children(), true);
		System.out.println(result.getMessage());
		assertEquals(TableStatus.UnRegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.UD, result.getDataTableType());
		assertEquals(TableHeaderResolveResult.NO_CONTENT, result.getMessage());
	}
	
	@Test
	public void testResolveHorizontalTableWithoutHeaderTagInTableBody() throws IOException {
		
		HTMLTableHeaderResolver resolver = new HTMLTableHeaderResolver();
		File input = new File("///Users/yankang/Documents/Temp/HorizontalHeaderTableWithoutHeadTagIntheTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0);

		TableHeaderResolveResult result = resolver.resolveHorizontalHeader(element.children(), false);
		assertEquals(TableStatus.UnRegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.UD, result.getDataTableType());

	}

	@Test
	public void testResolveHorizontalTableWithEmptyTableBody() throws IOException {
		
		HTMLTableHeaderResolver resolver = new HTMLTableHeaderResolver();
		File input = new File("///Users/yankang/Documents/Temp/HorizontalHeaderTableWithoutHeadIntheTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0); 
		
		TableHeaderResolveResult result = resolver.resolveHorizontalHeader(element.children(), false);
		System.out.println(result.getMessage());
		assertEquals(TableStatus.UnRegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.UD, result.getDataTableType());
		assertEquals(TableHeaderResolveResult.NO_CONTENT, result.getMessage());
	}
	
	
	@Test
	public void testResolveVerticalTableWithHeaderTagInTableBody() throws IOException {
		
		HTMLTableHeaderResolver resolver = new HTMLTableHeaderResolver();
		File input = new File("///Users/yankang/Documents/Temp/VerticalHeaderTableWithHeadinTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0); 
		
		TableHeaderResolveResult result = resolver.resolveVeriticalHeader(element.children());
		Position position = result.getVerticalHeaderPosition();
		assertEquals(TableStatus.RegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.VHT, result.getDataTableType());
		assertEquals(0, position.getRowStart());
		assertEquals(0, position.getRowEnd());
		assertEquals(5, position.getRowBorderCount());
		assertEquals(11, position.getColumnBorderCount());
	}
	
	@Test
	public void testResolveVerticalTableWithoutHeaderTagInTableBody() throws IOException {
		
		HTMLTableHeaderResolver resolver = new HTMLTableHeaderResolver();
		File input = new File("///Users/yankang/Documents/Temp/VerticalHeaderTableWithoutHeadInTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0); 
		
		TableHeaderResolveResult result = resolver.resolveVeriticalHeader(element.children());
		assertEquals(TableStatus.RegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.UD, result.getDataTableType());
		assertEquals(TableHeaderResolveResult.NO_HEADER, result.getMessage());
	}
	
	
}
