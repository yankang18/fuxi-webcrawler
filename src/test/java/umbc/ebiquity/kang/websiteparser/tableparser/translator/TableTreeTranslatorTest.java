package umbc.ebiquity.kang.websiteparser.tableparser.translator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Test;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlayRefiner;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.StandardHTMLTreeBlankNodeConsolidator;
import umbc.ebiquity.kang.htmldocument.util.HTMLTree2JSONTranslator;
import umbc.ebiquity.kang.htmldocument.util.HTMLTreeUtil;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.TableStatus;
import umbc.ebiquity.kang.htmltable.delimiter.impl.ClusteringBasedHorizontalTableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.delimiter.impl.ClusteringBasedTableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.delimiter.impl.ClusteringBasedVerticalTableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderDelimitedTable;
import umbc.ebiquity.kang.htmltable.translator.impl.TableTreeTranslator;

public class TableTreeTranslatorTest extends BaseTableHeaderTranslatorTest {

	private static final String TEST_FILE_FOLDER = "TableHeaderDelimiterTest/";

	@Ignore
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

	@Ignore
	@Test
	public void testTwoDirectionalHeaderTableHeaderTable() throws IOException {
		
		ClusteringBasedTableHeaderDelimiter delimiter = new ClusteringBasedTableHeaderDelimiter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "TwoDirectionalHeaderTable.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);
		HeaderDelimitedTable delimitedTable = delimiter.delimit(element);
		assertEquals(TableStatus.RegularTable, delimitedTable.getTableStatus());

		TableTreeTranslator tableTranslator = new TableTreeTranslator();
		IHTMLTreeNode tree = tableTranslator.translate(delimitedTable);

		HTMLTreeUtil.prettyPrint(tree);

		System.out.println("----------------------------------------");
		IHTMLTreeOverlay overlay = new HTMLTreeOverlay(tree, "", "");
		StandardHTMLTreeBlankNodeConsolidator cc = new StandardHTMLTreeBlankNodeConsolidator();
		overlay = cc.refine(overlay);
		// HTMLTreeUtil.prettyPrint(overlay.getTreeRoot());
		System.out.println("--------");
		JSONObject object = HTMLTree2JSONTranslator.translate(overlay.getTreeRoot());
		System.out.println(HTMLTree2JSONTranslator.prettyPrint(object));
	}

}
