package umbc.ebiquity.kang.websiteparser.tableparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Test;

import umbc.ebiquity.kang.webtable.core.DataCell;
import umbc.ebiquity.kang.webtable.core.TableCell;
import umbc.ebiquity.kang.webtable.core.TableRecord;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.TableStatus;
import umbc.ebiquity.kang.webtable.spliter.impl.HTMLHeaderTagBasedTableHeaderSpliter;
import umbc.ebiquity.kang.webtable.spliter.impl.TableSplitingResult;

public class HTMLHeaderTagBasedTableHeaderResolverTest {

	private static final String TEST_FILE_FOLDER = "TableHeaderLocatorTest/";

	// @Ignore
	@Test
	public void testResolveHorizontalTableWithHeaderTagInTableBody() throws IOException {

		HTMLHeaderTagBasedTableHeaderSpliter resolver = new HTMLHeaderTagBasedTableHeaderSpliter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "HorizontalHeaderTableWithHeadIntheTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);

		TableSplitingResult result = resolver.split(element);
		assertEquals(result.getTableStatus(), TableStatus.RegularTable);
		assertEquals(result.getDataTableHeaderType(), DataTableHeaderType.HorizontalHeaderTable);

		List<TableRecord> headerRecords = result.getHorizontalHeaderRecords();
		List<TableRecord> dataRecords = result.getHorizontalDataRecords();
		assertEquals(1, headerRecords.size());
		assertEquals(4, dataRecords.size());

		for (TableRecord headRecord : headerRecords) {
			for (TableCell cell : headRecord.getTableCells()) {
				System.out.println("");
				for (String key : cell.getDataCellKeySet()) {
					DataCell dc = cell.getDataCell(key);
					System.out.println(dc.getTagPath());
				}
			}
		}

		for (TableRecord dataRecord : dataRecords) {
			for (TableCell cell : dataRecord.getTableCells()) {
				System.out.println("");
				for (String key : cell.getDataCellKeySet()) {
					DataCell dc = cell.getDataCell(key);
					System.out.println(dc.getTagPath());
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testResolveHorizontalTableWithHeaderTagInTableHeader() throws IOException {
		HTMLHeaderTagBasedTableHeaderSpliter resolver = new HTMLHeaderTagBasedTableHeaderSpliter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "HorizontalHeaderTableWithHeadIntheThead.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);

		TableSplitingResult result = resolver.split(element);
		assertEquals(result.getTableStatus(), TableStatus.RegularTable);
		assertEquals(result.getDataTableHeaderType(), DataTableHeaderType.HorizontalHeaderTable);

		List<TableRecord> headerRecords = result.getHorizontalHeaderRecords();
		List<TableRecord> dataRecords = result.getHorizontalDataRecords();
		System.out.println(headerRecords.size());
		System.out.println(dataRecords.size());

		for (TableRecord dataRecord : dataRecords) {
			for (TableCell cell : dataRecord.getTableCells()) {
				System.out.println("");
				for (String key : cell.getDataCellKeySet()) {
					DataCell dc = cell.getDataCell(key);
					System.out.println(dc.getTagPath());
				}
			}
		}
	}

	// @Ignore
	@Test
	public void testResolveVerticalTableWithHeaderTagInTableBody() throws IOException {

		HTMLHeaderTagBasedTableHeaderSpliter resolver = new HTMLHeaderTagBasedTableHeaderSpliter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "VerticalHeaderTableWithHeadinTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);

		TableSplitingResult result = resolver.split(element);
		assertEquals(TableStatus.RegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.VerticalHeaderTable, result.getDataTableHeaderType());

		List<TableRecord> headerRecords = result.getVerticalHeaderRecords();
		List<TableRecord> dataRecords = result.getVerticalDataRecords();
		System.out.println(headerRecords.size());
		System.out.println(dataRecords.size());

		for (TableRecord dataRecord : dataRecords) {
			for (TableCell cell : dataRecord.getTableCells()) {
				System.out.println("");
				for (String key : cell.getDataCellKeySet()) {
					DataCell dc = cell.getDataCell(key);
					System.out.println(dc.getTagPath());
				}

			}
		}
	}

	private File loadFileOrDirectory(String fileName) {
		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File input = new File(classLoader.getResource(fileName).getFile());
		return input;
	}
}
