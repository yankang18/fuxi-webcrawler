package umbc.ebiquity.kang.webtable.feature.impl;

import umbc.ebiquity.kang.webtable.feature.ITableFeatureExtractor;

public class VerticalTableStructureUniformityFeatureExtractor extends TableStructureUniformityFeatureExtractor
		implements ITableFeatureExtractor {

	@Override
	public String getFeatureName() {
		return "VerUniformityOfStructure";
	}

}
