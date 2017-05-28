package umbc.ebiquity.kang.webtable.resolver;

import java.util.ArrayList;
import java.util.List;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.webtable.core.TableRecord;

/**
 * 
 * @author yankang
 *
 */
public class HeaderIdentificationResult {
	
	private boolean hasPrimaryHeaderRecord;
	private List<IHTMLTreeNode> primaryHeaderRecord;
	private List<List<IHTMLTreeNode>> secondaryHeaderRecords = new ArrayList<>();;
	
	/**
	 * @return the primaryHeaderRecord
	 */
	public List<IHTMLTreeNode> getPrimaryHeaderRecord() {
		return primaryHeaderRecord;
	}

	/**
	 * @param primaryHeaderRecord
	 *            the primaryHeaderRecord to set
	 */
	public void setPrimaryHeaderRecord(List<IHTMLTreeNode> primaryHeaderRecord) {
		this.primaryHeaderRecord = primaryHeaderRecord;

		if (primaryHeaderRecord != null)
			hasPrimaryHeaderRecord = true;
	}

	public void addSecondaryHeaderRecord(List<IHTMLTreeNode> secondaryHeaderRecord) {
		secondaryHeaderRecords.add(secondaryHeaderRecord);
	}

	/**
	 * 
	 * @return
	 */
	public List<List<IHTMLTreeNode>> getSecondaryHeaderRecords() {
		return secondaryHeaderRecords;
	}

	/**
	 * @return the hasPrimaryHeaderRecord
	 */
	public boolean hasPrimaryHeaderRecord() {
		return hasPrimaryHeaderRecord;
	}

}
