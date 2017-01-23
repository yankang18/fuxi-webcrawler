package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import java.util.Arrays;
import java.util.Set;

import umbc.ebiquity.kang.textprocessing.similarity.IWordListSimilarity;
import umbc.ebiquity.kang.textprocessing.similarity.impl.UnorderedWordPatternWordListSimilarity;
import umbc.ebiquity.kang.websiteparser.tableresolver.IAttributeTracer;
import umbc.ebiquity.kang.websiteparser.tableresolver.IAttributesSimilarity;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITaggedEntity;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITaggedEntityAttributeHolder;

public class AttributesSimilarity implements IAttributesSimilarity {
	
	private IAttributeTracer tableRecordAttributeTracer;
	private IWordListSimilarity wordListSimilarity;
	
	public AttributesSimilarity(IAttributeTracer tableRecordAttributeTracer) {
		this.tableRecordAttributeTracer = tableRecordAttributeTracer;
		this.wordListSimilarity = new UnorderedWordPatternWordListSimilarity();
	}
	
	public void setWordSimilarity(IWordListSimilarity wordSimilarity) {
		this.wordListSimilarity = wordSimilarity;
	}
	
	@Override
	public boolean hasTracedAttributes(ITaggedEntity taggedEntity) {
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
	public double computeSimilarity(ITaggedEntityAttributeHolder taggedEntityAttrHolder1,
											  ITaggedEntityAttributeHolder taggedEntityAttrHolder2) {

		Set<String> attrsToBeTraced = tableRecordAttributeTracer.getAttributesToBeTraced();
		String tagPath = taggedEntityAttrHolder1.getTagPath();

		double sum = 0.0;
		int counter = 0;
		for (String attr : attrsToBeTraced) {
			if (tableRecordAttributeTracer.isAttributeTraced(tagPath, attr)) {
				counter++;
				sum += computeAttributesSimilarity(taggedEntityAttrHolder1.getAttributeValue(attr),
						taggedEntityAttrHolder2.getAttributeValue(attr));
			}
		}

		if (counter == 0)
			return sum;
		else
			return sum / counter;
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
