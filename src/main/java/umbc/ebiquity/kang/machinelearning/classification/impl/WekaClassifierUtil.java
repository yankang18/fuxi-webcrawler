package umbc.ebiquity.kang.machinelearning.classification.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

public class WekaClassifierUtil {

	/**
	 * Converts a feature set object to a weka Instances object
	 * <p/>
	 * The class is set to the last attribute.
	 * 
	 * @param dataSet
	 *            the date set to convert
	 * @return a weka instances object
	 */
	public static Instances convertDataSetToWekaInstances(DataSet dataSet) {
		ArrayList<Attribute> attributes = convertDataFeaturesToWekaAttributes(dataSet.getFeatures());
		Instances instances = new Instances("dataset", attributes, dataSet.getData().size());
		for (DataPoint w : dataSet.getData()) {
			Instance inst = doConstructWekaInstance(instances, w);
			instances.add(inst);
		}

		setWekaClassAttribute(instances, dataSet.getClassAttribute());
		return instances;
	}
	
	/**
	 * Converts a single point to a weka instance
	 * 
	 * @param dataPoint
	 *            the data point
	 * @param features
	 *            the features to include on the data point
	 * @param classAttribute
	 *            the class attribute
	 * @return a weka instance of the point
	 */
	public static Instance convertDataPointToInstance(DataPoint dataPoint, Set<FeatureMetaData> features,
			String classAttribute) {
		ArrayList<Attribute> attributes = convertDataFeaturesToWekaAttributes(features);
		return constructWekaInstance(attributes, dataPoint, classAttribute);
	}

	/**
	 * Constructs a data point to a weka instance given a FastVector of weka
	 * attribute and a class attribute.
	 * 
	 * @param attributes
	 *            a FastVector of weka attributes
	 * @param dataPoint
	 *            the data point to convert
	 * @param classAttribute
	 *            the class attribute
	 * @return a weka instance.
	 */
	private static Instance constructWekaInstance(ArrayList<Attribute> attributes, DataPoint dataPoint,
			String classAttribute) {
		Instances instances = new Instances("single_instance_set", attributes, 0);

		setWekaClassAttribute(instances, classAttribute);
		return doConstructWekaInstance(instances, dataPoint);
	}

	/**
	 * Generate a List weka Attributes from a set of features.
	 * 
	 * @param features
	 *            the set of features
	 * @return a List of weka attributes
	 */
	private static ArrayList<Attribute> convertDataFeaturesToWekaAttributes(Set<FeatureMetaData> features) {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();

		for (FeatureMetaData f : features) {
			String attribute_name = f.getName();
			if (f.isNominal()) {
				List<String> attribute_values = new ArrayList<String>();
				for (String s : f.getNominalValues()) {
					attribute_values.add(s);
				}
				attributes.add(new Attribute(attribute_name, attribute_values, attributes.size()));
			} else if (f.isString()) {
				attributes.add(new weka.core.Attribute(attribute_name, (List<String>) null, attributes.size()));
			} else if (f.isNumberic()) {
				attributes.add(new weka.core.Attribute(attribute_name, attributes.size()));
			} else {
				// omit this current feature
			}
		}
		return attributes;
	}

	/**
	 * Given a (possibly empty) Instances object containing the required weka
	 * Attributes, generates a weka Instance for a single data point.
	 * 
	 * @param instances
	 *            the weka Instances object containing attributes
	 * @param datum
	 *            the data point to convert
	 * @return a weka instance with assigned attributes
	 */
	private static Instance doConstructWekaInstance(Instances instances, DataPoint datum) {
		double[] instance = new double[instances.numAttributes()];
		for (int i = 0; i < instances.numAttributes(); ++i) {
			Attribute attribute = instances.attribute(i);
			if (datum.hasAttribute(attribute.name()) && !datum.getAttribute(attribute.name()).toString().equals("?")) {
				switch (attribute.type()) {
				case Attribute.NOMINAL:
					int index = attribute.indexOfValue(datum.getAttribute(attribute.name()).toString());
					instance[i] = (double) index;
					break;
				case Attribute.NUMERIC:
					// Check if value is really a number.
					try {
						instance[i] = Double.valueOf(datum.getAttribute(attribute.name()).toString());
					} catch (NumberFormatException e) {
//						AuToBIUtils.error("Number expected for feature: " + attribute.name());
					}
					break;
				case Attribute.STRING:
					instance[i] = attribute.addStringValue(datum.getAttribute(attribute.name()).toString());
					break;
				default:
//					AuToBIUtils.error("Unknown attribute type");
				}
			} else {
				instance[i] = Utils.missingValue();
			}
		}

		Instance inst = new DenseInstance(1, instance);
		inst.setDataset(instances);
		return inst;
	}

	/**
	 * Assigns a class attribute to a weka Instances object.
	 * <p/>
	 * If no class attribute is given, or if the class attribute is not found in
	 * the list of attributes, the last attribute is set to the class attribute.
	 * 
	 * @param instances
	 *            the instances object
	 * @param classAttribute
	 *            the desired class attribute.
	 */
	static void setWekaClassAttribute(Instances instances, String classAttribute) {
		if (classAttribute != null) {
			int i = 0;
			boolean set = false;
			while (i < instances.numAttributes() && !set) {
				Attribute attr = instances.attribute(i);
				if (classAttribute.equals(attr.name())) {
					instances.setClassIndex(i);
					set = true;
				}
				++i;
			}
			if (!set) {
				instances.setClassIndex(instances.numAttributes() - 1);
			}
		} else {
			instances.setClassIndex(instances.numAttributes() - 1);
		}
	}
}
