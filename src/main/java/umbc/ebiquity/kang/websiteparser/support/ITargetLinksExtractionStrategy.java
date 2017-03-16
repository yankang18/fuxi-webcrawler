package umbc.ebiquity.kang.websiteparser.support;

import java.util.Set;

import org.jsoup.nodes.Document;

import umbc.ebiquity.kang.websiteparser.impl.TargetLink;

public interface ITargetLinksExtractionStrategy {

	Set<TargetLink> extract(Document webPage); 

}
