package ch.zhaw.activities;

import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.util.Pair;

public class Activity {
  private String name;
  private HashMap<String, Verb> verbs;
  private HashMap<String, RelationTriple> triples;

  public Activity(String name) {
    this.name = name;
    this.verbs = new HashMap<String, Verb>();
    this.triples = new HashMap<String, RelationTriple>();
  }

  public void addVerb(String name, Pair<List<String>, List<String>> pair) {
    if (!this.verbs.containsKey(name)) {
      this.verbs.put(name, new Verb(name));
    }
    this.verbs.get(name).add(pair);
  }

  public void addTriple(RelationTriple triple) {
    this.triples.put(triple.relationLemmaGloss(), triple);
  }

  public String name() {
    return this.name;
  }

  public HashMap<String, Verb> verbs() {
    return this.verbs;
  }

  public String getActivityName() {
    String name = null;
    for (RelationTriple triple : this.triples.values()) {
      if (name == null) {
        name = "";
      }
      String tripleActivityName = triple.relationLemmaGloss() + ": " + triple.subjectLemmaGloss() + ", "
          + triple.objectLemmaGloss();
      name += " | " + tripleActivityName;
    }
    System.out.println(name);
    return name;
  }

}
