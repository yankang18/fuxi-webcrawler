package umbc.ebiquity.kang.machinelearning.math.util;

import java.util.Map;

public class Mathematics {
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public static double computeEntropy(Map<String, Integer> map) {
		if(map == null) 
			return 0.0;

		int totalRec = 0;
		for (int v : map.values()) {
			totalRec += v;
		}

		double score = 0.0;
		for (String group : map.keySet()) {
			int rec = map.get(group);
			double ratio = (double) rec / totalRec;
			score += -1 * (ratio) * (ratio == 0 ? 0 : log(ratio, 2));
		}
		return score;
	}
	
	public static double computeMaxEntropy(int numOfGroup) {
		return log(numOfGroup, 2);
	}

	/**
	 * 
	 * @param x
	 * @param base
	 * @return
	 */
	public static double log(double x, int base) {
		return Math.log(x) / Math.log(base);
	}
}
