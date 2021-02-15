import org.json.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WeatherAPI {

    public static void main(String[] args) {
        WeatherAPI.getWeather();
    }

    public static void getWeather() {
        // Endpoint
        String baseURL = "https://api.openweathermap.org";
        // requesting data, api version 2.5, from the "current weather data"
        String callAction = "/data/2.5/weather";
        // type of search, in this case searching by city, state code, country code
        String searchParam = "?q=Greensboro,NC,us";
        // specify imperial units so americans can read it
        String unitParam = "&units=imperial";
        String apiKey = "&appid=f04b7400ca4371fe69fd8fafe04a68ff";
        String urlString = baseURL + callAction + searchParam + unitParam + apiKey;

        URL url;
        try {
            url = new URL(urlString);
            // Open http connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Using "GET" calls to the api
            connection.setRequestMethod("GET");
            // Check response code, ensure it is 200, meaning the request was successful
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Error: API Connection issue, Response code:" + responseCode);
            } else {
                // Create an input stream reader from the connection's input stream, create buffered reader from that
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // Crate variable to store the input by line as it is being read in
                String inputLine;
                // Create a stringBuffer to hold all of the input lines concatenated together
                StringBuffer content = new StringBuffer();
                // Read input by line and concatenate to content, stop when there ar no more input lines.
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                // Close connections
                in.close();
                connection.disconnect();
                //Print JSON string
                System.out.println("JSON output: " + content.toString());
                // Pass raw json to JSON parser to create a usable java object
                JSONObject jsonObj = new JSONObject(content.toString());
                // Get the "main" json object from the full json that was returned
                JSONObject basicInfo = jsonObj.getJSONObject("main");
                // Retrieve the temperature from this object
                int currentTemp = basicInfo.getInt("temp");
                //Print the current temperature
                System.out.println("It is currently " + currentTemp + " Degrees F in Greensboro, NC.");
                // Decide what to wear based on the temperature
                if (currentTemp > 60) {
                    System.out.println("You should wear shorts, flip-flops, and a t-shirt.");
                } else if (currentTemp > 32) {
                    System.out.println("You should wear shorts, flip-flops, and a long-sleeved shirt.");
                } else {
                    System.out.println("You should wear shorts, flip-flops, and a hoodie.");
                }

            }

        } catch (Exception e) {
            System.out.println("Caught exception: " + e);
        }

    }
}
