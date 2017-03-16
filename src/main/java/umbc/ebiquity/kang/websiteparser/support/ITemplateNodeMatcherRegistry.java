package umbc.ebiquity.kang.websiteparser.support;

public interface ITemplateNodeMatcherRegistry {

	public void register(String url, ITemplateNodeMatcher matcher);

	ITemplateNodeMatcher getMatcher(String url); 
}
