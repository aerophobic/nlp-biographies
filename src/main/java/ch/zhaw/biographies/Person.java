package ch.zhaw.biographies;

import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

public class Person {
  public String name;
  public String birthyear;
  public String birthcity;
  public String countryName;
  public String occupation;

  public String gender;

  private String biography;

  public Person(String name, String birthyear, String birthcity, String countryName, String occupation, String gender) {
    this.name = name;
    this.birthyear = birthyear;
    this.birthcity = birthcity;
    this.countryName = countryName;
    this.occupation = occupation;

    this.gender = gender;
  }

  public void setBiography(String biography) {
    this.biography = biography;
  }

  public String getBiography() {
    return this.biography;
  }

  private String getRandomTemplate(HashMap<String, String[]> templates) {
    String[] set = templates.get(this.gender);
    Random random = new Random();
    int index = random.nextInt(set.length);
    String template = set[index];

    return template;
  }

  public void createBiography(HashMap<String, String[]> templates) {
    String template = this.getRandomTemplate(templates);

    template = template.replace("__NAME__", this.name);
    template = template.replace("__BIRTHYEAR__", this.birthyear);
    template = template.replace("__BIRTHCITY__", this.birthcity);
    template = template.replace("__COUNTRYNAME__", this.countryName);
    template = template.replace("__OCCUPATION__", this.occupation);

    this.setBiography(template);
  }

  public String getFacts() {
    HashMap<String, String> pairs = new HashMap<String, String>();

    pairs.put("name", this.name);
    pairs.put("birth year", this.birthyear);
    pairs.put("birth city", this.birthcity);
    pairs.put("country", this.countryName);
    pairs.put("occupation", this.occupation);
    pairs.put("gender", this.gender);

    String facts = "";

    for (Entry<String, String> pair : pairs.entrySet()) {
      facts += pair.getKey() + ": " + pair.getValue() + "\n";
    }

    return facts;
  }
}