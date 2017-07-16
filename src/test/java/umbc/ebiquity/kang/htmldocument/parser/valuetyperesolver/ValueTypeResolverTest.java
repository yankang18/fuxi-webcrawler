package umbc.ebiquity.kang.htmldocument.parser.valuetyperesolver;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.StandardValueTypeResolver;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.ValueType;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.ValueTypeInfo;

public class ValueTypeResolverTest {

	private StandardValueTypeResolver valueTypeResolver = new StandardValueTypeResolver();

	@Test
	public void testNumber() {
		String text = "8.88";
		ValueTypeInfo type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.Number, type.getValueType());
	}

	@Test
	public void testNumberPhrase() {
		String text = "8.88 inch";
		ValueTypeInfo type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.NumberPhrase, type.getValueType());
		assertEquals("inch", type.getUnit());

		String text2 = "8.88 in";
		ValueTypeInfo type2 = valueTypeResolver.resolve(text2);
		assertEquals(ValueType.NumberPhrase, type2.getValueType());
		assertEquals("in", type2.getUnit());
		
		String text3 = "$8.88";
		ValueTypeInfo type3 = valueTypeResolver.resolve(text3);
		assertEquals(ValueType.NumberPhrase, type3.getValueType());
		assertEquals("$", type3.getUnit());
		
		String text4 = "8.88'";
		ValueTypeInfo type4 = valueTypeResolver.resolve(text4);
		assertEquals(ValueType.NumberPhrase, type4.getValueType());
		assertEquals("inch", type4.getUnit());
		
		String text5 = "8.88\"";
		ValueTypeInfo type5 = valueTypeResolver.resolve(text5);
		assertEquals(ValueType.NumberPhrase, type5.getValueType());
		assertEquals("inch", type4.getUnit());
	}
	
	@Test
	public void testNumberPhraseWithTwoNumbers() {
		// TODO 1366x768 inch does not work for now, may fix this
		String text = "1366 x 768 inch";
		ValueTypeInfo type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.NumberPhrase, type.getValueType());
		assertEquals("inch", type.getUnit());
	}

	@Test
	public void testNumberPhraseWithThreeNumbers() {
		String text = "1366 x 768 x 768 inch";
		ValueTypeInfo type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.NumberPhrase, type.getValueType());
		assertEquals("inch", type.getUnit());
	}

	@Test
	public void testNumberPhraseWithParenthesis() {
		String text = "1366 x 768 x 768 inch (1366 x 768 x 768 foot)";
		ValueTypeInfo type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.NumberPhrase, type.getValueType());
		assertEquals("inch", type.getUnit());
	}

	@Test
	public void testHythenConnectedNumberPhrase() {
		String text = "1366-yard";
		ValueTypeInfo type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.NumberPhrase, type.getValueType());
		assertEquals("yard", type.getUnit());
	}

	@Test
	public void testToConnectedNumberPhrase() {
		String text = "1366 to 1388.8 yard";
		ValueTypeInfo type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.NumberPhrase, type.getValueType());
		assertEquals("yard", type.getUnit());

		text = "1366 yard to 1388.8 yard";
		type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.NumberPhrase, type.getValueType());
		assertEquals("yard", type.getUnit());
	}

	@Test
	public void testTerm() {
		String text = "Window 10";
		ValueTypeInfo type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.Term, type.getValueType());

		text = "laser cutting";
		type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.Term, type.getValueType());
	}

	@Test
	public void testMultipleSentences() {
		String text = "This test is for text of multiple sentences. "
				+ "We expect that the returned value type is paragraph.";
		ValueTypeInfo type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.Paragraph, type.getValueType());
	}
	
	@Test
	public void testTerm2() {
		String text = "Plastic";
		ValueTypeInfo type = valueTypeResolver.resolve(text);
		assertEquals(ValueType.Term, type.getValueType());
	}

	// TODO: Add exception tests

}
