package umbc.ebiquity.kang.htmltable.translator.impl;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.util.HTMLTree2JSONConverter;

public class TableTree2JsonPipleline extends TableTranslationPipline {

	public JSONObject translate(Element element) {
		IHTMLTreeOverlay overlay = doTranslate(element);
		// IHTMLTreeOverlay -- (HTMLTree2JSONTranslator) --> JSONObject
		// representing the table
		JSONObject object = HTMLTree2JSONConverter.convert(overlay.getTreeRoot());
		// System.out.println(HTMLTree2JSONConverter.prettyPrint(object));
		return object;
	}
}
