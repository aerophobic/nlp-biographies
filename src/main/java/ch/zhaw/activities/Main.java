package ch.zhaw.activities;

// https://rss.com/blog/popular-rss-feeds/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

public class Main {

	private static final String FILE_T2F = "Text2Facts";
	private static final String FILE_F2T = "Facts2Text";

	private static final String __FILE_VERSION = "2.0.1";

	private static final Boolean __TEST = true;
	private static final String __FILE_TEST_INPUT = "protocol_3.txt";

	private static final String __FILE_G2A__TEST = "test\\Guides2Activities.txt";
	private static final String __FILE_A2G__TEST = "test\\Activities2Guides.txt";

	private static final String __FILE_G2A = "Guides2Activities.txt";
	private static final String __FILE_A2G = "Activities2Guides.txt";

	// private static final String[] SOURCES = new String[] { new
	// String("https://www.democracynow.org/democracynow.rss"),
	// new String("http://feeds.bbci.co.uk/news/world/rss.xml"),
	// new String("http://feeds.bbci.co.uk/news/rss.xml"),
	// new String("http://feeds.bbci.co.uk/news/business/rss.xml"),
	// new String("http://feeds.bbci.co.uk/news/politics/rss.xml"),
	// new String("http://feeds.bbci.co.uk/news/health/rss.xml"),
	// new String("http://feeds.bbci.co.uk/news/education/rss.xml"),
	// new String("http://feeds.bbci.co.uk/news/science_and_environment/rss.xml"),
	// new String("http://feeds.bbci.co.uk/news/technology/rss.xml"),
	// new String("http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml"),
	// new String("http://rss.cnn.com/rss/edition_world.rss"), new
	// String("http://rss.cnn.com/rss/edition.rss"),
	// new String("http://rss.cnn.com/rss/cnn_latest.rss"),
	// new String("http://rss.cnn.com/rss/money_news_international.rss"),
	// new String("http://rss.cnn.com/rss/money_latest.rss"),
	// new String("http://rss.cnn.com/rss/edition_technology.rss"),
	// new String("http://rss.cnn.com/rss/edition_space.rss"),
	// new String("http://rss.cnn.com/rss/edition_entertainment.rss"),
	// new String("http://rss.cnn.com/rss/cnn_allpolitics.rss"), new
	// String("http://rss.cnn.com/rss/cnn_tech.rss"),
	// new String("http://rss.cnn.com/rss/cnn_health.rss"), new
	// String("http://rss.cnn.com/rss/cnn_showbiz.rss"),
	// new String("http://rss.cnn.com/cnn-underscored.rss"),
	// new String("https://rss.nytimes.com/services/xml/rss/nyt/World.xml"),
	// new String("https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml"),
	// new String("https://rss.nytimes.com/services/xml/rss/nyt/Economy.xml"),
	// new
	// String("https://rss.nytimes.com/services/xml/rss/nyt/EnergyEnvironment.xml"),
	// new String("https://rss.nytimes.com/services/xml/rss/nyt/Business.xml"),
	// new String("https://rss.nytimes.com/services/xml/rss/nyt/Technology.xml"),
	// new String("https://rss.nytimes.com/services/xml/rss/nyt/PersonalTech.xml"),
	// new String("https://rss.nytimes.com/services/xml/rss/nyt/Science.xml"),
	// new String("https://rss.nytimes.com/services/xml/rss/nyt/Climate.xml"),
	// new String("https://rss.nytimes.com/services/xml/rss/nyt/Space.xml"),
	// // new String("https://www.nytimes.com/services/xml/rss/nyt/Health.xml"),
	// new String("https://rss.nytimes.com/services/xml/rss/nyt/sunday-review.xml")
	// };

	public static Manager mgmt;

	public static void main(String[] args) throws IllegalArgumentException, FeedException, IOException {

		// URL feedSource = new URL("https://www.democracynow.org/democracynow.rss");

		// outer loop variables
		URL feedSource;
		SyndFeedInput input;
		SyndFeed feed;

		// inner loop variables
		SyndEntry entry;
		SyndContent description;
		String text;

		mgmt = new Manager();

		String fileRoot = "C:\\Projekte\\zhaw\\int\\nlp-starter\\protocols";

		if (Main.__TEST) {
			String file = fileRoot + "\\" + Main.__FILE_TEST_INPUT;
			processWetLabData(file);
		} else {
			File directoryPath = new File(fileRoot);
			FilenameFilter textFilefilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					String lowercaseName = name.toLowerCase();
					if (lowercaseName.endsWith(".txt")) {
						return true;
					} else {
						return false;
					}
				}
			};

			// List of all files and directories
			File files[] = directoryPath.listFiles(textFilefilter);

			for (File file : files) {
				processWetLabData(file.getAbsolutePath());
			}
		}
	}

	private static void processText(String text) {
		Extractor extractor;

		try {
			// mgmt.addNewSet();
			extractor = new Extractor(mgmt);

			// text = reMatcher.group();

			extractor.getCorefs(text);
			// Main.writeToFile(text, mgmt.toString());
		} catch (Exception e) {
			System.out.println("> Exception while processing " + text);
			e.printStackTrace();
		}
	}

	private static void processText(String text, Manager mgmt) {
		Extractor extractor;

		try {
			// mgmt.addNewSet();
			extractor = new Extractor(mgmt);

			// text = reMatcher.group();

			extractor.getCorefs(text);
			// Main.writeToFile(text, mgmt.toString());
		} catch (Exception e) {
			System.out.println("> Exception while processing " + text);
			e.printStackTrace();
		}
	}

	public static void processWetLabData(String filename) {
		String text;
		String fullText = "";

		Manager mgmt = new Manager();

		try {
			// read from file
			File myObj = new File(filename);
			Scanner myReader = new Scanner(myObj);

			// read line
			while (myReader.hasNextLine()) {
				text = myReader.nextLine();
				fullText += " " + text;
			}
			myReader.close();

			BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
			String sentence = "";
			iterator.setText(fullText);
			int start = iterator.first();
			for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
				// grab sentence
				sentence = fullText.substring(start, end);
				// remove newlines
				sentence = sentence.replace("\n", "").replace("\r", "");
				processText(sentence, mgmt);
			}

			// analyze
			// System.out.println(mgmt.toBpmnXml(mgmt.getActivities()));
			// try {
			// Main.writeToFile(fullText, mgmt.getActivities(), mgmt);
			// } catch (IOException e) {
			// e.printStackTrace();
			// }

		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static void processMyFixitData() {
		String text;

		String fullText = new String(
				"disconnect the two keyboard connectors by disengaging the clamps and pulling the ribbons directly upward. slide a spudger downward between each plastic strain relief cable and the wall of the case in order to bow out the cable beyond the small tab holding it in place. Once the strain relief cables are free, lift the keyboard off. remove the two phillips screws from the heat shield. one screw is shorter than the other. when putting the wallstreet back together , this screw goes in the middle of the heat shield.");

		Pattern re = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)",
				Pattern.MULTILINE | Pattern.COMMENTS);
		Matcher reMatcher = re.matcher(fullText);
		while (reMatcher.find()) {
			text = reMatcher.group();

			// analyze
			processText(text);
		}
		System.out.println(mgmt.toBpmnXml(mgmt.getActivities()));
	}

	private static void writeToFile(String guide, List<Activity> activities, Manager mgmt) throws IOException {
		String bpmnXml = mgmt.toBpmnXml(mgmt.getActivities());

		String fileG2A = Main.__TEST ? Main.__FILE_G2A__TEST : Main.__FILE_VERSION + "__" + Main.__FILE_G2A;
		String fileA2G = Main.__TEST ? Main.__FILE_A2G__TEST : Main.__FILE_VERSION + "__" + Main.__FILE_A2G;

		if (bpmnXml != null) {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileG2A, true));
			writer.append("<|startoftext|>");
			writer.append("[GUIDE_PROMPT]");
			writer.append(guide);
			writer.newLine();
			writer.append("[ACTIVITIES_PROMPT]");
			writer.append(mgmt.toBpmnXml(mgmt.getActivities()));
			writer.newLine();
			writer.append("<|endoftext|>");
			writer.close();

			writer = new BufferedWriter(new FileWriter(fileA2G, true));
			writer.append("<|startoftext|>");
			writer.append("[ACTIVITIES_PROMPT]");
			writer.append(mgmt.toBpmnXml(mgmt.getActivities()));
			writer.newLine();
			writer.append("[GUIDE_PROMPT]");
			writer.append(guide);
			writer.newLine();
			writer.append("<|endoftext|>");
			writer.newLine();
			writer.close();
		}
	}

	private static void writeToFile(String news, String facts) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(Main.FILE_T2F, true));
		writer.append("<|startoftext|>");
		writer.append("[NEWS_PROMPT]");
		writer.append(news);
		writer.newLine();
		writer.append("[FACTS_PROMPT]");
		writer.append(facts);
		writer.newLine();
		writer.append("<|endoftext|>");
		writer.close();

		writer = new BufferedWriter(new FileWriter(Main.FILE_F2T, true));
		writer.append("<|startoftext|>");
		writer.append("[FACTS_PROMPT]");
		writer.append(facts);
		writer.newLine();
		writer.append("[NEWS_PROMPT]");
		writer.append(news);
		writer.append("<|endoftext|>");
		writer.newLine();
		writer.close();

	}

}
