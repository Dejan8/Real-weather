/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author Dejan
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private TextField textName;
    @FXML
    private Label textCountry;
    @FXML
    private Label textCity;
    @FXML
    private Label textCondition;
    @FXML
    private Label textPreasure;
    @FXML
    private Label textHumidity;
    
    
     
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    

    @FXML
    private void handleButtonActionEnterName(ActionEvent event) {
        label.setText("Can't find city " + textName.getText());
        
            // TODO add your handling code here:
        String cityName = textName.getText();
        textCity.setText(null);
        textCountry.setText(null);
        textCondition.setText(null);
        textHumidity.setText(null);
        textPreasure.setText(null);
        
        
        
        try {
      URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=ff36df026a63fbaf380e553d0ebbc65c");
      BufferedWriter writer;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                writer = new BufferedWriter(new FileWriter("data.html"));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    writer.write(line);
                    writer.newLine();
                    
                    
                    Pattern p = Pattern.compile("\"description\":\"(.*?)\",\"icon\"");
                    Matcher m = p.matcher(line);
                    while (m.find()) {
                        
                        String desc = m.group(1);
                        System.out.println(desc);
                        textCondition.setText("Condition: " + desc);
                        
                        //Temperatura
                        Pattern pt = Pattern.compile("\"temp\":(.*?),\"pressure\"");
                        Matcher mt = pt.matcher(line);
                        while (mt.find()){
                            
                            String temp = mt.group(1);
                            
                            double d= Double.parseDouble(temp);
                            int a = (int) Math.round(d);
                            System.out.println("Trenutna temperatura je: " + (a-273) + "C");
                            int bTempCelsius = a - 273;
                            label.setText("Temp:" + (a-273) + "C");
                        }
                        
                        Pattern pPreasure = Pattern.compile("\"pressure\":(.*?),\"humidity\"");
                        Matcher mp = pPreasure.matcher(line);
                        while (mp.find()){
                            
                            String preasure = mp.group(1);
                            
                            System.out.println("Pritisak je: " + preasure);
                            textPreasure.setText("Preasure: " + preasure + " Bars");
                            
                            //Grad
                            Pattern pCity = Pattern.compile("\"name\":\"(.*?)\",\"cod\"");
                            Matcher mc = pCity.matcher(line);
                            while (mc.find()){
                                
                                String city = mc.group(1);
                                
                                System.out.println("Grad je: " + city);
                                textCity.setText(city);
                                
                            }
                            
                            //Country
                            Pattern pCountry = Pattern.compile("\"country\":\"(.*?)\",\"sunrise\"");
                            Matcher mk = pCountry.matcher(line);
                            while (mk.find()){
                                
                                String country = mk.group(1);
                                
                                System.out.println("Country: " + country);
                                textCountry.setText(country + ",");
                                
                            }
                            
                            //Time"sunrise":1529120559,"sunset"
                            Pattern pTime = Pattern.compile("\"sunrise\":(.*?),\"sunset\"");
                            Matcher mT = pTime.matcher(line);
                            while (mT.find()){
                                
                                String time = mT.group(1);
                                
                                Date date = new Date();
                                Instant instant = date.toInstant();
                                int eT= Integer.parseInt(time);
                                long epochSeconds =eT;
                                ZonedDateTime zonedDateTime = LocalDateTime.ofEpochSecond(epochSeconds, 0,
                                        OffsetDateTime.now(ZoneId.systemDefault()).getOffset()).atZone(ZoneId.systemDefault());
                                System.out.println("epochSeconds = " + epochSeconds);
                                System.out.println("ZonedDateTime = " + zonedDateTime);
                                
                                LocalDateTime dt = LocalDateTime.ofEpochSecond(epochSeconds, 0, ZoneOffset.UTC);
                                System.out.println(dt.toLocalTime());
                                
                                
                                
                                System.out.println("Casova: " + dt.toLocalTime());
                                //jLabelTime.setText(zonedDateTime + ",");
                                
                                
                                
                            }
                            
                            
                            
                            //"humidity"
                            Pattern pHumidity = Pattern.compile("\"humidity\":(.*?),\"temp_min\"");
                            Matcher mH = pHumidity.matcher(line);
                            while (mH.find()){
                                
                                String humidity = mH.group(1);
                                
                                System.out.println("Vlaznost " + humidity);
                                textHumidity.setText("Humidity:  " + humidity +"%");
                                
                            }
                            
                        }
                    }
                    
                    
                }     }
      writer.close();
      
      
        } catch (MalformedURLException ex) {
            Logger.getLogger(JavaFXApplication2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JavaFXApplication2.class.getName()).log(Level.SEVERE, null, ex);
        }
     
        
        
    } 
    }

   
    

