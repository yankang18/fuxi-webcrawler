package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import java.util.HashSet;
import java.util.Set;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IValueTypeResolver;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeNodeValue.ValueType;

public class StandardValueTypeResolver implements IValueTypeResolver {

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

		StanfordPOStagger tagger = new StanfordPOStagger();
		ITaggedText taggedText = tagger.annotate(text);

		// not necessary true
		if (hasSentences(taggedText)) {
			return ValueType.Paragraph;
		}
		return ValueType.Term;
	}

	private boolean hasSentences(ITaggedText taggedText) { 
		return taggedText.hasSentences();
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
