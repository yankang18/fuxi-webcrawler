package umbc.ebiquity.kang.websiteparser.tableparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.TableStatus;
import umbc.ebiquity.kang.htmltable.delimiter.impl.HTMLHeaderTagBasedTableHeaderLocator;
import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderPosition;
import umbc.ebiquity.kang.htmltable.delimiter.impl.TableHeaderLocatingResult;

public class HTMLTableHeaderLocatorTest {

	private static final String TEST_FILE_FOLDER = "TableHeaderLocatorTest/";

	@Test
	public void testResolveHorizontalTableWithHeaderTagInTableBody() throws IOException {

		HTMLHeaderTagBasedTableHeaderLocator resolver = new HTMLHeaderTagBasedTableHeaderLocator();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "HorizontalHeaderTableWithHeadIntheTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0);

		TableHeaderLocatingResult result = resolver.locateHorizontalHeader(element.children(), false);
		HeaderPosition position = result.getHorizontalHeaderPosition();
		assertEquals(result.getTableStatus(), TableStatus.RegularTable);
		assertEquals(result.getDataTableType(), DataTableHeaderType.HorizontalHeaderTable);
		assertEquals(0, position.getRowStart());
		assertEquals(0, position.getRowEnd());
		assertEquals(5, position.getRowBorderCount());
		assertEquals(4, position.getColumnBorderCount());
	}

	@Test
	public void testResolveHorizontalTableWithHeaderTagInTableHead() throws IOException {

		HTMLHeaderTagBasedTableHeaderLocator resolver = new HTMLHeaderTagBasedTableHeaderLocator();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "HorizontalHeaderTableWithHeadIntheThead.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("thead").get(0);

		TableHeaderLocatingResult result = resolver.locateHorizontalHeader(element.children(), true);
		HeaderPosition position = result.getHorizontalHeaderPosition();
		System.out.println(result.getMessage());
		assertEquals(TableStatus.RegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.HorizontalHeaderTable, result.getDataTableType());
		assertEquals(0, position.getRowStart());
		assertEquals(0, position.getRowEnd());
		assertEquals(1, position.getRowBorderCount());
		assertEquals(4, position.getColumnBorderCount());
	}

	@Test
	public void testResolveHorizontalTableWithEmptyTableHead() throws IOException {

		HTMLHeaderTagBasedTableHeaderLocator resolver = new HTMLHeaderTagBasedTableHeaderLocator();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "HorizontalHeaderTableWithoutHeadIntheThead.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("thead").get(0);

		TableHeaderLocatingResult result = resolver.locateHorizontalHeader(element.children(), true);
		System.out.println(result.getMessage());
		assertEquals(TableStatus.UnRegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.UnDetermined, result.getDataTableType());
		assertEquals(TableHeaderLocatingResult.NO_CONTENT, result.getMessage());
	}

	@Test
	public void testResolveHorizontalTableWithoutHeaderTagInTableBody() throws IOException {

		HTMLHeaderTagBasedTableHeaderLocator resolver = new HTMLHeaderTagBasedTableHeaderLocator();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "HorizontalHeaderTableWithoutHeadTagIntheTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0);

		TableHeaderLocatingResult result = resolver.locateHorizontalHeader(element.children(), false);
		assertEquals(TableStatus.UnRegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.UnDetermined, result.getDataTableType());

	}

	@Test
	public void testResolveHorizontalTableWithEmptyTableBody() throws IOException {

		HTMLHeaderTagBasedTableHeaderLocator resolver = new HTMLHeaderTagBasedTableHeaderLocator();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "HorizontalHeaderTableWithoutHeadIntheTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0);

		TableHeaderLocatingResult result = resolver.locateHorizontalHeader(element.children(), false);
		System.out.println(result.getMessage());
		assertEquals(TableStatus.UnRegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.UnDetermined, result.getDataTableType());
		assertEquals(TableHeaderLocatingResult.NO_CONTENT, result.getMessage());
	}

	@Test
	public void testResolveVerticalTableWithHeaderTagInTableBody() throws IOException {

		HTMLHeaderTagBasedTableHeaderLocator resolver = new HTMLHeaderTagBasedTableHeaderLocator();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "VerticalHeaderTableWithHeadinTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0);

		TableHeaderLocatingResult result = resolver.locateVeriticalHeader(element.children());
		HeaderPosition position = result.getVerticalHeaderPosition();
		assertEquals(TableStatus.RegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.VerticalHeaderTable, result.getDataTableType());
		assertEquals(0, position.getRowStart());
		assertEquals(0, position.getRowEnd());
		assertEquals(5, position.getRowBorderCount());
		assertEquals(11, position.getColumnBorderCount());
	}

	@Test
	public void testResolveVerticalTableWithoutHeaderTagInTableBody() throws IOException {

		HTMLHeaderTagBasedTableHeaderLocator resolver = new HTMLHeaderTagBasedTableHeaderLocator();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "VerticalHeaderTableWithoutHeadInTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0);

		TableHeaderLocatingResult result = resolver.locateVeriticalHeader(element.children());
		assertEquals(TableStatus.RegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.UnDetermined, result.getDataTableType());
		assertEquals(TableHeaderLocatingResult.NO_HEADER, result.getMessage());
	}

	private File loadFileOrDirectory(String fileName) {
		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File input = new File(classLoader.getResource(fileName).getFile());
		return input;
	}
}
