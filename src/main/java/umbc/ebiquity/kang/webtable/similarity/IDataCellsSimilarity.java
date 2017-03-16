package umbc.ebiquity.kang.webtable.similarity;

import umbc.ebiquity.kang.webtable.IDataCellHolder;

public interface IDataCellsSimilarity {
	
	double computeSimilarity(IDataCellHolder dataCellHolder1, IDataCellHolder dataCellHolder2);
}
