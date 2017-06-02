package umbc.ebiquity.kang.websiteparser.tableparser.resolver;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeEntityNode;
import umbc.ebiquity.kang.webtable.resolver.DictionaryBasedPropertyTableHeaderIdentifier;
import umbc.ebiquity.kang.webtable.resolver.PropertyTableHeaderIdentifier;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.TableStatus;
import umbc.ebiquity.kang.webtable.spliter.impl.ClusteringBasedVerticalTableHeaderSpliter;
import umbc.ebiquity.kang.webtable.spliter.impl.TableSplitingResult;

public class PropertyTableHeaderIdentifierTest {
	
	private static final String TEST_FILE_FOLDER = "TableHeaderLocatorTest/";

	@Test
	public void testResolveVerticalTableWithoutHeaderTagInTableBody() throws IOException {

		ClusteringBasedVerticalTableHeaderSpliter resolver = new ClusteringBasedVerticalTableHeaderSpliter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "VerticalHeaderTableWithHeadinTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);

		TableSplitingResult result = resolver.split(element);
		assertEquals(TableStatus.RegularTable, result.getTableStatus());
		assertEquals(DataTableHeaderType.VerticalHeaderTable, result.getDataTableHeaderType());

		PropertyTableHeaderIdentifier propertyTableHeaderIdentifier = new DictionaryBasedPropertyTableHeaderIdentifier();

		List<HTMLTreeEntityNode> propertyHeaderRecord = propertyTableHeaderIdentifier
				.identifyPropertyHeader(result.getVerticalHeaderRecords(), 0);

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
