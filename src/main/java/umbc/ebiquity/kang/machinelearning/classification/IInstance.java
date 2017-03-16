package umbc.ebiquity.kang.machinelearning.classification;

import java.util.Set;

public interface IInstance {

	Set<String> getAttributeNames();

	IAttributeValue getAttributeValue(String attributeName); 

}
