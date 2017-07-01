package umbc.ebiquity.kang.websiteparser.tableparser.translator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreePropertyNode;
import umbc.ebiquity.kang.htmltable.delimiter.impl.ClusteringBasedHorizontalTableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.delimiter.impl.ClusteringBasedVerticalTableHeaderDelimiter;

public class PropertyTableHeaderTranslatorTest extends BaseTableHeaderTranslatorTest {

	private static final String TEST_FILE_FOLDER = "TableHeaderDelimiterTest/";

	// @Ignore
	@Test
	public void testDelimitVerticalTableWithHeaderTagInTableBody() throws IOException {

		ClusteringBasedVerticalTableHeaderDelimiter delimiter = new ClusteringBasedVerticalTableHeaderDelimiter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "VerticalHeaderTableWithPropertyHeadinTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);

		List<HTMLTreePropertyNode> propertyNodes = getPropertyNodes(delimiter, element);
		assertEquals(11, propertyNodes.size());

		for (HTMLTreePropertyNode node : propertyNodes) {
			System.out.println(node.getContent());
		}
	}

	@Test
	public void testDelimitVerticalTableWithoutHeaderTagInTableBody() throws IOException {

		ClusteringBasedHorizontalTableHeaderDelimiter delimiter = new ClusteringBasedHorizontalTableHeaderDelimiter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "HorizontalHeaderTableWithPropertyHeadIntheTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);

		List<HTMLTreePropertyNode> propertyNodes = getPropertyNodes(delimiter, element);
		assertEquals(4, propertyNodes.size());

		for (HTMLTreePropertyNode node : propertyNodes) {
			System.out.println(node.getContent());
		}
	}
}
