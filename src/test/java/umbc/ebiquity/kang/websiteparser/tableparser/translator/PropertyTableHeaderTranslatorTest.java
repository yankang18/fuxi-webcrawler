package umbc.ebiquity.kang.websiteparser.tableparser.translator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeEntityNode;
import umbc.ebiquity.kang.webtable.delimiter.impl.ClusteringBasedHorizontalTableHeaderDelimiter;
import umbc.ebiquity.kang.webtable.delimiter.impl.ClusteringBasedVerticalTableHeaderDelimiter;

public class PropertyTableHeaderTranslatorTest extends BaseTableHeaderTranslatorTest {

	private static final String TEST_FILE_FOLDER = "TableHeaderDelimiterTest/";

	// @Ignore
	@Test
	public void testDelimitVerticalTableWithHeaderTagInTableBody() throws IOException {

		ClusteringBasedVerticalTableHeaderDelimiter delimiter = new ClusteringBasedVerticalTableHeaderDelimiter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "VerticalHeaderTableWithPropertyHeadinTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);

		List<HTMLTreeEntityNode> entityNodes = delimitTableHeader(delimiter, element);
		assertEquals(11, entityNodes.size());

		for (HTMLTreeEntityNode node : entityNodes) {
			System.out.println(node.getContent());
		}
	}

	@Test
	public void testDelimitVerticalTableWithoutHeaderTagInTableBody() throws IOException {

		ClusteringBasedHorizontalTableHeaderDelimiter delimiter = new ClusteringBasedHorizontalTableHeaderDelimiter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "HorizontalHeaderTableWithPropertyHeadIntheTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);

		List<HTMLTreeEntityNode> entityNodes = delimitTableHeader(delimiter, element);
		assertEquals(4, entityNodes.size());

		for (HTMLTreeEntityNode node : entityNodes) {
			System.out.println(node.getContent());
		}
	}
}
