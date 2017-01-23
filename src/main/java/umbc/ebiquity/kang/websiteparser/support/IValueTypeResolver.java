package umbc.ebiquity.kang.websiteparser.support;

import umbc.ebiquity.kang.websiteparser.support.impl.Value.ValueType;

public interface IValueTypeResolver {

	ValueType resolve(String value);
}
