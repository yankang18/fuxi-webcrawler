package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp;

import java.util.List;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.ValueType;

public class StandardNumberTypeResolver {

	/**
	 * 
	 * @param tokens
	 * @return ValueTypeInfo, can be null if the input tokens can not be
	 *         resolved to a type
	 */
	public ValueTypeInfo resolve(List<POSTaggedToken> tokens) {

		if (tokens.size() == 1 && isNumber(tokens.get(0))) {
			return ValueTypeInfo.createValueTypeInfo(ValueType.Number, null);
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

					String[] result = getSuffixUnit(tokens, currentNumberIndex, windowSize);
					String suffixUnit = result[0];
					String suffixPosTag = result[1];
					if(isNoun(suffixPosTag) || isDefinedSufixUnit(suffixUnit)) {

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

		ValueTypeInfo type = null;
		if (unit != null) {
			type = ValueTypeInfo.createValueTypeInfo(ValueType.NumberPhrase, unit);
		}
		return type;
	}

	/**
	 * 
	 * @param tokens
	 * @param currentNumberIndex
	 * @param windowSize
	 * @return an array of string with two elements: the element at position 0
	 *         is the suffix unit and the element at position 1 is the POS tag.
	 *         Both element cannot be null but can be empty string is suffix
	 *         unit or POS tag does not exists
	 */
	private String[] getSuffixUnit(List<POSTaggedToken> tokens, int currentNumberIndex, int windowSize) {
		String[] result = { "", "" };
		StringBuilder suffixUnitBuilder = new StringBuilder();
		for (int i = currentNumberIndex + 1; i <= currentNumberIndex + windowSize; i++) {
			if (i >= tokens.size()) {
				return result;
			}
			String value = tokens.get(i).getValue().trim();
			String stdUnit = UnitUtil.toStandard(value);
			if (stdUnit == null) {
				suffixUnitBuilder.append(value + " ");
			} else {
				suffixUnitBuilder.append(stdUnit + " ");
			}
		}
		result[0] = suffixUnitBuilder.toString().trim();
		result[1] = currentNumberIndex + 1 < tokens.size() ? tokens.get(currentNumberIndex + 1).getPOSTag() : "";
		return result;
	}

	private boolean isDefinedSufixUnit(String sufixUnit) {
		return UnitUtil.isUnit(sufixUnit);
	}

	private boolean isDefinedPrefixUnit(String prefixUnit) {
		return UnitUtil.isCurrencyUnit(prefixUnit) || UnitUtil.isNumberUnit(prefixUnit);
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
	
	private boolean isNoun(String posTagName) {
		return POSTagUtil.isNoun(posTagName);
	}
}
