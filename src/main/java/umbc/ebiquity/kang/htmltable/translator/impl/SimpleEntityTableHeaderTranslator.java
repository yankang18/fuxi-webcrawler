package umbc.ebiquity.kang.htmltable.translator.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeEntityNode;
import umbc.ebiquity.kang.htmldocument.util.HtmlImageUtil;
import umbc.ebiquity.kang.htmltable.core.TableCell;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.translator.IEntityTableHeaderTranslator;
import umbc.ebiquity.kang.htmltable.translator.TableHeaderTranslationResult; 

public class SimpleEntityTableHeaderTranslator implements IEntityTableHeaderTranslator {

	private double threshold = 0.60;
	
	@Override
	public TableHeaderTranslationResult translator(List<TableRecord> headerRecords, int skipCellNum) {
		TableHeaderTranslationResult result = new TableHeaderTranslationResult();
		for (TableRecord record : headerRecords) {
			List<TableCell> tableCells = record.getTableCells();
			
			List<HTMLTreeEntityNode> entityHeaderNodes = new ArrayList<>(headerRecords.size());
				
			double txtFieldCount = 0;
			double imgFieldCount = 0;
			
			int i = 0;
			for (; i < skipCellNum; i++) {
				entityHeaderNodes.add(createEntityNode(tableCells.get(i).getWrappedElement()));
			}
			for (; i < tableCells.size(); i++) {
				TableCell cell = tableCells.get(i);
				Element element = cell.getWrappedElement();
				if (!isTextEmpty(element)) {
					entityHeaderNodes.add(createEntityNode(element));
					txtFieldCount++;
				} else if (containsImage(element)) {
					entityHeaderNodes.add(createImageEntityNode(element));
					imgFieldCount++;
				}
			}
			if (txtFieldCount / tableCells.size() >= threshold) {
				result.setPrimaryHeaderRecord(entityHeaderNodes);
			}
			if (imgFieldCount / tableCells.size() >= threshold) {
				result.addSecondaryHeaderRecord(entityHeaderNodes);
			}
		}
		return result; 
	}

	private boolean containsImage(Element element) {
		Elements imgs = element.select("img[src]");
		for (Element img : imgs) {
			String src = img.attr("src");
			if(!isEmpty(src))
				return true;
			
		}
		return false;
	}

	private boolean isTextEmpty(Element element) {
		return element != null && isEmpty(element.text());
	}

	private boolean isEmpty(String text) {
		// TODO: may use reliable third party library to do this task
		return text != null && text.trim().equals("");
	}

	private HTMLTreeEntityNode createEntityNode(Element element) {
		HTMLTreeEntityNode entityNode = new HTMLTreeEntityNode(element, extractContent(element));
		return entityNode;
	}

	private HTMLTreeEntityNode createImageEntityNode(Element element) {
		element = getFirstImageElement(element);
		HTMLTreeEntityNode entityNode = new HTMLTreeEntityNode(element, HtmlImageUtil.getText(element));
		return entityNode;
	}

	private Element getFirstImageElement(Element element) {
		return element.getElementsByTag("img").get(0); 
	}

	private String extractContent(Element element) {
		return element != null ? element.text() : "";
	}
}
