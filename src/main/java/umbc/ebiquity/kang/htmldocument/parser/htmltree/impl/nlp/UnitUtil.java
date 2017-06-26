package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp;

import java.util.HashSet;
import java.util.Set;

/**
 * Imperial and US customary measurement systems,
 * https://en.wikipedia.org/wiki/Imperial_and_US_customary_measurement_systems
 * 
 * @author yankang
 *
 */
public class UnitUtil {

	private static Set<String> USA_UNITS;
	private static Set<String> ELECTRONICS_UNITS;
	private static Set<String> CURRENCY_UNITS;
	
	static {
		USA_UNITS = new HashSet<>();
		
		// linear measure
		USA_UNITS.add("inch");
		USA_UNITS.add("in");
		USA_UNITS.add("\"");
		USA_UNITS.add("foot");
		USA_UNITS.add("ft");
		USA_UNITS.add("\'");
		USA_UNITS.add("yard");
		USA_UNITS.add("yd");
		USA_UNITS.add("mile");
		USA_UNITS.add("mi");
		
		// linear measure (UK)
		USA_UNITS.add("chain");
		USA_UNITS.add("furlong");
		
		// survey measure (US only)
		USA_UNITS.add("link");
		USA_UNITS.add("li");
		USA_UNITS.add("rod");
		USA_UNITS.add("pole");
		USA_UNITS.add("perch");
		USA_UNITS.add("rd");
		USA_UNITS.add("chain");
		USA_UNITS.add("ch");
		USA_UNITS.add("furlong");
		USA_UNITS.add("fur");
		
		// units of area
		USA_UNITS.add("square foot");
		USA_UNITS.add("square yard");
		USA_UNITS.add("sq yd");
		USA_UNITS.add("rood");
		USA_UNITS.add("acre");
		USA_UNITS.add("square mile");
		
		// land area in the US
		USA_UNITS.add("square rod");
		USA_UNITS.add("sq rd");
		USA_UNITS.add("square mile");
		USA_UNITS.add("sq mi");
		USA_UNITS.add("township");
		
		// dry volume
		USA_UNITS.add("cubic inch");
		USA_UNITS.add("cu in");
		USA_UNITS.add("cubic foot");
		USA_UNITS.add("cu ft");
		USA_UNITS.add("Cubic yard");
		USA_UNITS.add("cu yd");
		
		// dry volume (US)
		USA_UNITS.add("dry pint");
		USA_UNITS.add("pt");
		USA_UNITS.add("dry quart");
		USA_UNITS.add("qt");
		USA_UNITS.add("dry peck");
		USA_UNITS.add("pk");
		USA_UNITS.add("bushel");
		USA_UNITS.add("bu");
		
		// Volume of liquids
		
		// capacity (UK)
		USA_UNITS.add("bushell");
		USA_UNITS.add("peck");
		USA_UNITS.add("gallon");
		USA_UNITS.add("gal");
		USA_UNITS.add("Quart");
		USA_UNITS.add("qt");
		
		USA_UNITS.add("pint");
		USA_UNITS.add("pt");
		USA_UNITS.add("gill");
		USA_UNITS.add("fluid ounce");
		USA_UNITS.add("fl oz");
		USA_UNITS.add("fluid drachm");
		USA_UNITS.add("fluid dram");
		USA_UNITS.add("minim");
		
		// units of weight
		
		// Avoirdupois Weight (General use)
		USA_UNITS.add("grain");
		USA_UNITS.add("gr");
		USA_UNITS.add("dram");
		USA_UNITS.add("drachm");
		USA_UNITS.add("dr");
		
		USA_UNITS.add("ounce");
		USA_UNITS.add("oz");
		USA_UNITS.add("pound");
		USA_UNITS.add("lbs");
		USA_UNITS.add("lb");
		
		USA_UNITS.add("stone");
		USA_UNITS.add("st");
		USA_UNITS.add("quarter");
		USA_UNITS.add("cental");
		USA_UNITS.add("hundredweight");
		USA_UNITS.add("cwt");
		USA_UNITS.add("ton");
		USA_UNITS.add("tons");
		
		// Troy Weight (Used for precious metals)
		USA_UNITS.add("grain");
		USA_UNITS.add("gr");
		USA_UNITS.add("pennyweight");
		USA_UNITS.add("dwt");
		USA_UNITS.add("ounce troy");
		USA_UNITS.add("oz t");
		USA_UNITS.add("pound troy");
		USA_UNITS.add("lb t");
		
		// Apothecaries Weight (Used in pharmacy)
		USA_UNITS.add("scruple");
		
		// Energy, power and temperature
		USA_UNITS.add("poundal");
		USA_UNITS.add("pdl");
		USA_UNITS.add("pound force");
		USA_UNITS.add("lbf");
		USA_UNITS.add("slug");
		USA_UNITS.add("horsepower");
		USA_UNITS.add("hp");
		USA_UNITS.add("degree ");
		USA_UNITS.add("fahrenheit");
		USA_UNITS.add("degree fahrenheit");
		USA_UNITS.add("degree rankine");
		USA_UNITS.add("°F");
		USA_UNITS.add("rankine");
		USA_UNITS.add("degree rankine");
		USA_UNITS.add("°R");

		ELECTRONICS_UNITS = new HashSet<>();
		ELECTRONICS_UNITS.add("gb");
		ELECTRONICS_UNITS.add("ghz");
		ELECTRONICS_UNITS.add("bgn");
		ELECTRONICS_UNITS.add("pixel");
		ELECTRONICS_UNITS.add("pixels");

		CURRENCY_UNITS = new HashSet<>();
		CURRENCY_UNITS.add("$");
	}

	public static boolean isUnit(String input) {
		input = normalize(input);
		return USA_UNITS.contains(input) || ELECTRONICS_UNITS.contains(input) || CURRENCY_UNITS.contains(input);
	}

	public static boolean isCurrencyUnit(String input) {
		input = normalize(input);
		return CURRENCY_UNITS.contains(input);
	}
	
	private static String normalize(String input) {
		return input.trim().toLowerCase();
	}

}
