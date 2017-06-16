package umbc.ebiquity.kang.htmldocument.util;

import java.util.UUID;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.AbstractHTMLTreeContentNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeBlankNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeEntityNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreePropertyNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeValueNode;

public class HTMLTreeUtil {

	/**** For Manual Test Purpose *****/

	public static void prettyPrint(IHTMLTreeNode node) {
		System.out.println("" + node.getTagName());
		prettyPrintChildren(node, "---");
	}

	public static void prettyPrint(IHTMLTreeNode node, String intent) {
		prettyPrintChildren(node, intent + "---");
	}

	private static void prettyPrintChildren(IHTMLTreeNode node, String intent) {
		for (IHTMLTreeNode c : node.getChildren()) {
			String value = null;
			if (c instanceof HTMLTreeValueNode) {
				value = ((HTMLTreeValueNode) c).getMainValue().getValue();
			} else if (c instanceof AbstractHTMLTreeContentNode) {
				value = ((AbstractHTMLTreeContentNode) c).getContent();
			} else if (c instanceof HTMLTreeBlankNode) {
				
			}
			value = value == null || value.isEmpty() ? "" : "(" + value + ")";
			System.out.println(intent + c.getTagName() + " " + value);
			prettyPrint(c, intent + "---");
		}
	}
	
	public static final String TYPE = "type";
	public static final String ENTITY_TYPE = "ENTITY";
	public static final String PROPERTY_TYPE = "PROPERTY";
	public static final String VALUE_TYPE = "VALUE";
	public static final String BNODE_TYPE = "BNODE";
	private static void prettyPrintChildren2(IHTMLTreeNode node) {
		for (IHTMLTreeNode c : node.getChildren()) {
			String value = null;
			if (c instanceof HTMLTreeValueNode) {
				value = ((HTMLTreeValueNode) c).getMainValue().getValue();
			} else if (c instanceof HTMLTreeEntityNode) {
				value = ((HTMLTreeEntityNode) c).getContent();
			} else if (c instanceof HTMLTreePropertyNode) {
				value = ((HTMLTreePropertyNode) c).getContent();
				String id = generateUniqueId();
				String type = PROPERTY_TYPE;
				
			} else if (c instanceof HTMLTreeBlankNode) {

			}
		}
	}

	private static String generateUniqueId() {
		return UUID.randomUUID().toString();
	}
}
