package umbc.ebiquity.kang.webtable.core;

import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import umbc.ebiquity.kang.webtable.IAttributeHolder;
import umbc.ebiquity.kang.webtable.ITagAttributeHolder;
import umbc.ebiquity.kang.webtable.ITagHolder;

public class DataCell implements IAttributeHolder, ITagHolder, ITagAttributeHolder {

	public enum DataCellType {
		Value, Element
	}

	private Element elem;
	private TextNode node;
	private IAttributeHolder attributeReader;
	private String tagPath;
	private DataCellType dataCellType;

	public DataCell(Element element, String parentTagPath) {
		elem = element;
		attributeReader = new DefaultAttributeHolder(element);
		tagPath = parentTagPath + "." + getTagName();
		dataCellType = DataCellType.Element;
	}

	public DataCell(TextNode textNode, String parentTagPath) {
		attributeReader = new DefaultAttributeHolder();
		tagPath = parentTagPath + "." + getTagName();
		node = textNode;
		dataCellType = DataCellType.Value;
	}

	@Override
	public String getAttributeValue(String attributeName) {
		return attributeReader.getAttributeValue(attributeName);
	}

	@Override
	public String getTagName() {
		return elem == null ? "text" : elem.tagName().toLowerCase();
	}

	@Override
	public String getTagPath() {
		return tagPath;
	}

	public Node getWrappedNode() {
		return elem == null ? node : elem;
	}
	
	public String getValue() {
		return elem == null ? node.getWholeText().trim() : elem.text().trim();
	}

	/**
	 * @return the dataCellType
	 */
	public DataCellType getDataCellType() {
		return dataCellType;
	}

	@Override
	public Map<String, String> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}
}
