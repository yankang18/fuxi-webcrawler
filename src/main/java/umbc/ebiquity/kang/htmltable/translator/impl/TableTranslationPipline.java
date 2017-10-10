package umbc.ebiquity.kang.htmltable.translator.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlayRefiner;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.StandardHTMLTreeBlankNodeConsolidator;
import umbc.ebiquity.kang.htmldocument.util.HTMLTree2JSONConverter;
import umbc.ebiquity.kang.htmldocument.util.HTMLTreeUtil;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.TableStatus;
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
	
	public IHTMLTreeOverlay doTranslate(Element element) {
		HTMLTableValidator.isTable(element);

		/*
		 * (1)(2)(3) translate a html table to a html table tree
		 */

		// (1) Create a table element from certain source.
		// Document doc = Jsoup.parse("");
		// Element element = doc.getElementsByTag("table").get(0);

		// (2) table Element -- (ITableHeaderDelimiter) --> HeaderDelimitedTable
		HeaderDelimitedTable delimitedTable = headerDelimiter.delimit(element);

		// delimitedTable must be vertical, horizontal or two header
		assertEquals(TableStatus.RegularTable, delimitedTable.getTableStatus());

		// (3) HeaderDelimitedTable -- (TableTreeTranslator) --> IHTMLTreeNode representing the table
		IHTMLTreeNode tree = tableTreeTranslator.translate(delimitedTable);

		if (printDetail) {
			HTMLTreeUtil.prettyPrint(tree);
		}

		/*
		 * (4)(5) translate a html table tree to a JSON tree
		 */

		// (4)
		IHTMLTreeOverlay overlay = HTMLTreeOverlay.createDefaultHTMLTreeOverlay(tree);

		// (5) IHTMLTreeOverlay -- (StandardHTMLTreeBlankNodeConsolidator) --> IHTMLTreeOverlay
		overlay = htmlTreeBlankNodeConsolidator.refine(overlay);

		if (printDetail) {
			HTMLTreeUtil.prettyPrint(overlay.getTreeRoot());
		}
		
		return overlay;
	}
	
	
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
