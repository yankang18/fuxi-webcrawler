package umbc.ebiquity.kang.htmltable.translator;

import java.util.List;

import umbc.ebiquity.kang.htmltable.core.TableRecord;

public interface IPropertyHeaderLikelihoodCalculator {
	
	double calculate(List<TableRecord> headerRecords, int offset);
}
