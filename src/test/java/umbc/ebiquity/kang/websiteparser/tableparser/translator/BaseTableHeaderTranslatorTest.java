package umbc.ebiquity.kang.websiteparser.tableparser.translator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreePropertyNode;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.delimiter.AbstractClusteringBasedTableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.TableStatus;
import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderDelimitedTable;
import umbc.ebiquity.kang.htmltable.translator.IPropertyHeaderTranslator;
import umbc.ebiquity.kang.htmltable.translator.impl.DictionaryBasedPropertyHeaderTranslator;

public class BaseTableHeaderTranslatorTest {
	
	protected List<HTMLTreePropertyNode> getPropertyNodes(AbstractClusteringBasedTableHeaderDelimiter delimiter,
			Element element) {
		
		HeaderDelimitedTable delimitedTable = delimiter.delimit(element);
		assertEquals(TableStatus.RegularTable, delimitedTable.getTableStatus());

		IPropertyHeaderTranslator propertyTableHeaderIdentifier = new DictionaryBasedPropertyHeaderTranslator();
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
