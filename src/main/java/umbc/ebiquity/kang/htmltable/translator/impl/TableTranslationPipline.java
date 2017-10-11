package umbc.ebiquity.kang.htmltable.translator.impl;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlayRefiner;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.StandardHTMLTreeBlankNodeConsolidator;
import umbc.ebiquity.kang.htmldocument.util.HTMLTreeUtil;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.htmltable.delimiter.ITableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderDelimitedTable;
import umbc.ebiquity.kang.htmltable.delimiter.impl.StandardTableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.translator.ITableTreeTranslator;
import umbc.ebiquity.kang.htmltable.util.HTMLTableValidator;

public class TableTranslationPipline {
	
	private ITableHeaderDelimiter headerDelimiter;
	private ITableTreeTranslator tableTreeTranslator;
	private IHTMLTreeOverlayRefiner htmlTreeBlankNodeConsolidator;
	private boolean printDetail = false;

	public TableTranslationPipline() {
		headerDelimiter = new StandardTableHeaderDelimiter();
		tableTreeTranslator = new TableTreeTranslator();
		htmlTreeBlankNodeConsolidator = new StandardHTMLTreeBlankNodeConsolidator();
	}
	
	/**
	 * 
	 * @param element
	 *            the Element represents a table
	 * @return an instance of IHTMLTreeOverlay, can be null
	 */
	public IHTMLTreeOverlay doTranslate(Element element) {
		HTMLTableValidator.isTable(element);

		/*
		 * (1)(2) translate a html table to a html table tree
		 */

		// (1) table Element -- (ITableHeaderDelimiter) --> HeaderDelimitedTable
		HeaderDelimitedTable delimitedTable = headerDelimiter.delimit(element);

		if (isInvalid(delimitedTable)) {
			return null;
		}
		
		// (2) HeaderDelimitedTable -- (TableTreeTranslator) --> IHTMLTreeNode representing the table
		IHTMLTreeNode tree = tableTreeTranslator.translate(delimitedTable);

		if (printDetail) {
			HTMLTreeUtil.prettyPrint(tree);
		}

		/*
		 * (3)(4) translate a html table tree to a JSON tree
		 */

		// (3)
		IHTMLTreeOverlay overlay = HTMLTreeOverlay.createDefaultHTMLTreeOverlay(tree);

		// (4) IHTMLTreeOverlay -- (StandardHTMLTreeBlankNodeConsolidator) --> IHTMLTreeOverlay
		overlay = htmlTreeBlankNodeConsolidator.refine(overlay);

		if (printDetail) {
			HTMLTreeUtil.prettyPrint(overlay.getTreeRoot());
		}
		
		return overlay;
	}
	
	private boolean isInvalid(HeaderDelimitedTable delimitedTable) {
		// delimitedTable must be vertical, horizontal or two header
		return delimitedTable.getDataTableHeaderType() == DataTableHeaderType.NonHeaderTable
				|| delimitedTable.getDataTableHeaderType() == DataTableHeaderType.UnDetermined;
	}
	
	/*
	 * Setters for configuring this pipeline
	 */

	public TableTranslationPipline setTableHeaderDelimiter(ITableHeaderDelimiter headerDelimiter) {
		this.headerDelimiter = headerDelimiter;
		return this;
	}

	public TableTranslationPipline setTableTreeTranslator(ITableTreeTranslator tableTreeTranslator) {
		this.tableTreeTranslator = tableTreeTranslator;
		return this;
	}

	public TableTranslationPipline setHTMLTreeBlankNodeConsolidator(
			IHTMLTreeOverlayRefiner htmlTreeBlankNodeConsolidator) {
		this.htmlTreeBlankNodeConsolidator = htmlTreeBlankNodeConsolidator;
		return this;
	}

	/**
	 * @param printDetail the printDetail to set
	 */
	public void setPrintDetail(boolean printDetail) {
		this.printDetail = printDetail;
	}
	
}
