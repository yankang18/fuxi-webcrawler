package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import umbc.ebiquity.kang.websiteparser.support.ITemplateNodeMatcher;
import umbc.ebiquity.kang.webtable.ITagAttributeHolder;

public class SimpleTemplateNodeMatcher implements ITemplateNodeMatcher {

	private Map<String, Set<String>> attributeMap;
	private Set<String> tags;

	public SimpleTemplateNodeMatcher() {
		attributeMap = new HashMap<String, Set<String>>();
		tags = new HashSet<String>();
	}

	public void addTag(String tagName){
		tags.add(normalize(tagName));
	}
	
	public void addAttribute(String key, String value) {
		String nKey = normalize(key);
		String nValue = normalize(value);
		if (attributeMap.containsKey(nKey)) {
			attributeMap.get(nKey).add(nValue);
		} else {
			Set<String> values = new HashSet<String>();
			values.add(nValue);
			attributeMap.put(nKey, values);
		}
	}

	@Override
	public boolean isMatched(ITagAttributeHolder tagAttributeHolder) {
		String tagName = tagAttributeHolder.getTagName();
		Map<String, String> attr2Val = tagAttributeHolder.getAttributes();
		if (tags.contains(normalize(tagName)))
			return true;

		for (String key : attr2Val.keySet()) {
			String sourceValue = attr2Val.get(key);
			Set<String> targetValues = attributeMap.get(normalize(key));
			
			// TODO: more sophisticated process on the source value is needed
			String[] tokens = extractAtomValues(sourceValue);
			for (String token : tokens) {
				if (targetValues != null && targetValues.contains(normalize(token))) {
					return true;
				}
			}

		}
		return false;
	}

	private String[] extractAtomValues(String sourceValue) {
		return sourceValue.split(" ");
	}
	
	private String normalize(String tagName) {
		return tagName.trim().toLowerCase();
	}

}
