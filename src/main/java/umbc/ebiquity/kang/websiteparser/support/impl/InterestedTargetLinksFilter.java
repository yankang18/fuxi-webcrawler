package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import umbc.ebiquity.kang.websiteparser.impl.TargetLink;
import umbc.ebiquity.kang.websiteparser.support.ITargetLinksFilter;

public class InterestedTargetLinksFilter implements ITargetLinksFilter {

	@Override
	public Set<TargetLink> filter(Set<TargetLink> extractedLinks) {
		Set<TargetLink> filteredLinks = new LinkedHashSet<TargetLink>();
		for (TargetLink link : extractedLinks) {
			if (isUsefulWebPage(link.getUrl())) {
				filteredLinks.add(link);
			}
		}
		return filteredLinks;
	}

	private boolean isUsefulWebPage(String href) {

		List<String> list = new ArrayList<String>();
		String[] tokens = href.split("/");
		for (String token : tokens) {
			for (String token2 : token.split("_|-|\\."))
				list.add(token2);
		}
		Set<String> XXX = new HashSet<String>();
		XXX.add("blog");
		XXX.add("blogs");
		XXX.add("faq");
		XXX.add("faqs");
		XXX.add("carreer");
		XXX.add("carreers");
		XXX.add("portfolios");
		XXX.add("email");
		XXX.add("Email");
		XXX.add("request");

		for (String token : list) {
			// System.out.println("Token: " + token);
			if (XXX.contains(token)) {
				return false;
			}
		}
		return true;
	}

}
