package umbc.ebiquity.kang.websiteparser.tableparser.translator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeEntityNode;
import umbc.ebiquity.kang.webtable.Translator.DictionaryBasedPropertyTableHeaderTranslator;
import umbc.ebiquity.kang.webtable.Translator.PropertyTableHeaderTranslator;
import umbc.ebiquity.kang.webtable.core.TableRecord;
import umbc.ebiquity.kang.webtable.delimiter.AbstractClusteringBasedTableHeaderDelimiter;
import umbc.ebiquity.kang.webtable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.delimiter.IDelimitedTable.TableStatus;
import umbc.ebiquity.kang.webtable.delimiter.impl.HeaderDelimitedTable;

public class BaseTableHeaderTranslatorTest {
	
	protected List<HTMLTreeEntityNode> delimitTableHeader(AbstractClusteringBasedTableHeaderDelimiter delimiter,
			Element element) {
		HeaderDelimitedTable delimitedTable = delimiter.delimit(element);
		assertEquals(TableStatus.RegularTable, delimitedTable.getTableStatus());

		PropertyTableHeaderTranslator propertyTableHeaderIdentifier = new DictionaryBasedPropertyTableHeaderTranslator();
		List<TableRecord> records = null;
		if (DataTableHeaderType.VerticalHeaderTable.equals(delimitedTable.getDataTableHeaderType())) {
			records = delimitedTable.getVerticalHeaderRecords();
		} else {
			records = delimitedTable.getHorizontalHeaderRecords();
		}
		return propertyTableHeaderIdentifier.translate(records, 0);
	}

	protected File loadFileOrDirectory(String fileName) {
		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File input = new File(classLoader.getResource(fileName).getFile());
		return input;
	}
}
