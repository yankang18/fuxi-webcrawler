package umbc.ebiquity.kang.htmldocument.util;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.AbstractHTMLTreeContentNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeBlankNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeValueNode;

/**
 * This utility print IHTMLTreeNode in a human-readable way and is only used for
 * testing purpose
 * 
 * @author yankang
 *
 */
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

}
