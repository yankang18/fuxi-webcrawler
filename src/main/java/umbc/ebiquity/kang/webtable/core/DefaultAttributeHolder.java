package umbc.ebiquity.kang.webtable.core;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.webtable.IAttributeHolder;

public class DefaultAttributeHolder implements IAttributeHolder {

	private Element elem;
	private Map<String, String> attributes;
	private static Set<String> exposedAttrNameSet;

	static {
		exposedAttrNameSet = new HashSet<String>();
		exposedAttrNameSet.add("class");
		exposedAttrNameSet.add("style");
		exposedAttrNameSet.add("valign");
		exposedAttrNameSet.add("align");
	}

	private static boolean isExposedAttribute(String attributeName) {
		return exposedAttrNameSet.contains(attributeName.trim().toLowerCase());
	}

	public DefaultAttributeHolder(Element element) {
		elem = element;
		attributes = new LinkedHashMap<String, String>();
		for (Attribute attribute : elem.attributes()) {
			String key = attribute.getKey();
			if (isExposedAttribute(key)) {
				String value = attribute.getValue();
				if (isNotEmpty(value)) {
					attributes.put(attribute.getKey(), value);
				}
			}
		}
	}

	public DefaultAttributeHolder() {
		attributes = new LinkedHashMap<String, String>();
	}

	private boolean isNotEmpty(String value) {
		return !"".equals(value.trim());
	}

	@Override
	public String getAttributeValue(String attributeName) {
		return this.attributes.get(attributeName);
	}

	@Override
	public Map<String, String> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

}
