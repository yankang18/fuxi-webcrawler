package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

public class POSTaggedToken {

	private String pos;
	private String value;

	public POSTaggedToken(String value, String pos) {
		this.value = value;
		this.pos = pos;
	}

	public String getPOSTag() {
		return pos;
	}

	public String getValue() {
		return value;
	}
}
