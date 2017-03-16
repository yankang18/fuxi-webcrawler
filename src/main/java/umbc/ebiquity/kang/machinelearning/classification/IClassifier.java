package umbc.ebiquity.kang.machinelearning.classification;

public interface IClassifier<I> {

	IPrediction predict(I newInstance) throws Exception; 

}
