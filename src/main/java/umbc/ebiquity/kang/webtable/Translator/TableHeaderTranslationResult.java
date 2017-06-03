package umbc.ebiquity.kang.webtable.Translator;

import java.util.ArrayList;
import java.util.List;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeEntityNode;

/**
 * 
 * @author yankang
 *
 */
public class TableHeaderTranslationResult {

	private boolean hasPrimaryHeaderRecord;
	private List<HTMLTreeEntityNode> primaryHeaderRecord;
	private List<List<HTMLTreeEntityNode>> secondaryHeaderRecords = new ArrayList<>();;

	/**
	 * @return the primaryHeaderRecord
	 */
	public List<HTMLTreeEntityNode> getPrimaryHeaderRecord() {
		return primaryHeaderRecord;
	}

	/**
	 * @param primaryHeaderRecord
	 *            the primaryHeaderRecord to set
	 */
	public void setPrimaryHeaderRecord(List<HTMLTreeEntityNode> primaryHeaderRecord) {
		this.primaryHeaderRecord = primaryHeaderRecord;

		if (primaryHeaderRecord != null)
			hasPrimaryHeaderRecord = true;
	}

	public void addSecondaryHeaderRecord(List<HTMLTreeEntityNode> secondaryHeaderRecord) {
		secondaryHeaderRecords.add(secondaryHeaderRecord);
	}

	/**
	 * 
	 * @return
	 */
	public List<List<HTMLTreeEntityNode>> getSecondaryHeaderRecords() {
		return secondaryHeaderRecords;
	}

	/**
	 * @return the hasPrimaryHeaderRecord
	 */
	public boolean hasPrimaryHeaderRecord() {
		return hasPrimaryHeaderRecord;
	}

}
