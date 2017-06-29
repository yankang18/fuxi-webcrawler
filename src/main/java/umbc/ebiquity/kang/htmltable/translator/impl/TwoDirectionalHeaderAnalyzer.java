package umbc.ebiquity.kang.htmltable.translator.impl;

import java.util.List;

import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderDelimitedTable;

public class TwoDirectionalHeaderAnalyzer {

	public enum TwoDirectionalHeaderType {
		VerticalPropertyHeader, HorizontalPropertyHeader
	}

	public TwoDirectionalHeaderType analyze(HeaderDelimitedTable table) {
		
		List<TableRecord> horizontalHeaderRecords = table.getHorizontalHeaderRecords();
		List<TableRecord> verticalHeaderRecords = table.getVerticalHeaderRecords();

		int skipRowNum = verticalHeaderRecords.size();
		int skipColNum = horizontalHeaderRecords.size();
		
		
		List<TableRecord> dataRecords = table.getVerticalDataRecords();
		
		computeDataTypeDiversity(dataRecords, skipColNum);
		
		dataRecords = table.getHorizontalDataRecords();
		
		computeDataTypeDiversity(dataRecords, skipRowNum);

		return null;
	}

	private void computeDataTypeDiversity(List<TableRecord> dataRecords, int skipColNum) {
		// TODO Auto-generated method stub
		
	}

}
