package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp;

import java.util.ArrayList;
import java.util.List;

public class POSTaggedText implements ITaggedText {

	private List<ITaggedSentence> taggedSentences = new ArrayList<>();

	@Override
	public List<ITaggedSentence> getTaggedSentences() {
		return taggedSentences;
	}

	public void addTaggedSentences(ITaggedSentence taggedSentence) {
		taggedSentences.add(taggedSentence);
	}

}
