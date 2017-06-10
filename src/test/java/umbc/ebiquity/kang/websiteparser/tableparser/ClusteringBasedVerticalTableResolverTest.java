package umbc.ebiquity.kang.websiteparser.tableparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import umbc.ebiquity.kang.htmltable.core.DataCell;
import umbc.ebiquity.kang.htmltable.core.TableCell;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.TableStatus;
import umbc.ebiquity.kang.htmltable.delimiter.impl.ClusteringBasedVerticalTableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderDelimitedTable;

public class ClusteringBasedVerticalTableResolverTest {

	private static final String TEST_FILE_FOLDER = "TableHeaderLocatorTest/";
	
//	@Ignore
	@Test
	public void testResolveVerticalTableWithHeaderTagInTableBody() throws IOException {

		ClusteringBasedVerticalTableHeaderDelimiter resolver = new ClusteringBasedVerticalTableHeaderDelimiter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "VerticalHeaderTableWithHeadinTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);

		HeaderDelimitedTable result = resolver.delimit(element);
		assertEquals(TableStatus.RegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.VerticalHeaderTable, result.getDataTableHeaderType());
		
		List<TableRecord> headerRecords = result.getVerticalHeaderRecords();
		List<TableRecord> dataRecords = result.getVerticalDataRecords();
		assertEquals(1, headerRecords.size());
		assertEquals(4, dataRecords.size());
		
		for (TableRecord headRecord : headerRecords) {
			List<TableCell> tableCells = headRecord.getTableCells();
			for (TableCell cell : tableCells) {
				assertEquals(11, tableCells.size());
				System.out.println("");
				for (String key : cell.getDataCellKeySet()) {
					DataCell dc = cell.getDataCell(key);
					System.out.println(dc.getTagPath());
				}
			}
		}
		
		for(TableRecord dataRecord : dataRecords){
			for(TableCell cell : dataRecord.getTableCells()){
				System.out.println("");
				for(String key: cell.getDataCellKeySet()){
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
