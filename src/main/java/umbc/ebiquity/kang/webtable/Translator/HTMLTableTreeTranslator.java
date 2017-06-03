package umbc.ebiquity.kang.webtable.Translator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import umbc.ebiquity.kang.htmldocument.IHtmlElement;
import umbc.ebiquity.kang.htmldocument.impl.StandardHtmlElement;
import umbc.ebiquity.kang.htmldocument.parser.IHtmlDocumentParsedPathsHolder;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeBlankNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeEntityNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeOverlayConstructor;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.IgnoringHTMLTableNodeProcessor;
import umbc.ebiquity.kang.htmldocument.parser.impl.StandardHtmlPathsParser;
import umbc.ebiquity.kang.webtable.core.TableCell;
import umbc.ebiquity.kang.webtable.core.TableRecord;
import umbc.ebiquity.kang.webtable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.delimiter.impl.HeaderDelimitedTable;
/**
 * 
 * @author yankang
 *
 */
public class HTMLTableTreeTranslator {

	private HTMLTreeOverlayConstructor treeOverlayConstructor;
	private PropertyTableHeaderTranslator propertyTableHeaderIdentifier;
	private EntityTableHeaderTranslator entityTableHeaderIdentifier;

	public HTMLTableTreeTranslator() {
		treeOverlayConstructor = new HTMLTreeOverlayConstructor();
		treeOverlayConstructor.registerCustomizedHtmlNodeProcessors(new IgnoringHTMLTableNodeProcessor());

		propertyTableHeaderIdentifier = new DictionaryBasedPropertyTableHeaderTranslator();
		entityTableHeaderIdentifier = new SimpleEntityTableHeaderTranslator();
	}

	public IHTMLTreeNode translate(HeaderDelimitedTable headerDelimitedTable) {

		// General Rules:
		// (1) For horizontal or/and vertical header table, if no property
		// header found, do not process this table.
		// (2) For two directional header table, if not property header or
		// entity header found, do not process this table.
		// (3) Only consider text type for data in property header while only
		// consider text and image type for data in entity header.

		IHTMLTreeNode tableNode = createTableNode(headerDelimitedTable.getHTMLTableElement());

		DataTableHeaderType headerType = headerDelimitedTable.getDataTableHeaderType();
		if (DataTableHeaderType.NonHeaderTable == headerType) {
			// TODO
			return tableNode;

		} else if (DataTableHeaderType.HorizontalHeaderTable == headerType) {

			Map<Integer, IHTMLTreeNode> idx2Header = new HashMap<>();
			List<HTMLTreeEntityNode> propertyHeaderRecord = propertyTableHeaderIdentifier
					.translate(headerDelimitedTable.getHorizontalHeaderRecords(), 0);
			indexPropertyHeaderRecord(tableNode, idx2Header, propertyHeaderRecord, 0);

			List<TableRecord> dataRecords = headerDelimitedTable.getHorizontalDataRecords();
			translateDataRecords(new HashMap<>(), idx2Header, dataRecords);

			return tableNode;

		} else if (DataTableHeaderType.VerticalHeaderTable == headerType) {

			Map<Integer, IHTMLTreeNode> idx2Header = new HashMap<>();
			List<HTMLTreeEntityNode> propertyHeaderRecord = propertyTableHeaderIdentifier
					.translate(headerDelimitedTable.getVerticalHeaderRecords(), 0);
			indexPropertyHeaderRecord(tableNode, idx2Header, propertyHeaderRecord, 0);

			List<TableRecord> dataRecords = headerDelimitedTable.getVerticalDataRecords();
			translateDataRecords(new HashMap<>(), idx2Header, dataRecords);

			return tableNode;

		} else if (DataTableHeaderType.TwoDirectionalHeaderTable == headerType) {

			// Always treat the property headers as the horizontal headers and
			// entity headers as the vertical headers
			
			List<TableRecord> horizontalHeaderRecords = headerDelimitedTable.getHorizontalHeaderRecords();
			List<TableRecord> verticalHeaderRecords = headerDelimitedTable.getVerticalHeaderRecords();

			// First assuming that vertical header records hold property headers
			// and horizontal header records hold entity headers.
			int skipRowNum = verticalHeaderRecords.size();
			int skipColNum = horizontalHeaderRecords.size();
			List<TableRecord> dataRecords = headerDelimitedTable.getVerticalDataRecords();
			List<HTMLTreeEntityNode> propertyHeaderRecord = propertyTableHeaderIdentifier
					.translate(verticalHeaderRecords, skipColNum);

			TableHeaderTranslationResult entityHeaderRecords = null;
			if (propertyHeaderRecord != null) {
				entityHeaderRecords = entityTableHeaderIdentifier.translator(horizontalHeaderRecords,
						skipRowNum);
			} else {

				// If vertical header records do not hold property headers, we
				// try to find property headers from horizontal header records.
				// In the case, the entity headers should be hold in vertical
				// header records.
				skipRowNum = horizontalHeaderRecords.size();
				skipColNum = verticalHeaderRecords.size();
				dataRecords = headerDelimitedTable.getHorizontalDataRecords();
				propertyHeaderRecord = propertyTableHeaderIdentifier.translate(horizontalHeaderRecords,
						skipColNum);
				if (propertyHeaderRecord == null) {
					return tableNode;
				}
				entityHeaderRecords = entityTableHeaderIdentifier.translator(verticalHeaderRecords,
						skipRowNum);
			}

			if (!entityHeaderRecords.hasPrimaryHeaderRecord()) {
				return tableNode;
			}

			Map<Integer, IHTMLTreeNode> idx2EntityHeader = new HashMap<>();
			Map<Integer, IHTMLTreeNode> idx2PropertyHeader = new HashMap<>();

			indexEntityHeaderRecords(tableNode, idx2EntityHeader, entityHeaderRecords, skipRowNum);
			indexPropertyHeaderRecord(null, idx2PropertyHeader, propertyHeaderRecord, skipColNum);

			translateDataRecords(idx2EntityHeader, idx2PropertyHeader, dataRecords);
			return tableNode;
		} else {
			return tableNode;
		}
	}

	/**
	 * Index entity header records. Adds these tree nodes to the table tree
	 * node.
	 * 
	 * @param tableNode
	 *            the table tree node that the translating entity tree node will
	 *            be added to
	 * @param idx2EntityHeader
	 *            the map that maps index to its corresponding translating
	 *            entity tree node. It is to be populated in this method.
	 * @param entityHeaderRecords
	 *            the entity header records to be translated
	 * @param startIndex
	 *            the start index from where the cell of the entity header
	 *            records will be translated
	 */
	private void indexEntityHeaderRecords(IHTMLTreeNode tableNode, Map<Integer, IHTMLTreeNode> idx2EntityHeader,
			TableHeaderTranslationResult entityHeaderRecords, int startIndex) {
		List<HTMLTreeEntityNode> primaryHeaderRecord = entityHeaderRecords.getPrimaryHeaderRecord();
		List<List<HTMLTreeEntityNode>> secondaryHeaderRecords = entityHeaderRecords.getSecondaryHeaderRecords();
		for (int i = startIndex; i < primaryHeaderRecord.size(); i++) {
			IHTMLTreeNode headerNode = primaryHeaderRecord.get(i);

			for (List<HTMLTreeEntityNode> secondary : secondaryHeaderRecords) {
				headerNode.addChild(secondary.get(i));
			}

			idx2EntityHeader.put(i - startIndex, headerNode);
			if (tableNode != null)
				tableNode.addChild(headerNode);
		}

	}

	/**
	 * Index property header records. Adds these tree nodes to the table tree
	 * node.
	 * 
	 * @param tableNode
	 *            the table tree node that the translating property tree node
	 *            will be added to
	 * @param idx2PropertyHeader
	 *            the map that maps index to its corresponding translating
	 *            property tree node. It is to be populated in this method.
	 * @param propertyHeaderRecord
	 *            the property header records to be indexed
	 * @param startIndex
	 *            the start index from where the cell of the property header
	 *            records will be translated
	 */
	private void indexPropertyHeaderRecord(IHTMLTreeNode tableNode, Map<Integer, IHTMLTreeNode> idx2PropertyHeader,
			List<HTMLTreeEntityNode> propertyHeaderRecord, int startIndex) {
		for (int i = startIndex; i < propertyHeaderRecord.size(); i++) {
			IHTMLTreeNode headerNode = propertyHeaderRecord.get(i);
			idx2PropertyHeader.put(i - startIndex, headerNode);
			if (tableNode != null)
				tableNode.addChild(headerNode);
		}
	}

	/**
	 * Translates data records into HTML tree nodes. Forms the tree structure
	 * for the entities, properties and property values.
	 * 
	 * @param idx2EntityHeader
	 *            the map that maps index to its corresponding translating
	 *            entity tree node.
	 * @param idx2PropertyHeader
	 *            the map that maps index to its corresponding translating
	 *            property tree node.
	 * @param dataRecords
	 *            the data records to be translated
	 */
	private void translateDataRecords(Map<Integer, IHTMLTreeNode> idx2EntityHeader,
			Map<Integer, IHTMLTreeNode> idx2PropertyHeader, List<TableRecord> dataRecords) {

		for (int i = 0; i < dataRecords.size(); i++) {
			IHTMLTreeNode headerNode = idx2EntityHeader.get(i);
			TableRecord rec = dataRecords.get(i);
			List<TableCell> cells = rec.getTableCells();
			for (int j = 0; j < cells.size(); j++) {
				StandardHtmlElement htmlElement = StandardHtmlElement
						.createDefaultStandardHtmlElement(cells.get(j).getWrappedElement());
				StandardHtmlPathsParser webPagePathsImpl = new StandardHtmlPathsParser(htmlElement);
				IHtmlDocumentParsedPathsHolder webPagePathHolder = webPagePathsImpl.parse();
				IHTMLTreeOverlay treeOverlay = treeOverlayConstructor.build(webPagePathHolder);

				IHTMLTreeNode propertyHeaderNode = idx2PropertyHeader.get(j);

				if (propertyHeaderNode != null)
					propertyHeaderNode.addChild(treeOverlay.getTreeRoot());

				if (headerNode != null)
					headerNode.addChild(propertyHeaderNode);
			}
		}
	}

	/**
	 * Creates a table node as a place holder
	 * 
	 * @param IHtmlElement
	 * @return an IHtmlElement
	 */
	private IHTMLTreeNode createTableNode(IHtmlElement htmlElement) {
		return new HTMLTreeBlankNode(htmlElement.getBody());
	}

}
