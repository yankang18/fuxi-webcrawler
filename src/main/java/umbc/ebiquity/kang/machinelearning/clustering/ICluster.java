package umbc.ebiquity.kang.machinelearning.clustering;

import java.util.Set;

/**
 * This interface defines the behaviors of a cluster utilized in hierarchical
 * clustering algorithm.
 * 
 * @author yankang
 *
 * @param <M>
 *            M is the type of instances to be clustered and each instance is a
 *            member of the cluster it is clustered in
 */
public interface ICluster<M> {

	/**
	 * Add a member of type M to the cluster.
	 * 
	 * @param member
	 *            the member to be added
	 */
	void addMember(M member);

	/**
	 * Compute the similarity between this <code>ICluster</code> and the
	 * specified <code>ICluster</code>.
	 * 
	 * @param cluster
	 *            the <code>ICluster</code> to be compared with
	 * @return the similarity score of the two <code>ICluster</code>s
	 */
	double computeSimilarity(ICluster<M> cluster);

	/**
	 * Merge this <code>ICluster</code> with the specified <code>ICluster</code>
	 * .
	 * 
	 * @param cluster
	 *            the <code>ICluster</code> to be merged
	 * @return the merged <code>ICluster</code>
	 */
	ICluster<M> merge(ICluster<M> cluster);

	/**
	 * Check if this <code>ICluster</code> is the same as the specified
	 * <code>ICluster</code>.
	 * 
	 * @param cluster
	 *            the <code>ICluster</code> to be compared with
	 * @return true if the two code>ICluster</code>s are the same
	 */
	boolean isSameAs(ICluster<M> cluster);

	/**
	 * Get the identifier of this cluster.
	 * 
	 * @return a String representing the identifier of this cluster
	 */
	String getId();

	/**
	 * Get all the members of this cluster.
	 * 
	 * @return a Set of members of this cluster
	 */
	Set<M> getMembers();
}
