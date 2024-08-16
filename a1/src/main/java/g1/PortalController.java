package g1;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalTime;

import g1.HttpRequestHelper.HttpResponse;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;

import org.json.JSONArray;
import org.json.JSONObject;

public class PortalController {

    private static String historyKeywork = "history: ";
    private static Double tempOutput = 0.0;
    private static Double windOutput = 0.0;

    @FXML
    private VBox mainWeatherBox;

    @FXML
    private TextField cityInput = new TextField();

    @FXML
    private TextArea history = new TextArea();

    @FXML
    private ComboBox tempUnit = new ComboBox<String>();

    @FXML
    private ComboBox windUnit = new ComboBox<String>();

    @FXML
    private Label temperature;

    @FXML
    private Label humidity;

    @FXML
    private Label windSpeed;

    @FXML
    private Label condition;

    @FXML
    private Label forecastCondition;

    @FXML
    private Label mes;

    @FXML
    private ImageView iconImg;

    @FXML
    public void initialize() {
        tempUnit.getItems().add("Celsius");
        tempUnit.getItems().add("Fahrenheit");
        windUnit.getItems().add("meter/sec");
        windUnit.getItems().add("miles/hour");
        tempUnit.getSelectionModel().selectFirst();
        windUnit.getSelectionModel().selectFirst();

        LocalTime startOfDay = LocalTime.of(6, 0); // 06:00
        LocalTime endOfDay = LocalTime.of(18, 0);  // 18:00
        LocalTime now = LocalTime.now();

        BackgroundImage image= null;
        if (now.isAfter(startOfDay) && now.isBefore(endOfDay)) {
            image= new BackgroundImage(new Image(Paths.get("day.jpg").toUri().toString(),360,840,false,true),
            BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
              BackgroundSize.DEFAULT);
        } else {
            image= new BackgroundImage(new Image(Paths.get("sunset.jpg").toUri().toString(),360,840,false,true),
            BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
              BackgroundSize.DEFAULT);
        }

        mainWeatherBox.setBackground(new Background(image));
    }

    @FXML
    private void OnTempUnit() throws IOException {
        if(tempOutput == 0.0){
            return;
        }
        // format
        DecimalFormat f = new DecimalFormat("##.0");
        if(tempUnit.getSelectionModel().getSelectedItem().toString() == "Fahrenheit"){
            tempOutput = tempOutput*9/5+32;
            temperature.setText(f.format(tempOutput).toString());
        }else{
            tempOutput = (tempOutput-32)*5/9;
            temperature.setText(f.format(tempOutput).toString());
        }
    }

    @FXML
    private void OnWindUnit() throws IOException {
        if(windOutput == 0.0){
            return;
        }
        // format
        DecimalFormat f = new DecimalFormat("##.0");
        if(windUnit.getSelectionModel().getSelectedItem().toString() == "miles/hour"){
            windOutput = windOutput*2.23693629;
            windSpeed.setText(f.format(windOutput).toString());
        }else{
            windOutput = windOutput/2.23693629;
            windSpeed.setText(f.format(windOutput).toString());
        }
    }

    @FXML
    private void GetWeather() throws IOException {
        //find real path
        //System.out.println(Paths.get("./aaa.jpg").toUri().toString());
        LocalTime now = LocalTime.now();
        PortalController.historyKeywork += cityInput.getText()+" "+now+", ";
        history.setText(historyKeywork);
        String url = SysConfig.getBaseURL() + "?q="+cityInput.getText()+",jp&APPID=" + SysConfig.getAPIKey();
        HttpResponse response = HttpRequestHelper.makeHttpRequest(url,1000*30);

        int statusCode = response.getStatusCode();
        String responseContent = response.getResponseContent();

        // debug from API info
        System.out.println(responseContent);
        //System.out.println(statusCode);
        
        cleanLabel();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseContent);
        } catch(Exception e) {
            mes.setText("mes: "+responseContent);
            return;
        }
        if (statusCode !=200){
            mes.setText("mes: "+jsonObject.getString("message"));
        } else{
            JSONArray listArray = jsonObject.getJSONArray("list");
            JSONObject firstListItem = listArray.getJSONObject(0);
            JSONObject secondListItem = listArray.getJSONObject(1);
            JSONArray weatherArray = firstListItem.getJSONArray("weather");
            JSONArray forecastWeatherArray = secondListItem.getJSONArray("weather");
            JSONObject firstWeatherItem = weatherArray.getJSONObject(0);
            JSONObject forecastWeatherItem = forecastWeatherArray.getJSONObject(0);
            String description = firstWeatherItem.getString("description");
            String forecastDescription = forecastWeatherItem.getString("description");
            Double temp = firstListItem.getJSONObject("main").getDouble("temp")-273.15;
            PortalController.tempOutput = temp;
            Double humi = firstListItem.getJSONObject("main").getDouble("humidity");
            Double wind = firstListItem.getJSONObject("wind").getDouble("speed");
            PortalController.windOutput = wind;
            mes.setText("mes: query success!");
            condition.setText(description);
            DecimalFormat f = new DecimalFormat("##.0");
            temperature.setText(f.format(temp).toString());
            humidity.setText(humi.toString());
            windSpeed.setText(wind.toString());
            forecastCondition.setText(forecastDescription);

            if (description.contains("sun")) {
                iconImg.setImage(new Image(new FileInputStream("sun.jpg")));
            } else if (description.contains("cloud")) {
                iconImg.setImage(new Image(new FileInputStream("clouds.jpg")));
            } else {
                iconImg.setImage(new Image(new FileInputStream("default.jpg")));
            }
        }


    }

    private void cleanLabel() {
        // init data
        tempUnit.getSelectionModel().selectFirst();
        windUnit.getSelectionModel().selectFirst();
        condition.setText("-");
        humidity.setText("-");
        temperature.setText("-");
        windSpeed.setText("-");
        forecastCondition.setText("-");
    }

}
