package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.HashSet;
import java.util.Set;

import umbc.ebiquity.kang.websiteparser.support.IValueTypeResolver;
import umbc.ebiquity.kang.websiteparser.support.impl.Value.ValueType;

public class NaiveValueTypeResolver implements IValueTypeResolver {

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
	}

	@Override
	public ValueType resolve(String value) {

		String[] tokens = value.split(" ");

		if (tokens.length == 0)
			throw new IllegalArgumentException("The input should have at least one token");

		if (containsCopula(tokens))
			return ValueType.Paragraph;

		if (tokens.length <= 6)
			return ValueType.Term;
		else
			return ValueType.Paragraph;

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
