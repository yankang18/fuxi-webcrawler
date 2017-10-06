package umbc.ebiquity.kang.htmltable.translator.impl;

import java.util.List;

import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable;
import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderDelimitedTable;
import umbc.ebiquity.kang.htmltable.translator.IPropertyHeaderLikelihoodCalculator;
import umbc.ebiquity.kang.htmltable.translator.ITableRecordDataTypePurityCalculator;
import umbc.ebiquity.kang.htmltable.translator.ITwoDirectionalTablePropertyHeaderTypeAnalyzer;
import umbc.ebiquity.kang.machinelearning.math.util.BasicMath;

/**
 * This class is to analyze whether the vertical headers are property headers or
 * entity headers.
 * 
 * @author yankang
 *
 */
public class StandardTwoDirectionalTablePropertyHeaderTypeAnalyzer implements ITwoDirectionalTablePropertyHeaderTypeAnalyzer {

	public enum TwoDirectionalHeaderType {
		VerticalPropertyHeader, HorizontalPropertyHeader, Indeterminable
	}

	private IPropertyHeaderLikelihoodCalculator propertyHeadersScoreCalculator = new StandardPropertyHeaderLikelihoodCalculator();
	private ITableRecordDataTypePurityCalculator recordDataTypePurityCalculator = new StandardTableRecordDataTypePurityCalculator();

	@Override
	public TwoDirectionalHeaderType analyze(HeaderDelimitedTable table) {
		if (IDelimitedTable.DataTableHeaderType.TwoDirectionalHeaderTable != table.getDataTableHeaderType()) {
			throw new IllegalArgumentException("The input table must be two-directional-header data table");
		}

		List<TableRecord> hHeaderRecords = table.getHorizontalHeaderRecords();
		List<TableRecord> vHeaderRecords = table.getVerticalHeaderRecords();

		int hRecordOffset = vHeaderRecords.size();
		int vRecordOffset = hHeaderRecords.size();

		double hPropScore = propertyHeadersScoreCalculator.calculate(hHeaderRecords,
				hRecordOffset);
		double vPropScore = propertyHeadersScoreCalculator.calculate(vHeaderRecords,
				vRecordOffset);

		System.out.println("hPropScore: " + hPropScore);
		System.out.println("vPropScore: " + vPropScore);

		if (isAboveUpperThreshold(hPropScore) && isBelowLowerThreshold(vPropScore)) {
			return TwoDirectionalHeaderType.HorizontalPropertyHeader;
		} else if (isAboveUpperThreshold(vPropScore) && isBelowLowerThreshold(hPropScore)) {
			return TwoDirectionalHeaderType.VerticalPropertyHeader;
		} else {

			/*
			 * Further investigate to determine the property header type.
			 */
			List<TableRecord> hDataRecords = table.getHorizontalDataRecords();
			List<TableRecord> vDataRecords = table.getVerticalDataRecords();
			double hCellNum = BasicMath.log(hDataRecords.get(0).getTableCells().size() - hRecordOffset, 2);
			double vCellNum = BasicMath.log(vDataRecords.get(0).getTableCells().size() - vRecordOffset, 2);
			double hBeta = hCellNum > vCellNum && vCellNum >= 1 ? hCellNum / vCellNum : 0;
			double vBeta = vCellNum > hCellNum && hCellNum >= 1 ? vCellNum / hCellNum : 0;
			System.out.println("hBeta: " + hBeta);
			System.out.println("vBeta: " + vBeta);
			double hDataPurityScore = recordDataTypePurityCalculator.computeDataTypePurityScore(hDataRecords,
					hRecordOffset, hBeta);

			double vDataPurityScore = recordDataTypePurityCalculator.computeDataTypePurityScore(vDataRecords,
					vRecordOffset, vBeta);

			System.out.println("hPurityScore: " + hDataPurityScore);
			System.out.println("vPurityScore: " + vDataPurityScore);

			double hTotalScore = computeAggregateScore(hPropScore, vDataPurityScore);
			double vTotalScore = computeAggregateScore(vPropScore, hDataPurityScore);

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
