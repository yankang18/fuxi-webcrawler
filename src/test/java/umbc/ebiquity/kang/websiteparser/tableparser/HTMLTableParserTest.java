package umbc.ebiquity.kang.websiteparser.tableparser;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import umbc.ebiquity.kang.webtable.spliter.impl.HTMLHeaderTagBasedTableSpliter;
import umbc.ebiquity.kang.webtable.spliter.impl.TableSplitingResult;

public class HTMLTableParserTest {
	
	@Test
	public void xxx() throws IOException {
		
		HTMLHeaderTagBasedTableSpliter parser = new HTMLHeaderTagBasedTableSpliter();
		File input = new File("///Users/yankang/Documents/Temp/VerticalHeaderTable2.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		
		Element element = doc.getElementsByTag("table").get(0); 
		TableSplitingResult result = parser.split(element);
		System.out.println("Table Status: " + result.getTableStatus().name());
		System.out.println("DataTable Status: " + result.getDataTableHeaderType().name());
	}
}
