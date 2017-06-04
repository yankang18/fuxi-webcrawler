package umbc.ebiquity.kang.htmldocument.util;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeEntityNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeValueNode;

public class HTMLTreeUtil {

	/**** For Manual Test Purpose *****/

	public static void prettyPrint(IHTMLTreeNode node) {
		prettyPrint(node, "");
	}

	public static void prettyPrint(IHTMLTreeNode node, String intent) {
		for (IHTMLTreeNode c : node.getChildren()) {
			String value = null;
			if (c instanceof HTMLTreeValueNode) {
				value = ((HTMLTreeValueNode) c).getMainValue().getValue();
			} else if (c instanceof HTMLTreeEntityNode) {
				value = ((HTMLTreeEntityNode) c).getContent();
			}
			value = value == null || value.isEmpty() ? "" : "(" + value + ")";
			System.out.println(intent + c.getTagName() + " " + value);
			prettyPrint(c, intent + "---");
		}
	}
}
