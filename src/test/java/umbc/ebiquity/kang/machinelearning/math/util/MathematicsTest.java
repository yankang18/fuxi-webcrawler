package umbc.ebiquity.kang.machinelearning.math.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MathematicsTest {

	@Test
	public void testComputeEntropy() {
		
		Map<String, Integer> map = new HashMap<>();
		map.put("group1", 2);
		map.put("group2", 2);
		double result = Mathematics.computeEntropy(map);
		System.out.println("result: " + result);
		assertEquals(1, result, 0);
		
		map = new HashMap<>();
		map.put("group1", 4);
		map.put("group2", 4);
		map.put("group3", 4);
		map.put("group4", 4);
		result = Mathematics.computeEntropy(map);
		System.out.println("result: " + result);
		assertEquals(2, result, 0);
		
		map = new HashMap<>();
		map.put("group1", 4);
		map.put("group2", 0);
		result = Mathematics.computeEntropy(map);
		System.out.println("result: " + result);
		assertEquals(0, result, 0);
	}
	
	@Test
	public void testComputeMaxEntropy() {
		assertEquals(0, Mathematics.computeMaxEntropy(1), 0);
		assertEquals(1, Mathematics.computeMaxEntropy(2), 0);
		assertEquals(2, Mathematics.computeMaxEntropy(4), 0);
		assertEquals(3, Mathematics.computeMaxEntropy(8), 0);
	}
}
