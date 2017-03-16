package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.websiteparser.impl.TargetLink;
import umbc.ebiquity.kang.websiteparser.support.ITargetLinksExtractionStrategy;
import umbc.ebiquity.kang.websiteparser.support.ITargetLinksFilter;

public class StandardTargetLinksExtractor implements ITargetLinksExtractionStrategy {

	private ITargetLinksFilter targetLinkFilter;

	public StandardTargetLinksExtractor(ITargetLinksFilter targetLinkFilter) {
		this.targetLinkFilter = targetLinkFilter;
	}

	public StandardTargetLinksExtractor() {
		targetLinkFilter = new DoNothingTargetLinksFilter();
	}

	@Override
	public Set<TargetLink> extract(Document webPage) {
		String baseUrl = webPage.baseUri();

		Set<TargetLink> extractedLinks = new LinkedHashSet<TargetLink>();
		Elements links = webPage.select("a[href]");
		for (Element link : links) {
			String href = link.attr("abs:href").trim();
			if (withinHostDomain(href, baseUrl)) {
				String text = link.text();
				extractedLinks.add(new TargetLink(href, text));
			}
		}
		return targetLinkFilter.filter(extractedLinks);
	}

	private boolean withinHostDomain(String href, String baseUrl) {
		// String trimedHref = href.replace("http://", "").replace("http://",
		// "");
		return href.startsWith(baseUrl);
	}

}
