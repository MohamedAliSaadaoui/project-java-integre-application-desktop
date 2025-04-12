package com.example.rewear.controller;

import com.example.rewear.dao.CommandDAO;
import com.example.rewear.model.Command;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CommandController extends BaseController {
    private final CommandDAO commandDAO = new CommandDAO();
    private ObservableList<Command> commands = FXCollections.observableArrayList();

    @FXML private TableView<Command> commandTable;
    @FXML private TableColumn<Command, Integer> idColumn;
    @FXML private TableColumn<Command, Integer> userIdColumn;
    @FXML private TableColumn<Command, Integer> productIdColumn;
    @FXML private TableColumn<Command, String> statusColumn;
    @FXML private TableColumn<Command, String> dateColumn;
    @FXML private TextField searchField;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadCommands();
        setupEventHandlers();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    }

    private void loadCommands() {
        commands.setAll(commandDAO.getAllCommands());
        commandTable.setItems(commands);
    }

    private void setupEventHandlers() {
        addButton.setOnAction(e -> showCommandDialog(null));
        editButton.setOnAction(e -> {
            Command selected = commandTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showCommandDialog(selected);
            } else {
                showError("Please select a command to edit");
            }
        });
        deleteButton.setOnAction(e -> {
            Command selected = commandTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (showConfirmation("Are you sure you want to delete this command?")) {
                    if (commandDAO.deleteCommand(selected.getId())) {
                        commands.remove(selected);
                        showSuccess("Command deleted successfully");
                    } else {
                        showError("Failed to delete command");
                    }
                }
            } else {
                showError("Please select a command to delete");
            }
        });
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterCommands(newVal));
    }

    private void showCommandDialog(Command command) {
        // TODO: Implement command dialog
        // This should show a dialog with fields for all command properties
        // and handle saving/updating the command
    }

    private void filterCommands(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            commandTable.setItems(commands);
        } else {
            ObservableList<Command> filtered = FXCollections.observableArrayList();
            for (Command command : commands) {
                if (String.valueOf(command.getId()).contains(searchText) ||
                    String.valueOf(command.getUserId()).contains(searchText) ||
                    String.valueOf(command.getProductId()).contains(searchText) ||
                    command.getEtat().toLowerCase().contains(searchText.toLowerCase())) {
                    filtered.add(command);
                }
            }
            commandTable.setItems(filtered);
        }
    }
} 