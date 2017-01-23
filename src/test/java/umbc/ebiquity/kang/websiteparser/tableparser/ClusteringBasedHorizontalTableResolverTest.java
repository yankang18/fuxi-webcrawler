package umbc.ebiquity.kang.websiteparser.tableparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.TableStatus;
import umbc.ebiquity.kang.websiteparser.tableresolver.impl.ClusteringBasedHorizontalTableResolver;
import umbc.ebiquity.kang.websiteparser.tableresolver.impl.ClusteringBasedVerticalTableResolver;
import umbc.ebiquity.kang.websiteparser.tableresolver.impl.DataCell;
import umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLHeaderTagBasedTableResolver;
import umbc.ebiquity.kang.websiteparser.tableresolver.impl.TableCell;
import umbc.ebiquity.kang.websiteparser.tableresolver.impl.TableRecord;
import umbc.ebiquity.kang.websiteparser.tableresolver.impl.TableResolveResult;

public class ClusteringBasedHorizontalTableResolverTest {
	// @Ignore
	@Test
	public void testResolveHorizontalTableWithHeaderTagInTableBody() throws IOException {

		ClusteringBasedHorizontalTableResolver resolver = new ClusteringBasedHorizontalTableResolver();
		File input = new File("///Users/yankang/Documents/Temp/HorizontalHeaderTableWithHeadIntheTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);

		TableResolveResult result = resolver.resolve(element);
		assertEquals(result.getTableStatus(), TableStatus.RegularTable);
		assertEquals(result.getDataTableHeaderType(), DataTableHeaderType.HHT);

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
}
