package umbc.ebiquity.kang.webtable.resolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.IHtmlNode;
import umbc.ebiquity.kang.htmldocument.IHtmlPath;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.AbstractHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeBlankNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeEntityNode;
import umbc.ebiquity.kang.textprocessing.similarity.impl.EqualStemBoostingLabelSimilarity;
import umbc.ebiquity.kang.textprocessing.similarity.impl.OrderedTokenListSimilarity;
import umbc.ebiquity.kang.webtable.core.TableCell;
import umbc.ebiquity.kang.webtable.core.TableRecord;

/**
 * This class is to identify property headers from table records based a
 * pre-defined controlled dictionary that typically stores domain-specific
 * properties.
 * 
 * @author yankang
 *
 */
public class DictionaryBasedPropertyTableHeaderIdentifier implements PropertyTableHeaderIdentifier {
	
	private static Set<String> DICTIONARY;
	static {
		// TODO: initialize the dictionary from a predefined file
		DICTIONARY = new HashSet<>();
	}
	
	private EqualStemBoostingLabelSimilarity labelSimilarity = new EqualStemBoostingLabelSimilarity(
			new OrderedTokenListSimilarity());
	private double threshold = 0.65;

	@Override
	public List<IHTMLTreeNode> identifyPropertyHeader(List<TableRecord> headerRecords, int skipCellNumber) {

		List<IHTMLTreeNode> propertyHeaderNodes = null;
		for (TableRecord record : headerRecords) {
			propertyHeaderNodes = new ArrayList<>(headerRecords.size());

			double totalSim = 0.0;
			List<TableCell> tableCells = record.getTableCells();
			int i = 0;
			for (; i < skipCellNumber; i++) {
				propertyHeaderNodes.add(createEntityNode(tableCells.get(i)));
			}
			for (; i < tableCells.size(); i++) {
				propertyHeaderNodes.add(createEntityNode(tableCells.get(i)));
				totalSim += computeSimilarity(extractContent(tableCells.get(i)));
			}
			if (totalSim / tableCells.size() >= threshold) {
				return propertyHeaderNodes;
			}
		}
		return null;
	}

	private double computeSimilarity(String text) {
		double max = 0;
		for (String entry : DICTIONARY) {
			max = Math.max(max, labelSimilarity.computeLabelSimilarity(text, entry));
		}
		return max;
	}
	
	
	private IHTMLTreeNode createEntityNode(TableCell tableCell) {
		HTMLTreeEntityNode entityNode = new HTMLTreeEntityNode(extractContent(tableCell), tableCell.getWrappedElement());
		return entityNode;
	}

	private String extractContent(TableCell tableCell) {
		Element element = tableCell.getWrappedElement();
		return element != null ? tableCell.getWrappedElement().text() : "";
	}

}