package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Represents any module in sisu API
 * @author Tuomas Mäkinen, Vili Huotari
 */
public class Module {

  String name;
  String id;
  int targetCredits;
  String type;
  HashMap<String, Module> modules;
  HashMap<String, Course> courses;

  /**
   * Constructor for module. Calls jsonhandler to fetch module information from Sisu API.
   * @param id groupID for module in Sisu API
   */
  public Module(String id) {
    this.id = id;
    modules = new HashMap<>();
    courses = new HashMap<>();
    jsonHandler();
  }

  /**
   * returns name of the module
   * @return name of the module
   */
  public String getName() {
    return name;
  }

  /***
   * Returns minimum amount of credits needed to pass
   * @return amount of credits
   */
  public int getTargetCredits() {
    return targetCredits;
  }

  /**
   * Returns all modules directly under module
   * @return hashmap of modules
   */
  public HashMap<String, Module> getModules() {
    return modules;
  }

  /**
   * returns module with given name
   * @param name name of the module
   * @return . null if no such module exists
   */
  public Module getModule(String name){
    Module module = modules.get(name);
    return module;
  }

  /**
   * returns all courses directly under module
   * @return HashMap of courses
   */
  public HashMap<String, Course> getCourses() {
    return courses;
  }

  /***
   * returns modules type
   * @return module type
   */
  public String getType() {
    return type;
  }

  /**
   * 
   * A private helper method to handle JSON object retrieved from API.
   * Method go trough module and calls composite method with right parameter.
   * 
   */
  private void jsonHandler() {
    JsonObject obj;
    if (id.startsWith("otm")) {
      SisuAPI session = new SisuAPI(2, id);
      obj = session.getJsonObjectFromApi();
    } else {
      SisuAPI session = new SisuAPI(3, id);
      obj = session.getJsonObjectFromApi();
    }

    if (obj.getAsJsonObject("name").getAsJsonPrimitive("fi") == null) {
      this.name = obj.getAsJsonObject("name").getAsJsonPrimitive("en")
                                                        .getAsString();
    } else {
      this.name = obj.getAsJsonObject("name").getAsJsonPrimitive("fi")
                                                        .getAsString();
    }

    this.type = obj.get("type").getAsString();

    switch (obj.getAsJsonPrimitive("type").getAsString()) {
      case "GroupingModule":
        break;
      case "StudyModule":
        if (!obj.get("targetCredits").isJsonNull()) {
          this.targetCredits = obj.getAsJsonObject("targetCredits")
                                  .getAsJsonPrimitive("min")
                                  .getAsInt();
        }
        break;
    }

    JsonArray arr = null;
    String rule = obj.getAsJsonObject("rule").get("type").getAsString();
    if (rule.equals("CreditsRule")) {
      arr = obj.getAsJsonObject("rule").getAsJsonObject("rule")
                                                  .getAsJsonArray("rules");
    } else if (rule.equals("CompositeRule")) {
      arr = obj.getAsJsonObject("rule").getAsJsonArray("rules");
    }
    composite(arr);
  }

  /**
   * Method gets courses from studymodules and studymodules from upper modules.
   * Method stores modules that are inside modules to hashmap and then calls it
   * self to get courses added to hashmap also. 
   * 
   * @param arr the JSON array to composite
   */
  private void composite(JsonArray arr) {
    Iterator<JsonElement> iter = arr.iterator();

    while (iter.hasNext()) {
      JsonObject jObj = iter.next().getAsJsonObject();
      String type = jObj.get("type").getAsString();
      switch (type) {
        case "CompositeRule":
          composite(jObj.getAsJsonArray("rules"));
          break;
        case "ModuleRule":
          String moduleGroupId = jObj.get("moduleGroupId").getAsString();
          Module module = new Module(moduleGroupId);
          if (!modules.containsKey(module.getName())) {
            modules.put(module.getName(), module);
          }
          break;
        case "CourseUnitRule":
          JsonObject courseObj;
          String courseName;
          String outcomes;
          String courseId = jObj.get("courseUnitGroupId").getAsString();

          SisuAPI session = new SisuAPI(4, courseId);
          courseObj = session.getJsonObjectFromApi();

          int minCredits = courseObj.getAsJsonObject("credits")
                                    .getAsJsonPrimitive("min").getAsInt();

          if (courseObj.getAsJsonObject("name")
                       .getAsJsonPrimitive("fi") == null) {
            courseName = courseObj.getAsJsonObject("name")
                                  .getAsJsonPrimitive("en")
                                  .getAsString();
          } else {
            courseName = courseObj.getAsJsonObject("name")
                                  .getAsJsonPrimitive("fi")
                                  .getAsString();
          }

          if (!courseObj.get("outcomes").isJsonNull()) {
            if (courseObj.getAsJsonObject("outcomes").get("fi") == null) {
                outcomes = courseObj.getAsJsonObject("outcomes")
                                    .getAsJsonPrimitive("en").getAsString();
            } else {
                outcomes = courseObj.getAsJsonObject("outcomes")
                                    .getAsJsonPrimitive("fi").getAsString();
            }
          } else {
            outcomes = "No outcomes avaivable";
          }


          Course course = new Course(courseName, courseId, null, minCredits, outcomes);
          courses.put(course.getName(), course);
          break;
      }
    }
  }
}
