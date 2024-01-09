package fi.tuni.prog3.sisu;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.io.File;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


 /**
  * Class for reading and writing student date to and from json file
  * @author Tuomas Mäkinen
  */
public class SaveAndLoadStudent {
    private static final String FILEPATH = "./Sisu/src/main/resources/students.json";
     /**
      * Saves student progress into a jsonarray.
      * if such a student already exists in jsonarray
      * the information is updated. Creates file and directories if they do not exist 
      * @param student student object to be saved in to jsonarray. 
      * cannot be null.
      * @return returns true if saving was succesful.
      * returns false if an error occurs
      */
     public static boolean saveProgress(Student student) 
    {    
     Gson gson = new GsonBuilder().setPrettyPrinting().create();
     JsonArray jsonArray;
     try{
        // Creates file and directories if they don't exist   
        File file = new File(FILEPATH);
        boolean fileExists = file.exists();
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!fileExists) {
            file.createNewFile();
        }
        
        Reader reader = new FileReader(file);
        jsonArray = gson.fromJson(reader, JsonArray.class);
        if(jsonArray == null)
        {
            jsonArray = new JsonArray();
        }
        // Checks if student already has information saved and if so deletes it
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.get("studentNumber").getAsString()
                .equals(student.getStudentNumber())) {
            jsonArray.remove(jsonElement);
            break;
            }
        }
        // Writes student information into students.json
        JsonObject studentObject = gson.toJsonTree(student).getAsJsonObject();
        jsonArray.add(studentObject);
        Writer writer = new FileWriter(FILEPATH);
        gson.toJson(jsonArray, writer);
        writer.close();
    }
    catch (IOException e){
        e.printStackTrace();
        return false;
    }
    return true;
}
 
    /**
    * Loads previous progress of a student 
    * from a jsonarray file by studentnumber
    * @param studentNumber studentnumber of the student that will be searched
    * @return student object that has the specific studentNumber.
    * Null if no such student exists
    * or an error has occured
    */
     public static Student loadProgress(String studentNumber) {
 
     Gson gson = new Gson();
 
     try {
     JsonArray jsonArray;
     Reader reader = new FileReader(FILEPATH); 
     jsonArray = gson.fromJson(reader, JsonArray.class);
     if(jsonArray != null)
     {
         for (JsonElement jsonElement : jsonArray) {
             JsonObject jsonObject = jsonElement.getAsJsonObject();
             if (jsonObject.get("studentNumber").getAsString()
                    .equals(studentNumber)) {
                 return gson.fromJson(jsonObject, Student.class);
             }
         }
     }
     }
     catch(Exception e) {
        e.printStackTrace();
        return null;
     }
     return null;
     }
 
     /**
      * Helper function for registering a new student 
      * that checks wheter a student already exists
      * in a json file
      * @param studentNumber 
      * @return true if such a student exists. false if not or an error occurs
      */
     public static boolean studentExists(String studentNumber) {
         Gson gson = new Gson();
         JsonArray jsonArray;
         try (Reader reader = new FileReader(FILEPATH)) {
             jsonArray = gson.fromJson(reader, JsonArray.class);
         } catch (IOException e) {
             e.printStackTrace();
             return false;
         }
         if(jsonArray == null)
         {
            return false;
         }
         for (JsonElement jsonElement : jsonArray) {
             JsonObject jsonObject = jsonElement.getAsJsonObject();
             if (jsonObject.get("studentNumber").getAsString()
                    .equals(studentNumber)) {
                 return true;
             }
         }
         return false;
     }
 }    
    
    

