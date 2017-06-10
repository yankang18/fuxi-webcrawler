package umbc.ebiquity.kang.htmltable.similarity.impl;

import umbc.ebiquity.kang.htmltable.similarity.IAttributesSimilarity;
import umbc.ebiquity.kang.htmltable.similarity.IDataCellsSimilarity;
import umbc.ebiquity.kang.htmltable.similarity.ITableRecordSimilarity;
import umbc.ebiquity.kang.htmltable.similarity.ITableRecordsSimilarity;
import umbc.ebiquity.kang.htmltable.similarity.ITableRecordsSimiliartySuite;

public class TableRecordsSimiliartySuite implements ITableRecordsSimiliartySuite {
	
	private IAttributesSimilarity attributesSimilarity;
	private IDataCellsSimilarity dataCellsSimilarity;
	private ITableRecordSimilarity tableRecordSimilarity;
	private ITableRecordsSimilarity tableRecordsSimilarity;

	TableRecordsSimiliartySuite(
			ITableRecordsSimilarity tableRecordsSimilarity,
			ITableRecordSimilarity tableRecordSimilarity, 
			IDataCellsSimilarity dataCellsSimilarity,
			IAttributesSimilarity attributesSimilarity) {
		
		this.attributesSimilarity = attributesSimilarity;
		this.dataCellsSimilarity = dataCellsSimilarity;
		this.tableRecordSimilarity = tableRecordSimilarity;
		this.tableRecordsSimilarity = tableRecordsSimilarity;
	}

	/**
	 * @return the attributesSimilarity
	 */
	@Override
	public IAttributesSimilarity getAttributesSimilarity() {
		return attributesSimilarity;
	}

	/**
	 * @return the dataCellsSimilarity
	 */
	@Override
	public IDataCellsSimilarity getDataCellsSimilarity() {
		return dataCellsSimilarity;
	}

	/**
	 * @return the tableRecordSimilarity
	 */
	@Override
	public ITableRecordSimilarity getTableRecordSimilarity() {
		return tableRecordSimilarity;
	}

	/**
	 * @return the tableRecordsSimilarity
	 */
	@Override
	public ITableRecordsSimilarity getTableRecordsSimilarity() {
		return tableRecordsSimilarity;
	}
}
