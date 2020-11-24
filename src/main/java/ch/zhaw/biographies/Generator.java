package ch.zhaw.biographies;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import ch.zhaw.wikipedia.WikiApi;

public class Generator {
  private static final String[] female = new String[] {
      "__NAME__ was born in __BIRTHCITY__ and lived in __COUNTRYNAME__. She was born in __BIRTHYEAR__ and worked as a __OCCUPATION__.",
      "__NAME__ was originally born in __BIRTHCITY__ and lived in __COUNTRYNAME__. She was born in __BIRTHYEAR__ and worked as a __OCCUPATION__.",
      "The birthplace of __NAME__ is in __BIRTHCITY__. She was born in __BIRTHYEAR__ and worked as a __OCCUPATION__. She used to live in __COUNTRYNAME__.",
      "__NAME__ was born in the town of __BIRTHCITY__ and lived in __COUNTRYNAME__. In __BIRTHYEAR__ she was born and worked as a __OCCUPATION__.",
      "__NAME__ was born in __BIRTHYEAR__ and worked as a __OCCUPATION__. The birth place of __NAME__ is in __BIRTHCITY__. She has lived in __COUNTRYNAME__.",
      "In __BIRTHYEAR__ __NAME__ was born and worked as a __OCCUPATION__. She did live in __COUNTRYNAME__ and the town where she was born is in __BIRTHCITY__.",
      "The town where __NAME__ was born is in __BIRTHCITY__. She was born in __BIRTHYEAR__ and was a professional __OCCUPATION__. She was living in __COUNTRYNAME__.",
      "__NAME__ has been born in __BIRTHCITY__. She was a citizen of the __COUNTRYNAME__. She was born in __BIRTHYEAR__ and worked as a __OCCUPATION__.",
      "__NAME__ is born in __BIRTHCITY__. She did live in __COUNTRYNAME__ and her birth year is __BIRTHYEAR__. __NAME__ worked as a __OCCUPATION__.",
      "__NAME__ was __OCCUPATION__ and the town where she was born is in __BIRTHCITY__ in __BIRTHYEAR__. __NAME__ was based in __COUNTRYNAME__.",
      "__NAME__ was born in __BIRTHCITY__ and resided in __COUNTRYNAME__. She was born in __BIRTHYEAR__ and professionally she worked as a __OCCUPATION__.",
      "__NAME__ is a __BIRTHCITY__ native who lived in __COUNTRYNAME__. She was born in the year __BIRTHYEAR__ and worked professionally as a __OCCUPATION__.",
      "__NAME__ is a __BIRTHCITY__ native who lived in __COUNTRYNAME__. In __BIRTHYEAR__ she was born and worked as a __OCCUPATION__.",
      "__NAME__  was born in __BIRTHCITY__ and lived in __COUNTRYNAME__. Her professional career was as a __OCCUPATION__, and she was born in __BIRTHYEAR__.",
      "__NAME__ was a native of __BIRTHCITY__ and lived in __COUNTRYNAME__. Her professional career was as a __OCCUPATION__, and she born in __BIRTHYEAR__.",
      "__NAME__ is born in __BIRTHCITY__ and has been living in __COUNTRYNAME__. She was born in __BIRTHYEAR__ and has worked professionally as a __OCCUPATION__.",
      "__NAME__ grew up in __BIRTHCITY__ and has been living in __COUNTRYNAME__. She was born in __BIRTHYEAR__ and has worked as a professional __OCCUPATION__.",
      "__NAME__ grew up in __BIRTHCITY__ and has resided in __COUNTRYNAME__. She was borne in __BIRTHYEAR__ and worked occupationally as a __OCCUPATION__.",
      "__NAME__ was born in __BIRTHCITY__ and has lived in __COUNTRYNAME__. She was born in __BIRTHYEAR__ and her professional career was as a __OCCUPATION__.",
      "__NAME__ was a __OCCUPATION__ and was born in __BIRTHYEAR__. She grew up in __BIRTHCITY__ and lived in __COUNTRYNAME__.",
      "__NAME__ was a __OCCUPATION__ and was born in __BIRTHYEAR__. She grew up in __BIRTHCITY__ and used to live in __COUNTRYNAME__.",
      "__NAME__ was a __OCCUPATION__. She was born in the year __BIRTHYEAR__, grew up in __BIRTHCITY__ and lived in __COUNTRYNAME__.",
      "__NAME__ used to be a __OCCUPATION__ and was born in __BIRTHYEAR__. She grew up in __BIRTHCITY__ and has since lived in __COUNTRYNAME__.",
      "__NAME__ used to be a __OCCUPATION__ and was born in __BIRTHYEAR__. The birth place of __NAME__ is in __BIRTHCITY__ and has spent her childhood in __COUNTRYNAME__.",
      "__NAME__ was a __OCCUPATION__ and born in the year __BIRTHYEAR__ in __BIRTHCITY__. She has lived in __COUNTRYNAME__.",
      "In __BIRTHYEAR__ __NAME__ was born in __BIRTHCITY__. She was a __OCCUPATION__ and lived in __COUNTRYNAME__.",
      "In __BIRTHYEAR__ __NAME__ was born in the town of __BIRTHCITY__. She worked as a __OCCUPATION__ and lived in __COUNTRYNAME__.",
      "In __BIRTHYEAR__ __NAME__ from __BIRTHCITY__ was born. She served as a __OCCUPATION__ and lived in __COUNTRYNAME__.",
      "__NAME__ worked as a __OCCUPATION__ and lived in __COUNTRYNAME__. She was born in __BIRTHYEAR__ and grew up in __BIRTHCITY__.",
      "__NAME__ was a __OCCUPATION__ who worked and lived in __COUNTRYNAME__. She was born in __BIRTHYEAR__ and raised in __BIRTHCITY__." };

  private static final String[] male = new String[] {
      "__NAME__ was born in __BIRTHCITY__ and lived in __COUNTRYNAME__. He was born in __BIRTHYEAR__ and worked as a __OCCUPATION__.",
      "__NAME__ was originally born in __BIRTHCITY__ and lived in __COUNTRYNAME__. He was born in __BIRTHYEAR__ and worked as a __OCCUPATION__.",
      "The birthplace of __NAME__ is in __BIRTHCITY__. He was born in __BIRTHYEAR__ and worked as a __OCCUPATION__. He used to live in __COUNTRYNAME__.",
      "__NAME__ was born in the town of __BIRTHCITY__ and lived in __COUNTRYNAME__. In __BIRTHYEAR__ he was born and worked as a __OCCUPATION__.",
      "__NAME__ was born in __BIRTHYEAR__ and worked as a __OCCUPATION__. The birth place of __NAME__ is in __BIRTHCITY__. He has lived in __COUNTRYNAME__.",
      "In __BIRTHYEAR__ __NAME__ was born and worked as a __OCCUPATION__. He did live in __COUNTRYNAME__ and the town where he was born is in __BIRTHCITY__.",
      "The town where __NAME__ was born is in __BIRTHCITY__. He was born in __BIRTHYEAR__ and was a professional __OCCUPATION__. He was living in __COUNTRYNAME__.",
      "__NAME__ has been born in __BIRTHCITY__. He was a citizen of the __COUNTRYNAME__. He was born in __BIRTHYEAR__ and worked as a __OCCUPATION__.",
      "__NAME__ is born in __BIRTHCITY__. He did live in __COUNTRYNAME__ and his birth year is __BIRTHYEAR__. __NAME__ worked as a __OCCUPATION__.",
      "__NAME__ was __OCCUPATION__ and the town where he was born is in __BIRTHCITY__ in __BIRTHYEAR__. __NAME__ was based in __COUNTRYNAME__.",
      "__NAME__ was born in __BIRTHCITY__ and resided in __COUNTRYNAME__. He was born in __BIRTHYEAR__ and professionally he worked as a __OCCUPATION__.",
      "__NAME__ is a __BIRTHCITY__ native who lived in __COUNTRYNAME__. He was born in the year __BIRTHYEAR__ and worked professionally as a __OCCUPATION__.",
      "__NAME__ is a __BIRTHCITY__ native who lived in __COUNTRYNAME__. In __BIRTHYEAR__ he was born and worked as a __OCCUPATION__.",
      "__NAME__  was born in __BIRTHCITY__ and lived in __COUNTRYNAME__. His professional career was as a __OCCUPATION__, and he was born in __BIRTHYEAR__.",
      "__NAME__ was a native of __BIRTHCITY__ and lived in __COUNTRYNAME__. His professional career was as a __OCCUPATION__, and he born in __BIRTHYEAR__.",
      "__NAME__ is born in __BIRTHCITY__ and has been living in __COUNTRYNAME__. He was born in __BIRTHYEAR__ and has worked professionally as a __OCCUPATION__.",
      "__NAME__ grew up in __BIRTHCITY__ and has been living in __COUNTRYNAME__. He was born in __BIRTHYEAR__ and has worked as a professional __OCCUPATION__.",
      "__NAME__ grew up in __BIRTHCITY__ and has resided in __COUNTRYNAME__. He was borne in __BIRTHYEAR__ and worked occupationally as a __OCCUPATION__.",
      "__NAME__ was born in __BIRTHCITY__ and has lived in __COUNTRYNAME__. He was born in __BIRTHYEAR__ and his professional career was as a __OCCUPATION__.",
      "__NAME__ was a __OCCUPATION__ and was born in __BIRTHYEAR__. He grew up in __BIRTHCITY__ and lived in __COUNTRYNAME__.",
      "__NAME__ was a __OCCUPATION__ and was born in __BIRTHYEAR__. He grew up in __BIRTHCITY__ and used to live in __COUNTRYNAME__.",
      "__NAME__ was a __OCCUPATION__. He was born in the year __BIRTHYEAR__, grew up in __BIRTHCITY__ and lived in __COUNTRYNAME__.",
      "__NAME__ used to be a __OCCUPATION__ and was born in __BIRTHYEAR__. He grew up in __BIRTHCITY__ and has since lived in __COUNTRYNAME__.",
      "__NAME__ used to be a __OCCUPATION__ and was born in __BIRTHYEAR__. The birth place of __NAME__ is in __BIRTHCITY__ and has spent his childhood in __COUNTRYNAME__.",
      "__NAME__ was a __OCCUPATION__ and born in the year __BIRTHYEAR__ in __BIRTHCITY__. He has lived in __COUNTRYNAME__.",
      "In __BIRTHYEAR__ __NAME__ was born in __BIRTHCITY__. He was a __OCCUPATION__ and lived in __COUNTRYNAME__.",
      "In __BIRTHYEAR__ __NAME__ was born in the town of __BIRTHCITY__. He worked as a __OCCUPATION__ and lived in __COUNTRYNAME__.",
      "In __BIRTHYEAR__ __NAME__ from __BIRTHCITY__ was born. He served as a __OCCUPATION__ and lived in __COUNTRYNAME__.",
      "__NAME__ worked as a __OCCUPATION__ and lived in __COUNTRYNAME__. He was born in __BIRTHYEAR__ and grew up in __BIRTHCITY__.",
      "__NAME__ was a __OCCUPATION__ who worked and lived in __COUNTRYNAME__. He was born in __BIRTHYEAR__ and raised in __BIRTHCITY__." };

  public Generator() {
  }

  public static List<Person> generateBiographiesFromTemplates(List<Person> people) {
    HashMap<String, String[]> templates = new HashMap<String, String[]>();
    templates.put("male", Generator.male);
    templates.put("female", Generator.female);

    for (Person person : people) {
      person.createBiography(templates);
    }

    return people;
  }

  public static List<Person> generateBiographiesFromWiki(List<Person> people, Integer sentences) {
    for (Person person : people) {
      person.setBiography(WikiApi.get(person.name, sentences));
    }

    return people;
  }

  public static void generateBiographiesFromWiki(List<Person> people, Integer sentences, FileHandler handler, String version) {
    for (Person person : people) {
      person.setBiography(WikiApi.get(person.name, sentences));
      try {
        handler.writeToFile(person, version);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
