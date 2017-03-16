package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import java.util.ArrayList;
import java.util.List;

public class POSTaggedTokens implements ITaggedText { 

	private List<POSTaggedToken> taggedTokens;
	private boolean hasSentences;

	public POSTaggedTokens() {
		taggedTokens = new ArrayList<POSTaggedToken>();
	}

	public void addTaggedToken(POSTaggedToken token) {
		taggedTokens.add(token);
	}
	
	public void addTaggedToken(String token, String tag) {
		POSTaggedToken taggedToken = new POSTaggedToken(token, tag);
		taggedTokens.add(taggedToken);
	}
	
	public void setHasSentence(boolean sentences){
		hasSentences = sentences;
	}

	@Override
	public List<POSTaggedToken> getTaggedToken() {
		return taggedTokens;
	}

	@Override
	public boolean hasSentences() {
		return hasSentences;
	}

}
