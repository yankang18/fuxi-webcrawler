package umbc.ebiquity.kang.websiteparser.tableparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import umbc.ebiquity.kang.webtable.spliter.impl.DataCell;
import umbc.ebiquity.kang.webtable.spliter.impl.TableCell;
import umbc.ebiquity.kang.webtable.spliter.impl.DataCell.DataCellType;

public class TableCellConstructionTest {
	
	@Test
	public void testConstructHorizontalTableRecordListWithDepthOne() throws IOException {

		File input = new File("///Users/yankang/Documents/Temp/TableCell.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);
		
		Element tableRowElem = element.getElementsByTag("tr").get(0);
		TableCell cell = new TableCell(tableRowElem.getElementsByTag("td").get(0), "tr", 1);  
		
		DataCell cell0 = cell.getDataCell("0");
		DataCell cell1 = cell.getDataCell("1");
		
		assertEquals("td", cell.getTagName());
		assertEquals("tr.td", cell.getTagPath());
		assertEquals("attriute1", cell.getAttributeValue("class"));
		
		assertEquals("div", cell0.getTagName());
		assertEquals("tr.td.div", cell0.getTagPath());
		assertEquals("attriute2", cell0.getAttributeValue("class"));
		assertEquals(DataCellType.Element, cell0.getDataCellType());
		
		assertEquals("text", cell1.getTagName());
		assertEquals("tr.td.text", cell1.getTagPath());
		assertEquals(null, cell1.getAttributeValue("class"));
		assertEquals(DataCellType.Value, cell1.getDataCellType());
		assertEquals("value three", cell1.getValue());
	}
	
	@Test
	public void testConstructHorizontalTableRecordListWithDepthTwo() throws IOException {

		File input = new File("///Users/yankang/Documents/Temp/TableCell.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);
		Element tableRowElem = element.getElementsByTag("tr").get(0);
		TableCell cell = new TableCell(tableRowElem.getElementsByTag("td").get(0), "tr", 2);  

		DataCell cell00 = cell.getDataCell("0.0");
		DataCell cell01 = cell.getDataCell("0.1");
		DataCell cell02 = cell.getDataCell("0.2");
		DataCell cell10 = cell.getDataCell("1.0");
		System.out.println(cell.getDataCellKeySet());
		assertEquals("span", cell00.getTagName());
		assertEquals("tr.td.div.span", cell00.getTagPath());
		assertEquals("attriute3", cell00.getAttributeValue("class"));
		assertEquals(DataCellType.Element, cell00.getDataCellType());

		assertEquals("a", cell01.getTagName());
		assertEquals("tr.td.div.a", cell01.getTagPath());
		assertEquals("attriute6", cell01.getAttributeValue("class"));
		assertEquals(DataCellType.Element, cell01.getDataCellType());

		assertEquals("span", cell02.getTagName());
		assertEquals("tr.td.div.span", cell02.getTagPath());
		assertEquals("attriute7", cell02.getAttributeValue("class"));
		assertEquals(DataCellType.Element, cell02.getDataCellType());

		assertEquals(null, cell10);
	}
	
	@Test
	public void testConstructHorizontalTableRecordListWithDepthThree() throws IOException {

		File input = new File("///Users/yankang/Documents/Temp/TableCell.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);
		Element tableRowElem = element.getElementsByTag("tr").get(0);
		TableCell cell = new TableCell(tableRowElem.getElementsByTag("td").get(0), "tr", 3);

		DataCell cell000 = cell.getDataCell("0.0.0");
		DataCell cell010 = cell.getDataCell("0.1.0");
		System.out.println(cell.getDataCellKeySet());
		assertEquals("i", cell000.getTagName());
		assertEquals("tr.td.div.span.i", cell000.getTagPath());
		assertEquals("attriute4", cell000.getAttributeValue("class"));
		assertEquals(DataCellType.Element, cell000.getDataCellType());

		assertEquals("text", cell010.getTagName());
		assertEquals("tr.td.div.a.text", cell010.getTagPath());
		assertEquals(null, cell010.getAttributeValue("class"));
		assertEquals(DataCellType.Value, cell010.getDataCellType());
		assertEquals("value two", cell010.getValue());

	}
}
