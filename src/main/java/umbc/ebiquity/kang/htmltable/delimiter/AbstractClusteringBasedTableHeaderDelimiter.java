package umbc.ebiquity.kang.htmltable.delimiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.htmldocument.impl.StandardHtmlElement;
import umbc.ebiquity.kang.htmltable.core.HTMLDataTable;
import umbc.ebiquity.kang.htmltable.core.TableCell;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.TableStatus;
import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderDelimitedTable;
import umbc.ebiquity.kang.htmltable.delimiter.impl.TableRecordCluster;
import umbc.ebiquity.kang.htmltable.similarity.IAttributesSimilarity;
import umbc.ebiquity.kang.htmltable.similarity.IDataCellsSimilarity;
import umbc.ebiquity.kang.htmltable.similarity.ITableRecordsSimilarity;
import umbc.ebiquity.kang.htmltable.similarity.ITableRecordsSimilarityFactory;
import umbc.ebiquity.kang.htmltable.similarity.ITableRecordsSimiliartySuite;
import umbc.ebiquity.kang.htmltable.similarity.impl.TableRecordsSimilarityFactory;
import umbc.ebiquity.kang.htmltable.util.HTMLTableValidator;
import umbc.ebiquity.kang.machinelearning.clustering.HierarchicalClusteringAlgorithm;
import umbc.ebiquity.kang.machinelearning.clustering.ICluster;

/**
 * This abstract class implements common functions for separating table header
 * records from table data records. It also defines common behaviors that
 * implementing classes should follow.
 * 
 * @author yankang
 *
 */
public abstract class AbstractClusteringBasedTableHeaderDelimiter implements ITableHeaderDelimiter {

	private ITableRecordsSimilarityFactory tableRecordsSimilarityFactory;
	private ITableRecordsSimiliartySuite tableRecordsSimiliartySuite;
	private ITableRecordsSimilarity tableRecordsSimilarity;
	private HierarchicalClusteringAlgorithm<TableRecord> algorithm;

	public AbstractClusteringBasedTableHeaderDelimiter() {
		algorithm = new HierarchicalClusteringAlgorithm<TableRecord>();
		tableRecordsSimilarityFactory = new TableRecordsSimilarityFactory();
	}

	@Override
	public HeaderDelimitedTable delimit(Element tableElement) {
		HTMLTableValidator.isTable(tableElement);

		Element actualTableElem = null;
		Elements tbodies = tableElement.getElementsByTag("tbody");
		if (tbodies.size() > 0) {
			actualTableElem = tbodies.get(0);
		} else {
			actualTableElem = tableElement;
		}

		Set<ICluster<TableRecord>> clusters = clusterTableRecords(actualTableElem);
		clusters = filterClusterMembers(clusters);
		HeaderDelimitedTable result = resolve(clusters);
		result.setHtmlElement(StandardHtmlElement.createDefaultStandardHtmlElement(tableElement));
		return result;
	}

	/**
	 * Check whether each <code>TableRecord</code> in each cluster is a valid
	 * header record or data record. If a <code>TableRecord</code> is invalid,
	 * it will be removed and empty cluster will also be removed.
	 * 
	 * @param clusters
	 *            a set of clusters the <code>TableRecord</code> of which to be
	 *            validated
	 * @return a set of validated clusters
	 */
	private Set<ICluster<TableRecord>> filterClusterMembers(Set<ICluster<TableRecord>> clusters) {
		// TODO: Remove record that all date cells it contains have no value
		// (image and functional tag does not consider as a value for now).
		return clusters;
	}

	/**
	 * Clusters table records in the specified HTML table.
	 * 
	 * @param tableElement
	 *            the Element representing the actual table from which the table
	 *            records to be clustered
	 * @return clusters of <code>TableRecord</code> constructed from the
	 *         specified HTML table
	 */
	private Set<ICluster<TableRecord>> clusterTableRecords(Element tableElement) {
		HTMLDataTable table = convertToDataTable(tableElement);

		List<TableRecord> tableRecords = table.getTableRecords();
		System.out.println("tableRecords: " + tableRecords.size());

		tableRecordsSimiliartySuite = tableRecordsSimilarityFactory.createTableRecordsSimilairtySuite(tableRecords);
		tableRecordsSimilarity = tableRecordsSimiliartySuite.getTableRecordsSimilarity();
		Set<ICluster<TableRecord>> initClusters = createInitialClusters(tableRecords, tableRecordsSimilarity);

		// Delegates to HierarchicalClusteringAlgorithm to do the actual work of
		// clustering
		Set<ICluster<TableRecord>> afterClusters = algorithm.doCluster(initClusters);

		return afterClusters;
	}

	/**
	 * Create the initial set of clusters each of which contains only one table
	 * record as it member.
	 * 
	 * @param tableRecords
	 *            a list of table records each of which is to be initialized as
	 *            a cluster
	 * @param tableRecordsSimilarity
	 *            the similarity measurement the measures similarity between two
	 *            sets of table records
	 * @return a set of clusters
	 */
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

	/**
	 * Identifies clusters containing table header records and clusters
	 * containing table data records, and merge them into two groups
	 * respectively: one contains all identified table header records and the
	 * other contains all identified table date records. </br>
	 * 
	 * </br>
	 * If table header records and table data records cannot be identified and
	 * merged into two group successfully, this failure would be recored in the
	 * resolving result.
	 * 
	 * @param clusters
	 *            the clusters of table records
	 * @return HeaderDelimitedTable containing the resolve result
	 */
	private HeaderDelimitedTable resolve(Set<ICluster<TableRecord>> clusters) {

		List<ICluster<TableRecord>> copiedClusters = new ArrayList<ICluster<TableRecord>>(clusters);
		int numOfClusters = copiedClusters.size();
		if (numOfClusters == 1) {

			return doResolveTable(new ArrayList<TableRecord>(copiedClusters.get(0).getMembers()));

		} else if (numOfClusters == 2) {

			// This is the ideal case that table header records and table data
			// records were merged into two clusters by the clustering
			// algorithm. In this case, we delegates the sub-class to determine
			// which cluster contains table header records and which containing
			// table data records.
			List<TableRecord> cluster1 = new ArrayList<TableRecord>(copiedClusters.get(0).getMembers());
			List<TableRecord> cluster2 = new ArrayList<TableRecord>(copiedClusters.get(1).getMembers());
			return doResolveTable(cluster1, cluster2);

		} else if (numOfClusters == 3 && areClustersEqualSize(copiedClusters)) {

			// If there are three clusters and all of them have the same number
			// of members, we merge two of them that are most similar to each
			// other and then we delegates the sub-class to determine which
			// cluster contains table header records and which containing
			// table data records. If no two clusters can be merged, the table
			// delimiting failed.
			System.out.println("@@@ cluster 0 member size: " + copiedClusters.get(0).getMembers().size());
			System.out.println("@@@ cluster 1 member size: " + copiedClusters.get(1).getMembers().size());
			System.out.println("@@@ cluster 2 member size: " + copiedClusters.get(2).getMembers().size());

			MergedClusterResult result = mergeClusters(copiedClusters);

			if (result.isMergeFound()) {

				int clusterOneIndex = result.getMergedClusterOneIndex();
				int clusterTwoIndex = result.getMergedClusterTwoIndex();
				Set<TableRecord> cluster1 = copiedClusters.get(result.getMergedClusterOneIndex()).getMembers();
				Set<TableRecord> cluster2 = copiedClusters.get(result.getMergedClusterTwoIndex()).getMembers();

				System.out.println("@@@ merge found: " + result.getMergedClusterOneIndex() + " - "
						+ result.getMergedClusterTwoIndex());
				System.out.println("@@@ cluster size: " + cluster1.size() + " - " + cluster2.size());

				int leftClusterIndex = 3 - clusterOneIndex - clusterTwoIndex;
				List<TableRecord> mergedCluster = new ArrayList<TableRecord>(cluster1.size() + cluster2.size());
				for (TableRecord tr : cluster1) {
					mergedCluster.add(tr);
				}
				for (TableRecord tr : cluster2) {
					mergedCluster.add(tr);
				}

				List<TableRecord> cluster = new ArrayList<TableRecord>(
						copiedClusters.get(leftClusterIndex).getMembers());
				System.out.println("@@@ the other cluster size: " + leftClusterIndex + ", " + cluster.size());
				return doResolveTable(mergedCluster, cluster);

			} else {
				HeaderDelimitedTable resolveResult = new HeaderDelimitedTable(TableStatus.RegularTable,
						DataTableHeaderType.UnDetermined);
				return resolveResult;
			}

		} else {

			// Sorts clusters based on their members' minimal sequence number
			Collections.sort(copiedClusters, new ClusterMemberSequenceNumberComparator());

			// Finds the index of the cluster having the largest member size
			int indexOfLargestCluster = getIndexOfLargestCluster(copiedClusters);
			System.out.println("@@@ indexOfLargestCluster: " + indexOfLargestCluster);
			
			// We assume that cluster having the largest member size is the
			// cluster of table data records. Therefore, there must have
			// clusters before the data-records cluster and these clusters
			// contain table header records. In other words, the index should be
			// larger than 0 and if it is not, the table delimiting failed
			if (indexOfLargestCluster > 0) {
				List<TableRecord> mergedCluster = new ArrayList<TableRecord>();
				for (int i = 0; i < indexOfLargestCluster; i++) {
					for (TableRecord tr : copiedClusters.get(i).getMembers()) {
						mergedCluster.add(tr);
					}
				}

				List<TableRecord> cluster = new ArrayList<TableRecord>(
						copiedClusters.get(indexOfLargestCluster).getMembers());
				return doResolveTable(mergedCluster, cluster);
			} else {
				HeaderDelimitedTable resolveResult = new HeaderDelimitedTable(TableStatus.RegularTable,
						DataTableHeaderType.UnDetermined);
				return resolveResult;
			}
		}
	}

	/**
	 * Gets index of the cluster that has the largest member size from the
	 * specified list of clusters.
	 * 
	 * @param clusters
	 *            the list of clusters
	 * @return the index of the cluster having the largest member size
	 */
	private int getIndexOfLargestCluster(List<ICluster<TableRecord>> clusters) {
		int max = 0;
		int index = -1;
		for (int i = 0; i < clusters.size(); i++) {
			int clusterSize = clusters.get(i).getMembers().size();
			if (clusterSize > max) {
				max = clusterSize;
				index = i;
			}
		}
		return index;
	}

	/**
	 * Checks if all clusters in the specified list have the same number of
	 * members.
	 * 
	 * @param clusters
	 *            the list of clusters
	 * @return true if all clusters have the same number of members; false,
	 *         otherwise
	 */
	private boolean areClustersEqualSize(List<ICluster<TableRecord>> clusters) {
		boolean equal = true;
		for (int i = 0; i < clusters.size() - 1; i++) {
			if (clusters.get(i).getMembers().size() != clusters.get(i + 1).getMembers().size()) {
				equal = false;
				break;
			}
		}
		return equal;
	}

	/**
	 * Merges two clusters in the specified list that are most similar to each
	 * other.
	 * 
	 * @param clusters
	 *            the list of clusters
	 * @return the merging result
	 */
	private MergedClusterResult mergeClusters(List<ICluster<TableRecord>> clusters) {

		int numOfClusters = clusters.size();
		boolean mergeFound = false;
		int mergedCluster1Index = -1;
		int mergedCluster2Index = -1;
		double threshold = Math.min(0.3, algorithm.getClusterMergingThreshold() - 0.1);
		double maxSim = 0.0;
		for (int i = 0; i < numOfClusters - 1; i++) {
			for (int j = i + 1; j < numOfClusters; j++) {
				Set<TableRecord> cluster_i = clusters.get(i).getMembers();
				Set<TableRecord> cluster_j = clusters.get(j).getMembers();

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
		}

		MergedClusterResult result = new MergedClusterResult(mergeFound);
		result.setMergedClusterOneIndex(mergedCluster1Index);
		result.setMergedClusterTwoIndex(mergedCluster2Index);
		return result;
	}

	/**
	 * Validate if the member sequences across all the clusters is correct.
	 * Specifically, the sequences of members in the before cluster should all
	 * be smaller than the sequences of members in the after cluster.
	 * 
	 * @param clusterBefore
	 *            the cluster in the front
	 * @param clusterAfter
	 *            the cluster in the back
	 * @return true if sequences of members across all clusters are correct.
	 */
	protected boolean validateClusterSequence(List<TableRecord> clusterBefore, List<TableRecord> clusterAfter) {
		Collections.sort(clusterBefore);
		Collections.sort(clusterAfter);

		if (clusterBefore.get(clusterBefore.size() - 1).getSequenceNumber() >= clusterAfter.get(0).getSequenceNumber())
			return false;
		return true;
	}

	/**
	 * Validate to check if all the specified table records are valid header.
	 * <p>
	 * Note that this method should <strong>NOT</strong> be called before the
	 * {@link resolve} method.
	 * </p>
	 * 
	 * @param tableRecords
	 *            the table records to be validated
	 * @return
	 */
	protected List<TableRecord> validateHeaderCluster(List<TableRecord> tableRecords) {
		// Each cell in the header should have highly similar structure
		// (Not implemented: all cells must have a value. More strict way to
		// validate the header is to check whether all the data cell values have
		// highly similar type, if not the same).
		List<TableRecord> validatedTableRecords = new ArrayList<TableRecord>();
		IDataCellsSimilarity dateCellsSimilarity = tableRecordsSimiliartySuite.getDataCellsSimilarity();
		IAttributesSimilarity attributesSimilarity = tableRecordsSimiliartySuite.getAttributesSimilarity();
		for (TableRecord tableRecord : tableRecords) {
			List<TableCell> tableCells = tableRecord.getTableCells();

			int size = tableCells.size();
			int count = 0;
			double sum = 0.0;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j != i && j < size; j++) {
					count++;
					sum += computeDataCellsSimilarity(tableCells.get(i), tableCells.get(j), attributesSimilarity,
							dateCellsSimilarity);
				}
			}

			double average = (count == 0 ? 0 : sum / count);
			if (average > 0.65) {
				validatedTableRecords.add(tableRecord);
			}
		}

		return validatedTableRecords;
	}

	/**
	 * Computes the similarity between two table cells.
	 * 
	 * @param tableCell1
	 *            the table cell one
	 * @param tableCell2
	 *            the table cell two
	 * @param attributesSimilarity
	 *            the similarity measurement measures the attribute similarity
	 * @param dateCellsSimilarity
	 *            the similarity measurement measures the similarity of data
	 *            contained in cell
	 * @return the similarity
	 */
	private double computeDataCellsSimilarity(TableCell tableCell1, TableCell tableCell2,
			IAttributesSimilarity attributesSimilarity, IDataCellsSimilarity dateCellsSimilarity) {
		double attributeSim = attributesSimilarity.computeSimilarity(tableCell1, tableCell2);
		double dataCellSim = dateCellsSimilarity.computeSimilarity(tableCell1, tableCell2);
		return (attributeSim + dataCellSim) / 2;
	}

	/**
	 * 
	 * @param cluster
	 * @return
	 */
	protected abstract HeaderDelimitedTable doResolveTable(List<TableRecord> cluster);

	/**
	 * 
	 * @param cluster1
	 * @param cluster2
	 * @return
	 */
	protected abstract HeaderDelimitedTable doResolveTable(List<TableRecord> cluster1, List<TableRecord> cluster2);

	/**
	 * Convert the specified Element representing a table or tbody element to a
	 * <code>HTMLDataTable</code>
	 * 
	 * @param tableElem
	 * @return a <code>HTMLDataTable</code>
	 */
	protected abstract HTMLDataTable convertToDataTable(Element tableElem);

	/**
	 * This class compares two clusters of table records based on sequence
	 * numbers of these table records. Specifically, cluster who has smaller
	 * minimal sequence number among its members will rank higher.
	 * 
	 * @author yankang
	 */
	private class ClusterMemberSequenceNumberComparator implements Comparator<ICluster<TableRecord>> {

		@Override
		public int compare(ICluster<TableRecord> cluster1, ICluster<TableRecord> cluster2) {
			int minSequence1 = getMinSequenceNumber(cluster1);
			int minSequence2 = getMinSequenceNumber(cluster2);
			return Integer.compare(minSequence1, minSequence2);
		}

		private int getMinSequenceNumber(ICluster<TableRecord> cluster) {
			Set<TableRecord> members = cluster.getMembers();
			int min = Integer.MAX_VALUE;
			for (TableRecord rec : members) {
				min = Math.min(min, rec.getSequenceNumber());
			}
			return min;
		}
	}

	/**
	 * 
	 * @author yankang
	 *
	 */
	private class MergedClusterResult {

		private int mergedClusterOneIndex;
		private int mergedClusterTwoIndex;
		private boolean mergeFound = false;

		public MergedClusterResult(boolean mergeFound) {
			this.mergeFound = mergeFound;
		}

		public boolean isMergeFound() {
			return this.mergeFound;
		}

		/**
		 * @return the mergedClusterOneIndex
		 */
		public int getMergedClusterOneIndex() {
			return mergedClusterOneIndex;
		}

		/**
		 * @param mergedClusterOneIndex
		 *            the mergedClusterOneIndex to set
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
}
