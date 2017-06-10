package umbc.ebiquity.kang.htmltable;

import java.util.Map;

public interface IAttributeHolder {

	String getAttributeValue(String attributeName);

	Map<String, String> getAttributes();
}
