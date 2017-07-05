package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IValueTypeResolver;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.ValueType;

public class StandardValueTypeResolver implements IValueTypeResolver {

	private static Set<String> toKeepTags;
	private static Set<String> splittingTags;
	private StanfordNLPAnnotator tagger;
	private StandardNumberTypeResolver numberTypeResolver;

	public StandardValueTypeResolver() {
		tagger = new StanfordNLPAnnotator();
		numberTypeResolver = new StandardNumberTypeResolver();
	}

	static {

		toKeepTags = new HashSet<>();
		toKeepTags.addAll(POSTagUtil.getConjuncTags());
		toKeepTags.addAll(POSTagUtil.getNumberTags());
		toKeepTags.addAll(POSTagUtil.getNounTags());
		toKeepTags.addAll(POSTagUtil.getVerbTags());
		toKeepTags.addAll(POSTagUtil.getToTags());
		toKeepTags.addAll(POSTagUtil.getSymbolUnitTags());

		splittingTags = new HashSet<>();
		splittingTags.addAll(POSTagUtil.getConjuncTags());
		splittingTags.addAll(POSTagUtil.getToTags());
	}

	@Override
	public ValueTypeInfo resolve(String text) {

		if (text == null || text.trim().isEmpty())
			throw new IllegalArgumentException("The input text should not be empty");

		text = text.trim();
		
		/*
		 * Step (1): pre-annotation Step (2): annotation Step (3):
		 * post-annotation Step (4): pre-analyzing Step (5): analyzing
		 */
		text = preAnnotationProcess(text);
		
		ITaggedText taggedText = annotate(text);

		if (hasMultiSentences(taggedText)) {
			return ValueTypeInfo.createValueTypeInfo(ValueType.Paragraph, null);
		}

		List<POSTaggedToken> taggedTokens = preAnalyzingProcess(
				taggedText.getTaggedSentences().get(0).getTaggedTokens());

		return analyze(taggedTokens);
	}

	private String preAnnotationProcess(String text) {
		return text.replaceAll("\\(.*\\)", "");
	}

	private ITaggedText annotate(String text) {
		return tagger.annotate(text);
	}

	private List<POSTaggedToken> preAnalyzingProcess(List<POSTaggedToken> taggedTokens) {

		// This an ad-hoc process
		List<POSTaggedToken> tokens = new ArrayList<>();
		if (taggedTokens.size() == 1) {
			POSTaggedToken token = taggedTokens.get(0);
			if (token.getPOSTag().equalsIgnoreCase("JJ")) {
				String[] newTokens = token.getValue().split("-");
				if (newTokens.length == 2) {
					taggedTokens = annotate(newTokens[0] + " " + newTokens[1]).getTaggedSentences().get(0)
							.getTaggedTokens();
				}
			}
		}

		// 
		POSTaggedToken preToken = null;
		for (POSTaggedToken token : taggedTokens) {
			if (toKeep(token) || isNumber(preToken)) {
				tokens.add(token);
			}
			preToken = token;
		}
		return tokens;
	}

	private ValueTypeInfo analyze(List<POSTaggedToken> taggedTokens) {
		
		List<List<POSTaggedToken>> tokensList = new ArrayList<List<POSTaggedToken>>();
		List<POSTaggedToken> tokens = new ArrayList<>();
		tokensList.add(tokens);

		for (POSTaggedToken token : taggedTokens) {
			if (isSplittingToken(token)) {
				tokens = new ArrayList<>();
				tokensList.add(tokens);
			} else {
				tokens.add(token);
			}
		}

		boolean hasToken = false;
		boolean isNumberToken = true;
		ValueTypeInfo valueType = null;
		for (List<POSTaggedToken> list : tokensList) {
			if (list.size() != 0) {
				hasToken = true;
				ValueTypeInfo type = numberTypeResolver.resolve(list);
				if (type == null) {
					isNumberToken = false;
					break;
				} else {
					valueType = type;
				}
			}
		}

		if (!hasToken) {
			return null;
		} else if (isNumberToken) {
			return valueType;
		} else {
			return ValueTypeInfo.createValueTypeInfo(ValueType.Term, null);
		}
	}

	private boolean isNumber(POSTaggedToken token) {
		return token != null && POSTagUtil.isNumber(token.getPOSTag());
	}

	private boolean toKeep(POSTaggedToken token) {
		return token != null && toKeepTags.contains(token.getPOSTag().trim().toUpperCase());
	}

	private boolean isSplittingToken(POSTaggedToken token) {
		return token != null && splittingTags.contains(token.getPOSTag().trim().toUpperCase());
	}

	private boolean hasMultiSentences(ITaggedText taggedText) {
		return taggedText.getTaggedSentences().size() > 1;
	}
}
