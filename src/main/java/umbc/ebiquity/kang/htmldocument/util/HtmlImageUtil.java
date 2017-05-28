package umbc.ebiquity.kang.htmldocument.util;

import org.jsoup.nodes.Element;

public final class HtmlImageUtil {

	private HtmlImageUtil() {
	}

	private final static String SOURCE = "src";
	private final static String DESCRIPTION = "alt";

	public static boolean isImgElement(Element element) {
		if (element == null)
			return false;

		if (element.tagName().equalsIgnoreCase("img"))
			return true;

		return false;
	}

	public static String getText(Element element){
		BasicValidator.notNull(element);
		
		String text = getDescription(element);
		if (text.trim().equals("")) {
			text = getName(element);
		}
		return text;
	}

	public static String getName(Element element){
		BasicValidator.notNull(element);
		
		String src = element.attr(SOURCE);
		int lastIndex = src.lastIndexOf("/");
		if(lastIndex < 0) {
			return tripExtension(src);
		} else {
			return tripExtension(src.substring(lastIndex, src.length()));
		}
	} 

	public static String getDescription(Element element) {
		BasicValidator.notNull(element);
		return element.attr(DESCRIPTION);
	}

	public static String getSource(Element element) {
		BasicValidator.notNull(element);
		return element.attr(SOURCE);
	}
	
	private static String tripExtension(String imgName) {
		int lastIndex = imgName.lastIndexOf(".");
		if(lastIndex < 0) {
			return imgName;
		} else {
			return imgName.substring(0, lastIndex);
		}
	}
	
	private static void nullCheck(Element element) {
		if(element == null)
			throw new IllegalArgumentException();
	}

}
