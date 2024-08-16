package g1;


public class SysConfig {
  // TBD: load from .env
  private static String API_KEY = "";
  private static String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast";

  // openweathermap API key
  public static String getAPIKey(){
    return SysConfig.API_KEY;
  }

  public static String getBaseURL(){
    return SysConfig.BASE_URL;
  }

}