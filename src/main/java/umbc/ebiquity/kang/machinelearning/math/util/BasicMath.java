package umbc.ebiquity.kang.machinelearning.math.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class BasicMath {
	
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
		System.out.println("-- Number of type: " + map.keySet().size());
		for (String group : map.keySet()) {
			int rec = map.get(group);
			System.out.println("type: " + group + ", num: " + rec);
			double ratio = (double) rec / totalRec;
			System.out.println("ratio: " + ratio);
			System.out.println("score: " + (-1 * (ratio) * (ratio == 0 ? 0 : log(ratio, 2))));
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
	
	public static double roundDown(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_DOWN);
		return bd.doubleValue();
	}
}
