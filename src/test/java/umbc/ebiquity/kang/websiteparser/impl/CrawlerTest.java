package umbc.ebiquity.kang.websiteparser.impl;
import java.io.IOException;
import org.junit.Ignore;
import org.junit.Test;
import umbc.ebiquity.kang.websiteparser.IWebPage;
import umbc.ebiquity.kang.websiteparser.IWebPagePath;
import umbc.ebiquity.kang.websiteparser.object.CrawlerUrl;
import umbc.ebiquity.kang.websiteparser.support.IWebPageParsedPathsHolder;
import umbc.ebiquity.kang.websiteparser.support.impl.DefaultWebPagePathsParser;
import umbc.ebiquity.kang.websiteparser.support.impl.DefaultWebPagePathsParser2;

public class CrawlerTest {

	@Test
	public void xxx() throws IOException {
		CrawlerUrl url = new CrawlerUrl("http://www.accutrex.com/abrasive-waterjet-cutting", 1);
		IWebPage webpage = new WebPageImpl(url);
		webpage.load();
		DefaultWebPagePathsParser2 webPagePathsImpl = new DefaultWebPagePathsParser2(webpage);
		IWebPageParsedPathsHolder webPagePathHolder = webPagePathsImpl.parse();

		for (IWebPagePath path : webPagePathHolder.listWebTagPathsWithTextContent()) {
			System.out.println(path.getPathID());
		}
	}

}
