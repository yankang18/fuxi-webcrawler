package umbc.ebiquity.kang.htmldocument.parser.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import umbc.ebiquity.kang.htmldocument.IHtmlDocument;
import umbc.ebiquity.kang.htmldocument.IHtmlPath;
import umbc.ebiquity.kang.htmldocument.IHtmlElement;
import umbc.ebiquity.kang.htmldocument.impl.HtmlNode;
import umbc.ebiquity.kang.htmldocument.impl.HtmlPath;
import umbc.ebiquity.kang.htmldocument.parser.IHtmlDocumentParsedPathsHolder;
import umbc.ebiquity.kang.htmldocument.parser.IHtmlPathsParser;
import umbc.ebiquity.kang.textprocessing.util.TextProcessingUtils;
import umbc.ebiquity.kang.websiteparser.impl.HTMLTags;

public class StandardHtmlPathsParser implements IHtmlPathsParser {

	private List<IHtmlPath> parsedPathList;
	private Map<String, Integer> tagCounterMapper;
	private IHtmlElement htmlElement;
	private boolean parsed; 
	private IHtmlDocumentParsedPathsHolder parsedPathHolder;

	public StandardHtmlPathsParser(IHtmlElement htmlElement) {  
		this.htmlElement = htmlElement;
		this.parsed = false;
		this.parsedPathList = new ArrayList<IHtmlPath>(); 
		this.tagCounterMapper = new HashMap<String, Integer>();
	}

	@Override
	public IHtmlDocumentParsedPathsHolder parse() {
		if (parsed)
			return parsedPathHolder;

		Element body = htmlElement.getBody();
		// body children not necessarily element
		
		if (isTargetTagName(body.tagName().toLowerCase())) {
			HtmlNode node = this.createHtmlElementNode(body);
			HtmlPath path = new HtmlPath();
			parsedPathList.add(path);
			node.setLeafNode(false);
			path.addNode(node);
			appendPathNode(path, body);
		}
		
//		for (Element elem : body.children()) {
//			String childTagName = elem.tagName().toLowerCase();
//			if (isTargetTagName(childTagName)) {
//				HtmlPath path = new HtmlPath();
//				parsedPathList.add(path);
//				HtmlNode node = this.createHtmlNode(elem);
//				node.setLeafNode(false);
//				path.addNode(node);
//				appendPathNode(path, elem);
//			}
//		}
		
		
		parsed = true;
		parsedPathHolder = new DefaultHtmlParsedPathsHolder(htmlElement.getUniqueIdentifier(),
				htmlElement.getDomainName(), parsedPathList);
		return parsedPathHolder;
	}

	private boolean isTargetTagName(String childTagName) {
		return !HTMLTags.getEliminatedTags().contains(childTagName) && !HTMLTags.getIgnoredTags().contains(childTagName);
	}

	private void appendPathNode(HtmlPath parentPath, Element elem) {

		if (isLeafElement(elem)) {
			System.out.println("LeafElement: " + elem.tagName());
			HtmlNode node = parentPath.getLastNode();
			node.toValueNode();
//			WebPageNode newWebPageNode = this.createWebPageNode(webPageNode.getFullContent());
//			newWebPageNode.setLeafNode(true);
//			path.addNode(newWebPageNode); 
			
		} else {
			System.out.println("not LeafElement: " + elem.tagName());
			List<Node> childNodeList = elem.childNodes();
			int numOfChildrenNode = childNodeList.size();

			// Clone paths before navigating children of current element.
			int numOfPaths = numOfChildrenNode;
			HtmlPath[] paths = null;
			if (numOfPaths > 0) {
				paths = new HtmlPath[numOfPaths];

//				/*
//				 * The first path is not cloned but the path that has already
//				 * been created. We reuse this path to hold the first child of
//				 * current element
//				 */
//				webPagePaths[0] = path;
//
//				/*
//				 * Clone other paths to hold children except the first child of
//				 * current element
//				 */
//				for (int i = 1; i < numOfPaths; i++) {
//					webPagePaths[i] = path.clone();
//					// webPagePaths[i].setHost(this.getPageURLAsString());
//				}
				
				// Clone other paths to hold children except the first child of
				// current element
				for (int i = 0; i < numOfPaths; i++) {
					paths[i] = parentPath.clone();
				}
			}

			// cntTxt stores concatenated test content if ignored tags
			String cntTxt = "";
			int indexOfNode = 0;
			int pathIndexCursor = 0;
			for (; indexOfNode < numOfChildrenNode; indexOfNode++) {

				Node childNode = childNodeList.get(indexOfNode);
				if (childNode instanceof TextNode) {
					TextNode textNode = (TextNode) childNode;
					String text = textNode.text();
					if (!TextProcessingUtils.isStringEmpty(text)) {
						cntTxt += text;
					}

				} else if (childNode instanceof Element) {

					
					Element elementNode = (Element) childNode;
					String elemText = elementNode.text();
					String tagName = elementNode.tagName().toLowerCase();
					
					if ("br".equalsIgnoreCase(tagName)) {
						
						if (!TextProcessingUtils.isStringEmpty(cntTxt)) {
							HtmlNode node = createHtmlTextNode(cntTxt);
							node.setLeafNode(true);
							HtmlPath path = paths[pathIndexCursor++];
							path.addNode(node);
							this.parsedPathList.add(path);
							cntTxt = "";
						}

					} else if (HTMLTags.getIgnoredTags().contains(tagName)) {

						if (!TextProcessingUtils.isStringEmpty(elemText)) {
							cntTxt += elemText;
						}

					} else if (HTMLTags.isTopicTag(tagName)) {

						if (cntTxt != null && !TextProcessingUtils.isStringEmpty(cntTxt)) {
							HtmlNode node = createHtmlTextNode(cntTxt);
							node.setLeafNode(true);
							HtmlPath path = paths[pathIndexCursor];
							path.addNode(node);
							this.parsedPathList.add(path);
							pathIndexCursor++;
							cntTxt = "";
						}

						if (!TextProcessingUtils.isStringEmpty(elemText)) {
							HtmlNode node = createHtmlElementNode(elementNode);
							node.setLeafNode(true);
							HtmlPath path = paths[pathIndexCursor];
							path.addNode(node);
							this.parsedPathList.add(path);
							pathIndexCursor++;
						}

					} else if (HTMLTags.getEliminatedTags().contains(tagName)) {

						// TODO: refactor following if statement
						if (cntTxt != null && !TextProcessingUtils.isStringEmpty(cntTxt)) {
							HtmlNode node = createHtmlTextNode(cntTxt);
							node.setLeafNode(true);
							HtmlPath path = paths[pathIndexCursor];
							path.addNode(node);
							this.parsedPathList.add(path);
							pathIndexCursor++;
							cntTxt = "";
						}

					} else {

						if (cntTxt != null && !TextProcessingUtils.isStringEmpty(cntTxt)) {
							HtmlNode node = createHtmlTextNode(cntTxt);
							node.setLeafNode(true);
							HtmlPath path = paths[pathIndexCursor];
							path.addNode(node);
							this.parsedPathList.add(path);
							pathIndexCursor++;
							cntTxt = "";
						}

						if (!TextProcessingUtils.isStringEmpty(elemText)) {

							HtmlNode node = createHtmlElementNode(elementNode);
							HtmlPath path = paths[pathIndexCursor];
							path.addNode(node);
							parsedPathList.add(path);
							node.setLeafNode(false);
							appendPathNode(path, elementNode);
							pathIndexCursor++;
						}
					}
				}
			}

			// This is dealing with the situation where the last node of current
			// element is a text node.
			if (cntTxt != null && !TextProcessingUtils.isStringEmpty(cntTxt)) {
				HtmlNode node = this.createHtmlTextNode(cntTxt);
				node.setLeafNode(true);
				HtmlPath path = paths[pathIndexCursor];
				path.addNode(node);
				this.parsedPathList.add(path);
			}
		}
	}

	private boolean isLeafElement(Element elem) {
		String tagName = elem.tagName().toLowerCase();
		return HTMLTags.isValueTag(tagName);
	}

	// TODO: what is text node.
	// in what situation an element contains one text node. show an example.
	// in what situation an element contains multiple test nodes. show an
	// example.
	// /**
	// * To check if a element contains only one text node that is not empty.
	// One
	// * special case is that if this element contains multiple br tag, but only
	// * one non-empty text node, we still treat this element as the one
	// contains
	// * only one text node that is not empty.
	// */
	// private boolean containsOnlyOneTextNode(Element element) {
	// int numberOfTextNode = 0;
	// for (Node child : element.childNodes()) {
	// if (child instanceof TextNode) {
	// if (!TextProcessingUtils.isStringEmpty(((TextNode) child).text())) {
	// numberOfTextNode++;
	// }
	// } else if (child instanceof Element) {
	// if (!"br".equals(((Element) child).tagName().toLowerCase())) {
	// return false;
	// }
	// }
	// }
	// if (numberOfTextNode > 1) {
	// return false;
	// }
	// return true;
	// }

	private HtmlNode createHtmlElementNode(Element child) {
		String stdTagName = child.tagName().toLowerCase();
		// Here should create a unique number for each newly
		// created WebPageNode
		Integer counter = tagCounterMapper.get(stdTagName);
		if (counter == null) {
			counter = new Integer(1);
		} else {
			counter++;
		}
		tagCounterMapper.put(stdTagName, counter);
		HtmlNode node = new HtmlNode(child, counter);
		return node;
	}

	private HtmlNode createHtmlTextNode(String textNodeContent) {
		HtmlNode node = new HtmlNode(textNodeContent);
		return node;
	}

//	/***
//	 * get all web page paths
//	 * 
//	 * @return a list of WebPagePath instances
//	 */
//	public List<IHtmlPath> getPaths() {
//		return this.parsedPathList;
//	}

//	public List<IHtmlPath> listWebTagPathsWithTextContent() {
//		// Note here we use ArrayList that allows duplicate paths.
//		List<IHtmlPath> webPagePathsWithLeafContent = new ArrayList<IHtmlPath>();
//		for (IHtmlPath path : parsedPathList) {
//			if (path.containsTextContent() || path.getLastNode().getTag().equals("img")) {
//				webPagePathsWithLeafContent.add(path);
//			}
//		}
//		return webPagePathsWithLeafContent;
//	}

}
