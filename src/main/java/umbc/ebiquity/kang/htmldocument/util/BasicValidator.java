package umbc.ebiquity.kang.htmldocument.util;

public class BasicValidator {
	
	private BasicValidator() {
	}

	/**
	 * Validates that the object is not null
	 * 
	 * @param obj
	 *            object to test
	 */
	public static void notNull(Object obj) {
		if (obj == null)
			throw new IllegalArgumentException("Object must not be null");
	}

	/**
	 * Validates that the object is not null
	 * 
	 * @param obj
	 *            object to test
	 * @param msg
	 *            message to output if validation fails
	 */
	public static void notNull(Object obj, String msg) {
		if (obj == null)
			throw new IllegalArgumentException(msg);
	}
}
