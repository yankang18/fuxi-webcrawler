package umbc.ebiquity.kang.websiteparser.tableresolver;

public interface IAttributesSimilarity {

	boolean hasTracedAttributes(ITaggedEntity taggedEntity);

	double computeSimilarity(ITaggedEntityAttributeHolder taggedEntityAttrHolder1, 
			ITaggedEntityAttributeHolder taggedEntityAttrHolder2); 

}
