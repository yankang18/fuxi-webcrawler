package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp;

import java.util.ArrayList;
import java.util.List;

public class POSTaggedSentence implements ITaggedSentence { 

	private List<POSTaggedToken> taggedTokens;

	public POSTaggedSentence() {
		taggedTokens = new ArrayList<POSTaggedToken>();
	}

	public void addTaggedToken(POSTaggedToken token) {
		taggedTokens.add(token);
	}
	
	public void addTaggedToken(String token, String tag) {
		POSTaggedToken taggedToken = new POSTaggedToken(token, tag);
		taggedTokens.add(taggedToken);
	}
	
	@Override
	public List<POSTaggedToken> getTaggedToken() {
		return taggedTokens;
	}

}
