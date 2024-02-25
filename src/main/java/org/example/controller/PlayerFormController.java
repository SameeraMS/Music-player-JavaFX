package org.example.controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.entity.Song;
import org.example.model.SongModel;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlayerFormController extends Application {

    public Button btnAddMusic;
    public Button btnPlay;
    public Button btnStop;
    public Button btnNext;
    public Label lblSongTitle;
    public Button btnAddToFav;
    public ImageView imgPlay;
    public Button btnPlaylist;
    public Button btnPreviouse;
    public Slider slider;
    public Label lblstart;
    public Label lblend;
    private MediaPlayer currentMediaPlayer;
    private Song currentSong;
    private Song selectedSong;
    public boolean isPlaying = false;
    public boolean isStoped = true;


    public void btnAddMusicOnAction(ActionEvent actionEvent) throws IOException {
        start(new Stage());
        if (selectedSong != null) {
            lblSongTitle.setText(selectedSong.getTitle());
            currentSong = selectedSong;
        }
    }

    @FXML
    private void btnPlayOnAction(ActionEvent actionEvent) {

        if (currentSong != null) {
        if (isStoped) {
            playSong();
            imgPlay.setImage(new Image("/asserts/icon/pause.jpg"));
            isStoped = false;
            isPlaying = true;
        } else {
            if (isPlaying) {
                Pause();
                isPlaying = false;
                imgPlay.setImage(new Image("/asserts/icon/play.png"));
            } else {
                Resume();
                isPlaying = true;
                imgPlay.setImage(new Image("/asserts/icon/pause.jpg"));
            }
        }
    }else{
        new Alert(Alert.AlertType.WARNING, "Please select a song or click 'next' to play your favorite playlist..").show();
    }

    }

    public void playSong(){
        if (currentSong != null) {
            try {
                String filePath = currentSong.getFilePath();
                Media media = new Media(new File(filePath).toURI().toString());
                if (currentMediaPlayer != null) {
                    currentMediaPlayer.stop();
                }
                currentMediaPlayer = new MediaPlayer(media);
                currentMediaPlayer.play();

                updateTimeSlider();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            new Alert(Alert.AlertType.WARNING, "Please select a song or click 'next' to play your favorite playlist..").show();
        }
    }


    public void btnStopOnAction(ActionEvent actionEvent) {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.stop();
            isStoped = true;
            imgPlay.setImage(new Image("/asserts/icon/play.png"));
        } else {
            System.out.println("stoped");
        }
    }


    public void btnNextOnAction(ActionEvent actionEvent) {
        Song nextSong = SongModel.getNextSong();
        if (nextSong != null) {
            lblSongTitle.setText(nextSong.getTitle());
            currentSong = nextSong;
            btnPlayOnAction(actionEvent);
        }
    }


    public void Pause() {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.pause();
        } else {
            System.out.println("No media player is currently playing");
        }
    }

    public void Resume() {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.play();
        } else {
            System.out.println("No media player is currently playing");
        }

    }

    public void btnAddToFavOnAction(ActionEvent actionEvent) {
        boolean isAlreadyAdded = false;
        boolean isSaved = false;
        if(currentSong != null){
            List<Song> allFavs = SongModel.getAll();
            for (Song fav : allFavs) {
                if (fav.getFilePath().equals(currentSong.getFilePath())) {
                    new Alert(Alert.AlertType.WARNING, "Song already in your favorites").show();
                    isAlreadyAdded=true;
                }
            }
            if(isAlreadyAdded==false){
                isSaved = SongModel.saveSong(currentSong);
            }
            if(isSaved){
                new Alert(Alert.AlertType.INFORMATION, "Song added to favorites").show();
            }
        }else{
            new Alert(Alert.AlertType.WARNING, "No any song selected").show();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 files", "*.mp3","*.m4a"));
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            String title = selectedFile.getName().replaceFirst("[.][^.]+$", "");
            List<Song> allSongs = SongModel.getAll(); // Assuming SongModel.getAll() is accessible
            int size = allSongs.size();
            Song song = new Song(size +   1, title, "Unknown", filePath);
            selectedSong=song;
        }
    }

    public void btnViewFavOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent load = fxmlLoader.load(this.getClass().getResource("/view/playlist_form.fxml"));
        Object controller = fxmlLoader.getController();
        Scene scene = new Scene(load);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setTitle("Playlist");

        stage.show();
    }



    private void updateTimeSlider() {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.setOnReady(() -> {
                Duration totalDuration = currentMediaPlayer.getTotalDuration();
                double totalDurationInSeconds = totalDuration.toSeconds();
                slider.setMax(totalDurationInSeconds);
                String minutes = String.valueOf(totalDuration.toMinutes());
                String[] split = minutes.split("\\.");
                lblend.setText(split[0]+":"+split[1].substring(0, 2));


                // Update slider position when song time changes
                currentMediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                    double currentTimeInSeconds = newValue.toSeconds();
                    String minutes1 = String.valueOf(newValue.toMinutes());
                    slider.setValue(currentTimeInSeconds);

                    String[] split2 = minutes1.split("\\.");
                    lblstart.setText(split2[0]+":"+split2[1].substring(0, 2));
                });
            });
        }
    }


    public void btnPreviousOnAction(ActionEvent actionEvent) {
        Song nextSong = SongModel.getNextSong();
        if (nextSong != null) {
            lblSongTitle.setText(nextSong.getTitle());
            currentSong = nextSong;
            btnPlayOnAction(actionEvent);
        }
    }
}
