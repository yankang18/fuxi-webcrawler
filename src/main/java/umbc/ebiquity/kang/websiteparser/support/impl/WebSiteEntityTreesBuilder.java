package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.ArrayList;
import java.util.List;

import umbc.ebiquity.kang.htmldocument.parser.DefaultHtmlDocumentPathsParser;
import umbc.ebiquity.kang.htmldocument.parser.IHtmlDocumentParsedPathsHolder;
import umbc.ebiquity.kang.websiteparser.ICrawledWebSite;
import umbc.ebiquity.kang.websiteparser.IWebPageDocument;
import umbc.ebiquity.kang.websiteparser.support.IBlankNodeResolver;
import umbc.ebiquity.kang.websiteparser.support.IHTMLTreeOverlay;
import umbc.ebiquity.kang.websiteparser.support.IHTMLTreeOverlayBuilder;
import umbc.ebiquity.kang.websiteparser.support.IWebSiteParsedPathsHolder;
import umbc.ebiquity.kang.websiteparser.support.IWebSiteTemplateMarker;

public class WebSiteEntityTreesBuilder {

	private IHTMLTreeOverlayBuilder overlayBuilder;
	private IBlankNodeResolver blankNodeResolver;
	private IWebSiteTemplateMarker templateMarker;
	
	public WebSiteEntityTreesBuilder() {
		overlayBuilder = new HTMLTreeOverlayBuilder();
		blankNodeResolver = new BlankNodeResolver();
		templateMarker = new WebSiteTemplateMarker();
	}

	public void build(IWebSiteParsedPathsHolder webSiteParsedPathsHolder) {

		List<IHtmlDocumentParsedPathsHolder> webpages = webSiteParsedPathsHolder.getHtmlDocumentParsedPathHolders();

		List<IHTMLTreeOverlay> htmlTreeOverlayList = buildOverlays(webpages);

		markTemplates(htmlTreeOverlayList);

		htmlTreeOverlayList = resolveBlanNodeInOverlays(htmlTreeOverlayList);
		
		// TODO: combine html trees and identify relations, concept hierarchies

	}

	private List<IHTMLTreeOverlay> resolveBlanNodeInOverlays(List<IHTMLTreeOverlay> htmlTreeOverlayList) {
		List<IHTMLTreeOverlay> newHtmlTreeOverlayList = new ArrayList<IHTMLTreeOverlay>();
		for(IHTMLTreeOverlay overlay : htmlTreeOverlayList){
			IHTMLTreeOverlay newRoot = blankNodeResolver.resolve(overlay);
			newHtmlTreeOverlayList.add(newRoot);
		}
		return newHtmlTreeOverlayList;
	}

	private List<IHTMLTreeOverlay> buildOverlays(List<IHtmlDocumentParsedPathsHolder> webpages) {
		List<IHTMLTreeOverlay> htmlTreeOverlayList = new ArrayList<IHTMLTreeOverlay>(webpages.size());
		for (IHtmlDocumentParsedPathsHolder webpage : webpages) {
			IHTMLTreeOverlay treeOverlay = overlayBuilder.build(webpage);
			htmlTreeOverlayList.add(treeOverlay);
		}
		return htmlTreeOverlayList;
	}

	private void markTemplates(List<IHTMLTreeOverlay> htmlTreeOverlayList) {
		if (templateMarker != null) {
			templateMarker.mark(htmlTreeOverlayList);
		}
	}

}
