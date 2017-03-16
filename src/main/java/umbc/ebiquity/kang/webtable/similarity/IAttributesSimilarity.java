package umbc.ebiquity.kang.webtable.similarity;

import umbc.ebiquity.kang.webtable.ITagAttributeHolder;
import umbc.ebiquity.kang.webtable.ITagHolder;

public interface IAttributesSimilarity {

	boolean hasTracedAttributes(ITagHolder tagHolder);

	double computeSimilarity(ITagAttributeHolder taggedEntityAttrHolder1, ITagAttributeHolder taggedEntityAttrHolder2);

}
