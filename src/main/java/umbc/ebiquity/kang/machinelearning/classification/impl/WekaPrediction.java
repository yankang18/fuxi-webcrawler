package umbc.ebiquity.kang.machinelearning.classification.impl;

import umbc.ebiquity.kang.machinelearning.classification.IPrediction;

public class WekaPrediction implements IPrediction {

	private double classValue;

	public WekaPrediction(double value) {
		classValue = value;
	}

//	@Override
//	public double weight() {
//		return 1.0;
//	}
//
//	@Override
//	public double actual() {
//		return MISSING_VALUE;
//	}

	@Override
	public double predicted() {
		return classValue;
	}

}
