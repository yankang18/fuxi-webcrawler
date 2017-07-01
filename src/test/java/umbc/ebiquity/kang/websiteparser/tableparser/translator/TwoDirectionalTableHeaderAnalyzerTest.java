package umbc.ebiquity.kang.websiteparser.tableparser.translator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.TableStatus;
import umbc.ebiquity.kang.htmltable.delimiter.impl.ClusteringBasedTableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderDelimitedTable;
import umbc.ebiquity.kang.htmltable.translator.impl.TwoDirectionalTableHeaderAnalyzer;
import umbc.ebiquity.kang.htmltable.translator.impl.TwoDirectionalTableHeaderAnalyzer.TwoDirectionalHeaderType;

public class TwoDirectionalTableHeaderAnalyzerTest extends BaseTableHeaderTranslatorTest {
	private static final String TEST_FILE_FOLDER = "TableHeaderDelimiterTest/";

	// @Ignore
	@Test
	public void testTwoDirectionalHeaderTableHeaderTable() throws IOException {

		ClusteringBasedTableHeaderDelimiter delimiter = new ClusteringBasedTableHeaderDelimiter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "TwoDirectionalHeaderTable.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);
		HeaderDelimitedTable delimitedTable = delimiter.delimit(element);
		assertEquals(TableStatus.RegularTable, delimitedTable.getTableStatus());

		TwoDirectionalTableHeaderAnalyzer analyzer = new TwoDirectionalTableHeaderAnalyzer();
		TwoDirectionalHeaderType type = analyzer.analyze(delimitedTable);
		System.out.println(type.name());
		assertEquals(TwoDirectionalHeaderType.VerticalPropertyHeader, type);
	}
}
