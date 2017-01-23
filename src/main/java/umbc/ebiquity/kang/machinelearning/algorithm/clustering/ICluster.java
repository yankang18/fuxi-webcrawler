package umbc.ebiquity.kang.machinelearning.algorithm.clustering;

import java.util.Set;

public interface ICluster<M> {

	void addMember(M member);

	double computeSimilarity(ICluster<M> m);

	ICluster<M> merge(ICluster<M> m);

	boolean isSameAs(ICluster<M> m);

	String getId();

	Set<M> getMembers();

}
