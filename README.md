# A. How to use the Weather Information App
  ## Install JDK 17.0 from Oracle
    Following https://code.visualstudio.com/docs/java/java-gui to create a project and get
    pom.xml file
  ## Install apache-maven-3.9.6 and assign path
  ## Compile & Running the program:
    1. Open PowerShell from Windows
    2. Change path to target folder:
    ### `cd C:\xxx\app_weather_forecast`
    ### `run "C:\Program Files\apache-maven-3.9.6\bin\mvn.cmd" javafx:run -f "c:\yourPath\a1\pom.xml"`

  ## Interacting with GUI
    Click when using the button, text field, and dropdown.
    Close the program by the top-right ‘X’ button.

  ## Get weather info
    input city name (case-insensitive)
    click button [Get weather]

# B. Details about your implementation. 

  ## UI: use JavaFX
  ## API: use openweathermap.org
  ## JSON parser: org.json
  ## Icon: use local image
  ## Unit converter: use comboBox to choose after each weather query. it auto resets when clicking query weather.
  ## History Tracking: use textarea and variable to log input
  ## Dynamic Backgrounds: use the machine's local time to check day or night. then show image


<p align="center">
  <img src="./demo1.png" width="700px">
</p>

<p align="center">
  <img src="./demo2.png" width="700px">
</p>
