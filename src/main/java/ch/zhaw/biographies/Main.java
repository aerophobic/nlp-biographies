package ch.zhaw.biographies;

import java.io.IOException;
import java.util.List;

import com.sun.syndication.io.FeedException;

public class Main {

  public static void main(String[] args) throws IllegalArgumentException, FeedException, IOException {
    FileHandler file = new FileHandler();
    List<Person> people = file.createPeopleFromCsv("people.csv");

    Generator generator = new Generator();
    generator.generateBiographies(people);

    file.writeToFile(people, "4.0.0");
  }
}
