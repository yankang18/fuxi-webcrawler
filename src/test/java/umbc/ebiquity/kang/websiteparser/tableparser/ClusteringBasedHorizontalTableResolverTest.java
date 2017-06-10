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

import umbc.ebiquity.kang.htmltable.core.DataCell;
import umbc.ebiquity.kang.htmltable.core.TableCell;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.TableStatus;
import umbc.ebiquity.kang.htmltable.delimiter.impl.ClusteringBasedHorizontalTableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderDelimitedTable;

public class ClusteringBasedHorizontalTableResolverTest {
	
	private static final String TEST_FILE_FOLDER = "TableHeaderLocatorTest/";

//	@Ignore
	@Test
	public void testResolveHorizontalTableWithHeaderTagInTableBody() throws IOException {

		ClusteringBasedHorizontalTableHeaderDelimiter resolver = new ClusteringBasedHorizontalTableHeaderDelimiter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "HorizontalHeaderTableWithHeadIntheTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);

		HeaderDelimitedTable result = resolver.delimit(element);
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
	
	public File loadFileOrDirectory(String fileName) {
		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File input = new File(classLoader.getResource(fileName).getFile());
		return input;
	} 
}
