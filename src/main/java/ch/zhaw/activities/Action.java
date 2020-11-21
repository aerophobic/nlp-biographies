package ch.zhaw.activities;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;

public class Action {
  private String verb;
  private String subject;
  private String object;
  private String modifier;

  private SemanticGraph graph;

  public Action(SemanticGraph graph) {
    this.graph = graph;
    
    this.findElements();
  }

  public void findElements() {
    for (SemanticGraphEdge edge : this.graph.edgeListSorted()) {
      if (edge.getGovernor().tag().equals("VB")) {
        // its a verb
        this.verb = edge.getGovernor().lemma().toString();
      } else if (edge.getGovernor().tag().equals("NN")) {
        // its a nominal noun
        this.subject = edge.getGovernor().lemma().toString();
      }
      // System.out.println(sge.getGovernor().word() + "," + sge.getGovernor().index() + "," + sge.getGovernor().tag()
      //     + "," + sge.getGovernor().ner() + " - " + sge.getRelation().getLongName() + " -> " + sge.getDependent().word()
      //     + "," + +sge.getDependent().index() + "," + sge.getDependent().tag() + "," + sge.getDependent().ner());
    }
  }

  public void findElement(SemanticGraphEdge edge) {

  }
}
