package umbc.ebiquity.kang.htmltable.feature.impl;

import umbc.ebiquity.kang.htmltable.feature.ITableFeatureExtractor;
import umbc.ebiquity.kang.htmltable.feature.ITableFeaturesExtractor;
import umbc.ebiquity.kang.htmltable.feature.ITableFeaturesExtractorFactory;

public class StandardTableFeaturesExtractorFactory implements ITableFeaturesExtractorFactory {

	@Override
	public ITableFeaturesExtractor create() {
		
		ITableFeatureExtractor feature1 = new TableRowCountFeatureExtractor();
		ITableFeatureExtractor feature2 = new TableColumnCountFeatureExtractor();
		ITableFeatureExtractor feature3 = new TableNestingDepthFeatureExtractor();
		ITableFeatureExtractor feature4 = new TableValueCellsRatioFeatureExtractor();
		ITableFeatureExtractor feature5 = new TableEmptyCellsRatioFeatureExtractor();
		ITableFeatureExtractor feature6 = new TableStructureComplexityFeatureExtractor();
		ITableFeatureExtractor feature7 = new HorizontalTableStructureUniformityFeatureExtractor();
		ITableFeatureExtractor feature8 = new VerticalTableStructureUniformityFeatureExtractor();

		TableFeaturesExtractor extractor = new TableFeaturesExtractor();
		extractor.registerHorizontalTableFeatureExtactor(feature1);
		extractor.registerHorizontalTableFeatureExtactor(feature2);
		extractor.registerHorizontalTableFeatureExtactor(feature3);
		extractor.registerHorizontalTableFeatureExtactor(feature4);
		extractor.registerHorizontalTableFeatureExtactor(feature5);
		extractor.registerHorizontalTableFeatureExtactor(feature6);
		extractor.registerHorizontalTableFeatureExtactor(feature7);
		extractor.registerVerticalTableFeatureExtactor(feature8);
		
		return extractor;
	}
}
