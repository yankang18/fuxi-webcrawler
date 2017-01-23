package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableTagDefinition.TABLE_DATA_TAG;
import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableTagDefinition.TABLE_ROW_TAG;
import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableTagDefinition.TABLE_HEADER_TAG;
import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableRecordConstructor.createHorizontalTableRecord;
import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableRecordConstructor.createVerticalTableRecords;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

public class HTMLDataTable {
	
	private List<TableRecord> records;
	private Element elem;
	
	HTMLDataTable(Element element){
		elem = element;
		records = new ArrayList<TableRecord>();
	}

	public static HTMLDataTable createHorizontalDataTable(Element element) {
		
		TableBorder border = calculateTableBorder(element);
		int rowBorderCount = border.getHorizontalRecordCount();
		int colBorderCount = border.getVerticalRecordCount();
		if (colBorderCount == 0 || rowBorderCount == 0) {
			return null;
		}
		
		List<TableRecord> records = createHorizontalTableRecord(element, 0, rowBorderCount - 1, colBorderCount);
		
		HTMLDataTable dt = new HTMLDataTable(element);
		dt.setTableRecords(records); 
		return dt;
	}

	public static HTMLDataTable createVerticalDataTable(Element element) {
		
		TableBorder border = calculateTableBorder(element);
		int colBorderCount = border.getHorizontalRecordCount();
		int rowBorderCount = border.getVerticalRecordCount();
		if (colBorderCount == 0 || rowBorderCount == 0) {
			return null;
		}
		System.out.println("rowBorderCount: " + rowBorderCount);
		System.out.println("colBorderCount: " + colBorderCount);
		
		List<TableRecord> records = createVerticalTableRecords(element, 0, rowBorderCount - 1, colBorderCount);
		
		HTMLDataTable dt = new HTMLDataTable(element);
		dt.setTableRecords(records); 
		return dt;
	}

	public List<TableRecord> getTableRecords(){
		return records;
	}
	
	private static TableBorder calculateTableBorder(Element element) {
		TableBorder border = new TableBorder();

		int horizontalRecordCount = 0;
		int verticalRecordCount = 0;

		System.out.println(element.children().size());
		System.out.println(element.html());
		for (Element child : element.children()) {
			if (TABLE_ROW_TAG.equals(child.tagName())) {
				horizontalRecordCount++;
				int tempVerticalRecordCount = 0;
				for (Element c : child.children()) {
					if (TABLE_DATA_TAG.equals(c.tagName()) || TABLE_HEADER_TAG.equals(c.tagName())) {
						tempVerticalRecordCount++;
					} else {
						// TODO: throw exception
					}
				}
				
				// record the max number of column across all rows
				if (tempVerticalRecordCount > verticalRecordCount) {
					verticalRecordCount = tempVerticalRecordCount;
				}
				
			} else {
				// TODO: throw exception
			}
		}

		border.setHorizontalRecordCount(horizontalRecordCount);
		border.setVerticalRecordCount(verticalRecordCount);
		return border;
	}
	
	private void setTableRecords(List<TableRecord> records) {
		this.records = records;
	}
	
	/**
	 * 
	 * @author yankang
	 *
	 */
	private static class TableBorder {

		private int verticalRecordCount;
		private int horizontalRecordCount;

		/**
		 * @return the verticalRecordCount
		 */
		public int getVerticalRecordCount() {
			return verticalRecordCount;
		}

		/**
		 * @param verticalRecordCount
		 *            the verticalRecordCount to set
		 */
		public void setVerticalRecordCount(int verticalRecordCount) {
			this.verticalRecordCount = verticalRecordCount;
		}

		/**
		 * @return the horizontalRecordCount
		 */
		public int getHorizontalRecordCount() {
			return horizontalRecordCount;
		}

		/**
		 * @param horizontalRecordCount
		 *            the horizontalRecordCount to set
		 */
		public void setHorizontalRecordCount(int horizontalRecordCount) {
			this.horizontalRecordCount = horizontalRecordCount;
		}
	}
}
