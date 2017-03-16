package umbc.ebiquity.kang.webtable.spliter.impl;

import static umbc.ebiquity.kang.webtable.spliter.impl.HTMLTableTagDefinition.TABLE_DATA_TAG;
import static umbc.ebiquity.kang.webtable.spliter.impl.HTMLTableTagDefinition.TABLE_HEADER_TAG;
import static umbc.ebiquity.kang.webtable.spliter.impl.HTMLTableTagDefinition.TABLE_ROW_TAG;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLTableRecordsCounter {
	
	public static int getMinVerticalColumnCount(Element element) {
		Elements elements = element.children();
		return getMinVerticalColumnCount(elements);
	}

	public static int getMinVerticalColumnCount(Elements elements) {
		int minVerticalColCount = Integer.MAX_VALUE;
		for (Element e : elements) {
			int count = 0;
			for (Element c : e.children()) {
				if (TABLE_DATA_TAG.equals(c.tagName()) || TABLE_HEADER_TAG.equals(c.tagName())) {
					count++;
				}
			}
			if (count < minVerticalColCount) {
				minVerticalColCount = count;
			}
		}
		return minVerticalColCount;
	}
	
	public static int getMaxVerticalColumnCount(Element element) {
		Elements elements = element.children();
		return getMaxVerticalColumnCount(elements);
	}

	public static int getMaxVerticalColumnCount(Elements elements) {
		int maxVerticalColCount = 0;
		for (Element e : elements) {
			int count = 0;
			for (Element c : e.children()) {
				if (TABLE_DATA_TAG.equals(c.tagName()) || TABLE_HEADER_TAG.equals(c.tagName())) {
					count++;
				}
			}
			if (count > maxVerticalColCount) {
				maxVerticalColCount = count;
			}
		}
		return maxVerticalColCount;
	}
	
	/**
	 * Calculate the of HTML table represented by
	 * {@link org.jsoup.nodes.Element}. Specifically, it calculates the number
	 * of rows and number of columns in the specified HTML table.
	 * <p>
	 * The number of columns may be variant from row to row. We define the
	 * number of columns of this HTML table as the maximal number of columns
	 * across all the rows.
	 * </p>
	 * 
	 * @param element
	 *            the HTML table represented by {@link org.jsoup.nodes.Element}
	 * @return a <code>TableBorder</code> holding the table border information
	 */
	public static TableBorder calculateTableBorder(Element element) {
		TableBorder border = new TableBorder();

		int horizontalRecordCount = 0;
		int verticalRecordCount = 0;

//		System.out.println(element.children().size());
//		System.out.println(element.html());
		for (Element child : element.children()) {
			if (TABLE_ROW_TAG.equals(child.tagName())) {
				horizontalRecordCount++;
				int tempVerticalRecordCount = 0;
				for (Element c : child.children()) {
					if (TABLE_DATA_TAG.equals(c.tagName()) || TABLE_HEADER_TAG.equals(c.tagName())) {
						tempVerticalRecordCount++;
					} else {
						// TODO: throw exception or omit the column ???
					}
				}

				// record the max number of column across all rows
				if (tempVerticalRecordCount > verticalRecordCount) {
					verticalRecordCount = tempVerticalRecordCount;
				}

			} else {
				// TODO: throw exception or omit the column ???
			}
		}

		border.setHorizontalRecordCount(horizontalRecordCount);
		border.setVerticalRecordCount(verticalRecordCount);
		return border;
	}
	
	/**
	 * 
	 * @author yankang
	 *
	 */
	public static class TableBorder {

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
