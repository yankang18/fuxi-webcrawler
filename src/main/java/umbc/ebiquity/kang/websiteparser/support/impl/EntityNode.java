package umbc.ebiquity.kang.websiteparser.support.impl;

import org.jsoup.nodes.Element;

public class EntityNode extends AbstractNode {

	private String name;
	private String normalizedName;
	public EntityNode(String name, Element element){
		super(element);
		this.name = name;
		this.normalizedName = createNormalizedName(name);
	}
	
	private String createNormalizedName(String name) {
		return name;
	}

	public String getName(){
		return name;
	}
	
	public String getNormalizedName(){
		return normalizedName;
	}

}
