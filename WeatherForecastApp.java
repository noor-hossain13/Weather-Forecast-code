

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class WeatherForecastApp {

    private static JFrame frame;
    private static JTextField locationField;
    private static JTextArea weatherDisplay;
    private static JButton fetchButton;
    private static String apiKey = "ea37c49f3a3b41d67b7919026ce7f2e3"; // Replace with your API key

    private static String fetchWeatherData(String city) {
         try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = "";
            String line;
            while ((line = reader.readLine()) != null) {
                response += line;
            }
            reader.close();

            JSONObject jsonObject = (JSONObject) JSONValue.parse(response.toString());
            JSONObject mainObj = (JSONObject) jsonObject.get("main");

            double temperaturekelvin = (double) mainObj.get("temp");
            long humidity = (long) mainObj.get("humidity");

            // convert into celsius
            double temperatureCelsius = temperaturekelvin - 273.15;

            // Retrieve weather description
            JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
            JSONObject weather = (JSONObject) weatherArray.get(0);
            String description = (String) weather.get("description");

            return "Description: " + description + "\nTemperature: " + temperatureCelsius + " Celsius\nHumidity: " + humidity + "%";

        } catch (Exception e) {
            return "Failed to fetch weather data.\nPlease check city and API key";
        }
    }

    public static void main(String[] args) {
        frame = new JFrame("Weather App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Panel for better organization
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        locationField = new JTextField(15);
        locationField.setFont(new Font("Arial", Font.BOLD, 30)); // Set font size and style for the text field
        fetchButton = new JButton("Get Weather");
        weatherDisplay = new JTextArea(30, 30);
        weatherDisplay.setEditable(false);

        // Create a label and set its font
        JLabel cityLabel = new JLabel("Enter City Name");
        cityLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font size for the label

        panel.add(cityLabel);
        panel.add(locationField);

        // Customize the Fetch Weather button
        fetchButton.setFont(new Font("Arial", Font.BOLD, 20));
        fetchButton.setBackground(Color.BLACK); // Customize the button background color
        fetchButton.setForeground(Color.WHITE); // Customize the button text color
        panel.add(fetchButton);

        frame.add(panel, BorderLayout.NORTH);

        // Customize the JTextArea appearance
        JScrollPane scrollPane = new JScrollPane(weatherDisplay);
        frame.add(scrollPane, BorderLayout.CENTER);

        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String city = locationField.getText();
                String weatherInfo = fetchWeatherData(city);
                weatherDisplay.setText(weatherInfo);
            }
        });

        frame.setLocationRelativeTo(null); // Center the frame on the screen

        frame.setVisible(true);
    }
}
