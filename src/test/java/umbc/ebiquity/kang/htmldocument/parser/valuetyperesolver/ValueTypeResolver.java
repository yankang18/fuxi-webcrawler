package umbc.ebiquity.kang.htmldocument.parser.valuetyperesolver;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeNodeValue.ValueType;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.StandardValueTypeResolver;

public class ValueTypeResolver {

	private StandardValueTypeResolver valueTypeResolver = new StandardValueTypeResolver();

	@Test
	public void testNumber() {
		String text = "8.88";
		ValueType type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.Number, type);
	}

	@Test
	public void testNumberPhrase() {
		String text = "8.88 inch";
		ValueType type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.NumberPhrase, type);
		assertEquals("inch", type.getUnit());

		String text2 = "8.88 in";
		ValueType type2 = valueTypeResolver.resolve(text2);
		assertEquals(ValueType.NumberPhrase, type2);
		assertEquals("in", type2.getUnit());
		
		String text3 = "$8.88";
		ValueType type3 = valueTypeResolver.resolve(text3);
		assertEquals(ValueType.NumberPhrase, type3);
		assertEquals("$", type3.getUnit());
		
		String text4 = "8.88'";
		ValueType type4 = valueTypeResolver.resolve(text4);
		assertEquals(ValueType.NumberPhrase, type4);
		assertEquals("inch", type4.getUnit());
		
		String text5 = "8.88\"";
		ValueType type5 = valueTypeResolver.resolve(text5);
		assertEquals(ValueType.NumberPhrase, type5);
		assertEquals("inch", type4.getUnit());
	}
	
	@Test
	public void testNumberPhraseWithTwoNumbers() {
		// TODO 1366x768 inch does not work for now, may fix this
		String text = "1366 x 768 inch";
		ValueType type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.NumberPhrase, type);
		assertEquals("inch", type.getUnit());
	}

	@Test
	public void testNumberPhraseWithThreeNumbers() {
		String text = "1366 x 768 x 768 inch";
		ValueType type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.NumberPhrase, type);
		assertEquals("inch", type.getUnit());
	}

	@Test
	public void testNumberPhraseWithParenthesis() {
		String text = "1366 x 768 x 768 inch (1366 x 768 x 768 foot)";
		ValueType type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.NumberPhrase, type);
		assertEquals("inch", type.getUnit());
	}

	@Test
	public void testHythenConnectedNumberPhrase() {
		String text = "1366-yard";
		ValueType type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.NumberPhrase, type);
		assertEquals("yard", type.getUnit());
	}

	@Test
	public void testToConnectedNumberPhrase() {
		String text = "1366 to 1388.8 yard";
		ValueType type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.NumberPhrase, type);
		assertEquals("yard", type.getUnit());

		text = "1366 yard to 1388.8 yard";
		type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.NumberPhrase, type);
		assertEquals("yard", type.getUnit());
	}

	@Test
	public void testTerm() {
		String text = "Window 10";
		ValueType type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.Term, type);

		text = "laser cutting";
		type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.Term, type);
	}

	@Test
	public void testMultipleSentences() {
		String text = "This test is for text of multiple sentences. "
				+ "We expect that the returned value type is paragraph.";
		ValueType type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.Paragraph, type);
	}

	// TODO: Add exception tests

}
