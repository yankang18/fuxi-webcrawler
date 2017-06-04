package umbc.ebiquity.kang.webtable.Translator;

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
import umbc.ebiquity.kang.htmldocument.util.BasicValidator;
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
public class DictionaryBasedPropertyTableHeaderTranslator implements PropertyTableHeaderTranslator {
	
	private static Set<String> DICTIONARY;
	static {
		// TODO: initialize the dictionary from a predefined file
		
		DICTIONARY = new HashSet<>();
		
		// General properties
		DICTIONARY.add("Length");
		DICTIONARY.add("Width");
		DICTIONARY.add("Height");
		DICTIONARY.add("Size");
		DICTIONARY.add("Type");
		DICTIONARY.add("Price");
		DICTIONARY.add("Ratio");
		DICTIONARY.add("Brand");
		DICTIONARY.add("Logo");
		
		//
		DICTIONARY.add("Rating");
		DICTIONARY.add("Rate");
		DICTIONARY.add("Ratio");
		DICTIONARY.add("Dimension");
		DICTIONARY.add("Resolution");
		
		//
		DICTIONARY.add("Thickness");
		DICTIONARY.add("Diameter");
		DICTIONARY.add("Capacity");
		DICTIONARY.add("Tolerance");
		DICTIONARY.add("Services");
		DICTIONARY.add("Inspection");
		DICTIONARY.add("Product");
		DICTIONARY.add("Volume");
		DICTIONARY.add("Time");
		DICTIONARY.add("Process");
		DICTIONARY.add("Axis");
		DICTIONARY.add("Center");
		DICTIONARY.add("Feeding");
		DICTIONARY.add("Feed");
		DICTIONARY.add("Material");
	}

	private EqualStemBoostingLabelSimilarity labelSimilarity = new EqualStemBoostingLabelSimilarity(
			new OrderedTokenListSimilarity());
	private double threshold = 0.30;

	@Override
	public List<HTMLTreeEntityNode> translate(List<TableRecord> headerRecords, int skipCellNumber) {

		List<HTMLTreeEntityNode> propertyHeaderNodes = null;
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
				System.out.println(DictionaryBasedPropertyTableHeaderTranslator.class.getName()
						+ "#identifyPropertyHeader:" + totalSim / tableCells.size());
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
	
	
	private HTMLTreeEntityNode createEntityNode(TableCell tableCell) {
		HTMLTreeEntityNode entityNode = new HTMLTreeEntityNode(tableCell.getWrappedElement(), extractContent(tableCell));
		return entityNode;
	}

	private String extractContent(TableCell tableCell) {
		Element element = tableCell.getWrappedElement();
		return element != null ? tableCell.getWrappedElement().text() : "";
	}

	/**
	 * @return the threshold
	 */
	public double getThreshold() {
		return threshold;
	}

	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(double threshold) {
		BasicValidator.is0to1(threshold);
		this.threshold = threshold;
	}

}