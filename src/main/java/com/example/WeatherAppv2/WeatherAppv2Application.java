package com.example.WeatherAppv2;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.PageAttributes.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;

@SpringBootApplication
public class WeatherAppv2Application {

    private static final String API_KEY = "a2d623c78b2a1cc057284ac1f8921e36";

    public static void main(String[] args) {
        SpringApplication.run(WeatherAppv2Application.class, args);
    }

    @RestController
    public class WeatherController {

    	@GetMapping("/")
    	public String getHomePage() {
    	    return "index.html";
    	}

    	
        @GetMapping("/Weather")
        public String getWeather(@RequestParam(value = "city", defaultValue = "Wien") String city) throws IOException {
            String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;


            // HTTP Request
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            System.out.println("URL de la requête : " + url);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // JSON extraction
            String weatherData = response.toString();
            String temperature = weatherData.split("\"temp\":")[1].split(",")[0];
            String description = weatherData.split("\"description\":\"")[1].split("\"")[0];
            String cityOutput = weatherData.split("\"name\":\"")[1].split("\"")[0];
            float temperatureCelsius = Float.parseFloat(temperature) - 273.15f;
            NumberFormat nf = new DecimalFormat("0.###");
            String temperatureCelsiusRounded = nf.format(temperatureCelsius);

            return "Météo actuelle à " + cityOutput + ":\n" +
                    "Température : " + temperatureCelsiusRounded + " degrés Celsius\n" +
                    "Description : " + description;
        }
    }
}
