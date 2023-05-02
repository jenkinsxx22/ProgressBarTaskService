package application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class BlankController implements Initializable{
	  @FXML
	    private BorderPane Blankborderpane;
	  @FXML
	    private Label lblHome; 
	  private static List<String> loadingParts;
	  
	  @Override
	    public void initialize(URL location, ResourceBundle resources) {
		  
		  loadingParts.add("Loading scenes...");
		  loadingParts.add("Loading memory database...");
		  loadingParts.add("Loading program structures");
		  loadingParts.add("Loading setup data...");
		 
		  
		  lblHome.setText("Current Datetime: "+System.getenv("TIME"));
		  
	  }
	  
 
	    
}
