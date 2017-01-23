package umbc.ebiquity.kang.websiteparser.impl;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import umbc.ebiquity.kang.htmldocument.IHtmlDocument;
import umbc.ebiquity.kang.htmldocument.IHtmlDocumentPath;
import umbc.ebiquity.kang.htmldocument.impl.SingleHtmlDocument;
import umbc.ebiquity.kang.htmldocument.parser.DefaultHtmlDocumentPathsParser;
import umbc.ebiquity.kang.htmldocument.parser.IHtmlDocumentParsedPathsHolder;
import umbc.ebiquity.kang.websiteparser.support.impl.DefaultWebPagePathsParser;
import umbc.ebiquity.kang.websiteparser.support.impl.DefaultWebSiteParsedPathsHolder;
import umbc.ebiquity.kang.websiteparser.support.impl.HTMLTreeOverlayBuilder;
import umbc.ebiquity.kang.websiteparser.support.impl.EntityNode;
import umbc.ebiquity.kang.websiteparser.support.IHTMLTreeOverlay;
import umbc.ebiquity.kang.websiteparser.support.IWebSiteParsedPathsHolder;
import umbc.ebiquity.kang.websiteparser.support.IWebSiteTemplateMarker;
import umbc.ebiquity.kang.websiteparser.support.impl.BlankNodeResolver;
import umbc.ebiquity.kang.websiteparser.support.impl.INode;
import umbc.ebiquity.kang.websiteparser.support.impl.ValueNode;
import umbc.ebiquity.kang.websiteparser.support.impl.WebSiteEntityTreesBuilder;
import umbc.ebiquity.kang.websiteparser.support.impl.WebSiteTemplateMarker;

public class CrawlerTest {

//	@Ignore
	@Test
	public void xxx() throws IOException {
		CrawlerUrl url = new CrawlerUrl("http://www.accutrex.com/abrasive-waterjet-cutting", 1);
//		CrawlerUrl url = new CrawlerUrl("http://www.accutrex.com/aerospace", 1);
		IHtmlDocument webpage = new WebPageImpl(url);
		webpage.load();
		DefaultHtmlDocumentPathsParser webPagePathsImpl = new DefaultHtmlDocumentPathsParser(webpage);
		IHtmlDocumentParsedPathsHolder webPagePathHolder = webPagePathsImpl.parse();

		for (IHtmlDocumentPath path : webPagePathHolder.listHtmlDocumentPaths()) {
			System.out.println(path.getPathID());
		}
		
		HTMLTreeOverlayBuilder constructor = new HTMLTreeOverlayBuilder();
		IHTMLTreeOverlay overLay = constructor.build(webPagePathHolder);
		
		
		HTMLTreeOverlayBuilder.pettyPrint(overLay.getTreeRoot());
		
		BlankNodeResolver cc = new BlankNodeResolver();
		overLay = cc.resolve(overLay);
		
		HTMLTreeOverlayBuilder.pettyPrint(overLay.getTreeRoot());
		
	}
	
	@Ignore
	@Test
	public void xxx2() throws IOException {
//		String location = "///Users/yankang/Documents/Temp/abrasive-waterjet-cutting.html";
		String location = "///Users/yankang/Documents/Temp/abrasive-waterjet-cutting.html";
		File input = new File(location);
		
		System.out.println(input.getAbsolutePath());
		System.out.println(input.getCanonicalPath());
		System.out.println(input.getPath());
		System.out.println(input.getParent());
		
		IHtmlDocument webpage = new SingleHtmlDocument(input);
		webpage.load();
		DefaultHtmlDocumentPathsParser webPagePathsImpl = new DefaultHtmlDocumentPathsParser(webpage);
		IHtmlDocumentParsedPathsHolder webPagePathHolder = webPagePathsImpl.parse();

		for (IHtmlDocumentPath path : webPagePathHolder.listHtmlDocumentPaths()) {
			System.out.println(path.getPathID());
		}
		
		HTMLTreeOverlayBuilder constructor = new HTMLTreeOverlayBuilder();
		IHTMLTreeOverlay overLay = constructor.build(webPagePathHolder);
		
		
		HTMLTreeOverlayBuilder.pettyPrint(overLay.getTreeRoot());
		
		BlankNodeResolver cc = new BlankNodeResolver();
		overLay = cc.resolve(overLay);
		
		HTMLTreeOverlayBuilder.pettyPrint(overLay.getTreeRoot());
		
		WebSiteEntityTreesBuilder builder = new WebSiteEntityTreesBuilder();
		
		builder.build(null);
	}

	@Ignore
	@Test
	public void xxx3() {
		
		List<IHtmlDocumentParsedPathsHolder> webPagePathHolders = new ArrayList<IHtmlDocumentParsedPathsHolder>();
		
		IWebSiteParsedPathsHolder webSiteParsedHolder = new DefaultWebSiteParsedPathsHolder(null,
				webPagePathHolders);
		
		IWebSiteTemplateMarker ident = new WebSiteTemplateMarker();
		
		
		
		

	}

}
