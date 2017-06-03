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
import umbc.ebiquity.kang.webtable.Translator.SimpleEntityTableHeaderTranslator;
import umbc.ebiquity.kang.webtable.Translator.TableHeaderTranslationResult;
import umbc.ebiquity.kang.webtable.core.DataCell;
import umbc.ebiquity.kang.webtable.core.TableCell;
import umbc.ebiquity.kang.webtable.core.TableRecord;
import umbc.ebiquity.kang.webtable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.delimiter.IDelimitedTable.TableStatus;
import umbc.ebiquity.kang.webtable.delimiter.impl.ClusteringBasedHorizontalTableHeaderDelimiter;
import umbc.ebiquity.kang.webtable.delimiter.impl.HeaderDelimitedTable;

public class EntityTableHeaderDelimiterTest {

	private static final String TEST_FILE_FOLDER = "TableHeaderLocatorTest/";

	@Test
	public void testResolveHorizontalTableWithHeaderTagInTableBody() throws IOException {

		ClusteringBasedHorizontalTableHeaderDelimiter resolver = new ClusteringBasedHorizontalTableHeaderDelimiter();
		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "HorizontalHeaderTableWithHeadIntheTbody.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);

		HeaderDelimitedTable result = resolver.delimit(element);
		assertEquals(result.getTableStatus(), TableStatus.RegularTable);
		assertEquals(result.getDataTableHeaderType(), DataTableHeaderType.HorizontalHeaderTable);

		List<TableRecord> headerRecords = result.getHorizontalHeaderRecords();
		List<TableRecord> dataRecords = result.getHorizontalDataRecords();
		assertEquals(1, headerRecords.size());
		assertEquals(4, dataRecords.size());

		SimpleEntityTableHeaderTranslator translator = new SimpleEntityTableHeaderTranslator();
		TableHeaderTranslationResult translationResult = translator.translator(headerRecords, 0);
		
		List<HTMLTreeEntityNode> entityHeader = translationResult.getPrimaryHeaderRecord();
		
		assertEquals(4, entityHeader.size());
		
		for(HTMLTreeEntityNode node : entityHeader){
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
