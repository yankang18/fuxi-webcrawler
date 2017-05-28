package umbc.ebiquity.kang.webtable.spliter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.htmldocument.impl.StandardHtmlElement;
import umbc.ebiquity.kang.machinelearning.clustering.HierarchicalClusteringAlgorithm;
import umbc.ebiquity.kang.machinelearning.clustering.ICluster;
import umbc.ebiquity.kang.webtable.core.HTMLDataTable;
import umbc.ebiquity.kang.webtable.core.HTMLTableValidator;
import umbc.ebiquity.kang.webtable.core.TableCell;
import umbc.ebiquity.kang.webtable.core.TableRecord;
import umbc.ebiquity.kang.webtable.similarity.IAttributesSimilarity;
import umbc.ebiquity.kang.webtable.similarity.IDataCellsSimilarity;
import umbc.ebiquity.kang.webtable.similarity.ITableRecordsSimilarity;
import umbc.ebiquity.kang.webtable.similarity.ITableRecordsSimilarityFactory;
import umbc.ebiquity.kang.webtable.similarity.ITableRecordsSimiliartySuite;
import umbc.ebiquity.kang.webtable.similarity.impl.TableRecordsSimilarityFactory;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.TableStatus;
import umbc.ebiquity.kang.webtable.spliter.impl.TableRecordCluster;
import umbc.ebiquity.kang.webtable.spliter.impl.TableSplitingResult;

public abstract class AbstractClusteringBasedTableSpliter implements ITableHeaderSpliter {

	private ITableRecordsSimilarityFactory tableRecordsSimilarityFactory;
	private ITableRecordsSimiliartySuite tableRecordsSimiliartySuite;
	private ITableRecordsSimilarity tableRecordsSimilarity;
	private HierarchicalClusteringAlgorithm<TableRecord> algorithm;

	public AbstractClusteringBasedTableSpliter() {
		algorithm = new HierarchicalClusteringAlgorithm<TableRecord>();
		tableRecordsSimilarityFactory = new TableRecordsSimilarityFactory();
	}

	@Override
	public TableSplitingResult split(Element tableElement) {
		HTMLTableValidator.isTable(tableElement); 

		Element actualTableElem = null;
		Elements tbodies = tableElement.getElementsByTag("tbody");
		if (tbodies.size() > 0) {
			actualTableElem = tbodies.get(0);
		} else {
			actualTableElem = tableElement;
		}
		Set<ICluster<TableRecord>> clusters = clusterTableRecords(actualTableElem);
		clusters = validateClusterMembers(clusters);
		TableSplitingResult result = resolveTableBasedOnClusters(clusters);
		result.setHtmlElement(StandardHtmlElement.createDefaultStandardHtmlElement(actualTableElem));
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
	private Set<ICluster<TableRecord>> validateClusterMembers(Set<ICluster<TableRecord>> clusters) {
		// TODO: Remove record that all date cells it contains have no value
		// (image and functional tag does not consider as a value for now).
		return clusters;
	}

	/**
	 * Do the real work of clustering <code>TableRecord</code>s in the specified
	 * HTML table.
	 * 
	 * @param actualTableElem
	 *            the Element representing the actual table from which the table
	 *            records to be clustered
	 * @return a Set of clusters of <code>TableRecord</code> in the specified
	 *         HTML table
	 */
	private Set<ICluster<TableRecord>> clusterTableRecords(Element actualTableElem) {

		HTMLDataTable table = convertToDataTable(actualTableElem);

		List<TableRecord> tableRecords = table.getTableRecords();
		System.out.println("tableRecords: " + tableRecords.size());
		
		tableRecordsSimiliartySuite = tableRecordsSimilarityFactory.createTableRecordsSimilairtySuite(tableRecords);
		tableRecordsSimilarity = tableRecordsSimiliartySuite.getTableRecordsSimilarity();
		
		Set<ICluster<TableRecord>> initClusters = createInitialClusters(tableRecords, tableRecordsSimilarity);

		System.out.println("initClusters: " + initClusters.size());
		Set<ICluster<TableRecord>> afterClusters = algorithm.doCluster(initClusters);
		System.out.println("afterClusters: " + afterClusters.size());

		// for (ICluster<TableRecord> c : afterClusters) {
		// System.out.println();
		// System.out.println("------ cluster: " + c.getMembers().size());
		//
		// for (TableRecord r : c.getMembers()) {
		// System.out.println("------ TableRecord: ");
		// for (TableCell cell : r.getTableCells()) {
		// System.out.println("------ TableCell size: " +
		// cell.getDataCells().size());
		// for (DataCell dc : cell.getDataCells()) {
		// System.out.println(dc.getWrappedNode());
		// }
		// }
		// }
		// }

		return afterClusters;
	}

	/**
	 * 
	 * @param clusters
	 * @return
	 */
	private TableSplitingResult resolveTableBasedOnClusters(Set<ICluster<TableRecord>> clusters) {

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
				TableSplitingResult resolveResult = new TableSplitingResult(TableStatus.RegularTable,
						DataTableHeaderType.UnDetermined);
				return resolveResult;
			}

		} else {
			TableSplitingResult resolveResult = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.UnDetermined);
			return resolveResult;
		}
	}

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

	private MergedClusterResult mergeClusters(List<ICluster<TableRecord>> list) {

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
	
	private double computeDataCellsSimilarity(TableCell tableCell1, TableCell tableCell2,
			IAttributesSimilarity attributesSimilarity, IDataCellsSimilarity dateCellsSimilarity) {
		double attributeSim = attributesSimilarity.computeSimilarity(tableCell1, tableCell2);
		double dataCellSim = dateCellsSimilarity.computeSimilarity(tableCell1, tableCell2);
		return (attributeSim + dataCellSim) / 2;
	}

	protected abstract TableSplitingResult doResolveTable(List<TableRecord> cluster);

	/**
	 * 
	 * @param cluster1
	 * @param cluster2
	 * @return
	 */
	protected abstract TableSplitingResult doResolveTable(List<TableRecord> cluster1, List<TableRecord> cluster2);

	/**
	 * Convert the specified Element representing a table or tbody element to a
	 * <code>HTMLDataTable</code>
	 * 
	 * @param tableElem
	 * @return a <code>HTMLDataTable</code>
	 */
	protected abstract HTMLDataTable convertToDataTable(Element tableElem);

}
