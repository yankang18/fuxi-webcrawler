package umbc.ebiquity.kang.websiteparser.tableresolver;

import java.util.Set;

public interface IAttributeTracer {

	boolean isAttributeTraced(String tagPah, String attributeName);

	Set<String> getAttributesToBeTraced();  

}
