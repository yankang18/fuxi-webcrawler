package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.websiteparser.tableresolver.IAttributeHolder;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITaggedEntity;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITaggedEntityAttributeHolder;

public class TableRecord implements IAttributeHolder, ITaggedEntity, ITaggedEntityAttributeHolder, Comparable<TableRecord> {

	private int sequenceNumber;
	private Element elem;
	private List<TableCell> tableCellList;
	private IAttributeHolder attributeReader;
	private String tagPath;

	public TableRecord(Element element, String parentTagPath) {
		elem = element;
		tableCellList = new ArrayList<TableCell>();
		attributeReader = new SimpleAttributeHolder(element);
		tagPath = parentTagPath + "." + getTagName();
		sequenceNumber = 0;
	}

	public TableRecord(String parentTagPath) {
		tableCellList = new ArrayList<TableCell>();
		attributeReader = new SimpleAttributeHolder();
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

}
