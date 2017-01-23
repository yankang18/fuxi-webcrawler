package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import umbc.ebiquity.kang.htmldocument.IHtmlDocument;
import umbc.ebiquity.kang.htmldocument.IHtmlDocumentPath;
import umbc.ebiquity.kang.htmldocument.impl.HtmlDocumentNode;
import umbc.ebiquity.kang.htmldocument.impl.HtmlDocumentPath;
import umbc.ebiquity.kang.htmldocument.parser.DefaultHtmlDocumentParsedPathsHolder;
import umbc.ebiquity.kang.htmldocument.parser.IHtmlDocumentParsedPathsHolder;
import umbc.ebiquity.kang.textprocessing.util.TextProcessingUtils;
import umbc.ebiquity.kang.websiteparser.ILeafNodeDetermintor;
import umbc.ebiquity.kang.websiteparser.impl.HTMLTags;

public class DefaultWebPagePathsParser {

	private List<IHtmlDocumentPath> webPagePathList;
	private Map<String, Integer> tagCounterMapper;
	private IHtmlDocument webPage;
	private boolean parsed; 
	private ILeafNodeDetermintor leafNodeDetermintor;
	private IHtmlDocumentParsedPathsHolder webPageParsedPathHolder;

	public DefaultWebPagePathsParser(IHtmlDocument webPage) { 
		this.webPage = webPage;
		this.parsed = false;
		this.webPagePathList = new ArrayList<IHtmlDocumentPath>(); 
		this.tagCounterMapper = new HashMap<String, Integer>();
		this.leafNodeDetermintor = new DefaultLeafNodeDetermintor();
	}

	public IHtmlDocumentParsedPathsHolder parse() { 

		if (parsed)
			return webPageParsedPathHolder;

		Document webPageDoc = webPage.getDocument();
		Element body = webPageDoc.body();
		for (Element elem : body.children()) {
			String childTagName = elem.tagName().toLowerCase();
			// System.out.println("## " + childTagName);
			if (isTargetTagName(childTagName)) {
				HtmlDocumentPath path = new HtmlDocumentPath();
				webPagePathList.add(path);
				// path.setHost(this.getPageURLAsString());
				HtmlDocumentNode webPageNode = this.createWebPageNode(elem);
				webPageNode.setLeafNode(false);
				path.addNode(webPageNode);
				appendWebPagePathNode(path, elem);
			}
		}
		parsed = true;
		webPageParsedPathHolder = new DefaultHtmlDocumentParsedPathsHolder(webPage.getUniqueIdentifier(), webPagePathList);
		return webPageParsedPathHolder;
	}

	private boolean isTargetTagName(String childTagName) {
		return !HTMLTags.getEliminatedTags().contains(childTagName) && !HTMLTags.getIgnoredTags().contains(childTagName);
	}

	private void appendWebPagePathNode(HtmlDocumentPath path, Element elem) {

		if (leafNodeDetermintor.isLeafNode(elem)) {
			
			HtmlDocumentNode webPageNode = path.getLastNode();
			HtmlDocumentNode newWebPageNode = this.createWebPageNode(webPageNode.getFullContent());
			newWebPageNode.setLeafNode(true);
			path.addNode(newWebPageNode);
			
		} else {
			
			List<Node> childNodeList = elem.childNodes();
			int numOfChildrenNode = childNodeList.size();

			// Clone paths before navigating children of current element.
			int numOfPaths = numOfChildrenNode;
			HtmlDocumentPath[] webPagePaths = null;
			if (numOfPaths > 0) {
				webPagePaths = new HtmlDocumentPath[numOfPaths];

				/*
				 * The first path is not cloned but the path that has already
				 * been created. We reuse this path to hold the first child of
				 * current element
				 */
				webPagePaths[0] = path;

				/*
				 * Clone other paths to hold children except the first child of
				 * current element
				 */
				for (int i = 1; i < numOfPaths; i++) {
					webPagePaths[i] = path.clone();
					// webPagePaths[i].setHost(this.getPageURLAsString());
				}
			}

			String textContent = "";
			int indexOfNode = 0;
			int indexOfPath = 0;
			for (; indexOfNode < numOfChildrenNode; indexOfNode++) {

				Node firstChildNode = childNodeList.get(indexOfNode);
				if (firstChildNode instanceof TextNode) {
					TextNode textNode = (TextNode) firstChildNode;
					String text = textNode.text();
					if (!TextProcessingUtils.isStringEmpty(text)) {
						textContent += text;
					}

				} else if (firstChildNode instanceof Element) {

					Element elementNode = (Element) firstChildNode;
					String elemText = elementNode.text();
					String elementNodeTagName = elementNode.tagName().toLowerCase();

					if (HTMLTags.getIgnoredTags().contains(elementNodeTagName)) {

						// String text = elementNode.text();
						if (!TextProcessingUtils.isStringEmpty(elemText)) {
							textContent += elemText;
						}

					} else if (HTMLTags.isTopicTag(elementNodeTagName)) {

						// System.out.println("#### Topic Tag: " +
						// elementNodeTagName);
						if (textContent != null && !TextProcessingUtils.isStringEmpty(textContent)) {
							HtmlDocumentNode newWebPageNode = this.createWebPageNode(textContent);
							newWebPageNode.setLeafNode(true);
							HtmlDocumentPath webPagePath = webPagePaths[indexOfPath];
							webPagePath.addNode(newWebPageNode);

							/*
							 * The first path has already been added to the web
							 * page path list. Other paths are clones, thus they
							 * should be added to the web page path list.
							 */
							if (indexOfPath != 0) {
								this.webPagePathList.add(webPagePath);
							}
							indexOfPath++;
							textContent = "";
						}

						// String text = elementNode.text();
						if (!TextProcessingUtils.isStringEmpty(elemText)) {
							// System.out.println("#### content: " +
							// combinedText);
							HtmlDocumentNode newWebPageNode = this.createWebPageNode(elementNode);
							newWebPageNode.setLeafNode(true);
							HtmlDocumentPath webPagePath = webPagePaths[indexOfPath];
							webPagePath.addNode(newWebPageNode);
							if (indexOfPath != 0) {
								// System.out.println("#### *content: " +
								// combinedText);
								this.webPagePathList.add(webPagePath);
							}
							indexOfPath++;
						}

					} else if (HTMLTags.getEliminatedTags().contains(elementNodeTagName)) {

						// TODO: refactor following if statement
						if (textContent != null && !TextProcessingUtils.isStringEmpty(textContent)) {
							HtmlDocumentNode newWebPageNode = this.createWebPageNode(textContent);
							newWebPageNode.setLeafNode(true);
							HtmlDocumentPath webPagePath = webPagePaths[indexOfPath];
							webPagePath.addNode(newWebPageNode);
							if (indexOfPath != 0) {
								this.webPagePathList.add(webPagePath);
							}
							indexOfPath++;
							textContent = "";
						}

					} else {

						if (textContent != null && !TextProcessingUtils.isStringEmpty(textContent)) {
							HtmlDocumentNode newWebPageNode = this.createWebPageNode(textContent);
							newWebPageNode.setLeafNode(true);
							HtmlDocumentPath webPagePath = webPagePaths[indexOfPath];
							webPagePath.addNode(newWebPageNode);
							if (indexOfPath != 0) {
								this.webPagePathList.add(webPagePath);
							}
							indexOfPath++;
							textContent = "";
						}

						if (!TextProcessingUtils.isStringEmpty(elemText)) {

							// Here should create a unique number for each newly
							// created WebPageNode
							HtmlDocumentNode newWebPageNode = this.createWebPageNode(elementNode);
							HtmlDocumentPath webPagePath = webPagePaths[indexOfPath];
							webPagePath.addNode(newWebPageNode);
							if (indexOfPath != 0) {
								this.webPagePathList.add(webPagePath);
							}
							newWebPageNode.setLeafNode(false);
							this.appendWebPagePathNode(webPagePath, elementNode);
							indexOfPath++;
						}
					}
				}
			}

			// This is dealing with the situation where the last node of current
			// element is a text node.
			if (textContent != null && !TextProcessingUtils.isStringEmpty(textContent)) {
				HtmlDocumentNode newWebPageNode = this.createWebPageNode(textContent);
				newWebPageNode.setLeafNode(true);
				HtmlDocumentPath webPagePath = webPagePaths[indexOfPath];
				webPagePath.addNode(newWebPageNode);
				if (indexOfPath != 0) {
					this.webPagePathList.add(webPagePath);
				}
			}
		}
	}

	// TODO: what is text node.
	// in what situation an element contains one text node. show an example.
	// in what situation an element contains multiple test nodes. show an
	// example.
	/**
	 * To check if a element contains only one text node that is not empty. One
	 * special case is that if this element contains multiple br tag, but only
	 * one non-empty text node, we still treat this element as the one contains
	 * only one text node that is not empty.
	 */
//	private boolean containsOnlyOneTextNode(Element element) {
//		int numberOfTextNode = 0;
//		for (Node child : element.childNodes()) {
//			if (child instanceof TextNode) {
//				if (!TextProcessingUtils.isStringEmpty(((TextNode) child).text())) {
//					numberOfTextNode++;
//				}
//			} else if (child instanceof Element) {
//				if (!"br".equals(((Element) child).tagName().toLowerCase())) {
//					return false;
//				}
//			}
//		}
//		if (numberOfTextNode > 1) {
//			return false;
//		}
//		return true;
//	}

	private HtmlDocumentNode createWebPageNode(Element child) {
		String stdTagName = child.tagName().toLowerCase();
		Integer counter = tagCounterMapper.get(stdTagName);
		if (counter == null) {
			counter = new Integer(1);
			tagCounterMapper.put(stdTagName, counter);
		} else {
			counter++;
			tagCounterMapper.put(stdTagName, counter);
		}
		HtmlDocumentNode node = new HtmlDocumentNode(child, counter);
		return node;
	}

	private HtmlDocumentNode createWebPageNode(String textNodeContent) {
		HtmlDocumentNode node = new HtmlDocumentNode(textNodeContent);
		return node;
	}

	/***
	 * get all web page paths
	 * 
	 * @return a list of WebPagePath instances
	 */
	public List<IHtmlDocumentPath> getPaths() {
		return this.webPagePathList;
	}

	public List<IHtmlDocumentPath> listWebTagPathsWithTextContent() {
		// Note here we use ArrayList that allows duplicate paths.
		List<IHtmlDocumentPath> webPagePathsWithLeafContent = new ArrayList<IHtmlDocumentPath>();
		for (IHtmlDocumentPath path : webPagePathList) {
			if (path.containsTextContent() || path.getLastNode().getTag().equals("img")) {
				webPagePathsWithLeafContent.add(path);
			}
		}
		return webPagePathsWithLeafContent;
	}
}
