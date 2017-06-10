package umbc.ebiquity.kang.htmltable.similarity.impl;

import java.util.Arrays;
import java.util.Set;

import umbc.ebiquity.kang.htmltable.IAttributeTracer;
import umbc.ebiquity.kang.htmltable.ITagAttributeHolder;
import umbc.ebiquity.kang.htmltable.ITagHolder;
import umbc.ebiquity.kang.htmltable.similarity.IAttributesSimilarity;
import umbc.ebiquity.kang.textprocessing.similarity.ITokenListSimilarity;
import umbc.ebiquity.kang.textprocessing.similarity.impl.UnorderedWordPatternTokenListSimilarity;

public class AttributesSimilarity implements IAttributesSimilarity {
	
	private IAttributeTracer tableRecordAttributeTracer;
	private ITokenListSimilarity wordListSimilarity;
	
	public AttributesSimilarity(IAttributeTracer tableRecordAttributeTracer) {
		this.tableRecordAttributeTracer = tableRecordAttributeTracer;
		this.wordListSimilarity = new UnorderedWordPatternTokenListSimilarity();
	}
	
	public void setWordSimilarity(ITokenListSimilarity wordSimilarity) {
		this.wordListSimilarity = wordSimilarity;
	}
	
	@Override
	public boolean hasTracedAttributes(ITagHolder taggedEntity) {
		String tagPath = taggedEntity.getTagPath();
		Set<String> attrsToBeTraced = tableRecordAttributeTracer.getAttributesToBeTraced();
		boolean hasTracedAttributes = false;
		for (String attr : attrsToBeTraced) {
			if (tableRecordAttributeTracer.isAttributeTraced(tagPath, attr)) {
				hasTracedAttributes = true;
				break;
			}
		}
		return hasTracedAttributes;
	}

	@Override
	public double computeSimilarity(
			ITagAttributeHolder taggedEntityAttrHolder1,
			ITagAttributeHolder taggedEntityAttrHolder2) {

		Set<String> attrsToBeTraced = tableRecordAttributeTracer.getAttributesToBeTraced();
		String tagPath = taggedEntityAttrHolder1.getTagPath();

		boolean isClassAttrHasSameValue = false;
		double sum = 0.0;
		int counter = 0;
		for (String attrName : attrsToBeTraced) {
			if (tableRecordAttributeTracer.isAttributeTraced(tagPath, attrName)) {
				counter++;
				double sim = computeAttributesSimilarity(taggedEntityAttrHolder1.getAttributeValue(attrName),
						taggedEntityAttrHolder2.getAttributeValue(attrName));
				// Check if the two attribute holders have the same value for
				// class attribute.
				if (attrName.equalsIgnoreCase("class") && sim == 1.0) {
					isClassAttrHasSameValue = true;
				}
				sum += sim;
			}
		}

		if (counter == 0)
			return sum;
		else {

			// If the two attribute holders have the same value of
			// class attribute, boost the total similarity.
			if (isClassAttrHasSameValue) {
				sum = sum + (1 - sum) * 0.2;
			}
			return sum / counter;
		}
	}
	
	private double computeAttributesSimilarity(String attributeValue1, String attributeValue2) {
		if (attributeValue1 != null && attributeValue2 != null) {
			return computeStringSimilarity(attributeValue1, attributeValue2);
		} else if (attributeValue1 == null && attributeValue2 == null) {
			return 1.0;
		} else {
			return 0.0;
		}
	}

	private double computeStringSimilarity(String string1, String string2) {
		String[] wordArray1 = string1.split(" ");
		String[] wordArray2 = string2.split(" ");
		return wordListSimilarity.computeSimilarity(Arrays.asList(wordArray1), Arrays.asList(wordArray2));
	}
}
