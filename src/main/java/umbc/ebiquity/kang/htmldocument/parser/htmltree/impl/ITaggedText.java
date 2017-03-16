package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import java.util.List;

public interface ITaggedText {

	List<POSTaggedToken> getTaggedToken(); 
	
	boolean hasSentences();

}
