package umbc.ebiquity.kang.htmltable.translator.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IValueTypeResolver;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeNodeValue.ValueType;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.StandardValueTypeResolver;
import umbc.ebiquity.kang.htmltable.core.TableCell;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable;
import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderDelimitedTable;
import umbc.ebiquity.kang.htmltable.translator.IPropertyTableHeaderLikelihoodCalculator;
import umbc.ebiquity.kang.htmltable.translator.ITableRecordDataTypePurityCalculator;
import umbc.ebiquity.kang.machinelearning.math.util.Mathematics;

public class TwoDirectionalTableHeaderAnalyzer {

	public enum TwoDirectionalHeaderType {
		VerticalPropertyHeader, HorizontalPropertyHeader, Indeterminable
	}

	private IPropertyTableHeaderLikelihoodCalculator propertyTableHeaderLikelihoodCalculator = new StandardPropertyTableHeaderLikelihoodCalculator();
	private ITableRecordDataTypePurityCalculator tableRecordDataTypePurityCalculator = new StandardTableRecordDataTypePurityCalculator();

	public TwoDirectionalHeaderType analyze(HeaderDelimitedTable table) {
		if (IDelimitedTable.DataTableHeaderType.TwoDirectionalHeaderTable != table.getDataTableHeaderType()) {
			throw new IllegalArgumentException("The input table must be two-directional-header data table");
		}

		List<TableRecord> hHeaderRecords = table.getHorizontalHeaderRecords();
		List<TableRecord> vHeaderRecords = table.getVerticalHeaderRecords();

		int hRecordOffset = vHeaderRecords.size();
		int vRecordOffset = hHeaderRecords.size();

		double hPropLikeHood = propertyTableHeaderLikelihoodCalculator.computePropertyHeaderLikelihood(hHeaderRecords,
				hRecordOffset);
		double vPropLikeHood = propertyTableHeaderLikelihoodCalculator.computePropertyHeaderLikelihood(vHeaderRecords,
				vRecordOffset);

		System.out.println("hPropLikeHood: " + hPropLikeHood);
		System.out.println("vPropLikeHood: " + vPropLikeHood);

		if (isAboveUpperThreshold(hPropLikeHood) && isBelowLowerThreshold(vPropLikeHood)) {
			return TwoDirectionalHeaderType.HorizontalPropertyHeader;
		} else if (isAboveUpperThreshold(vPropLikeHood) && isBelowLowerThreshold(hPropLikeHood)) {
			return TwoDirectionalHeaderType.VerticalPropertyHeader;
		} else {

			List<TableRecord> hDataRecords = table.getHorizontalDataRecords();
			List<TableRecord> vDataRecords = table.getVerticalDataRecords();
			int hCellNum = hDataRecords.get(0).getTableCells().size() - hRecordOffset;
			int vCellNum = vDataRecords.get(0).getTableCells().size() - vRecordOffset;
			double hBeta = hCellNum > vCellNum ? hCellNum / (double) vCellNum : 0;
			double vBeta = vCellNum > hCellNum ? vCellNum / (double) hCellNum : 0;
			System.out.println("hBeta: " + hBeta);
			System.out.println("vBeta: " + vBeta);
			double hDataPurityScore = tableRecordDataTypePurityCalculator.computeDataTypePurityScore(hDataRecords,
					hRecordOffset, hBeta);

			System.out.println();
			double vDataPurityScore = tableRecordDataTypePurityCalculator.computeDataTypePurityScore(vDataRecords,
					vRecordOffset, vBeta);

			System.out.println("hPurityScore: " + hDataPurityScore);
			System.out.println("vPurityScore: " + vDataPurityScore);

			double hTotalScore = computeAggregateScore(hPropLikeHood, vDataPurityScore);
			double vTotalScore = computeAggregateScore(vPropLikeHood, hDataPurityScore);

			if (hTotalScore - vTotalScore >= 0.3) {
				return TwoDirectionalHeaderType.HorizontalPropertyHeader;
			} else if (vTotalScore - hTotalScore >= 0.3) {
				return TwoDirectionalHeaderType.VerticalPropertyHeader;
			}
		}
		return TwoDirectionalHeaderType.Indeterminable;
	}

	private boolean isBelowLowerThreshold(double vPropLikeHood) {
		return vPropLikeHood <= 0;
	}

	private boolean isAboveUpperThreshold(double hPropLikeHood) {
		return hPropLikeHood > 0.3;
	}

	private double computeAggregateScore(double hPropLikeHood, double hDiversityRatio) {
		return 0.7 * Math.max(hPropLikeHood, hDiversityRatio) + 0.3 * Math.min(hPropLikeHood, hDiversityRatio);
	}
}
