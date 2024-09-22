package sample;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Tab databaseTab;
    @FXML
    private TextField apiLink;
    @FXML
    private TableView<Location> locations;

    @FXML
    private TableColumn<Location, String> cityLoc;

    @FXML
    private TableColumn<Location, String> coordLoc;

    @FXML
    private TableColumn<Location, String> countryLoc;

    @FXML
    private TableColumn<Location, Long> idLoc;

    @FXML
    private TextField ipField;

    @FXML
    private TableColumn<Location, String> ipLoc;

    @FXML
    private TableColumn<Location, String> orgLoc;

    @FXML
    private TableColumn<Location, Integer> postalLoc;

    @FXML
    private TableColumn<Location, String> regionLoc;

    @FXML
    private Button sendRequest;

    @FXML
    private Tab sendRequestTab;

    @FXML
    private Text text;

    @FXML
    private TableColumn<Location, String> timezoneLoc;

    private LocationService locationService = new LocationService(1);
    private ObservableList<Location> locationsList;

    @FXML
    public void sendRequest() throws Exception {
        String ipValue = ipField.getText();

        if (!ipValue.matches("\\b((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\b")) {
            text.setText("Somethings went wrong");
            text.getStyleClass().add("wrong");
            text.setOpacity(1);
            throw new Exception("Ip doesn't match");
        }

        try {
            URL url = new URL(apiLink.getText() + "/" + ipValue + "/geo");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                Gson gson = new GsonBuilder().create();
                Location location = gson.fromJson(response.toString(), Location.class);
                if (text.getStyleClass().size() != 0) {
                    text.getStyleClass().remove(0);
                    text.getStyleClass().add("correct");
                    text.setText("Data was saved");
                }
                text.setOpacity(1);
                locationService.save(location);

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        text.setOpacity(0);
        idLoc.setCellValueFactory(new PropertyValueFactory<>("id"));
        cityLoc.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryLoc.setCellValueFactory(new PropertyValueFactory<>("country"));
        ipLoc.setCellValueFactory(new PropertyValueFactory<>("ip"));
        coordLoc.setCellValueFactory(new PropertyValueFactory<>("loc"));
        orgLoc.setCellValueFactory(new PropertyValueFactory<>("org"));
        postalLoc.setCellValueFactory(new PropertyValueFactory<>("postal"));
        regionLoc.setCellValueFactory(new PropertyValueFactory<>("region"));
        timezoneLoc.setCellValueFactory(new PropertyValueFactory<>("timezone"));

        databaseTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                if (databaseTab.isSelected()) {
                    locationsList = FXCollections.observableList(locationService.findAll());
                    locations.setItems(locationsList);
                }
            }
        });
    }
}
