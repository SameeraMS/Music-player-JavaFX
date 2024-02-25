package org.example.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.entity.Song;
import org.example.model.SongModel;
import org.example.model.tm.PlaylistTm;


import java.util.List;

public class PlaylistFormController {
    public Button btnRemove;
    public TableView<PlaylistTm> tbl;

    public void initialize() {
        tbl.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tbl.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("title"));

        loadPlaylist();

    }

    private void loadPlaylist() {

        tbl.getItems().clear();

            List<Song> allPlaylist = SongModel.getAll();

            for (Song s : allPlaylist) {
                tbl.getItems().add(new PlaylistTm(s.getId(), s.getTitle()));
            }


    }

    public void btnRemoveOnAction(ActionEvent actionEvent) {
        int id = tbl.getSelectionModel().getSelectedItem().getId();

        boolean isDeleted = SongModel.deleteSong(new Song(id, null, null, null));

        if (isDeleted) {
            loadPlaylist();
            new Alert(Alert.AlertType.CONFIRMATION, "Song Deleted").show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }
    }
}
