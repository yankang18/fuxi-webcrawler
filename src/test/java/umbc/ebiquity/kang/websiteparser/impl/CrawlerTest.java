package umbc.ebiquity.kang.websiteparser.impl;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import umbc.ebiquity.kang.htmldocument.IHtmlDocument;
import umbc.ebiquity.kang.htmldocument.IHtmlPath;
import umbc.ebiquity.kang.htmldocument.impl.SandardHtmlDocument;
import umbc.ebiquity.kang.htmldocument.parser.IHtmlParsedPathsHolder;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlayRefiner;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.StandardHTMLTreeBlankNodePruner;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeOverlayConstructor;
import umbc.ebiquity.kang.htmldocument.parser.impl.StandardHtmlPathsParser;
import umbc.ebiquity.kang.websiteparser.support.ITemplateNodeMatcher;
import umbc.ebiquity.kang.websiteparser.support.ITemplateNodeMatcherRegistry;
import umbc.ebiquity.kang.websiteparser.support.IWebSiteParsedPathsHolder;
import umbc.ebiquity.kang.websiteparser.support.impl.DefaultWebSiteParsedPathsHolder;
import umbc.ebiquity.kang.websiteparser.support.impl.SimpleTemplateNodeMatcher;
import umbc.ebiquity.kang.websiteparser.support.impl.TemplateNodeMatcherRegistry;
import umbc.ebiquity.kang.websiteparser.support.impl.TemplateNodePruner;
import umbc.ebiquity.kang.websiteparser.support.impl.WebSiteEntityTreesBuilder;

public class CrawlerTest {

	@Ignore
	@Test
	public void xxx() throws IOException {
		CrawlerUrl url = new CrawlerUrl("http://www.accutrex.com/abrasive-waterjet-cutting", 1);
//		CrawlerUrl url = new CrawlerUrl("http://www.accutrex.com/aerospace", 1);
		IHtmlDocument webpage = new WebPageImpl(url);
		webpage.load();
		StandardHtmlPathsParser htmlDocumentPathsParser = new StandardHtmlPathsParser(webpage);
		IHtmlParsedPathsHolder pathHolder = htmlDocumentPathsParser.parse();

		for (IHtmlPath path : pathHolder.listHtmlPaths()) {
			System.out.println(path.getPathID());
		}
		
		System.out.println(pathHolder.getDomainName());
		System.out.println(pathHolder.getUniqueIdentifier());
		
		HTMLTreeOverlayConstructor constructor = new HTMLTreeOverlayConstructor();
		IHTMLTreeOverlay overLay = constructor.build(pathHolder);
		
		
		System.out.println("@2");
		HTMLTreeOverlayConstructor.pettyPrint(overLay.getTreeRoot());
		
		ITemplateNodeMatcherRegistry templateElementMatcherRegistry = new TemplateNodeMatcherRegistry();
		SimpleTemplateNodeMatcher templateElementMatcher = new SimpleTemplateNodeMatcher();
		templateElementMatcher.addAttribute("id", "header");
		templateElementMatcher.addAttribute("id", "footer");
		templateElementMatcher.addAttribute("id", "sidebar");
		templateElementMatcher.addAttribute("id", "sidebar-left");
		templateElementMatcher.addTag("nav"); 
		templateElementMatcherRegistry.register(pathHolder.getDomainName(), templateElementMatcher); 
		IHTMLTreeOverlayRefiner templateNodePruner = new TemplateNodePruner(templateElementMatcherRegistry);
		overLay = templateNodePruner.refine(overLay);
		
		System.out.println("@3");
		HTMLTreeOverlayConstructor.pettyPrint(overLay.getTreeRoot());
		
		
		StandardHTMLTreeBlankNodePruner cc = new StandardHTMLTreeBlankNodePruner();
		overLay = cc.refine(overLay);
		
		System.out.println("@4");
		HTMLTreeOverlayConstructor.pettyPrint(overLay.getTreeRoot());
	}
	
//	@Ignore
	@Test
	public void xxx2() throws IOException {
//		String location = "///Users/yankang/Documents/Temp/abrasive-waterjet-cutting.html";
		String location = "///Users/yankang/Documents/Temp/abrasive-waterjet-cutting.html";
		File input = new File(location);
		
		System.out.println(input.getAbsolutePath());
		System.out.println(input.getCanonicalPath());
		System.out.println(input.getPath());
		System.out.println(input.getParent());
		
		IHtmlDocument webpage = new SandardHtmlDocument(input);
		webpage.load();
		StandardHtmlPathsParser webPagePathsImpl = new StandardHtmlPathsParser(webpage);
		IHtmlParsedPathsHolder webPagePathHolder = webPagePathsImpl.parse();

		for (IHtmlPath path : webPagePathHolder.listHtmlPaths()) {
			System.out.println(path.getPathID());
		}
		
		HTMLTreeOverlayConstructor constructor = new HTMLTreeOverlayConstructor();
		IHTMLTreeOverlay overLay = constructor.build(webPagePathHolder);
		
		
		HTMLTreeOverlayConstructor.pettyPrint(overLay.getTreeRoot());
		
		StandardHTMLTreeBlankNodePruner cc = new StandardHTMLTreeBlankNodePruner();
		overLay = cc.refine(overLay);
		
		HTMLTreeOverlayConstructor.pettyPrint(overLay.getTreeRoot());
		
		WebSiteEntityTreesBuilder builder = new WebSiteEntityTreesBuilder();
		
		builder.build(null);
	}
	
	@Test
	public void xxx4(){
		
	}

	@Ignore
	@Test
	public void xxx3() {
		
		List<IHtmlParsedPathsHolder> webPagePathHolders = new ArrayList<IHtmlParsedPathsHolder>();
		
		IWebSiteParsedPathsHolder webSiteParsedHolder = new DefaultWebSiteParsedPathsHolder(null,
				webPagePathHolders);
		

	}

}
