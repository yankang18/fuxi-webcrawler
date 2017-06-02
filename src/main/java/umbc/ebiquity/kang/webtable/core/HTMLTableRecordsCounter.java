package umbc.ebiquity.kang.webtable.core;

import static umbc.ebiquity.kang.webtable.core.HTMLTableTagDefinition.TABLE_DATA_TAG;
import static umbc.ebiquity.kang.webtable.core.HTMLTableTagDefinition.TABLE_HEADER_TAG;
import static umbc.ebiquity.kang.webtable.core.HTMLTableTagDefinition.TABLE_ROW_TAG;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.webtable.util.HTMLTableValidator;
import umbc.ebiquity.kang.webtable.util.HTMLTableValidator.TableElementValidationResult;

public class HTMLTableRecordsCounter {

	/**
	 * Gets minimal number of column counts across all the rows of table
	 * represented by the input <code>element</code>
	 * 
	 * @param elements
	 *            the elements represent rows of table
	 * @return the minimal number of column counts
	 */
	public static int getMinVerticalColumnCount(Element element) {
		Elements elements = element.children();
		return getMinVerticalColumnCount(elements);
	}

	/**
	 * Gets minimal number of column counts across all the rows represented by
	 * the input <code>elements</code>
	 * 
	 * @param elements
	 *            the elements represent rows of table
	 * @return the minimal number of column counts
	 */
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

	// public static int getMaxVerticalColumnCount(Element element) {
	// Elements elements = element.children();
	// return getMaxVerticalColumnCount(elements);
	// }
	//
	// public static int getMaxVerticalColumnCount(Elements elements) {
	// int maxVerticalColCount = 0;
	// for (Element e : elements) {
	// int count = 0;
	// for (Element c : e.children()) {
	// if (TABLE_DATA_TAG.equals(c.tagName()) ||
	// TABLE_HEADER_TAG.equals(c.tagName())) {
	// count++;
	// }
	// }
	// if (count > maxVerticalColCount) {
	// maxVerticalColCount = count;
	// }
	// }
	// return maxVerticalColCount;
	// }
	
	/**
	 * Calculate the of HTML table represented by
	 * {@link org.jsoup.nodes.Element}. Specifically, it calculates the number
	 * of rows and number of columns in the specified HTML table.
	 * <p>
	 * The number of columns may vary from row to row. We define the number of
	 * columns of this HTML table as the minimal number of columns across all
	 * the rows.
	 * </p>
	 * 
	 * @param element
	 *            the HTML table represented by {@link org.jsoup.nodes.Element}
	 * @return a <code>TableBorder</code> holding the table border information
	 */
	public static TableBorder calculateTableBorder(Element element) {
		validateTableStructure(element);

		TableBorder border = new TableBorder();
		int verticalRecordCount = Integer.MAX_VALUE;

		for (Element child : element.children()) {
			int tempVerticalRecordCount = child.children().size();
			// record the min number of column across all rows
			if (tempVerticalRecordCount < verticalRecordCount) {
				verticalRecordCount = tempVerticalRecordCount;
			}
		}

		border.setHorizontalRecordCount(element.children().size());
		border.setVerticalRecordCount(verticalRecordCount);
		return border;
	}
	
	private static void validateTableStructure(Element element) {
		TableElementValidationResult result = HTMLTableValidator.validateTableStructure(element);
		if (!result.isValid()) {
			throw new IllegalArgumentException(result.getMessage());
		}
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
