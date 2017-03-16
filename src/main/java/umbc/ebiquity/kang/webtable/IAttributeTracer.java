package umbc.ebiquity.kang.webtable;

import java.util.Set;

public interface IAttributeTracer {

	boolean isAttributeTraced(String tagPah, String attributeName);

	Set<String> getAttributesToBeTraced();  

}
