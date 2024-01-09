package fi.tuni.prog3.sisu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import com.google.gson.JsonObject;
class SisuAPITest {

  // Test for url1 in getJsonObjectFromAPi
  @Test
  public void testGetUrl1FromApi(){

    SisuAPI sisuAPI = new SisuAPI(1, "");
    JsonObject jsonObject = sisuAPI.getJsonObjectFromApi();

    assertTrue(jsonObject != null);

  }

  // Test for url2 in getJsonObjectFromAPi
  @Test
  public void testGetUrl2FromApi(){
    SisuAPI sisuAPI = new SisuAPI(2, "otm-1d25ee85-df98-4c03-b4ff-6cad7b09618b");

    assertTrue(sisuAPI.getJsonObjectFromApi() != null);

    SisuAPI sisuAPI1 = new SisuAPI(2, "1");

    assertEquals(null, sisuAPI1.getJsonObjectFromApi());
  }

  // Test for url3 in getJsonObjectFromAPi
  @Test
  public void testGetUrl3FromApi(){

    SisuAPI sisuAPI = new SisuAPI(3, "otm-3858f1d8-4bf9-4769-b419-3fee1260d7ff");

    assertTrue(sisuAPI.getJsonObjectFromApi() != null);

    SisuAPI sisuAPI1 = new SisuAPI(3, "1");

    assertEquals(null, sisuAPI1.getJsonObjectFromApi());
  }

    // Test for url4 in getJsonObjectFromAPi
    @Test
    public void testGetUrl4FromApi(){
  
      SisuAPI sisuAPI = new SisuAPI(4, "tut-cu-g-45462");
  
      assertTrue(sisuAPI.getJsonObjectFromApi() != null);
  
      SisuAPI sisuAPI1 = new SisuAPI(4, "1");
  
      assertEquals(null, sisuAPI1.getJsonObjectFromApi());
    }
  

}
