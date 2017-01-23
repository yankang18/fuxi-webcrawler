package umbc.ebiquity.kang.websiteparser.tableparser;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLHeaderTagBasedTableResolver;
import umbc.ebiquity.kang.websiteparser.tableresolver.impl.TableResolveResult;

public class HTMLTableParserTest {
	
	@Test
	public void xxx() throws IOException {
		
		HTMLHeaderTagBasedTableResolver parser = new HTMLHeaderTagBasedTableResolver();
		File input = new File("///Users/yankang/Documents/Temp/VerticalHeaderTable2.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		
		Element element = doc.getElementsByTag("table").get(0); 
		TableResolveResult result = parser.resolve(element);
		System.out.println("Table Status: " + result.getTableStatus().name());
		System.out.println("DataTable Status: " + result.getDataTableHeaderType().name());
	}
}
