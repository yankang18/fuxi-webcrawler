package umbc.ebiquity.kang.htmltable.translator.impl;

import umbc.ebiquity.kang.textprocessing.similarity.ILabelSimilarity;
import umbc.ebiquity.kang.textprocessing.similarity.impl.EqualStemBoostingLabelSimilarity;
import umbc.ebiquity.kang.textprocessing.similarity.impl.OrderedTokenListSimilarity;

public class DictionariedPropertyScoreCalculator {

	private static ILabelSimilarity labelSimilarity = new EqualStemBoostingLabelSimilarity(
			new OrderedTokenListSimilarity());

	/**
	 * 
	 * @param text
	 * @return
	 */
	public static double calculatorScore(String text) {
		double max = 0;
		for (String entry : TablePropertyDictionary.getDictionary()) {
			max = Math.max(max, labelSimilarity.computeLabelSimilarity(text, entry));
		}
		return max;
	}
}
