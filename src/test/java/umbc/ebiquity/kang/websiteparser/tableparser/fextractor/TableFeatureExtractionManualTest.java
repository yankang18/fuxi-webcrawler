package umbc.ebiquity.kang.websiteparser.tableparser.fextractor;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Test;

import edu.stanford.nlp.coref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import umbc.ebiquity.kang.htmltable.feature.ITableFeaturesExtractor;
import umbc.ebiquity.kang.htmltable.feature.impl.StandardTableFeaturesExtractorFactory;
import umbc.ebiquity.kang.htmltable.feature.impl.TrainingDataSetCreator;
import umbc.ebiquity.kang.machinelearning.classification.FileDataSetCreator;
import umbc.ebiquity.kang.machinelearning.classification.IFeatureData;
import umbc.ebiquity.kang.machinelearning.classification.impl.DataSet;
import umbc.ebiquity.kang.machinelearning.classification.impl.WekaClassifierUtil;
import umbc.ebiquity.kang.machinelearning.classification.modelselection.ICrossValidationDetail;
import umbc.ebiquity.kang.machinelearning.classification.modelselection.ICrossValidationSplits;
import umbc.ebiquity.kang.machinelearning.classification.modelselection.impl.WekaClassifierEvaluationLab;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class TableFeatureExtractionManualTest {
	
	@Ignore
	@Test
	public void testConstructHorizontalTableRecordListWithDepthOne() throws IOException {

		// String fileName =
		// "TableFeaturesTest/HorizontalHeaderTableWithHeadIntheTbody.html";
		String fileName = "TableFeaturesTest/DataTable0.html";
		File input = loadFileOrDirectory(fileName);
		
		Document doc = Jsoup.parse(input, "UTF-8");
		Element element = doc.getElementsByTag("table").get(0);

		System.out.println(element.toString());

		ITableFeaturesExtractor extractor = (new StandardTableFeaturesExtractorFactory()).create();
		IFeatureData features = extractor.extract(element);
		
		printExtractedFeatures(features);

	}

	private void printExtractedFeatures(IFeatureData features) {
		Set<String> featureNames = features.getFeatureNames();
		for (String name : featureNames) {
			System.out.println(name + " : " + features.getFeatureValue(name));
		}
	}

	@Ignore
	@Test
	public void WekaJ48ClassifierTrainingTest() throws IOException {

		String dirName = "TableFeaturesTest";
		File directory = loadFileOrDirectory(dirName);

		Set<String> classLabels = new HashSet<String>(2);
		classLabels.add("layout");
		classLabels.add("data");

		ITableFeaturesExtractor extractor = (new StandardTableFeaturesExtractorFactory()).create();

		FileDataSetCreator trainingDataSetCreator = new TrainingDataSetCreator(extractor, extractor.getFeatureNames(),
				classLabels);
		DataSet trainingDataSet = trainingDataSetCreator.createDataSet(directory);
		Instances instances = WekaClassifierUtil.convertDataSetToWekaInstances(trainingDataSet);
		try {
			Classifier classifier = createSVMClassifier();
			ICrossValidationSplits splits = WekaClassifierEvaluationLab.createCrossValidationSplits(instances, 10,
					true);
			ICrossValidationDetail detail = WekaClassifierEvaluationLab.crossValidate(classifier, splits);
			double[] accuracies = detail.getAccuracies();
			double overallAccuracy = detail.getOverallAccuracy();
			Classifier[] classifiers = detail.getClassifiers();

			for (int i = 0; i < accuracies.length; i++) {
				System.out.println(i + ": " + accuracies[i]);
				System.out.println(classifiers[i]);
			}
			System.out.println(overallAccuracy);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Classifier createSVMClassifier() {
		SMO classifier = new SMO();
		PolyKernel polyKernel = new PolyKernel();
		polyKernel.setExponent(2);
		classifier.setKernel(polyKernel);
		return classifier;
	}

//	@Ignore
	@Test
	public void StanforNLPParseTest() throws IOException {
		
//		String text = "Up to 310 in (25 ft)";
//		System.out.println(text.replaceAll("\\(.*\\)", ""));
		
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization,
		// NER, parsing, and coreference resolution
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// read some text in the text variable
		String text = "Although one can dig into the documentation, I am going to provide code here on SO, "
				+ "especially since links move and/or die. This particular answer uses the whole pipeline. "
				+ "If not interested in the whole pipeline, I will provide an alternative answer in just a second. "
				+ "The below example is the complete way of using the Stanford pipeline. "
				+ "If not interested in coreference resolution, remove dcoref from the 3rd "
				+ "line of code. So in the example below, the pipeline does the sentence splitting "
				+ "for you (the ssplit annotator) if you just feed it in a body of text (the text variable). "
				+ "Have just one sentence? Well, that is ok, you can feed that in as the text variable.";

	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);

	    // run all Annotators on this text
	    pipeline.annotate(document);

	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

	    for(CoreMap sentence: sentences) {
	    	System.out.println("---------");
	      // traversing the words in the current sentence
	      // a CoreLabel is a CoreMap with additional token-specific methods
	      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	    		System.out.println("=======");
	        // this is the text of the token
	        String word = token.get(TextAnnotation.class);
	        System.out.println(word);
	        // this is the POS tag of the token
	        String pos = token.get(PartOfSpeechAnnotation.class);
	        System.out.println(pos);
	        System.out.println(pos.equals("''"));
//	        // this is the NER label of the token
//	        String ne = token.get(NamedEntityTagAnnotation.class);  
//	        System.out.println(ne);
	      }
//	      // this is the parse tree of the current sentence
//	      Tree tree = sentence.get(TreeAnnotation.class);
//	      System.out.println(tree);
	    }
	}
	

	public File loadFileOrDirectory(String fileName) {
		// Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File input = new File(classLoader.getResource(fileName).getFile());
		return input;
	} 
}
