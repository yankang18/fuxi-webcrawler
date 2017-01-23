package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

public class HTMLTableTagDefinition {
	
	public final static String TABLE_TAG = "table";
	public final static String TABLE_BODY_TAG = "tbody";
	public final static String TABLE_HEADER_SECTION_TAG = "thead";

	public final static String TABLE_ROW_TAG = "tr";
	public final static String TABLE_DATA_TAG = "td";
	public final static String TABLE_HEADER_TAG = "th";

	public boolean isTableTag(String tagName) {
		return TABLE_TAG.equalsIgnoreCase(tagName.trim());
	}
}
