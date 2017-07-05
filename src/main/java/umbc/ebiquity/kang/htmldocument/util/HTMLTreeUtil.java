package umbc.ebiquity.kang.htmldocument.util;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.AbstractHTMLTreeContentNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeBlankNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeNodeValue;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeValueNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.ValueTypeInfo;

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
			String content = null;
			ValueTypeInfo valueTypeInfo = null;
			if (c instanceof HTMLTreeValueNode) {
				HTMLTreeNodeValue treeNodeValue = ((HTMLTreeValueNode) c).getMainValue();
				valueTypeInfo = treeNodeValue.getValueTypeInfo();
				content = ((HTMLTreeValueNode) c).getMainValue().getContent();
			} else if (c instanceof AbstractHTMLTreeContentNode) {
				content = ((AbstractHTMLTreeContentNode) c).getContent();
			} else if (c instanceof HTMLTreeBlankNode) {

			}
			String unit = null;
			if (valueTypeInfo != null)
				unit = valueTypeInfo.getUnit();
			
			content = content == null || content.isEmpty() ? "" : "(" + content + ")";
			unit = unit == null || unit.isEmpty() ? "" : "(" + unit + ")";
			System.out.println(intent + c.getTagName() + " " + content + " " + unit);
			prettyPrint(c, intent + "---");
		}
	}
}
