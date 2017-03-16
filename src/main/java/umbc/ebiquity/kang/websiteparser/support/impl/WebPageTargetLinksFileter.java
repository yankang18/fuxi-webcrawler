package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import umbc.ebiquity.kang.websiteparser.impl.TargetLink;
import umbc.ebiquity.kang.websiteparser.support.ITargetLinksFilter;

public class WebPageTargetLinksFileter implements ITargetLinksFilter {

	@Override
	public Set<TargetLink> filter(Set<TargetLink> extractedLinks) {
		Set<TargetLink> filteredLinks = new LinkedHashSet<TargetLink>();
		for (TargetLink link : extractedLinks) {
			if (isWebPage(link.getUrl())) {
				filteredLinks.add(link);
			}
		}
		return filteredLinks;
	}

	private boolean isWebPage(String href) {
		if (href.endsWith(".pdf") || href.endsWith(".jpg")) {
			return false;
		}
		return true;
	}

}
