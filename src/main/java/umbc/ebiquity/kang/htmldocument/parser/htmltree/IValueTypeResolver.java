package umbc.ebiquity.kang.htmldocument.parser.htmltree;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeNodeValue.ValueType;

public interface IValueTypeResolver {

	ValueType resolve(String value);
}
