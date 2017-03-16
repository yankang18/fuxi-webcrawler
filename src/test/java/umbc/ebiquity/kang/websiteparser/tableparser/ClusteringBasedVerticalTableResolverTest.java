package umbc.ebiquity.kang.websiteparser.tableparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.TableStatus;
import umbc.ebiquity.kang.webtable.spliter.impl.ClusteringBasedVerticalTableSpliter;
import umbc.ebiquity.kang.webtable.spliter.impl.DataCell;
import umbc.ebiquity.kang.webtable.spliter.impl.HTMLHeaderTagBasedTableSpliter;
import umbc.ebiquity.kang.webtable.spliter.impl.TableCell;
import umbc.ebiquity.kang.webtable.spliter.impl.TableRecord;
import umbc.ebiquity.kang.webtable.spliter.impl.TableSplitingResult;

public class ClusteringBasedVerticalTableResolverTest {

	@Test
	public void testResolveVerticalTableWithHeaderTagInTableBody() throws IOException {
		
		ClusteringBasedVerticalTableSpliter resolver = new ClusteringBasedVerticalTableSpliter();
		File input = new File("///Users/yankang/Documents/Temp/VerticalHeaderTableWithHeadinTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0); 
		
		TableSplitingResult result = resolver.split(element);
		assertEquals(TableStatus.RegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.VHT, result.getDataTableHeaderType());
		
		List<TableRecord> headerRecords = result.getVerticalHeaderRecords();
		List<TableRecord> dataRecords = result.getVerticalDataRecords();
		System.out.println(headerRecords.size());
		System.out.println(dataRecords.size());
		
		for (TableRecord headRecord : headerRecords) {
			for (TableCell cell : headRecord.getTableCells()) {
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
}
