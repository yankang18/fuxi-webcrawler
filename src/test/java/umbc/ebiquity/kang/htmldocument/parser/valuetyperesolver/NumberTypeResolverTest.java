package umbc.ebiquity.kang.htmldocument.parser.valuetyperesolver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeNodeValue.ValueType;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.POSTaggedToken;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.StandardNumberTypeResolver;

public class NumberTypeResolverTest {

	private StandardNumberTypeResolver numberTypeResolver = new StandardNumberTypeResolver();

	@Test
	public void testNumber() {
		List<POSTaggedToken> tokens = new ArrayList<>();
		POSTaggedToken taggedToken = new POSTaggedToken("88.8", "CD");
		tokens.add(taggedToken);
		ValueType type = numberTypeResolver.resolve(tokens);
		assertEquals(ValueType.Number, type);
	}

	@Test
	public void testNumberPhraseWithOneWordOfSuffixUnitType() {
		List<POSTaggedToken> tokens = new ArrayList<>();
		POSTaggedToken taggedToken1 = new POSTaggedToken("88.8", "CD");
		POSTaggedToken taggedToken2 = new POSTaggedToken("inch", "NN");

		// This third token should have no effects on the outcome
		POSTaggedToken taggedToken3 = new POSTaggedToken("random", "NN");
		tokens.add(taggedToken1);
		tokens.add(taggedToken2);
		tokens.add(taggedToken3);
		ValueType type = numberTypeResolver.resolve(tokens);
		assertEquals(ValueType.NumberPhrase, type);
		assertEquals("inch", type.getUnit());
	}

	@Test
	public void testNumberPhraseWithTwoWordsOfSuffixUnitType() {
		List<POSTaggedToken> tokens = new ArrayList<>();
		POSTaggedToken taggedToken1 = new POSTaggedToken("88.8", "CD");
		POSTaggedToken taggedToken2 = new POSTaggedToken("square", "NN");
		POSTaggedToken taggedToken3 = new POSTaggedToken("foot", "NN");
		tokens.add(taggedToken1);
		tokens.add(taggedToken2);
		tokens.add(taggedToken3);
		ValueType type = numberTypeResolver.resolve(tokens);
		assertEquals(ValueType.NumberPhrase, type);
		assertEquals("square foot", type.getUnit());
	}

	@Test
	public void testNumberPhraseWithOneWordOfPrefixUnitType() {
		List<POSTaggedToken> tokens = new ArrayList<>();
		POSTaggedToken taggedToken1 = new POSTaggedToken("$", "$");
		POSTaggedToken taggedToken2 = new POSTaggedToken("88.8", "CD");
		// This third token should have no effects on the outcome
		POSTaggedToken taggedToken3 = new POSTaggedToken("random", "NN");
		tokens.add(taggedToken1);
		tokens.add(taggedToken2);
		tokens.add(taggedToken3);
		ValueType type = numberTypeResolver.resolve(tokens);
		assertEquals(ValueType.NumberPhrase, type);
		assertEquals("$", type.getUnit());
	}

	@Test
	public void testNumberPhraseWithOneWordOfNN() {
		List<POSTaggedToken> tokens = new ArrayList<>();
		POSTaggedToken taggedToken1 = new POSTaggedToken("88.8", "CD");
		POSTaggedToken taggedToken2 = new POSTaggedToken("random", "NN");

		tokens.add(taggedToken1);
		tokens.add(taggedToken2);
		ValueType type = numberTypeResolver.resolve(tokens);
		assertEquals(ValueType.NumberPhrase, type);
		assertEquals("random", type.getUnit());
	}

	@Test
	public void testVerbAppearsBeforeNumber() {
		List<POSTaggedToken> tokens = new ArrayList<>();
		POSTaggedToken taggedToken1 = new POSTaggedToken("has", "VB");
		POSTaggedToken taggedToken2 = new POSTaggedToken("88.8", "CD");
		POSTaggedToken taggedToken3 = new POSTaggedToken("random", "NN");
		tokens.add(taggedToken1);
		tokens.add(taggedToken2);
		tokens.add(taggedToken3);
		ValueType type = numberTypeResolver.resolve(tokens);
		assertEquals(null, type);
	}
	
	@Test
	public void testVerbAppearsAfterNumber() {
		List<POSTaggedToken> tokens = new ArrayList<>();
		POSTaggedToken taggedToken1 = new POSTaggedToken("$", "$");
		POSTaggedToken taggedToken2 = new POSTaggedToken("88.8", "CD");
		POSTaggedToken taggedToken3 = new POSTaggedToken("random", "VB");
		tokens.add(taggedToken1);
		tokens.add(taggedToken2);
		tokens.add(taggedToken3);
		ValueType type = numberTypeResolver.resolve(tokens);
		assertEquals(null, type);
	}
	
	@Test
	public void testOneNounAppearsBeforeNumber(){
		List<POSTaggedToken> tokens = new ArrayList<>();
		POSTaggedToken taggedToken1 = new POSTaggedToken("random", "NN");
		POSTaggedToken taggedToken2 = new POSTaggedToken("88.8", "CD");
		tokens.add(taggedToken1);
		tokens.add(taggedToken2);
		ValueType type = numberTypeResolver.resolve(tokens);
		assertEquals(null, type);
	}
	
	@Test
	public void testThreeNounAppearsAfterNumber(){
		List<POSTaggedToken> tokens = new ArrayList<>();
		POSTaggedToken taggedToken1 = new POSTaggedToken("88.8", "CD");
		POSTaggedToken taggedToken2 = new POSTaggedToken("random", "NN");
		POSTaggedToken taggedToken3 = new POSTaggedToken("random", "NN");
		POSTaggedToken taggedToken4 = new POSTaggedToken("random", "NN");
		tokens.add(taggedToken1);
		tokens.add(taggedToken2);
		tokens.add(taggedToken3);
		tokens.add(taggedToken4);
		ValueType type = numberTypeResolver.resolve(tokens);
		assertEquals(null, type);
	}
	
	@Test
	public void testNounBeforePrefixUnit(){
		List<POSTaggedToken> tokens = new ArrayList<>();
		POSTaggedToken taggedToken1 = new POSTaggedToken("random", "NN");
		POSTaggedToken taggedToken2 = new POSTaggedToken("$", "$");
		POSTaggedToken taggedToken3 = new POSTaggedToken("88.8", "CD");
		tokens.add(taggedToken1);
		tokens.add(taggedToken2);
		tokens.add(taggedToken3);
		ValueType type = numberTypeResolver.resolve(tokens);
		assertEquals(null, type);
	}
	
	@Test
	public void testNoNounNoVerbNoNumber(){
		List<POSTaggedToken> tokens = new ArrayList<>();
		POSTaggedToken taggedToken1 = new POSTaggedToken("random", "TO");
		tokens.add(taggedToken1);
		ValueType type = numberTypeResolver.resolve(tokens);
		assertEquals(null, type);
	}
	
	@Test
	public void testNumberWithNoNounNoVerb(){
		List<POSTaggedToken> tokens = new ArrayList<>();
		POSTaggedToken taggedToken1 = new POSTaggedToken("88.8", "CD");
		POSTaggedToken taggedToken2 = new POSTaggedToken("random", "TO");
		tokens.add(taggedToken1);
		tokens.add(taggedToken2);
		ValueType type = numberTypeResolver.resolve(tokens);
		assertEquals(null, type);
	}

}
