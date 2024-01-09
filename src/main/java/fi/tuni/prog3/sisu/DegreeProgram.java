package fi.tuni.prog3.sisu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * A class that represents a degreeprogram 
 * @author Tuomas Mäkinen, Vili Huotari
 */
public class DegreeProgram extends DegreeModule {

  private HashMap<String, Module> modules;

  /**
   * Constructor for DegreeProgram class
   * 
   * @param name       name of the Degree
   * @param id         local id of the degreeProgram in Sisu API
   * @param groupId    GroupID of the degreeProgram in Sisu API
   * @param minCredits minumum credits to pass the degreeprogram
   */
  public DegreeProgram(String name,
                       String id, 
                       String groupId, 
                       int minCredits) {
    super(name, id, groupId, minCredits);
    this.modules = new HashMap<String, Module>();
  }

  /**
   * Returns all modules diretcly under degreeprogram
   * @return HashMap of modules
   */
  public HashMap<String, Module> getModules() {
    return modules;
  }

  /**
   * Returns module with specific name
   * 
   * @param name name of the module
   * @return module object
   * @throws NoSuchElementException if no such module exists
   */
  public Module getModule(String name) {
    Module module = modules.get(name);

    if (module.equals(null)) {
      throw new NoSuchElementException("Module does not exist");
    } else {
      return module;
    }
  }

  /**
   * 
   * Handles JSON data received from Sisu API and extracts information about
   * Modules.
   */
  public void jsonHandler() {
    SisuAPI session = new SisuAPI(3, getGroupId());
    JsonObject obj = session.getJsonObjectFromApi();

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
   * 
   * Extracts module information from a given JsonArray of rules.
   * 
   * @param arr the JsonArray containing ModuleRule or CompositeRule objects
   * 
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
          modules.put(module.getName(), module);
          break;
      }
    }
  }

  /**
   * 
   * Returns an ArrayList of orientations for the given group.
   * 
   * @return An ArrayList of orientations.
   */
  public ArrayList<String> getOrientations() {
    SisuAPI session = new SisuAPI(3, getGroupId());
    JsonObject obj = session.getJsonObjectFromApi();

    JsonArray arr = null;
    String rule = obj.getAsJsonObject("rule").get("type").getAsString();
    if (rule.equals("CreditsRule")) {
      // Degree not include different orientations
      return null;
    } else if (rule.equals("CompositeRule")) {
      arr = obj.getAsJsonObject("rule").getAsJsonArray("rules");
    }
    return orientationComposite(arr);

  }

  /**
   * 
   * Extracts orientation information only from a given JsonArray of rules.
   * 
   * @param arr the JsonArray containing ModuleRule or CompositeRule objects
   * @param ori the selector value for orientation selection
   * 
   */
  private ArrayList<String> orientationComposite(JsonArray arr) {
    ArrayList<String> orientations = new ArrayList<>();
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

          JsonObject obj;
          String name;
          SisuAPI session = new SisuAPI(2, moduleGroupId);
          obj = session.getJsonObjectFromApi();
          if (obj.getAsJsonObject("name").getAsJsonPrimitive("fi") == null) {
            name = obj.getAsJsonObject("name").getAsJsonPrimitive("en")
                                                         .getAsString();
          } else {
            name = obj.getAsJsonObject("name").getAsJsonPrimitive("fi")
                                                         .getAsString();
          }
          orientations.add(name);
          break;
      }
    }
    return orientations;
  }

}
