package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class POSTagUtil {

	private static Set<String> NounTags;
	private static Set<String> VerbTags;
	private static Set<String> NumberTags;

	private static Set<String> ConjuncTags;

	private static Set<String> ToTags;
	private static Set<String> SymbolTags;

	static {

		SymbolTags = new HashSet<>();
		SymbolTags.add("$");
		SymbolTags.add("#");
		SymbolTags.add(".");
		SymbolTags.add(",");
		SymbolTags.add(":");
		SymbolTags.add("''");

		ConjuncTags = new HashSet<>();
		ConjuncTags.add("CC");

		ToTags = new HashSet<>();
		ToTags.add("TO");

		NounTags = new HashSet<>();
		NounTags.add("NN");
		NounTags.add("NNS");
		NounTags.add("NNP");
		NounTags.add("NNPS");

		VerbTags = new HashSet<>();
		VerbTags.add("VB");
		VerbTags.add("VBD");
		VerbTags.add("VBG");
		VerbTags.add("VBN");
		VerbTags.add("VBP");
		VerbTags.add("VBZ");

		NumberTags = new HashSet<>();
		NumberTags.add("CD");
	}

	public static boolean isNoun(String posTag) {
		return NounTags.contains(posTag.trim().toUpperCase());
	}

	public static boolean isVerb(String posTag) {
		return VerbTags.contains(posTag.trim().toUpperCase());
	}

	public static boolean isNumber(String posTag) {
		return NumberTags.contains(posTag.trim().toUpperCase());
	}

	/**
	 * @return the nounTags
	 */
	public static Set<String> getNounTags() {
		return toUnmodifiableSet(NounTags);
	}
	
	/**
	 * @return the verbTags
	 */
	public static Set<String> getVerbTags() {
		return toUnmodifiableSet(VerbTags);
	}

	/**
	 * @return the numberTags
	 */
	public static Set<String> getNumberTags() {
		return toUnmodifiableSet(NumberTags);
	}

	/**
	 * @return the conjuncTags
	 */
	public static Set<String> getConjuncTags() {
		return toUnmodifiableSet(ConjuncTags);
	}

	/**
	 * @return the toTags
	 */
	public static Set<String> getToTags() {
		return toUnmodifiableSet(ToTags);
	}

	/**
	 * @return the symbolTags
	 */
	public static Set<String> getSymbolTags() {
		return toUnmodifiableSet(SymbolTags);
	}
	
	private static Set<String> toUnmodifiableSet(Set<String> set){
		return Collections.unmodifiableSet(set);
	}
}
