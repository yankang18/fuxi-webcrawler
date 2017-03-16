package umbc.ebiquity.kang.websiteparser.support;

import java.util.Set;

import umbc.ebiquity.kang.websiteparser.impl.TargetLink;

public interface ITargetLinksFilter {

	Set<TargetLink> filter(Set<TargetLink> targetLinks); 
	

}
