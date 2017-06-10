package umbc.ebiquity.kang.htmltable.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmltable.IAttributeHolder;
import umbc.ebiquity.kang.htmltable.ITagAttributeHolder;
import umbc.ebiquity.kang.htmltable.ITagHolder;

public class TableRecord implements IAttributeHolder, ITagHolder, ITagAttributeHolder, Comparable<TableRecord> {

	private int sequenceNumber;
	private Element elem;
	private List<TableCell> tableCellList;
	private IAttributeHolder attributeReader;
	private String tagPath;

	public TableRecord(Element element, String parentTagPath) {
		elem = element;
		tableCellList = new ArrayList<TableCell>();
		attributeReader = new DefaultAttributeHolder(element);
		tagPath = parentTagPath + "." + getTagName();
		sequenceNumber = 0;
	}

	public TableRecord(String parentTagPath) {
		tableCellList = new ArrayList<TableCell>();
		attributeReader = new DefaultAttributeHolder();
		tagPath = parentTagPath + "." + getTagName();
		sequenceNumber = 0;
	}

	public void addData(TableCell td) {
		tableCellList.add(td);
	}

	@Override
	public String getAttributeValue(String attributeName) {
		return attributeReader.getAttributeValue(attributeName);
	}

	public List<TableCell> getTableCells() {
		return tableCellList;
	}

	@Override
	public String getTagName() {
		return elem == null ? "tr" : elem.tagName().trim();
	}

	@Override
	public String getTagPath() {
		return tagPath;
	}

	/**
	 * @return the sequenceNumber
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber the sequenceNumber to set
	 */
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public int compareTo(TableRecord o) {
		if (this.getSequenceNumber() < o.getSequenceNumber()) {
			return -1;
		} else if (this.getSequenceNumber() > o.getSequenceNumber()) {
			return 1;
		}
		return 0;
	}

	@Override
	public Map<String, String> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

}
