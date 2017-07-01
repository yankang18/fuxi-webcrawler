package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp;

import java.util.List;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeNodeValue;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeNodeValue.ValueType;

public class StandardNumberTypeResolver {

	/**
	 * 
	 * @param tokens
	 * @return
	 */
	public ValueType resolve(List<POSTaggedToken> tokens) {

		if (tokens.size() == 1 && isNumber(tokens.get(0))) {
			return ValueType.Number;
			// TODO: May further resolve integer type and decimal type
		}

		for (int i = 0; i < tokens.size(); i++) {
			if (isVerb(tokens.get(i))) {
				return null;
			}
		}

		POSTaggedToken token = null;
		String unit = null;
		for (int i = 0; i < tokens.size(); i++) {
			token = tokens.get(i);
			if (isNumber(token)) {

				String prefixUnit = (i - 1) >= 0 ? tokens.get(i - 1).getValue().trim() : null;
				if (prefixUnit != null && isDefinedPrefixUnit(prefixUnit)) {
					unit = prefixUnit;
					break;
				}

				int windowSize = 2;
				int currentNumberIndex = i;
				for (; windowSize >= 1; windowSize--) {

					String suffixUnit = getSuffixUnit(tokens, currentNumberIndex, windowSize);
					if (suffixUnit != null) {

						//
						i = currentNumberIndex + windowSize;

						//
						if (isDefinedSufixUnit(suffixUnit)) {
							unit = suffixUnit;
							break;
						} else {
							unit = suffixUnit;
						}
					}
				}

			} else if (isNoun(token)) {
				for (int winSize = 1; winSize <= 2; winSize++) {
					int index = i - winSize;
					POSTaggedToken ancToken = index >= 0 ? tokens.get(index) : null;
					if (ancToken == null) {
						return null;
					} else if (isNumber(ancToken)) {
						break;
					} else if (winSize == 2) {
						return null;
					}
				}
			}
		}

		ValueType type = null;
		if (unit != null) {
			type = ValueType.NumberPhrase;
			type.setUnit(unit);
		}
		return type;
	}

	/**
	 * 
	 * @param tokens
	 * @param currentNumberIndex
	 * @param windowSize
	 * @return
	 */
	private String getSuffixUnit(List<POSTaggedToken> tokens, int currentNumberIndex, int windowSize) {
		StringBuilder suffixUnitBuilder = new StringBuilder();
		for (int i = currentNumberIndex + 1; i <= currentNumberIndex + windowSize; i++) {
			if (i >= tokens.size()) {
				return null;
			}
			POSTaggedToken token = tokens.get(i);
			if (isNoun(token)) {
				suffixUnitBuilder.append(token.getValue().trim() + " ");
			} else {
				return null;
			}
		}
		return suffixUnitBuilder.toString().trim();
	}

	private boolean isDefinedSufixUnit(String sufixUnit) {
		return UnitUtil.isUnit(sufixUnit);
	}

	private boolean isDefinedPrefixUnit(String prefixUnit) {
		return UnitUtil.isCurrencyUnit(prefixUnit);
	}

	private boolean isVerb(POSTaggedToken token) {
		return POSTagUtil.isVerb(token.getPOSTag());
	}

	private boolean isNumber(POSTaggedToken token) {
		return POSTagUtil.isNumber(token.getPOSTag());
	}

	private boolean isNoun(POSTaggedToken token) {
		return POSTagUtil.isNoun(token.getPOSTag());
	}
}
