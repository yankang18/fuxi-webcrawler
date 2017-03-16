package umbc.ebiquity.kang.webtable.spliter.impl;

import static umbc.ebiquity.kang.webtable.spliter.impl.HTMLTableTagDefinition.TABLE_BODY_TAG;
import static umbc.ebiquity.kang.webtable.spliter.impl.HTMLTableTagDefinition.TABLE_DATA_TAG;
import static umbc.ebiquity.kang.webtable.spliter.impl.HTMLTableTagDefinition.TABLE_HEADER_SECTION_TAG;
import static umbc.ebiquity.kang.webtable.spliter.impl.HTMLTableTagDefinition.TABLE_HEADER_TAG;
import static umbc.ebiquity.kang.webtable.spliter.impl.HTMLTableTagDefinition.TABLE_ROW_TAG;
import static umbc.ebiquity.kang.webtable.spliter.impl.HTMLTableTagDefinition.TABLE_TAG;

import org.jsoup.nodes.Element;

public class HTMLTableValidator {

	/**
	 * Validates if the specified element with a tag name of "table".
	 * 
	 * @param tableElement
	 *            the table element
	 */
	public static void validateTableElement(Element tableElement) {
		if (!HTMLTableTagDefinition.isTableTag(tableElement.tagName())) {
			throw new IllegalArgumentException("the specified Element should be a HTML table");
		}
	}
	
	/**
	 * 
	 * @param element
	 * @return
	 */
	public static TableElementValidationResult validateTableChildElement(Element element) {
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
			}

			if (e.children().size() <= 0) {
				String msg = TABLE_ROW_TAG + " element should have content.";
				result.setMessage(msg);
				result.setValid(false);
			}

			for (Element c : e.children()) {
				if (!TABLE_DATA_TAG.equals(c.tagName()) && !TABLE_HEADER_TAG.equals(c.tagName())) {
					String msg = TABLE_ROW_TAG + " elements should only contain either " + TABLE_DATA_TAG + " or "
							+ TABLE_HEADER_TAG;
					result.setMessage(msg);
					result.setValid(false);
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
