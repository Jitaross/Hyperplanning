package fr.utln.atlas.projethyp.fxapp2.view;

import fr.utln.atlas.projethyp.fxapp2.model.Person;
import fr.utln.atlas.projethyp.fxapp2.model.Group;
import fr.utln.atlas.projethyp.fxapp2.model.Page;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.extern.java.Log;

import java.util.List;
import java.util.UUID;

@Log
public class Controller {
    //A DAO to access the model
    private final Group.DAO groupDAO = Group.DAO.newInstance();

    //A programmatic widget
    private final TableView<Person> table = createTable();
    private static final int PAGE_SIZE = 10;
    //FXML widget bindings
    @FXML
    Pagination paginator;
    @FXML
    private ListView<Person> searchList;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private VBox dataContainer;

    @FXML
    private void initialize() {
        //The pagination to browse data
        paginator = new Pagination();
        paginator.setMaxPageIndicatorCount(10);
        //The method to build a page on change
        paginator.setPageFactory(this::createPage);

        //The search field and button action
        searchButton.setOnAction(event -> searchInNames());

        //  javafx.util.Callback as a lambda
        searchList.setCellFactory(callback-> new ListCell<>() {
                    @Override
                    public void updateItem(Person person, boolean empty) {
                        super.updateItem(person, empty);
                        if (empty || person == null) {
                            setText(null);
                        } else {
                            setText(person.getName());
                        }
                    }
                });

        dataContainer.getChildren().add(paginator);
    }

    //Programmatic creation a of tableview
    @SuppressWarnings("unchecked")
    private TableView<Person> createTable() {
        //A tableview of persons
        TableView<Person> tableView = new TableView<>();

        TableColumn<Person, String> name = new TableColumn<>("NAME");
        //The simplest way to bind a data item field by variable name.
        //NOT TYPESAFE at compile time should be avoided
        //name.setCellValueFactory(new PropertyValueFactory<>("name"));

        //The same with explicit callback call
        /*
        name.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Person, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Person, String> personNameCellDataFeatures) {
                return new SimpleStringProperty(personNameCellDataFeatures.getValue().getName());
            }
        });
        */

        //the type safe version with lambda (returns to an Observable)
        //We use a SimpleXProperty utils method from JavaFX
        name.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getName()));
        name.setSortable(false);

        TableColumn<Person, String> address = new TableColumn<>("ADDRESS");
        address.setSortable(false);
        address.setCellValueFactory(data->new SimpleStringProperty(data.getValue().getAddress()));

        //If there is not corresponding SimpleXProperty
        //we use the ObjectBinding wrapper class.
        TableColumn<Person, UUID> id = new TableColumn<>("UUID");
        id.setSortable(false);
        id.setCellValueFactory(data-> new ObjectBinding<>() {
            @Override
            protected UUID computeValue() {
                return data.getValue().getUuid();
            }
        });

        tableView.getColumns().addAll(id, name, address);
        return tableView;
    }

    private Node createPage(int pageNumber) {
        Page<Person> page = groupDAO.findAll(PAGE_SIZE, pageNumber);
        table.setItems(FXCollections.observableArrayList(page.content()));
        paginator.setPageCount((int) (page.dataSize() / page.pageSize()));
        return new BorderPane(table);
    }

    private void searchInNames() {
        String searchText = searchField.getText();
        Task<List<Person>> task = new Task<>() {
            @Override
            protected List<Person> call() {
                updateMessage("Loading data");
                return Group.DAO.newInstance().search(searchText).content();
            }
        };
        task.setOnSucceeded(event -> searchList.setItems(FXCollections.observableList(task.getValue())));

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
}
