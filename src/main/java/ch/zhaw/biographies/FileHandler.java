package ch.zhaw.biographies;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class FileHandler {

  private final String __ROOT_PATH;
  private final Boolean __TEST = true;

  private final String __FILE_F2B__TEST = "output\\test\\__FILE_VERSION__F2B.txt";
  private final String __FILE_B2F__TEST = "output\\test\\__FILE_VERSION__B2F.txt";

  private final String __FILE_F2B = "output\\__FILE_VERSION__F2B.txt";
  private final String __FILE_B2F = "output\\__FILE_VERSION__B2F.txt";

  public FileHandler() {
    this.__ROOT_PATH = System.getProperty("user.dir") + "\\";
  }

  public List<Person> createPeopleFromCsv(String filename) {
    String path = this.__ROOT_PATH + "data\\" + filename;
    List<Person> people = new ArrayList<Person>();

    CSVReader reader = null;
    try {
      // Get the CSVReader instance with specifying the delimiter to be used
      reader = new CSVReader(new FileReader(path), ';');

      String[] nextLine;

      // Read one line at a time
      while ((nextLine = reader.readNext()) != null) {
        String name = nextLine[0];
        String birthcity = nextLine[1];
        String countryName = nextLine[2];
        String birthyear = nextLine[3];
        String gender = nextLine[4].toLowerCase();
        String occupation = nextLine[5].toLowerCase();

        Person person = new Person(name, birthyear, birthcity, countryName, occupation, gender);

        people.add(person);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return people;
  }

  public void writeToFile(List<Person> people, String version) throws IOException {
    String fileF2B = this.__TEST ? this.__FILE_F2B__TEST : this.__FILE_F2B;
    String fileB2F = this.__TEST ? this.__FILE_B2F__TEST : this.__FILE_B2F;

    fileF2B = this.__ROOT_PATH + fileF2B.replace("__FILE_VERSION", version);
    fileB2F = this.__ROOT_PATH + fileB2F.replace("__FILE_VERSION", version);

    for (Person person : people) {
      this.writeToFile(person, version);
    }
  }

  public void writeToFile(Person person, String version) throws IOException {
    String fileF2B = this.__TEST ? this.__FILE_F2B__TEST : this.__FILE_F2B;
    String fileB2F = this.__TEST ? this.__FILE_B2F__TEST : this.__FILE_B2F;

    fileF2B = this.__ROOT_PATH + fileF2B.replace("__FILE_VERSION", version);
    fileB2F = this.__ROOT_PATH + fileB2F.replace("__FILE_VERSION", version);

    BufferedWriter writer = new BufferedWriter(new FileWriter(fileF2B, true));
    writer.append("<|startoftext|>");
    writer.newLine();
    writer.append("[FACTS_PROMPT]");
    writer.newLine();
    writer.append(person.getFacts());
    writer.append("[BIOGRAPHY_PROMPT]");
    writer.newLine();
    writer.append(person.getBiography());
    writer.newLine();
    writer.append("<|endoftext|>");
    writer.newLine();
    writer.append("");
    writer.newLine();
    writer.close();

    writer = new BufferedWriter(new FileWriter(fileB2F, true));
    writer.append("<|startoftext|>");
    writer.newLine();
    writer.append("[BIOGRAPHY_PROMPT]");
    writer.newLine();
    writer.append(person.getBiography());
    writer.newLine();
    writer.append("[FACTS_PROMPT]");
    writer.newLine();
    writer.append(person.getFacts());
    writer.append("<|endoftext|>");
    writer.newLine();
    writer.append("");
    writer.newLine();
    writer.close();
  }
}
