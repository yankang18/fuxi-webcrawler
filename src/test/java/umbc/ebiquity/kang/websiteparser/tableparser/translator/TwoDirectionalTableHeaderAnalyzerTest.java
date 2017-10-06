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
import umbc.ebiquity.kang.htmltable.translator.impl.StandardTwoDirectionalTablePropertyHeaderTypeAnalyzer;
import umbc.ebiquity.kang.htmltable.translator.impl.StandardTwoDirectionalTablePropertyHeaderTypeAnalyzer.TwoDirectionalHeaderType;

public class TwoDirectionalTableHeaderAnalyzerTest extends BaseTableHeaderTranslatorTest {
	private static final String TEST_FILE_FOLDER = "TableHeaderAnalyzerTest/";

	// @Ignore
	@Test
	public void testAnalyzeTwoDirectionalHeaderTableWithVerticalPropertyHeader() throws IOException {

		ClusteringBasedTableHeaderDelimiter delimiter = new ClusteringBasedTableHeaderDelimiter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "TwoDirectionalHeaderTable.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);
		HeaderDelimitedTable delimitedTable = delimiter.delimit(element);
		assertEquals(TableStatus.RegularTable, delimitedTable.getTableStatus());

		StandardTwoDirectionalTablePropertyHeaderTypeAnalyzer analyzer = new StandardTwoDirectionalTablePropertyHeaderTypeAnalyzer();
		TwoDirectionalHeaderType type = analyzer.analyze(delimitedTable);
		assertEquals(TwoDirectionalHeaderType.VerticalPropertyHeader, type);
	}
}
