package umbc.ebiquity.kang.machinelearning.classification;

public interface IPrediction {

	  /**
	   * Gets the predicted class value.
	   *
	   * @return the predicted class value, or MISSING_VALUE if no
	   * prediction was made.  
	   */
	  double predicted();

}
