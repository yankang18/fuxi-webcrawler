package umbc.ebiquity.kang.websiteparser.impl;

public class TargetLink {

	private String url;
	private String topic;

	public TargetLink(String url, String topic) {
		this.url = url;
		this.topic = topic;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}

}
