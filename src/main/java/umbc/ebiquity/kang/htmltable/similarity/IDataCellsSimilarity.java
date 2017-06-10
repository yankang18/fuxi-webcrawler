package umbc.ebiquity.kang.htmltable.similarity;

import umbc.ebiquity.kang.htmltable.IDataCellHolder;

public interface IDataCellsSimilarity {
	
	double computeSimilarity(IDataCellHolder dataCellHolder1, IDataCellHolder dataCellHolder2);
}
