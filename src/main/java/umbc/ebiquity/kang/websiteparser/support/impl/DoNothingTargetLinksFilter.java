package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.Set;

import umbc.ebiquity.kang.websiteparser.impl.TargetLink;
import umbc.ebiquity.kang.websiteparser.support.ITargetLinksFilter;

public class DoNothingTargetLinksFilter implements ITargetLinksFilter {

	@Override
	public Set<TargetLink> filter(Set<TargetLink> targetLinks) {
		return targetLinks;
	}

}
