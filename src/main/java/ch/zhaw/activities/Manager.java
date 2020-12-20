package ch.zhaw.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.builder.StartEventBuilder;
import org.camunda.bpm.model.bpmn.builder.UserTaskBuilder;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.util.Pair;

public class Manager {

	private static final String EOL = "\n";

	private final List<Activity> activities;
	private int index = -1;

	public Manager() {
		this.activities = new ArrayList<Activity>();
	}

	public void addActivity(String name) {
		this.activities.add(new Activity(name));
	}

	public List<Activity> getActivities() {
		return this.activities;
	}

	public void store(String fact, String arg1, String arg2) {
		String factKey = toCamelCase(cleanUp(fact));

		String arg1Clean = cleanUp(arg1);
		List<String> arg1List = toList(arg1Clean);
		// String arg1ListString = listToString(arg1List);

		String arg2Clean = cleanUp(arg2);
		List<String> arg2List = toList(arg2Clean);

		// forward storage
		this.activities.get(this.activities.size() - 1).addVerb(factKey,
				new Pair<List<String>, List<String>>(arg1List, arg2List));
	}

	public void store(RelationTriple triple) {
		this.activities.get(this.activities.size() - 1).addTriple(triple);
	}

	public String toBpmnXml(List<Activity> activities) {
		StartEventBuilder startEventBuilder = Bpmn.createProcess().name("Example	process").executable().startEvent();
		UserTaskBuilder userTaskBuilder = null;

		for (Activity activity : activities) {
			if (activity.getActivityName() != null) {
				if (userTaskBuilder == null) {
					userTaskBuilder = startEventBuilder.userTask().name(activity.getActivityName());
				} else {
					userTaskBuilder = userTaskBuilder.userTask().name(activity.getActivityName());
				}
			}
		}

		try {
			BpmnModelInstance modelInstance = userTaskBuilder.endEvent().done();
			return Bpmn.convertToString(modelInstance);
		} catch (java.lang.NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static boolean contains(List<String> hayStack, String needle) {
		for (String current : hayStack) {
			if (current.equals(needle)) {
				return true;
			}
		}
		return false;
	}

	private static String cleanUp(String string) {
		String noHTML = string.replaceAll("\\<.*?\\>", "");
		String noSpecialChars = noHTML.replaceAll("[^a-zA-Z0-9]", " ");
		String trimed = noSpecialChars.trim();
		String lower = trimed.toLowerCase();
		return lower;
	}

	private static String toCamelCase(String string) {
		String[] tokens = string.split(" ");
		if (tokens.length < 1) {
			return "";
		}

		StringBuffer result = new StringBuffer(tokens[0]);
		for (int i = 1; i < tokens.length; i++) {
			String tokenCapitalised = "";
			if (tokens[i].length() > 0) {
				tokenCapitalised += tokens[i].substring(0, 1).toUpperCase();
			}
			if (tokens[i].length() > 1) {
				tokenCapitalised += tokens[i].substring(1);
			}
			result.append(tokenCapitalised);
		}

		return result.toString();
	}

	private static List<String> toList(String string) {
		StringBuffer result = new StringBuffer("[");

		String[] tokens = string.split(" ");
		List<String> tokensList = new ArrayList<String>(Arrays.asList(tokens));
		tokensList.removeAll(Arrays.asList("", null));
		return tokensList;
	}

	private static String listToString(List<String> list) {
		StringBuffer result = new StringBuffer("[");
		result.append(String.join(",", list));
		result.append("]");

		return result.toString();
	}
}
