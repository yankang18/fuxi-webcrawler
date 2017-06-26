package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp;

import java.util.HashSet;
import java.util.Set;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IValueTypeResolver;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeNodeValue;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeNodeValue.ValueType;

public class SimpleValueTypeResolver implements IValueTypeResolver {
	
	private StanfordNLPAnnotator tagger = new StanfordNLPAnnotator();
	private static Set<String> copula;

	static {
		copula = new HashSet<String>();
		copula.add("am");
		copula.add("is");
		copula.add("are");
		copula.add("were");
		copula.add("was");
		copula.add("been");
		copula.add("be");
		copula.add("seem");
		copula.add("become");
	}

	@Override
	public ValueType resolve(String text) {
		String[] tokens = text.split(" ");

		if (tokens.length == 0)
			throw new IllegalArgumentException("The input should have at least one token");

		if (containsCopula(tokens))
			return ValueType.Paragraph;
		
		ITaggedText taggedText = annotate(text);

		// not necessary true
		if (hasMultiSentences(taggedText)) {
			return ValueType.Paragraph;
		}
		
		return ValueType.Term;
	}

	private ITaggedText annotate(String text) {
		return tagger.annotate(text);
	}

	private boolean hasMultiSentences(ITaggedText taggedText) {
		return taggedText.getTaggedSentences().size() > 1;
	}

	private boolean containsCopula(String[] tokens) {
		for (String token : tokens) {
			if (copula.contains(token)) {
				return true;
			}
		}
		return false;
	}

}
