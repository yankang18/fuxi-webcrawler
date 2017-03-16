package umbc.ebiquity.kang.webtable.feature.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import umbc.ebiquity.kang.machinelearning.classification.IFeatureData;

/**
 * This class holds all the features extract from a table and it should be put
 * in the same package as the {@link TableFeaturesExtractor}
 * 
 * @author yankang
 *
 */
public class TableFeatures implements IFeatureData {

	private Map<String, Object> attribute2Value;

	TableFeatures() {
		attribute2Value = new LinkedHashMap<String, Object>();
	}

	@Override
	public Set<String> getFeatureNames() {
		return attribute2Value.keySet();
	}

	@Override
	public Object getFeatureValue(String featureName) {
		return attribute2Value.get(featureName);
	}

	public void addFeature(String attributeName, Object value){
		attribute2Value.put(attributeName, value);
	}
	
	public void setNumberOfRows(int row) {
		attribute2Value.put("NumberOfRows", Double.valueOf(row));
	}

	public void setNumberOfColumns(int col) {
		attribute2Value.put("NumberOfColumns", Double.valueOf(col));
	}

	public void setDepthOfNestedTables(int depthOfNestedTables) {
		attribute2Value.put("DepthOfTables", Double.valueOf(depthOfNestedTables));
	}

	public void setRatioOfValueCells(double ratioOfValueCells) {
		attribute2Value.put("RatioOfValueCells", ratioOfValueCells);
	}

	public void setRatioOfEmptyCells(double ratioOfEmptyCells) {
		attribute2Value.put("RatioOfEmptyCells", ratioOfEmptyCells);
	}

	public void setStructureComplexity(double structureComplexity) {
		attribute2Value.put("ComplexityOfStructure", structureComplexity);
	}

	public void setHorizontalStructureUniformity(double hStructureUniformity) {
		attribute2Value.put("HorUniformityOfStructure", hStructureUniformity);
	}

	public void setVerticalStructureUniformity(double vStructureUniformity) {
		attribute2Value.put("VerUniformityOfStructure", vStructureUniformity);
	}

	public void setHorizontalAttributeDiversity(double hAttributeDiversity) {
		attribute2Value.put("HorDiversityOfAttribute", hAttributeDiversity);
	}

	public void setVerticalAttributeDiversity(double structureComplexity) {
		attribute2Value.put("VerDiversityOfAttribute", structureComplexity);
	}
}
