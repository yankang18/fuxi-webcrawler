package umbc.ebiquity.kang.htmltable.translator.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TablePropertyDictionary {
	
	private static Set<String> DICTIONARY;

	static {

		DICTIONARY = new HashSet<>();

		// General properties
		DICTIONARY.add("Length");
		DICTIONARY.add("Width");
		DICTIONARY.add("Height");
		DICTIONARY.add("Size");
		DICTIONARY.add("Type");
		DICTIONARY.add("Price");
		DICTIONARY.add("Ratio");
		DICTIONARY.add("Brand");
		DICTIONARY.add("Logo");

		//
		DICTIONARY.add("Rating");
		DICTIONARY.add("Rate");
		DICTIONARY.add("Ratio");
		DICTIONARY.add("Dimension");
		DICTIONARY.add("Resolution");

		//
		DICTIONARY.add("Thickness");
		DICTIONARY.add("Diameter");
		DICTIONARY.add("Capacity");
		DICTIONARY.add("Tolerance");
		DICTIONARY.add("Services");
		DICTIONARY.add("Inspection");
		DICTIONARY.add("Product");
		DICTIONARY.add("Volume");
		DICTIONARY.add("Time");
		DICTIONARY.add("Process");
		DICTIONARY.add("Axis");
		DICTIONARY.add("Center");
		DICTIONARY.add("Feeding");
		DICTIONARY.add("Feed");
		DICTIONARY.add("Material");
	}
	
	public static Set<String> getDictionary(){
		return Collections.unmodifiableSet(DICTIONARY);
	}
}
