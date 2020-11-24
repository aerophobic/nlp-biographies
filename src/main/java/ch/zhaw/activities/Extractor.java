package ch.zhaw.activities;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class Extractor {

	private final Manager mgmt;
	private final StanfordCoreNLP simplePipeline;
	private final StanfordCoreNLP complexPipeline;

	public Extractor(Manager mgmt) {
		this.mgmt = mgmt;

		Properties properties = new Properties();
		properties.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote,entitymentions");
		this.simplePipeline = new StanfordCoreNLP(properties);

		properties = new Properties();
		properties.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,coref,natlog,openie");
		this.complexPipeline = new StanfordCoreNLP(properties);
	}

	public void getCorefs(String text) {
		Annotation annotation = new Annotation(text);
		simplePipeline.annotate(annotation);
		System.out.println("---");
		System.out.println("text: " + text);
		System.out.println("");
		System.out.println("dependency edges:");
		for (CoreMap sentence1 : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
			SemanticGraph sg = sentence1.get(SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation.class);
			for (SemanticGraphEdge sge : sg.edgeListSorted()) {
				System.out
						.println(sge.getGovernor().word() + "," + sge.getGovernor().index() + "," + sge.getGovernor().tag() + ","
								+ sge.getGovernor().ner() + " - " + sge.getRelation().getLongName() + " -> " + sge.getDependent().word()
								+ "," + +sge.getDependent().index() + "," + sge.getDependent().tag() + "," + sge.getDependent().ner());
			}
			String verb = "";
			String subject = "";
			String object = "";
			String modifier = "";
			for (SemanticGraphEdge sge : sg.edgeListSorted()) {
				System.out
						.println(sge.getGovernor().word() + "," + sge.getGovernor().index() + "," + sge.getGovernor().tag() + ","
								+ sge.getGovernor().ner() + " - " + sge.getRelation().getLongName() + " -> " + sge.getDependent().word()
								+ "," + +sge.getDependent().index() + "," + sge.getDependent().tag() + "," + sge.getDependent().ner());
			}
			System.out.println();
			System.out.println("entity mentions:");
			for (CoreMap entityMention : sentence1.get(CoreAnnotations.MentionsAnnotation.class)) {
				int lastTokenIndex = entityMention.get(CoreAnnotations.TokensAnnotation.class).size() - 1;
				System.out.println(entityMention.get(CoreAnnotations.TextAnnotation.class) + "\t"
						+ entityMention.get(CoreAnnotations.TokensAnnotation.class).get(lastTokenIndex)
								.get(CoreAnnotations.IndexAnnotation.class)
						+ "\t" + entityMention.get(CoreAnnotations.NamedEntityTagAnnotation.class));
			}
		}

		System.out.println("------------ find the fuking verb");
		CoreDocument simpleDocument = new CoreDocument(text);
		this.simplePipeline.annotate(simpleDocument);
		for (CoreSentence sentence : simpleDocument.sentences()) {
			for (String verbPhrase : sentence.verbPhrases()) {
				System.out.println(verbPhrase);
			}

			for (Tree verbPhrase : sentence.verbPhraseTrees()) {
				
				for (TaggedWord consti : verbPhrase.taggedYield()) {
					System.out.println(consti);
				}
			}
			// System.out.println(sentence.tokensAsStrings());
			// for (CoreLabel token : sentence.tokens()) {
			// System.out.println(token.tag());
			// if (token.tag().equals("VB")) {
			// System.out.println("its a verb");
			// }
			// }
			// for(String verbPhrase : sentence.verbPhrases()) {
			// System.out.println(verbPhrase);
			// }
		}
		System.out.println("----end of-------- find the fuking verb");

		CoreDocument complexDocument = new CoreDocument(text);
		this.runComplexPipeline(complexDocument);
		Map<Integer, CorefChain> map = complexDocument.corefChains();
		for (Map.Entry<Integer, CorefChain> entry : map.entrySet()) {
			this.printCoreference(entry);
		}
		HashMap<String, RelationTriple> selectedTriples = new HashMap<String, RelationTriple>();
		for (CoreMap sentence : complexDocument.annotation().get(CoreAnnotations.SentencesAnnotation.class)) {
			Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class); //
			System.out.println("\t" + triples.size());
			for (RelationTriple triple : triples) {
				Boolean existsInMap = selectedTriples
						.containsKey(triple.subjectLemmaGloss() + " " + triple.relationLemmaGloss());

				if (existsInMap) {
					RelationTriple tripleInMap = selectedTriples
							.get(triple.subjectLemmaGloss() + " " + triple.relationLemmaGloss());

					System.out.println("relation");
					System.out.println(tripleInMap.relationLemmaGloss());
					System.out.println(triple.relationLemmaGloss());

					// Boolean hasSameRelation = tripleInMap.relationLemmaGloss() ==
					// triple.relationLemmaGloss();
					// System.out.println("\thave same relation");
					// System.out.println("\t" + hasSameRelation);
					Boolean hasCommonObjectElements = Collections.disjoint(
							Arrays.asList(tripleInMap.objectLemmaGloss().split(" ")),
							Arrays.asList(triple.objectLemmaGloss().split(" ")));
					Boolean objectLemmaIsShorter = tripleInMap.objectLemmaGloss().length() > triple.objectLemmaGloss().length();

					if (hasCommonObjectElements && objectLemmaIsShorter) {
						selectedTriples.put(triple.subjectLemmaGloss() + " " + triple.relationLemmaGloss(), triple);
					}
				} else {
					selectedTriples.put(triple.subjectLemmaGloss() + " " + triple.relationLemmaGloss(), triple);
				}
			}
		}

		System.out.println("\t" + selectedTriples.size());
		for (RelationTriple triple : selectedTriples.values()) {
			System.out.println("\t" + triple.subjectLemmaGloss());
			System.out.println("\t" + triple.relationLemmaGloss());
			System.out.println("\t" + triple.objectLemmaGloss());
			System.out.println("-----------------------------------------");
		}
	}

	public void printCoreference(Map.Entry<Integer, CorefChain> entry) {
		System.out.println("Co-reference Mentions:");
		CorefChain chain = entry.getValue();
		for (CorefChain.CorefMention mention : chain.getMentionsInTextualOrder()) {
			System.out.println("\t" + mention);
		}

		System.out.println("\t" + chain.getRepresentativeMention());
	}

	public void analyze(String text) {
		CoreDocument document = new CoreDocument(text);
		this.runComplexPipeline(document);

		for (CoreMap sentence : document.annotation().get(CoreAnnotations.SentencesAnnotation.class)) {
			// Get the OpenIE triples for the sentence Collection<RelationTriple> triples

			Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class); //

			// System.out.println(sentence.get(TreeAnnotation.class));
			this.mgmt.addActivity(sentence.toShorterString());

			// Print the triple- and ner-facts
			for (RelationTriple triple : triples) {

				this.mgmt.store(triple);

				// List<String> subjects = new ArrayList<String>();
				// List<String> objects = new ArrayList<String>();

				// System.out.println("subjects");
				// for (CoreLabel current : triple.subject) {
				// String ner = current.get(NamedEntityTagAnnotation.class);
				// if (ner != null) {
				// System.out.println(current.lemma());
				// subjects.add(current.lemma());
				// }
				// }

				// System.out.println("objects");
				// for (CoreLabel current : triple.object) {
				// String ner = current.get(NamedEntityTagAnnotation.class);
				// if (ner != null) {
				// System.out.println(current.lemma());
				// objects.add(current.lemma());
				// }
				// }
			}
		}
	}

	private void runComplexPipeline(CoreDocument document) {
		this.complexPipeline.annotate(document);
	}

	public Manager getMgmt() {
		return mgmt;
	}
}
