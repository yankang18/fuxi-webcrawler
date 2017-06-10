package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.AbstractHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlayRefiner;
import umbc.ebiquity.kang.htmltable.ITagAttributeHolder;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.websiteparser.support.ITemplateNodeMatcher;
import umbc.ebiquity.kang.websiteparser.support.ITemplateNodeMatcherRegistry;

public class TemplateNodePruner implements IHTMLTreeOverlayRefiner {

	private ITemplateNodeMatcherRegistry matcherRegistry;
	private ITemplateNodeMatcher templateMatcher;
	private TagAttributeHolder tagAttributeHolder;

	public TemplateNodePruner(ITemplateNodeMatcherRegistry templateNodeMatcherRegistry) {
		matcherRegistry = templateNodeMatcherRegistry;
		tagAttributeHolder = new TagAttributeHolder();
	}

	/*
	 * (non-Javadoc)
	 * @see umbc.ebiquity.kang.websiteparser.support.IHTMLTreeOverlayRefiner#refine(umbc.ebiquity.kang.websiteparser.support.IHTMLTreeOverlay)
	 */
	@Override
	public IHTMLTreeOverlay refine(IHTMLTreeOverlay overlay) {
		if (overlay == null) {
			throw new IllegalAccessError("the inputed overlay can not be null");
		}
		templateMatcher = matcherRegistry.getMatcher(overlay.getDomainName());
		IHTMLTreeNode root = overlay.getTreeRoot();
		doPrune(root);
//		overlay.setRootNote(root);
		return overlay;
	}

	private void doPrune(IHTMLTreeNode currentNode) {
		System.out.println("**** doPrune");
		if (currentNode instanceof AbstractHTMLTreeNode) {
			System.out.println("**** 1");
			Element element = ((AbstractHTMLTreeNode) currentNode).getWrappedElement();
			if(element == null) return;
			tagAttributeHolder.setElement(element);
			if (templateMatcher.isMatched(tagAttributeHolder)) {
				System.out.println("****");
				currentNode.getChildren().clear();
			} else {
				for (IHTMLTreeNode child : currentNode.getChildren()) {
					doPrune(child);
				}
			}
		} else {
			for (IHTMLTreeNode child : currentNode.getChildren()) {
				doPrune(child);
			}
		}
	}

	/**
	 * This class serves as an adapter to the place where the instance of
	 * <code>ITagAttributeHolder</code> interface is needed within in outer
	 * class.
	 * 
	 * @author Yan Kang
	 *
	 */
	private final class TagAttributeHolder implements ITagAttributeHolder {
		
		private Element element;
		
		@Override
		public String getTagPath() {
			return "";
		}

		@Override
		public String getTagName() {
			return element.tagName().toLowerCase().trim();
		}

		@Override
		public String getAttributeValue(String attributeName) {
			return element.attr(attributeName);
		}
		
		@Override
		public Map<String, String> getAttributes() {
			Map<String, String> attr2Value = new HashMap<String, String>();
			for (Attribute attr : element.attributes()) {
				attr2Value.put(attr.getKey(), attr.getValue());
			}
			return attr2Value;
		}

		/**
		 * @param element the element to set
		 */
		public void setElement(Element element) {
			this.element = element;
		}
	}
}
