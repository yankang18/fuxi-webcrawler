package umbc.ebiquity.kang.machinelearning.clustering;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import umbc.ebiquity.kang.machinelearning.clustering.impl.Member;
import umbc.ebiquity.kang.machinelearning.clustering.impl.SimpleCluster;

/**
 * This is class performs the core part of the hierarchical clustering algorithm
 * 
 * @author yankang
 *
 * @param <M>
 *            the type of instance to be clustered. The type M is defined by the
 *            client of this hierarchical clustering
 */
public class HierarchicalClusteringAlgorithm<M> {

	private double clusterMergingThreshold = 0.5;

	public HierarchicalClusteringAlgorithm() {
	}

	public HierarchicalClusteringAlgorithm(double clusterMergingThreshold) {
		this.clusterMergingThreshold = clusterMergingThreshold;
	}

	/**
	 * Do the hierarchical clustering start from the set of initial
	 * <code>ICluster</code>s.
	 * 
	 * @param initClusters
	 *            the set of initial <code>ICluster</code>s
	 * @return
	 */
	public Set<ICluster<M>> doCluster(Set<ICluster<M>> initClusters) {

		if (initClusters.size() <= 1) {
			return initClusters;
		}

		// the merged relation cluster set is used to record clusters that have
		// been merged.
		Set<ICluster<M>> mergedClusters = new HashSet<ICluster<M>>();
		int numOfClusters = 0;
		Set<ICluster<M>> newClusters = null;
		int counter = 1;
		do {
			System.out.println();
			System.out.println("round: " + counter ++);
			numOfClusters = initClusters.size();
			newClusters = new HashSet<ICluster<M>>();
			for (ICluster<M> cluster1 : initClusters) {
				if (mergedClusters.contains(cluster1))
					continue;
				
				ICluster<M> matchedCluster = null;
				double maxScore = 0.0;
				for (ICluster<M> c : initClusters) {
					if (cluster1.isSameAs(c) || mergedClusters.contains(c))
						continue;

					double simScore = cluster1.computeSimilarity(c);
					if (simScore > maxScore) {
						maxScore = simScore;
						matchedCluster = c;
					}
				}

				if (matchedCluster != null) {
					if (maxScore >= this.getClusterMergingThreshold()) {
						System.out.println("MERGE WITH CLUSTER: " + maxScore);
						ICluster<M> newCluster = merge(matchedCluster, cluster1);
						newClusters.add(newCluster);
						mergedClusters.add(matchedCluster);
						mergedClusters.add(cluster1);
					} else {
						System.out.println("MERGE WITH NO CLUSTER: " + maxScore);
						newClusters.add(cluster1);
					}

				} else {
					System.out.println("MERGE WITH NO CLUSTER: " + maxScore);
					newClusters.add(cluster1);
				}
			}
			initClusters = newClusters;
		} while (initClusters.size() < numOfClusters);

		return newClusters;
	}

	private ICluster<M> merge(ICluster<M> c1, ICluster<M> c2) {
		return c1.merge(c2);
	}
	
	public static void main(String[] arg){
		
		Set<ICluster<Member>> initClusters = new LinkedHashSet<ICluster<Member>>();
		ICluster<Member> cluster1 = new SimpleCluster(UUID.randomUUID().toString());
		cluster1.addMember(new Member(9.0));
		
		ICluster<Member> cluster2 = new SimpleCluster(UUID.randomUUID().toString());
		cluster2.addMember(new Member(10.0));
		
		ICluster<Member> cluster3 = new SimpleCluster(UUID.randomUUID().toString());
		cluster3.addMember(new Member(3.0));
		
		ICluster<Member> cluster4 = new SimpleCluster(UUID.randomUUID().toString());
		cluster4.addMember(new Member(4.0));
		
		ICluster<Member> cluster5 = new SimpleCluster(UUID.randomUUID().toString());
		cluster5.addMember(new Member(3.5));
		
		initClusters.add(cluster1);
		initClusters.add(cluster2);
		initClusters.add(cluster3);
		initClusters.add(cluster4);
		initClusters.add(cluster5);
		
		HierarchicalClusteringAlgorithm<Member> alg = new HierarchicalClusteringAlgorithm<Member>();
		Set<ICluster<Member>> afterClusters = alg.doCluster(initClusters);
		
		System.out.println(afterClusters.size());
		
		for(ICluster<Member> cluster : afterClusters){
			System.out.println(cluster.getId());
			for(Member m :cluster.getMembers()){
				System.out.println(m.getValue());
			}
			
		}
		
	}

	/**
	 * @return the clusterMergingThreshold
	 */
	public double getClusterMergingThreshold() {
		return clusterMergingThreshold;
	}

	/**
	 * @param clusterMergingThreshold the clusterMergingThreshold to set
	 */
	public void setClusterMergingThreshold(double clusterMergingThreshold) {
		this.clusterMergingThreshold = clusterMergingThreshold;
	}
}
