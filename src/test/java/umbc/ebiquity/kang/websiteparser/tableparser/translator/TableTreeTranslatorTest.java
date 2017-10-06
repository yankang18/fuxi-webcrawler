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
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.StandardHTMLTreeBlankNodeConsolidator;
import umbc.ebiquity.kang.htmldocument.util.HTMLTree2JSONConverter;
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
		
		prettyPrintJSON(tree);
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
		
		prettyPrintJSON(tree);
	}
	
//	@Ignore
	@Test
	public void testVerticalPropertyHeaderWithIrregularDataRecordsTable() throws IOException {
		ClusteringBasedVerticalTableHeaderDelimiter delimiter = new ClusteringBasedVerticalTableHeaderDelimiter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "VerticalPropertyHeaderWithIrregularDataRecords.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);
		HeaderDelimitedTable delimitedTable = delimiter.delimit(element);
		assertEquals(TableStatus.RegularTable, delimitedTable.getTableStatus());

		TableTreeTranslator tableTranslator = new TableTreeTranslator();
		IHTMLTreeNode tree = tableTranslator.translate(delimitedTable);

		HTMLTreeUtil.prettyPrint(tree);
		
		prettyPrintJSON(tree);
	}

	@Ignore
	@Test
	public void testTwoDirectionalHeaderTableHeaderTable() throws IOException {
		
		ClusteringBasedTableHeaderDelimiter delimiter = new ClusteringBasedTableHeaderDelimiter();
		
		// (1) Create a table element from certain source. 
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "TwoDirectionalHeaderTable.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);
		
		// (2) table Element -- (ITableHeaderDelimiter) --> HeaderDelimitedTable
		HeaderDelimitedTable delimitedTable = delimiter.delimit(element);
		assertEquals(TableStatus.RegularTable, delimitedTable.getTableStatus());

		// (3) HeaderDelimitedTable -- (TableTreeTranslator) --> IHTMLTreeNode representing the table
		TableTreeTranslator tableTranslator = new TableTreeTranslator();
		IHTMLTreeNode tree = tableTranslator.translate(delimitedTable);

		HTMLTreeUtil.prettyPrint(tree);

		prettyPrintJSON(tree);
	}

	private void prettyPrintJSON(IHTMLTreeNode tree) {
		System.out.println("##################################################################");
		
		// (4) 
		IHTMLTreeOverlay overlay = HTMLTreeOverlay.createDefaultHTMLTreeOverlay(tree);
		
		// (5) IHTMLTreeOverlay -- (StandardHTMLTreeBlankNodeConsolidator) --> IHTMLTreeOverlay
		StandardHTMLTreeBlankNodeConsolidator cc = new StandardHTMLTreeBlankNodeConsolidator();
		overlay = cc.refine(overlay);
		HTMLTreeUtil.prettyPrint(overlay.getTreeRoot());
		
		// (6) IHTMLTreeOverlay -- (HTMLTree2JSONTranslator) --> JSONObject representing the table
		// HTMLTreeUtil.prettyPrint(overlay.getTreeRoot());
		System.out.println("--------");
		JSONObject object = HTMLTree2JSONConverter.convert(overlay.getTreeRoot());
		System.out.println(HTMLTree2JSONConverter.prettyPrint(object));
	}
}
