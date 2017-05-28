package umbc.ebiquity.kang.websiteparser.impl;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import umbc.ebiquity.kang.htmldocument.IHtmlDocument;
import umbc.ebiquity.kang.htmldocument.IHtmlPath;
import umbc.ebiquity.kang.htmldocument.impl.SandardHtmlDocument;
import umbc.ebiquity.kang.htmldocument.parser.IHtmlDocumentParsedPathsHolder;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlayRefiner;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.StandardHTMLTreeBlankNodeConsolidator;
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

public class CrawlerManualTest {
	
	@Ignore
	@Test
	public void testWorkflowOfParsingWebPage() throws IOException {
		
		System.out.println("@1: Crawls and loads web page");
		CrawlerUrl url = new CrawlerUrl("http://www.accutrex.com/abrasive-waterjet-cutting", 1);
//		CrawlerUrl url = new CrawlerUrl("http://www.accutrex.com/aerospace", 1);
		IHtmlDocument webpage = new WebPageImpl(url);
		webpage.load();
		
		System.out.println("@2: Parse loaded web page into paths");
		StandardHtmlPathsParser htmlDocumentPathsParser = new StandardHtmlPathsParser(webpage);
		IHtmlDocumentParsedPathsHolder pathHolder = htmlDocumentPathsParser.parse();

		for (IHtmlPath path : pathHolder.listHtmlPaths()) {
			System.out.println(path.getPathIdent());
		}
		
		System.out.println(pathHolder.getDomainName());
		System.out.println(pathHolder.getUniqueIdentifier());
		
		System.out.println("@3: Create tree overlaying parsed paths");
		HTMLTreeOverlayConstructor constructor = new HTMLTreeOverlayConstructor();
		IHTMLTreeOverlay overLay = constructor.build(pathHolder);
		
		HTMLTreeOverlayConstructor.pettyPrint(overLay.getTreeRoot());
		
		System.out.println("@4: Refine the tree overlay by pruning template nodes");
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
		
		HTMLTreeOverlayConstructor.pettyPrint(overLay.getTreeRoot());
		
		System.out.println("@5: Refine the tree overlay by consolidating blank nodes");
		IHTMLTreeOverlayRefiner cc = new StandardHTMLTreeBlankNodeConsolidator();
		overLay = cc.refine(overLay);
		
		HTMLTreeOverlayConstructor.pettyPrint(overLay.getTreeRoot());
	}
	
	@Ignore
	@Test
	public void testWorkflowOfParsingHtmlDocument() throws IOException {

		System.out.println("@1: loads HTML document");
		File input = loadFileOrDirectory("CrawlerManual/abrasive-waterjet-cutting.html");

		System.out.println(input.getAbsolutePath());
		System.out.println(input.getCanonicalPath());
		System.out.println(input.getPath());
		System.out.println(input.getParent());
		
		IHtmlDocument webpage = new SandardHtmlDocument(input);
		webpage.load();
		
		System.out.println("@2: Parse loaded HTML document into paths");
		StandardHtmlPathsParser webPagePathsImpl = new StandardHtmlPathsParser(webpage);
		IHtmlDocumentParsedPathsHolder webPagePathHolder = webPagePathsImpl.parse();

		for (IHtmlPath path : webPagePathHolder.listHtmlPaths()) {
			System.out.println(path.getPathIdent());
		}
		
		System.out.println("@3: Create tree overlaying parsed paths");
		HTMLTreeOverlayConstructor constructor = new HTMLTreeOverlayConstructor();
		IHTMLTreeOverlay overLay = constructor.build(webPagePathHolder);
		
		HTMLTreeOverlayConstructor.pettyPrint(overLay.getTreeRoot());
		
		System.out.println("@4: Refine the tree overlay by consolidating blank nodes");
		StandardHTMLTreeBlankNodeConsolidator cc = new StandardHTMLTreeBlankNodeConsolidator();
		overLay = cc.refine(overLay);
		
		HTMLTreeOverlayConstructor.pettyPrint(overLay.getTreeRoot());
		
//		WebSiteEntityTreesBuilder builder = new WebSiteEntityTreesBuilder();
//		builder.build(null);
	}

	public File loadFileOrDirectory(String fileName) {
		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File input = new File(classLoader.getResource(fileName).getFile());
		return input;
	} 
}
