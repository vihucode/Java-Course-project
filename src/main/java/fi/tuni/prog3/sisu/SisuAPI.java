package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 
 * The SisuAPI class represents an API client for retrieving data for a specific
 * group from a Sisu system.
 * 
 * @author Vili Huotari
 */
class SisuAPI {
  private int url;
  private String groupId;

  /**
   * 
   * Creates a new instance of the SisuAPI class with the specified URL and group
   * ID.
   * 
   * @param url     the URL of the API endpoint to use
   * @param groupId the ID of the group to retrieve data from
   */
  public SisuAPI(int url, String groupId) {
    this.url = url;
    this.groupId = groupId;
  }

  /**
   * 
   * This method retrieves data from the SisuAPI.
   * 
   * It first gets a JsonObject from the API using an instance of the SisuAPI
   * class.
   * 
   * @throws MalformedURLException if an error occurs while forming the URL for
   *                               the API
   * @return JsonObject, which includes all needed information from url
   */
  public JsonObject getJsonObjectFromApi() {
    URL correctUrl = null;
    try {
      switch (url) {
        case 1:
          correctUrl = new URL(
                "https://sis-tuni.funidata.fi/kori/api/module-search?"
              + "curriculumPeriodId=uta-lvv-2021&universityId=tuni-university"
              + "-root-id&moduleType=DegreeProgramme&limit=1000");
          break;
        case 2:
          correctUrl = new URL(String.format(
            "https://sis-tuni.funidata.fi/kori/api/modules/" + groupId));
          break;
        case 3:
          correctUrl = new URL(
            String.format(
              "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId="
               + groupId + "&universityId=tuni-university-root-id"));
          break;
        case 4:
          correctUrl = new URL(
            String.format(
              "https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId="
              + groupId + "&universityId=tuni-university-root-id"));
          break;
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    try {
      HttpURLConnection connection = (HttpURLConnection) correctUrl
                                     .openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
      int responseCode = connection.getResponseCode();
      if (responseCode == 400) {
        return null;
      }
      InputStream inputStream = connection.getInputStream();
      InputStreamReader reader = new InputStreamReader(inputStream, 
                                            StandardCharsets.UTF_8);
      JsonElement jsonElement = JsonParser.parseReader(reader);
      reader.close();
      inputStream.close();
      if (jsonElement.isJsonArray()) {
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        if (jsonArray.size() > 0) {
          JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
          return jsonObject;
        }
      } else if (jsonElement.isJsonObject()) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return jsonObject;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
