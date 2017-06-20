package umbc.ebiquity.kang.htmltable.translator.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import umbc.ebiquity.kang.htmldocument.IHtmlElement;
import umbc.ebiquity.kang.htmldocument.impl.StandardHtmlElement;
import umbc.ebiquity.kang.htmldocument.parser.IHtmlDocumentParsedPathsHolder;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.AbstractHTMLTreeContentNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeBlankNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeEntityNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeOverlayConstructor;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreePropertyNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.IgnoringHTMLTableNodeProcessor;
import umbc.ebiquity.kang.htmldocument.parser.impl.StandardHtmlPathsParser;
import umbc.ebiquity.kang.htmltable.core.TableCell;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderDelimitedTable;
import umbc.ebiquity.kang.htmltable.translator.IEntityTableHeaderTranslator;
import umbc.ebiquity.kang.htmltable.translator.IPropertyTableHeaderTranslator;
import umbc.ebiquity.kang.htmltable.translator.TableHeaderTranslationResult;

/**
 * 
 * @author yankang
 *
 */
public class TableTreeTranslator {

	private HTMLTreeOverlayConstructor treeOverlayConstructor;
	private IPropertyTableHeaderTranslator mainPropertyTableHeaderTranslator;
	private IEntityTableHeaderTranslator entityTableHeaderTranslator;
	private List<IPropertyTableHeaderTranslator> propertyTableHeaderTranslatorList;

	public TableTreeTranslator() {
		treeOverlayConstructor = new HTMLTreeOverlayConstructor();
		treeOverlayConstructor.registerCustomizedHtmlNodeProcessors(new IgnoringHTMLTableNodeProcessor());
		mainPropertyTableHeaderTranslator = new DictionaryBasedPropertyTableHeaderTranslator();
		entityTableHeaderTranslator = new SimpleEntityTableHeaderTranslator();
		
		// Note, the order of the translators in the list matters
		propertyTableHeaderTranslatorList = new ArrayList<>(2);
		propertyTableHeaderTranslatorList.add(mainPropertyTableHeaderTranslator);
		propertyTableHeaderTranslatorList.add(new SimplePropertyTableHeaderTranslator());
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

			Map<Integer, HTMLTreePropertyNode> idx2propertyHeader = new HashMap<>();
			List<HTMLTreePropertyNode> propertyHeaderRecord = translatePropertyHeaderRecords(
					headerDelimitedTable.getHorizontalHeaderRecords());

			if (propertyHeaderRecord == null) {
				return tableNode;
			}

			indexPropertyHeaderRecord(idx2propertyHeader, propertyHeaderRecord, 0);
			List<TableRecord> dataRecords = headerDelimitedTable.getHorizontalDataRecords();
			Map<Integer, HTMLTreeEntityNode> idx2EntityHeader = createDefaultEntityHeaderIndex(dataRecords.size());
			translateDataRecords(tableNode, idx2EntityHeader, idx2propertyHeader, dataRecords, 0);

			return tableNode;

		} else if (DataTableHeaderType.VerticalHeaderTable == headerType) {

			Map<Integer, HTMLTreePropertyNode> idx2PropertyHeader = new HashMap<>();
			List<HTMLTreePropertyNode> propertyHeaderRecord = translatePropertyHeaderRecords(
					headerDelimitedTable.getVerticalHeaderRecords());

			if (propertyHeaderRecord == null) {
				return tableNode;
			}

			indexPropertyHeaderRecord(idx2PropertyHeader, propertyHeaderRecord, 0);

			List<TableRecord> dataRecords = headerDelimitedTable.getVerticalDataRecords();
			Map<Integer, HTMLTreeEntityNode> idx2EntityHeader = createDefaultEntityHeaderIndex(dataRecords.size());
			translateDataRecords(tableNode, idx2EntityHeader, idx2PropertyHeader, dataRecords, 0);

			return tableNode;

		} else if (DataTableHeaderType.TwoDirectionalHeaderTable == headerType) {

			// Always treat the property headers as the horizontal headers and
			// entity headers as the vertical headers

			List<TableRecord> horizontalHeaderRecords = headerDelimitedTable.getHorizontalHeaderRecords();
			List<TableRecord> verticalHeaderRecords = headerDelimitedTable.getVerticalHeaderRecords();

			System.out.println("entityHeaderRecords size: " + verticalHeaderRecords.size());
			System.out.println("propertyHeaderRecord size: " + horizontalHeaderRecords.size());
			
			// First assuming that vertical header records hold property headers
			// and horizontal header records hold entity headers.
			int skipRowNum = verticalHeaderRecords.size();
			int skipColNum = horizontalHeaderRecords.size();
			List<TableRecord> dataRecords = headerDelimitedTable.getVerticalDataRecords();
			List<HTMLTreePropertyNode> propertyHeaderRecord = mainPropertyTableHeaderTranslator
					.translate(verticalHeaderRecords, skipColNum);

			TableHeaderTranslationResult entityHeaderRecords = null;
			if (propertyHeaderRecord != null) {
				entityHeaderRecords = entityTableHeaderTranslator.translator(horizontalHeaderRecords, skipRowNum);
			} else {

				// If vertical header records do not hold property headers, we
				// try to find property headers from horizontal header records.
				// In the case, the entity headers should be hold in vertical
				// header records.
				skipRowNum = horizontalHeaderRecords.size();
				skipColNum = verticalHeaderRecords.size();
				dataRecords = headerDelimitedTable.getHorizontalDataRecords();
				propertyHeaderRecord = mainPropertyTableHeaderTranslator.translate(horizontalHeaderRecords, skipColNum);
				if (propertyHeaderRecord == null) {
					return tableNode;
				}
				entityHeaderRecords = entityTableHeaderTranslator.translator(verticalHeaderRecords, skipRowNum);
			}

			if (!entityHeaderRecords.hasPrimaryHeaderRecord()) {
				return tableNode;
			}

			Map<Integer, HTMLTreeEntityNode> idx2EntityHeader = new HashMap<>();
			Map<Integer, HTMLTreePropertyNode> idx2PropertyHeader = new HashMap<>();

			indexEntityHeaderRecords(idx2EntityHeader, entityHeaderRecords, skipRowNum);
			indexPropertyHeaderRecord(idx2PropertyHeader, propertyHeaderRecord, skipColNum);

			System.out.println("dataRecords size: " + dataRecords.size());
			translateDataRecords(tableNode, idx2EntityHeader, idx2PropertyHeader, dataRecords, skipColNum);
			return tableNode;
		} else {
			return tableNode;
		}
	}

	/**
	 * Index entity header records. Adds these tree nodes to the table tree
	 * node.
	 * 
	 * @param idx2EntityHeader
	 *            the map that maps index to its corresponding translating
	 *            entity tree node. It is to be populated in this method.
	 * @param entityHeaderRecords
	 *            the entity header records to be translated
	 * @param startIndex
	 *            the start index from where the cell of the entity header
	 *            records will be translated
	 */
	private void indexEntityHeaderRecords(Map<Integer, HTMLTreeEntityNode> idx2EntityHeader,
			TableHeaderTranslationResult entityHeaderRecords, int startIndex) {
		
		List<HTMLTreeEntityNode> primaryHeaderRecord = entityHeaderRecords.getPrimaryHeaderRecord();
		List<List<HTMLTreeEntityNode>> secondaryHeaderRecords = entityHeaderRecords.getSecondaryHeaderRecords();
		for (int i = startIndex; i < primaryHeaderRecord.size(); i++) {
			
			HTMLTreeEntityNode headerNode = primaryHeaderRecord.get(i);
			for (List<HTMLTreeEntityNode> secondary : secondaryHeaderRecords) {
				headerNode.addChild(secondary.get(i));
			}
			
			idx2EntityHeader.put(i - startIndex, headerNode);
		}
	}

	/**
	 * Index property header records. Adds these tree nodes to the table tree
	 * node.
	 * 
	 * @param idx2PropertyHeader
	 *            the map that maps index to its corresponding translating
	 *            property tree node. It is to be populated in this method.
	 * @param propertyHeaderRecord
	 *            the property header records to be indexed
	 * @param startIndex
	 *            the start index from where the cell of the property header
	 *            records will be translated
	 */
	private void indexPropertyHeaderRecord(Map<Integer, HTMLTreePropertyNode> idx2PropertyHeader,
			List<HTMLTreePropertyNode> propertyHeaderRecord, int startIndex) {
		
		for (int i = 0; i < propertyHeaderRecord.size(); i++) {
			HTMLTreePropertyNode headerNode = propertyHeaderRecord.get(i);
			idx2PropertyHeader.put(i, headerNode);
		}
	}

	/**
	 * Translates data records into HTML tree nodes. Forms the tree structure
	 * for the entities, properties and property values.
	 * 
	 * @param tableNode
	 *            the IHTMLTreeNode representing a table that data to be added
	 *            into, cannot be null
	 * @param idx2EntityHeader
	 *            the map that maps index to its corresponding translating
	 *            entity tree node.
	 * @param idx2PropertyHeader
	 *            the map that maps index to its corresponding translating
	 *            property tree node.
	 * @param dataRecords
	 *            the data records to be translated
	 * @param skipColNum
	 */
	private void translateDataRecords(IHTMLTreeNode tableNode, Map<Integer, HTMLTreeEntityNode> idx2EntityHeader,
			Map<Integer, HTMLTreePropertyNode> idx2PropertyHeader, List<TableRecord> dataRecords, int skipColNum) {

		for (int i = 0; i < dataRecords.size(); i++) {
			
			HTMLTreeEntityNode itemHeaderNode = idx2EntityHeader.get(i);
			TableRecord rec = dataRecords.get(i);
			List<TableCell> cells = rec.getTableCells();
			for (int j = skipColNum; j < cells.size(); j++) {
				
				StandardHtmlElement htmlElement = StandardHtmlElement
						.createDefaultStandardHtmlElement(cells.get(j).getWrappedElement());
				StandardHtmlPathsParser webPagePathsImpl = new StandardHtmlPathsParser(htmlElement);
				IHtmlDocumentParsedPathsHolder webPagePathHolder = webPagePathsImpl.parse();
				IHTMLTreeOverlay treeOverlay = treeOverlayConstructor.build(webPagePathHolder);

				HTMLTreePropertyNode propertyheaderNode = idx2PropertyHeader.get(j);
				if (isValid(propertyheaderNode) && isValid(itemHeaderNode)) {
					IHTMLTreeNode propertyHeaderNode = clonePropertyNode(propertyheaderNode);
					propertyHeaderNode.addChild(treeOverlay.getTreeRoot());
					itemHeaderNode.addChild(propertyHeaderNode);
				}
			}
			tableNode.addChild(itemHeaderNode);
		}
	}

	/**
	 * Identifies one property header table records from the specified list that
	 * best represents the property header, and translate it into a list of
	 * HTMLTreePropertyNode.
	 * 
	 * @param headerRecords
	 *            the list of property header table records
	 * @return a List of HTMLTreePropertyNode; can be null if no appropriate
	 *         property header can be found
	 */
	private List<HTMLTreePropertyNode> translatePropertyHeaderRecords(List<TableRecord> headerRecords) {
		List<HTMLTreePropertyNode> propertyHeaderRecord = null;
		for (IPropertyTableHeaderTranslator translator : propertyTableHeaderTranslatorList) {
			propertyHeaderRecord = translator.translate(headerRecords, 0);
			if (propertyHeaderRecord != null)
				return propertyHeaderRecord;
		}
		return null;
	}

	/**
	 * Creates a table node as a place holder.
	 * 
	 * @param IHtmlElement
	 * @return an IHtmlElement
	 */
	private IHTMLTreeNode createTableNode(IHtmlElement htmlElement) {
		return new HTMLTreeBlankNode(htmlElement.getBody());
	}

	private HTMLTreePropertyNode clonePropertyNode(HTMLTreePropertyNode propertyNode) {
		if (propertyNode == null)
			return null;
		HTMLTreePropertyNode clonedPropertyNode = new HTMLTreePropertyNode(propertyNode.getWrappedElement(),
				propertyNode.getContent());
		return clonedPropertyNode;
	}

	private Map<Integer, HTMLTreeEntityNode> createDefaultEntityHeaderIndex(int size) {
		Map<Integer, HTMLTreeEntityNode> idx2EntityHeader = new HashMap<>();
		for (int i = 0; i < size; i++) {
			HTMLTreeEntityNode entityNode = new HTMLTreeEntityNode("th", "Item");
			idx2EntityHeader.put(i, entityNode);
		}
		return idx2EntityHeader;
	}

	private boolean isValid(AbstractHTMLTreeContentNode contentNode) {
		if (contentNode == null)
			return false;
		String content = contentNode.getContent();
		return content != null && !content.trim().isEmpty();
	}

}
