package umbc.ebiquity.kang.websiteparser.tableparser.randomtest;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class RandomTest {
	private static final String TEST_FILE_FOLDER = "RandomTest/";

	// @Ignore
	@Test
	public void testAnalyzeTwoDirectionalHeaderTableWithVerticalPropertyHeader() throws IOException {

		File input = loadFileOrDirectory(TEST_FILE_FOLDER + "brTagTest.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("div").get(0);
		System.out.println(element.tagName());
		
		for(Node n :  element.childNodes()) {
			if(n instanceof Element) {
				Element elem = (Element) n;
				System.out.println(elem.nodeName() + ", " + elem.tagName());
			} else if (n instanceof TextNode) {
				TextNode txtNode = (TextNode) n;
				System.out.println(txtNode.nodeName() + ", " + txtNode.text());
			}
		}
	}
	
	protected File loadFileOrDirectory(String fileName) {
		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File input = new File(classLoader.getResource(fileName).getFile());
		return input;
	}
}
