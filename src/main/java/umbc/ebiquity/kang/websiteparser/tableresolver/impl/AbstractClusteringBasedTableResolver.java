package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.machinelearning.algorithm.clustering.HierarchicalClusteringAlgorithm;
import umbc.ebiquity.kang.machinelearning.algorithm.clustering.ICluster;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableRecordsSimilarity;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableRecordsSimilarityFactory;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableResolver;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.TableStatus;

public abstract class AbstractClusteringBasedTableResolver implements ITableResolver {
	
	private ITableRecordsSimilarityFactory tableRecordsSimilarityFactory;
	private ITableRecordsSimilarity tableRecordsSimilarity;
	private HierarchicalClusteringAlgorithm<TableRecord> algorithm;

	public AbstractClusteringBasedTableResolver() {
		this.algorithm = new HierarchicalClusteringAlgorithm<TableRecord>();
		tableRecordsSimilarityFactory = new TableRecordsSimilarityFactory();
	}
	
	@Override
	public TableResolveResult resolve(Element element) {
		Element elem = null;
		Elements tbodies = element.getElementsByTag("tbody");
		if (tbodies.size() > 0) {
			elem = tbodies.get(0);
		} else {
			elem = element;
		}
		Set<ICluster<TableRecord>> clusters = clusterTableRecords(elem);
		clusters = validateClusterMembers(clusters);
		return resolveTableBasedOnClusters(clusters);
	}
	
	protected Set<ICluster<TableRecord>> validateClusterMembers(Set<ICluster<TableRecord>> clusters) {
		// TODO Auto-generated method stub
		return clusters;
	}

	protected boolean validateClusterSequence(List<TableRecord> clusterBefore, List<TableRecord> clusterAfter) {
		Collections.sort(clusterBefore);
		Collections.sort(clusterAfter);

		if (clusterBefore.get(clusterBefore.size() - 1).getSequenceNumber() 
				>= clusterAfter.get(clusterAfter.size() - 1).getSequenceNumber())
			return false;
		return true;
	}
	
	protected Set<ICluster<TableRecord>> clusterTableRecords(Element tableElem) {

		HTMLDataTable table = convertToDataTable(tableElem);
		
		List<TableRecord> tableRecords = table.getTableRecords();
		System.out.println("tableRecords: " + tableRecords.size());
//		TableRecordSimilarity tableRecordSimilarity = createTableRecordSimilarity(tableRecords);
		tableRecordsSimilarity = tableRecordsSimilarityFactory.createTableRecordsSimilairty(tableRecords);
		Set<ICluster<TableRecord>> initClusters = createInitialClusters(tableRecords, tableRecordsSimilarity);

		System.out.println("initClusters: " + initClusters.size());
		Set<ICluster<TableRecord>> afterClusters = algorithm.doCluster(initClusters);
		System.out.println("afterClusters: " + afterClusters.size());

		for (ICluster<TableRecord> c : afterClusters) {
			System.out.println();
			System.out.println("------ cluster: " + c.getMembers().size());

			for (TableRecord r : c.getMembers()) {
				System.out.println("------ TableRecord: ");
				for (TableCell cell : r.getTableCells()) {
					System.out.println("------ TableCell size: " + cell.getDataCells().size());
					for (DataCell dc : cell.getDataCells()) {
						System.out.println(dc.getWrappedNode());
					}
				}
			}
		}

		return afterClusters;
	}
	
	protected TableResolveResult resolveTableBasedOnClusters(Set<ICluster<TableRecord>> clusters) {

		System.out.println("@@@: " + clusters);
		List<ICluster<TableRecord>> list = new ArrayList<ICluster<TableRecord>>(clusters);
		int numOfClusters = list.size();
		if (numOfClusters == 1) {
			
			return doResolveTable(new ArrayList<TableRecord>(list.get(0).getMembers()));
			
		} else if (numOfClusters == 2) {

			List<TableRecord> cluster1 = new ArrayList<TableRecord>(list.get(0).getMembers());
			List<TableRecord> cluster2 = new ArrayList<TableRecord>(list.get(1).getMembers());
			return doResolveTable(cluster1, cluster2);
			
		} else if (numOfClusters == 3) {

			MergedClusterResult result = mergeClusters(list);

			if (result.isMergeFound()) {

				Set<TableRecord> cluster1 = list.get(result.getMergedClusterOneIndex()).getMembers();
				Set<TableRecord> cluster2 = list.get(result.getMergedClusterTwoIndex()).getMembers();
				int leftClusterIndex = 0;
				List<TableRecord> mergedCluster = new ArrayList<TableRecord>(cluster1.size() + cluster2.size());
				for (TableRecord tr : cluster1) {
					mergedCluster.add(tr);
				}
				for (TableRecord tr : cluster2) {
					mergedCluster.add(tr);
				}

				List<TableRecord> cluster = new ArrayList<TableRecord>(list.get(leftClusterIndex).getMembers());
				return doResolveTable(mergedCluster, cluster);

			} else {
				TableResolveResult resolveResult = new TableResolveResult(TableStatus.RegularTable, DataTableHeaderType.UD);
				return resolveResult;
			}

		} else {
			TableResolveResult resolveResult = new TableResolveResult(TableStatus.RegularTable, DataTableHeaderType.UD);
			return resolveResult;
		}
	}

	protected abstract TableResolveResult doResolveTable(List<TableRecord> cluster);

	private Set<ICluster<TableRecord>> createInitialClusters(List<TableRecord> tableRecords,
			ITableRecordsSimilarity tableRecordsSimilarity) {
		Set<ICluster<TableRecord>> initClusters = new LinkedHashSet<ICluster<TableRecord>>();
		for (TableRecord record : tableRecords) {
			TableRecordCluster cluster = new TableRecordCluster(UUID.randomUUID().toString(), tableRecordsSimilarity);
			cluster.addMember(record);
			initClusters.add(cluster);
		}
		return initClusters;
	}
	
	private MergedClusterResult mergeClusters(List<ICluster<TableRecord>> list){
		
		int numOfClusters = list.size();
		boolean mergeFound = false;
		int mergedCluster1Index = -1;
		int mergedCluster2Index = -1;
		double threshold = algorithm.getClusterMergingThreshold() - 0.1;
		while (threshold >= 0.3) {
			double maxSim = 0.0;
			for (int i = 0; i < numOfClusters; i++) {
				for (int j = i + 1; i < numOfClusters; i++) {
					Set<TableRecord> cluster_i = list.get(i).getMembers();
					Set<TableRecord> cluster_j = list.get(j).getMembers();

					double sim = tableRecordsSimilarity.computeSimilarity(cluster_i, cluster_j);
					if (maxSim < sim) {
						maxSim = sim;
						mergedCluster1Index = i;
						mergedCluster2Index = j;
					}
				}
			}

			if (maxSim >= threshold) {
				mergeFound = true;
				break;
			}

			threshold = threshold - 0.1;
		}
		
		MergedClusterResult result = new MergedClusterResult(mergeFound); 
		result.setMergedClusterOneIndex(mergedCluster1Index);
		result.setMergedClusterTwoIndex(mergedCluster2Index);
		return result;
	}
	
	private class MergedClusterResult{

		private int mergedClusterOneIndex;
		private int mergedClusterTwoIndex;
		private boolean mergeFound = false;
		public MergedClusterResult(boolean mergeFound) {
			this.mergeFound = mergeFound;
		}
		
		public boolean isMergeFound(){
			return this.mergeFound;
		}

		/**
		 * @return the mergedClusterOneIndex
		 */
		public int getMergedClusterOneIndex() {
			return mergedClusterOneIndex;
		}

		/**
		 * @param mergedClusterOneIndex the mergedClusterOneIndex to set
		 */
		public void setMergedClusterOneIndex(int mergedClusterOneIndex) {
			this.mergedClusterOneIndex = mergedClusterOneIndex;
		}

		/**
		 * @return the mergedClusterTwoIndex
		 */
		public int getMergedClusterTwoIndex() {
			return mergedClusterTwoIndex;
		}

		/**
		 * @param mergedClusterTwoIndex
		 *            the mergedClusterTwoIndex to set
		 */
		public void setMergedClusterTwoIndex(int mergedClusterTwoIndex) {
			this.mergedClusterTwoIndex = mergedClusterTwoIndex;
		}

	}

	protected abstract TableResolveResult doResolveTable(List<TableRecord> cluster1, List<TableRecord> cluster2);
	
	protected abstract HTMLDataTable convertToDataTable(Element tableElem);

}
