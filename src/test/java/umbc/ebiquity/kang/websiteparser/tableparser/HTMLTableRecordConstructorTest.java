package umbc.ebiquity.kang.websiteparser.tableparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import umbc.ebiquity.kang.websiteparser.tableresolver.impl.DataCell;
import umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableRecordConstructor;
import umbc.ebiquity.kang.websiteparser.tableresolver.impl.TableCell;
import umbc.ebiquity.kang.websiteparser.tableresolver.impl.TableRecord;

public class HTMLTableRecordConstructorTest {

	@Test
	public void testConstructHorizontalTableRecordList() throws IOException {

		File input = new File("///Users/yankang/Documents/Temp/HorizontalHeaderTable.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0);

		List<TableRecord> records = HTMLTableRecordConstructor.createHorizontalTableRecord(element, 0, 4, 4);
		assertEquals(5, records.size());
		TableRecord header = records.get(0);
		for (TableCell cell : header.getTableCells()) {
			assertEquals("th", cell.getTagName());
			assertEquals("comparison_image_title_cell", cell.getAttributeValue("class"));
		}
	}

	@Test
	public void testConstructVerticalTableRecordList() throws IOException {

		File input = new File("///Users/yankang/Documents/Temp/VerticalHeaderTable_5_rows.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("tbody").get(0);

		List<TableRecord> records = HTMLTableRecordConstructor.createVerticalTableRecords(element, 0, 4, 10);
		assertEquals(5, records.size());
		
		// assert header records
		TableRecord header = records.get(0);
		for (TableCell cell : header.getTableCells()) {
			assertEquals("td", cell.getTagName());
			assertEquals(true, contains("a-span3 comparison_attribute_name_column comparison_table_first_col",
					cell.getAttributeValue("class")));
			
			System.out.println(cell.getDataCellKeySet());
			DataCell dc1_1 = cell.getDataCell("0");
			assertEquals("span", dc1_1.getTagName());
			assertEquals("a-color-base", dc1_1.getAttributeValue("class")); 
		}

		// assert data records
		TableRecord data = null;
		for (int i = 1; i < records.size(); i++) {
			data = records.get(i);
			for (int j = 0; j < data.getTableCells().size(); j++) {
				TableCell cell = data.getTableCells().get(j);
				assertEquals("td", cell.getTagName());
				if (i == 1) {
					assertEquals(true, contains("comparison_baseitem_column", cell.getAttributeValue("class")));
				} else {
					assertEquals(true, contains("comparison_sim_items_column", cell.getAttributeValue("class")));
				}

				DataCell dc1_1 = cell.getDataCell("0");
				assertEquals("span", dc1_1.getTagName());
				String attrValue = dc1_1.getAttributeValue("class");
				assertEquals(true, contains("a-color-price a-text-bold a-icon-alt a-color-base",
						attrValue == null ? "" : attrValue));
			}
		}
	}

	private boolean contains(String container, String value) {
		return container.contains(value);
	}
}
