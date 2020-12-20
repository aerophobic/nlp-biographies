package ch.zhaw.biographies;

import java.io.IOException;
import java.util.List;

import com.sun.syndication.io.FeedException;

public class Main {
  public static void main(String[] args) throws IllegalArgumentException, FeedException, IOException {
    FileHandler handler = new FileHandler();
    List<Person> people = handler.createPeopleFromCsv("people.csv");

    // option 1:
    // generate from templates...
    Generator.generateBiographiesFromTemplates(people);

    // ...and write all people to file
    handler.writeToFile(people, "7.0.0__templates");

    // option 2:
    // Integer sentences = 3;
    // generate from wiki request
    // make all requests first...
    // Generator.generateBiographiesFromWiki(people, sentences);

    // ...then write all people to file at once
    // handler.writeToFile(people, "4.0.0");

    // option 3:
    Integer sentences = 3;
    // write after each wiki request
    Generator.generateBiographiesFromWiki(people, sentences, handler, "7.0.0__wiki");
  }
}
