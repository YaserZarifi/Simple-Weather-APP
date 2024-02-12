import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class OpenWeatherAPIExample {
    public static void main(String[] args) {
        try {
            // Replace "YOUR_API_KEY" with your actual OpenWeather API key
            String apiKey = "35622ba50b71bcc91267f3bed1f5eaf7";

            // Get the city name from the user
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the city name: ");
            String city = reader.readLine();

            // Create URL object for the OpenWeather API endpoint
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey);

            // Open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method to GET
            connection.setRequestMethod("GET");

            // Get response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            if (responseCode == 200){
                // Read response body
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = responseReader.readLine()) != null) {
                    response.append(line);
                }
                responseReader.close();

                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Extract weather information
                JSONObject main = jsonResponse.getJSONObject("main");
                JSONObject coord = jsonResponse.getJSONObject("coord");
                double temperature = main.getDouble("temp");
                double humidity = main.getDouble("humidity");
                double feelsLike = main.getDouble("feels_like");
                double pressure = main.getDouble("pressure");

                JSONObject sys = jsonResponse.getJSONObject("sys");
                long sunriseTime = sys.getLong("sunrise");
                long sunsetTime = sys.getLong("sunset");


                double lon = coord.getDouble("lon");
                double lat = coord.getDouble("lat");

                JSONObject weather = jsonResponse.getJSONArray("weather").getJSONObject(0);

                String description = weather.getString("description");

                //Change Kalvin to Celsius
                String temp = String.format("%.2f", temperature-  273.15);
                String feels = String.format("%.2f", feelsLike -  273.15);
                // Display weather information
                System.out.println("");
                System.out.println("");
                System.out.println("Weather in " + city + ":");
                System.out.println("");
                System.out.println("Temperature: " +temp + " C");
                System.out.println("Feels Like: " + feels + " C");
                System.out.println("Longitude: " + lon );
                System.out.println("Latitude: " + lat );
                System.out.println("Humidity: " + humidity + "%");
                System.out.println("Pressure: " + pressure + " hPa");
                System.out.println("Description: " + description);
                System.out.println("Sunrise: " + formatTime(sunriseTime));
                System.out.println("Sunset: " + formatTime(sunsetTime));

                // Close connection
                connection.disconnect();
            } else if (responseCode == 404) {
                System.out.println("City not found !! ");
            }else {
                System.out.println("Dunno whats happenning");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String formatTime(long timestamp) {
        return new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date(timestamp * 1000));
    }
}
