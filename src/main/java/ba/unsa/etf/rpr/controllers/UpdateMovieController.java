package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.business.MovieManager;
import ba.unsa.etf.rpr.domain.Movie;
import ba.unsa.etf.rpr.exception.MovieException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class UpdateMovieController {
    @FXML
    private ChoiceBox<Integer> idBox;
    @FXML
    private Label updateMessage;
    private List<Integer> ids=new ArrayList<Integer>() ;
    private MovieManager movieManager=new MovieManager();
    @FXML
    private ChoiceBox<Integer> hourBox;
    @FXML
    private ChoiceBox<Integer> minBox;
    @FXML
    private ChoiceBox<Integer> durationBox;
    @FXML
    private TextField nameField;
    @FXML
    private TextField genreField;
    @FXML
    DatePicker dateBox;


    private Integer[] hour = {10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
    private Integer[] min = {00, 10, 20, 30, 40, 50};
    private Integer[] duration = {60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 220};
    @FXML
    public void initialize() throws MovieException {
        List<Movie> list=movieManager.getAll();
        for(int i=0; i< list.size(); i++){
            Movie movie= list.get(i);
            ids.add(movie.getId());
        }
        idBox.getItems().addAll(ids);
        hourBox.getItems().addAll(hour);
        minBox.getItems().addAll(min);
        durationBox.getItems().addAll(duration);
    }
    public void updateButtonOnAction(ActionEvent actionEvent) throws MovieException {
       try {
           movieManager.validateAddFields(nameField.getText(), genreField.getText(),
                   dateBox.toString(), hourBox.getValue(), minBox.getValue(), durationBox.getValue());
           movieManager.validateDeleteFields(idBox.getValue());
           AdminController admin = new AdminController();
           LocalDate localDate = dateBox.getValue();
           LocalDateTime localDateTime = localDate.atTime(hourBox.getValue(), minBox.getValue());
           Movie movie = admin.createMovie(nameField.getText(), genreField.getText(), localDateTime, durationBox.getValue());
           movie.setId(idBox.getValue());
           movieManager.update(movie);
           openDialog("Information", "/fxml/information.fxml");
           Stage stage=(Stage) idBox.getScene().getWindow();
           stage.close();
       } catch (Exception e){
        new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK).show();
    }
    }
    private void openDialog(String title,String file ) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
        Stage stage = new Stage();
        stage.setScene(new Scene((Parent) loader.load(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.setTitle(title);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();

    }
}
