package ch.zhaw.activities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.stanford.nlp.util.Pair;

public class Verb {
  private List<Pair<List<String>, List<String>>> pairs;
  // name e.g. placeTubeIn
  private String name;

  public Verb(String name) {
    this.name = name;
    this.pairs = new ArrayList<Pair<List<String>, List<String>>>();
  }

  public Verb(String name, List<Pair<List<String>, List<String>>> pairs) {
    this.name = name;
    this.pairs = pairs;
  }

  public String name() {
    return this.name;
  }

  public List<String> subjects() {
    HashSet<String> set = new HashSet<String>();

    for (Pair<List<String>, List<String>> pair : this.pairs) {
      set.addAll(pair.first());
    }
    // convert to array
    List<String> unifiedList = new ArrayList<String>();

    unifiedList.addAll(set);

    return unifiedList;
  }

  public void add(Pair<List<String>, List<String>> pair) {
    this.pairs.add(pair);
  }

  public List<String> objects() {
    HashSet<String> set = new HashSet<String>();

    for (Pair<List<String>, List<String>> pair : this.pairs) {
      set.addAll(pair.second());
    }
    // convert to array
    List<String> unifiedList = new ArrayList<String>();

    unifiedList.addAll(set);

    return unifiedList;
  }
}
