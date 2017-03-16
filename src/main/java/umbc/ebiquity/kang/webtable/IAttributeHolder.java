package umbc.ebiquity.kang.webtable;

import java.util.Map;

public interface IAttributeHolder {

	String getAttributeValue(String attributeName);

	Map<String, String> getAttributes();
}
