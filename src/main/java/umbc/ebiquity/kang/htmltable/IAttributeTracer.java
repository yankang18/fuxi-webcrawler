package umbc.ebiquity.kang.htmltable;

import java.util.Set;

public interface IAttributeTracer {

	boolean isAttributeTraced(String tagPah, String attributeName);

	Set<String> getAttributesToBeTraced();  

}
