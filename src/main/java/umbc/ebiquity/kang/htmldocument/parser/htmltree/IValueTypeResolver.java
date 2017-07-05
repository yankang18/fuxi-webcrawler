package umbc.ebiquity.kang.htmldocument.parser.htmltree;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.ValueTypeInfo;

public interface IValueTypeResolver {

	ValueTypeInfo resolve(String value);
}
