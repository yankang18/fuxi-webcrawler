package umbc.ebiquity.kang.htmltable.similarity;

import umbc.ebiquity.kang.htmltable.ITagAttributeHolder;
import umbc.ebiquity.kang.htmltable.ITagHolder;

public interface IAttributesSimilarity {

	boolean hasTracedAttributes(ITagHolder tagHolder);

	double computeSimilarity(ITagAttributeHolder taggedEntityAttrHolder1, ITagAttributeHolder taggedEntityAttrHolder2);

}
