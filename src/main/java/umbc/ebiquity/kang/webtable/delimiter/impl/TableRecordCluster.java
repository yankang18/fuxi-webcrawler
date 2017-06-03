package umbc.ebiquity.kang.webtable.delimiter.impl;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import umbc.ebiquity.kang.machinelearning.clustering.ICluster;
import umbc.ebiquity.kang.webtable.core.TableRecord;
import umbc.ebiquity.kang.webtable.similarity.ITableRecordsSimilarity;

public class TableRecordCluster implements ICluster<TableRecord> {

	private String id;
	private Set<TableRecord> members;
	private ITableRecordsSimilarity tableRecordsSimilarity;

	public TableRecordCluster(String id, ITableRecordsSimilarity tableRecordsSimilarity) {
		this.id = id;
		this.members = new LinkedHashSet<TableRecord>();
		this.tableRecordsSimilarity = tableRecordsSimilarity;
	}
	
	@Override
	public void addMember(TableRecord member) {
		members.add(member);
	}

	@Override
	public double computeSimilarity(ICluster<TableRecord> cluster) {
		Set<TableRecord> memberSet1 = getMembers();
		Set<TableRecord> memberSet2 = cluster.getMembers();
		return tableRecordsSimilarity.computeSimilarity(memberSet1, memberSet2); 
	}

	@Override
	public ICluster<TableRecord> merge(ICluster<TableRecord> m) {
		TableRecordCluster cluster = new TableRecordCluster(UUID.randomUUID().toString(), tableRecordsSimilarity);
		for (TableRecord tr : m.getMembers()) {
			cluster.addMember(tr);
		}
		for (TableRecord tr : getMembers()) {
			cluster.addMember(tr);
		}
		return cluster;
	}

	@Override
	public boolean isSameAs(ICluster<TableRecord> m) {
		return getId().equals(m.getId());
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Set<TableRecord> getMembers() {
		return members;
	}
	
	@Override
	public int hashCode(){
		return this.getId().hashCode();
	}

	@Override
	public boolean equals(Object c) {
		if (this == c)
			return true;
		if (c instanceof TableRecordCluster) {
			TableRecordCluster cluster = (TableRecordCluster) c;
			return this.getId().equals(cluster.getId());
		}
		return false;
	}

}
