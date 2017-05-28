package umbc.ebiquity.kang.webtable.core;

import static umbc.ebiquity.kang.webtable.core.HTMLTableTagDefinition.TABLE_BODY_TAG;
import static umbc.ebiquity.kang.webtable.core.HTMLTableTagDefinition.TABLE_DATA_TAG;
import static umbc.ebiquity.kang.webtable.core.HTMLTableTagDefinition.TABLE_HEADER_SECTION_TAG;
import static umbc.ebiquity.kang.webtable.core.HTMLTableTagDefinition.TABLE_HEADER_TAG;
import static umbc.ebiquity.kang.webtable.core.HTMLTableTagDefinition.TABLE_ROW_TAG;
import static umbc.ebiquity.kang.webtable.core.HTMLTableTagDefinition.TABLE_TAG;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.util.BasicValidator;

public class HTMLTableValidator {

	/**
	 * Checks whether the input element is table element (i.e., with a tag name of
	 * "table" or "tbody") that may directly contain data.
	 * 
	 * @param tableElement
	 *            the table element
	 */
	public static void isDataTable(Element element) {
		BasicValidator.notNull(element, "The input table element cannot be null");
		if (!HTMLTableTagDefinition.isTableTag(element.tagName()))
			throw new IllegalArgumentException("The input element must not be null");

		if (element.getElementsByTag("tbody").isEmpty()) {
			throw new IllegalArgumentException("The input element must be either a table element or a tbody element");
		}
	}

	/**
	 * Checks whether the input element is table element (i.e., with a tag name of
	 * "table").
	 * 
	 * @param tableElement
	 *            the table element
	 */
	public static void isTable(Element element) {
		BasicValidator.notNull(element, "The input table element cannot be null");
		if (!HTMLTableTagDefinition.isTableTag(element.tagName())) {
			throw new IllegalArgumentException("the specified Element should be a HTML table");
		}
	}
	
	/**
	 * Validates the input table element structure that should follow
	 * rules: </br>
	 * (1) The input element should be table, tbody or thead element.</br>
	 * (2) The input should not be empty.</br>
	 * (3) The table, tbody or thead should and only contain tr elements as it
	 * children </br>
	 * (4) Each tr element should and only contain td, th elements as it
	 * children </br>
	 * 
	 * @param element
	 *            the table element
	 * @return a TableElementValidationResult
	 */
	public static TableElementValidationResult validateTableStructure(Element element) {
		BasicValidator.notNull(element, "The input table element cannot be null");
		
		String tagName = element.tagName().trim();
		TableElementValidationResult result = new TableElementValidationResult();
		result.setValid(true);

		if (!tagName.equalsIgnoreCase(TABLE_TAG) && !tagName.equalsIgnoreCase(TABLE_BODY_TAG)
				&& !tagName.equalsIgnoreCase(TABLE_HEADER_SECTION_TAG)) {
			String msg = "Element should be " + TABLE_TAG + ", " + TABLE_BODY_TAG + ", or " + TABLE_HEADER_SECTION_TAG;
			result.setMessage(msg);
			result.setValid(false);
		}

		if (element.children().size() <= 0) {
			String msg = "Element should have content.";
			result.setMessage(msg);
			result.setValid(false);
		}

		for (Element e : element.children()) {
			if (!e.tagName().equalsIgnoreCase(TABLE_ROW_TAG)) {
				String msg = "Element should only contain " + TABLE_ROW_TAG + " elements";
				result.setMessage(msg);
				result.setValid(false);
				return result;
			}

			if (e.children().size() <= 0) {
				String msg = TABLE_ROW_TAG + " element should have content.";
				result.setMessage(msg);
				result.setValid(false);
				return result;
			}

			for (Element c : e.children()) {
				if (!TABLE_DATA_TAG.equals(c.tagName()) && !TABLE_HEADER_TAG.equals(c.tagName())) {
					String msg = TABLE_ROW_TAG + " elements should only contain either " + TABLE_DATA_TAG + " or "
							+ TABLE_HEADER_TAG;
					result.setMessage(msg);
					result.setValid(false);
					return result;
				}
			}
		}

		return result;
	}

	public static class TableElementValidationResult {
		private boolean isValid;
		private String message;

		/**
		 * @return the isValid
		 */
		public boolean isValid() {
			return isValid;
		}

		/**
		 * @param isValid
		 *            the isValid to set
		 */
		public void setValid(boolean isValid) {
			this.isValid = isValid;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @param message
		 *            the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}
	}
}
