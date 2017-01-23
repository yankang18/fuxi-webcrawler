package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.websiteparser.tableresolver.IAttributeHolder;

public class SimpleAttributeHolder implements IAttributeHolder {

	private Element elem;
	private Map<String, String> attributes;
	
	public SimpleAttributeHolder(Element element) {
		elem = element;
		attributes = new LinkedHashMap<String, String>();
		for (Attribute attribute : elem.attributes()) {
			String key = attribute.getKey();
			if (key.equalsIgnoreCase("class") || key.equalsIgnoreCase("style")) {
				String value = attribute.getValue();
				if (isNotEmpty(value)) {
					attributes.put(attribute.getKey(), value);
				}
			}
		}
	}
	
	public SimpleAttributeHolder() {
		attributes = new LinkedHashMap<String, String>();
	}

	private boolean isNotEmpty(String value) {
		return !"".equals(value.trim());
	}

	@Override
	public String getAttributeValue(String attributeName) {
		return this.attributes.get(attributeName);
	}

}
