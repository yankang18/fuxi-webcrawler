package umbc.ebiquity.kang.htmltable.feature.impl;

import umbc.ebiquity.kang.htmltable.feature.ITableFeatureExtractor;

public class VerticalTableStructureUniformityFeatureExtractor extends TableStructureUniformityFeatureExtractor
		implements ITableFeatureExtractor {

	@Override
	public String getFeatureName() {
		return "VerUniformityOfStructure";
	}

}
