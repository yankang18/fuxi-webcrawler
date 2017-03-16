package umbc.ebiquity.kang.machinelearning.clustering.impl;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import umbc.ebiquity.kang.machinelearning.clustering.ICluster;

public class SimpleCluster implements ICluster<Member> {

	private String id;
	private Set<Member> members;
	
	public SimpleCluster(String id) {
		this.id = id;
		members = new LinkedHashSet<Member>();
	}

	@Override
	public double computeSimilarity(ICluster<Member> c) {
		
		System.out.println("-------------------");
		// TODO Auto-generated method stub
		double total1 = 0;
		int size1 = c.getMembers().size();
		for(Member m: c.getMembers()){
			total1 += m.getValue();
		}
		
		double total2 = 0 ;
		int size2 = getMembers().size();
		for(Member m: getMembers()){
			total2 += m.getValue();
		}

		if (total1 == total2)
			return 1.0;
		
		double ave1 = total1 / size1;

		double ave2 = total2 / size2;
		
		System.out.println("ave1: " + ave1);
		System.out.println("ave2: " + ave2);
		double res = 1 / (Math.abs(ave1 - ave2) + 0.01); 
		System.out.println("res: " + res);
		return res;
	}

	@Override
	public ICluster<Member> merge(ICluster<Member> c) {
		SimpleCluster cluster = new SimpleCluster(UUID.randomUUID().toString());
		for (Member m : c.getMembers()) {
			cluster.addMember(m);
		}
		for (Member m : getMembers()) {
			cluster.addMember(m);
		}
		return cluster;
	}

	@Deprecated
	@Override
	public boolean isSameAs(ICluster<Member> c) {
		return getId().equals(c.getId());
	}

	@Override
	public void addMember(Member member) {
		members.add(member);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public Set<Member> getMembers() {
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
		if (c instanceof SimpleCluster) {
			SimpleCluster cluster = (SimpleCluster) c;
			return this.getId().equals(cluster.getId());
		}
		return false;
	}

}
