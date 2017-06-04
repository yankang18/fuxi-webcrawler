package umbc.ebiquity.kang.websiteparser.tableparser.translator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Test;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.util.HTMLTreeUtil;
import umbc.ebiquity.kang.webtable.Translator.TableTreeTranslator;
import umbc.ebiquity.kang.webtable.delimiter.IDelimitedTable.TableStatus;
import umbc.ebiquity.kang.webtable.delimiter.impl.ClusteringBasedHorizontalTableHeaderDelimiter;
import umbc.ebiquity.kang.webtable.delimiter.impl.ClusteringBasedVerticalTableHeaderDelimiter;
import umbc.ebiquity.kang.webtable.delimiter.impl.HeaderDelimitedTable;

public class TableTreeTranslatorTest extends BaseTableHeaderTranslatorTest {

	private static final String TEST_FILE_FOLDER = "TableHeaderDelimiterTest/";

	// @Ignore
	@Test
	public void testHorizontalHeaderTable() throws IOException {

		ClusteringBasedHorizontalTableHeaderDelimiter delimiter = new ClusteringBasedHorizontalTableHeaderDelimiter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "HorizontalHeaderTableWithPropertyHeadIntheTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);
		HeaderDelimitedTable delimitedTable = delimiter.delimit(element);
		assertEquals(TableStatus.RegularTable, delimitedTable.getTableStatus());

		TableTreeTranslator tableTranslator = new TableTreeTranslator();
		IHTMLTreeNode tree = tableTranslator.translate(delimitedTable);

		HTMLTreeUtil.prettyPrint(tree);
	}

	@Ignore
	@Test
	public void testVerticalHeaderTable() throws IOException {
		ClusteringBasedVerticalTableHeaderDelimiter delimiter = new ClusteringBasedVerticalTableHeaderDelimiter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "VerticalHeaderTableWithPropertyHeadinTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);
		HeaderDelimitedTable delimitedTable = delimiter.delimit(element);
		assertEquals(TableStatus.RegularTable, delimitedTable.getTableStatus());

		TableTreeTranslator tableTranslator = new TableTreeTranslator();
		IHTMLTreeNode tree = tableTranslator.translate(delimitedTable);

		HTMLTreeUtil.prettyPrint(tree);
	}

	// @Test
	// public void testTwoDirectionalHeaderTableHeaderTable() throws IOException
	// {
	// }

}
