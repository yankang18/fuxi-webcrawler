package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.HashMap;
import java.util.Map;

import umbc.ebiquity.kang.websiteparser.support.ITemplateNodeMatcher;
import umbc.ebiquity.kang.websiteparser.support.ITemplateNodeMatcherRegistry;

public class TemplateNodeMatcherRegistry implements ITemplateNodeMatcherRegistry{
	private Map<String, ITemplateNodeMatcher> url2matcher;

	public TemplateNodeMatcherRegistry() {
		url2matcher = new HashMap<String, ITemplateNodeMatcher>();
	}
	
	@Override
	public void register(String matcherUniqueName, ITemplateNodeMatcher matcher) {
		url2matcher.put(matcherUniqueName, matcher);
	}
	
	@Override
	public ITemplateNodeMatcher getMatcher(String matcherUniqueName){
		return url2matcher.get(matcherUniqueName);
	}

}
