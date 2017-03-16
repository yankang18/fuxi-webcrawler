package umbc.ebiquity.kang.websiteparser.tableparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.TableStatus;
import umbc.ebiquity.kang.webtable.spliter.impl.HTMLHeaderTagBasedTableHeaderLocator;
import umbc.ebiquity.kang.webtable.spliter.impl.HeaderPosition;
import umbc.ebiquity.kang.webtable.spliter.impl.TableHeaderLocatingResult;

public class HTMLTableHeaderResolverTest {
	
	@Test
	public void testResolveHorizontalTableWithHeaderTagInTableBody() throws IOException {
		
		HTMLHeaderTagBasedTableHeaderLocator resolver = new HTMLHeaderTagBasedTableHeaderLocator();
		File input = new File("///Users/yankang/Documents/Temp/HorizontalHeaderTableWithHeadIntheTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0); 
		
		TableHeaderLocatingResult result = resolver.locateHorizontalHeader(element.children(), false);
		HeaderPosition position = result.getHorizontalHeaderPosition();
		assertEquals(result.getTableStatus(), TableStatus.RegularTable);
		assertEquals(result.getDataTableType(), DataTableHeaderType.HHT);
		assertEquals(0, position.getRowStart());
		assertEquals(0, position.getRowEnd());
		assertEquals(5, position.getRowBorderCount());
		assertEquals(4, position.getColumnBorderCount());
	}
	
	@Test
	public void testResolveHorizontalTableWithHeaderTagInTableHead() throws IOException {
		
		HTMLHeaderTagBasedTableHeaderLocator resolver = new HTMLHeaderTagBasedTableHeaderLocator();
		File input = new File("///Users/yankang/Documents/Temp/HorizontalHeaderTableWithHeadIntheThead.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("thead").get(0); 
		
		TableHeaderLocatingResult result = resolver.locateHorizontalHeader(element.children(), true);
		HeaderPosition position = result.getHorizontalHeaderPosition();
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
		
		HTMLHeaderTagBasedTableHeaderLocator resolver = new HTMLHeaderTagBasedTableHeaderLocator();
		File input = new File("///Users/yankang/Documents/Temp/HorizontalHeaderTableWithoutHeadIntheThead.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("thead").get(0); 
		
		TableHeaderLocatingResult result = resolver.locateHorizontalHeader(element.children(), true);
		System.out.println(result.getMessage());
		assertEquals(TableStatus.UnRegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.UD, result.getDataTableType());
		assertEquals(TableHeaderLocatingResult.NO_CONTENT, result.getMessage());
	}
	
	@Test
	public void testResolveHorizontalTableWithoutHeaderTagInTableBody() throws IOException {
		
		HTMLHeaderTagBasedTableHeaderLocator resolver = new HTMLHeaderTagBasedTableHeaderLocator();
		File input = new File("///Users/yankang/Documents/Temp/HorizontalHeaderTableWithoutHeadTagIntheTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0);

		TableHeaderLocatingResult result = resolver.locateHorizontalHeader(element.children(), false);
		assertEquals(TableStatus.UnRegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.UD, result.getDataTableType());

	}

	@Test
	public void testResolveHorizontalTableWithEmptyTableBody() throws IOException {
		
		HTMLHeaderTagBasedTableHeaderLocator resolver = new HTMLHeaderTagBasedTableHeaderLocator();
		File input = new File("///Users/yankang/Documents/Temp/HorizontalHeaderTableWithoutHeadIntheTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0); 
		
		TableHeaderLocatingResult result = resolver.locateHorizontalHeader(element.children(), false);
		System.out.println(result.getMessage());
		assertEquals(TableStatus.UnRegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.UD, result.getDataTableType());
		assertEquals(TableHeaderLocatingResult.NO_CONTENT, result.getMessage());
	}
	
	
	@Test
	public void testResolveVerticalTableWithHeaderTagInTableBody() throws IOException {
		
		HTMLHeaderTagBasedTableHeaderLocator resolver = new HTMLHeaderTagBasedTableHeaderLocator();
		File input = new File("///Users/yankang/Documents/Temp/VerticalHeaderTableWithHeadinTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0); 
		
		TableHeaderLocatingResult result = resolver.locateVeriticalHeader(element.children());
		HeaderPosition position = result.getVerticalHeaderPosition();
		assertEquals(TableStatus.RegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.VHT, result.getDataTableType());
		assertEquals(0, position.getRowStart());
		assertEquals(0, position.getRowEnd());
		assertEquals(5, position.getRowBorderCount());
		assertEquals(11, position.getColumnBorderCount());
	}
	
	@Test
	public void testResolveVerticalTableWithoutHeaderTagInTableBody() throws IOException {
		
		HTMLHeaderTagBasedTableHeaderLocator resolver = new HTMLHeaderTagBasedTableHeaderLocator();
		File input = new File("///Users/yankang/Documents/Temp/VerticalHeaderTableWithoutHeadInTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0); 
		
		TableHeaderLocatingResult result = resolver.locateVeriticalHeader(element.children());
		assertEquals(TableStatus.RegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.UD, result.getDataTableType());
		assertEquals(TableHeaderLocatingResult.NO_HEADER, result.getMessage());
	}
	
	
}
