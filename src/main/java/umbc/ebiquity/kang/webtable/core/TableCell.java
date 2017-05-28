package umbc.ebiquity.kang.webtable.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import umbc.ebiquity.kang.webtable.IAttributeHolder;
import umbc.ebiquity.kang.webtable.IDataCellHolder;
import umbc.ebiquity.kang.webtable.ITagAttributeHolder;
import umbc.ebiquity.kang.webtable.ITagHolder;

public class TableCell implements IDataCellHolder, IAttributeHolder, ITagHolder, ITagAttributeHolder {
	
	private Element elem;
	private Map<String, DataCell> key2dataMap;
	private IAttributeHolder attributeHolder;
	private String tagPath;
	private int depth;

	public TableCell(Element element, String parentTagPath, int dataCellRecordingDepth) {
		elem = element;
		attributeHolder = new DefaultAttributeHolder(element);
		key2dataMap = new LinkedHashMap<String, DataCell>();
		tagPath = parentTagPath + "." + getTagName();
		depth = dataCellRecordingDepth;
		addDataCells(null, tagPath, element.childNodes(), 0, key2dataMap); 
	}
	
	public TableCell(Element element, String parentTagPath) {
		this(element, parentTagPath, 2);
	}

	public void addDataCells(String parentKey, String parentPath, List<Node> nodes, int depthTraversed,
			Map<String, DataCell> dataCellRegistry) {
		if (depthTraversed == getDepth()) {
			return;
		}
		depthTraversed++;
		int counter = 0;
		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if (node instanceof Element) {
				Element elem = (Element) node;
				DataCell dc = new DataCell(elem, parentPath);
				String dataCellKey = createDataCellKey(parentKey, counter);
				dataCellRegistry.put(dataCellKey, dc);
				addDataCells(dataCellKey, dc.getTagPath(), elem.childNodes(), depthTraversed, dataCellRegistry);
				counter++;
			} else if (node instanceof TextNode) {
				TextNode textNode = (TextNode) node;
				if (isNotEmpty(textNode.text())) {
					DataCell dc = new DataCell(textNode, parentPath);
					dataCellRegistry.put(createDataCellKey(parentKey, counter), dc);
					counter++;
				}
			}
		}
	}

	public String createDataCellKey(String parentKey, int counter) {
		if (parentKey == null) {
			return String.valueOf(counter);
		} else {
			return String.valueOf(parentKey) + "." + String.valueOf(counter);
		}
	}

	private boolean isNotEmpty(String text) {
		return !"".equals(text.trim());
	}

	@Override
	public String getAttributeValue(String attributeName) {
		return attributeHolder.getAttributeValue(attributeName);
	}
	
	@Override
	public DataCell getDataCell(String key){
		return key2dataMap.get(key);
	}
	
	@Override
	public Set<String> getDataCellKeySet(){
		return key2dataMap.keySet();
	}
	
	@Override
	public List<DataCell> getDataCells(){
		return new ArrayList<DataCell>(key2dataMap.values());
	}
	
	public Element getWrappedElement(){
		return elem;
	}
	
	@Override
	public String getTagName(){
		return elem.tagName().trim();
	}

	@Override
	public String getTagPath(){
		return tagPath;
	}

	/**
	 * @return the depth
	 */
	private int getDepth() {
		return depth;
	}

	@Override
	public Map<String, String> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

}
