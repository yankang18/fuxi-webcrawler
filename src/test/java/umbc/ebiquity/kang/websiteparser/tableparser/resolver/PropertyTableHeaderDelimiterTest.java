package umbc.ebiquity.kang.websiteparser.tableparser.resolver;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeEntityNode;
import umbc.ebiquity.kang.webtable.Translator.DictionaryBasedPropertyTableHeaderTranslator;
import umbc.ebiquity.kang.webtable.Translator.PropertyTableHeaderTranslator;
import umbc.ebiquity.kang.webtable.delimiter.AbstractClusteringBasedTableHeaderDelimiter;
import umbc.ebiquity.kang.webtable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.delimiter.IDelimitedTable.TableStatus;
import umbc.ebiquity.kang.webtable.delimiter.impl.ClusteringBasedVerticalTableHeaderDelimiter;
import umbc.ebiquity.kang.webtable.delimiter.impl.HeaderDelimitedTable;

public class PropertyTableHeaderDelimiterTest {
	
	private static final String TEST_FILE_FOLDER = "TableHeaderLocatorTest/";

	@Test
	public void testDelimitVerticalTableWithHeaderTagInTableBody() throws IOException {

		ClusteringBasedVerticalTableHeaderDelimiter delimiter = new ClusteringBasedVerticalTableHeaderDelimiter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "VerticalHeaderTableWithHeadinTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);

		delimitTableHeader(delimiter, element);
	}
	
	// TODO: change this test to use Horizontal header table
	@Test
	public void testDelimitVerticalTableWithoutHeaderTagInTableBody() throws IOException {

		ClusteringBasedVerticalTableHeaderDelimiter delimiter = new ClusteringBasedVerticalTableHeaderDelimiter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "VerticalHeaderTableWithoutHeadinTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);

		delimitTableHeader(delimiter, element);
	}

	private void delimitTableHeader(AbstractClusteringBasedTableHeaderDelimiter delimiter, Element element) {
		HeaderDelimitedTable delimitedTable = delimiter.delimit(element);
		assertEquals(TableStatus.RegularTable, delimitedTable.getTableStatus());
		assertEquals(DataTableHeaderType.VerticalHeaderTable, delimitedTable.getDataTableHeaderType());

		PropertyTableHeaderTranslator propertyTableHeaderIdentifier = new DictionaryBasedPropertyTableHeaderTranslator();

		List<HTMLTreeEntityNode> propertyHeaderRecord = propertyTableHeaderIdentifier
				.translate(delimitedTable.getVerticalHeaderRecords(), 0);

		assertEquals(11, propertyHeaderRecord.size());
		
		for(HTMLTreeEntityNode node : propertyHeaderRecord){
			System.out.println(node.getContent());
		}
	}

	private File loadFileOrDirectory(String fileName) {
		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File input = new File(classLoader.getResource(fileName).getFile());
		return input;
	}

}
