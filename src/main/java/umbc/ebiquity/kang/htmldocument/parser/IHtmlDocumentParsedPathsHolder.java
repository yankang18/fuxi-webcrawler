package umbc.ebiquity.kang.htmldocument.parser;

import java.util.List;

import umbc.ebiquity.kang.htmldocument.IHtmlDocumentPath;
import umbc.ebiquity.kang.htmldocument.impl.HtmlDocumentPath;

public interface IHtmlDocumentParsedPathsHolder {

	List<IHtmlDocumentPath> listHtmlDocumentPaths(); 
	
	String getUniqueIdentifier();

}
