/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Inventory;
import static Model.Inventory.deletePart;
import static Model.Inventory.deleteProduct;
import static Model.Inventory.getPartInventory;
import static Model.Inventory.getProductInventory;
import static Model.Inventory.partDeleteValidation;
import Model.Part;
import Model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kyknightC482.MainApp;

/**
 * FXML Controller class
 *
 * @author kyleighknight
 */
public class MainScreenController implements Initializable {
    
    @FXML private Label mainScreenLabel;
    @FXML private Label partsLabel;
    @FXML private Label productsLabel;
    @FXML private Button partsSearchButton;
    @FXML private Button productSearchButton;
    @FXML private Button partsClearSearchFieldButton;
    @FXML private Button productClearSearchFieldButton;
    @FXML private Button exitButton;
    @FXML private Pane partsPane;
    @FXML private Pane productPane;
    
    //Parts Objects
    @FXML private Button addPartButton;
    @FXML private Button modifyPartButton;
    @FXML private Button deletePartButton;
    //Parts TableView
    @FXML private TableView<Part> mainPartsTableView;
    @FXML private TableColumn<Part, Integer> mainPartIDCol;
    @FXML private TableColumn<Part, String> mainPartNameCol;
    @FXML private TableColumn<Part, Integer> mainPartInventoryCol;
    @FXML private TableColumn<Part, Double> mainPartPriceCol;
    //Parts TextField
    @FXML private TextField mainPartsSearchField;
    //Product modify objects
    private static Part modifyPart;
    private static int modifyPartIndex;
    
    //Products Objects
    @FXML private Button addProductButton;
    @FXML private Button modifyProductButton;
    @FXML private Button deleteProductButton;
    //Products TableView
    @FXML private TableView<Product> mainProductTableView;
    @FXML private TableColumn<Product, Integer> mainProductIDCol;
    @FXML private TableColumn<Product, String> mainProductNameCol;
    @FXML private TableColumn<Product, Integer> mainProductInventoryCol;
    @FXML private TableColumn<Product, Double> mainProductPriceCol;
    //Product TextField
    @FXML private TextField mainProductSearchField;
    //Product modify objects
    private static Product modifyProduct;
    private static int modifyProductIndex;
    
    public static int partModifyIndex(){
        return modifyPartIndex;
    }
    
    public static int productModifyIndex(){
        return modifyProductIndex;
    }
    
    public void MainScreenController(){
    }
    
    /**
     * This method clears the parts search field when the clear button is selected
     * @param event
     * @throws IOException 
     */
    public void clearPartsSearch(ActionEvent event) throws IOException{
        updatePartTableView();
        mainPartsSearchField.setText("");
    }
    
    /**
     * This method clears the product search field
     * @param event
     * @throws IOException 
     */
    public void clearProductSearch(ActionEvent event) throws IOException{
        updateProductTableView();
        mainProductSearchField.setText("");
    }
    
    /**
     * This method allows the user to exit the system
     * @param event 
     */
    public void mainExitButtonPushed(ActionEvent event){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("You are leaving the system. Do you wish to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK) {
            System.exit(0);
        } else {
            System.out.println("You selected cancel. Please complete form.");
        }
    }
    
    /**
     * This method changes the screen to the AddParts screen when Add button 
     * is selected and a parts record is selected
     * @param event
     * @throws IOException 
     */
    public void addPartButtonPushed(ActionEvent event) throws IOException{
        Parent addPartParent = FXMLLoader.load(getClass().getResource("AddParts.fxml"));
        Scene addPartScene = new Scene(addPartParent);
        
        //This gets the Stage information
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow(); 
        window.setScene(addPartScene);
        window.show();
    }
    
    /**
     * This method changes the screen to the ModifyPartsScreen when Modify 
     * button is selected and a parts record is selected
     * @param event
     * @throws IOException 
     */
    public void modifyPartButtonPushed(ActionEvent event) throws IOException{
        modifyPart = mainPartsTableView.getSelectionModel().getSelectedItem();
        modifyPartIndex = getPartInventory().indexOf(modifyPart);
        Parent modifyPartParent = FXMLLoader.load(getClass().getResource("ModifyParts.fxml"));
        Scene modifyPartScene = new Scene(modifyPartParent);
        
        //This gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow(); 
        window.setScene(modifyPartScene);
        window.show();
    }
    
    /**
     * This method will search and return part entered in the search field
     * @param event 
     * @throws java.io.IOException 
     */
    public void mainPartsSearchButtonPushed(ActionEvent event) throws IOException{
        String searchPartString = mainPartsSearchField.getText();
        int partIndex = -1;
        if(Inventory.lookupPart(searchPartString) == -1){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Search Error"); //I guess its better than saying 'Danger, Will Robertson!'
            alert.setHeaderText(searchPartString + " part not found");
            alert.setContentText(searchPartString + " does not match current records.");
            alert.showAndWait();
        } else {
            partIndex = Inventory.lookupPart(searchPartString);
            Part tempPart = Inventory.getPartInventory().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            mainPartsTableView.setItems(tempPartList);
        }
    }
    
    /**
     * This method deletes the selected part, asks for confirmation of delete,
     * and return whether or not the part was deleted.
     * @param event
     * @throws IOException 
     */
    public void deletePartButtonPushed(ActionEvent event) throws IOException{
        Part part = mainPartsTableView.getSelectionModel().getSelectedItem();
        if(partDeleteValidation(part)){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Delete Part Error!");
            alert.setHeaderText("Selected Part cannot be removed!");
            alert.setContentText(part.getPartName() + " is connected to a product(s).");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Delete Product");
            alert.setHeaderText("Confirm Product Delete?");
            alert.setContentText("Do you wish to permanently delete " + part.getPartName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            
            if(result.get() == ButtonType.OK){
                deletePart(part);
                updatePartTableView();
                System.out.println(part.getPartName() + " was removed.");
            } else {
                System.out.println(part.getPartName() + " was not removed.");
            }
        }
    }
    
    /**
     * This method changes the screen to the AddPartsScreen when Add button 
     * is selected and a parts record is selected
     * @param event
     * @throws IOException 
     */
    public void addProductButtonPushed(ActionEvent event) throws IOException{
        Parent addProductParent = FXMLLoader.load(getClass().getResource("AddProductScreen.fxml"));
        Scene addProductScene = new Scene(addProductParent);
        
        //This gets the Stage information
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow(); 
        window.setScene(addProductScene);
        window.show();
    }
    
    /**
     * This method changes the screen to the ModifyPartsScreen when Modify 
     * button is selected and a parts record is selected
     * @param event
     * @throws IOException 
     */
    public void modifyProductButtonPushed(ActionEvent event) throws IOException{
        modifyProduct = mainProductTableView.getSelectionModel().getSelectedItem();
        modifyProductIndex = getProductInventory().indexOf(modifyProduct);
        Parent modifyProductParent = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));
        Scene modifyProductScene = new Scene(modifyProductParent);
        
        //This gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow(); 
        window.setScene(modifyProductScene);
        window.show();
    }
    
    /**
     * This method will search and return product entered in the search field
     * @param event
     * @throws IOException 
     */
    public void mainProductSearchButtonPushed(ActionEvent event) throws IOException{
        String searchProductString = mainProductSearchField.getText();
        int productIndex = -1;
        if(Inventory.lookupProduct(searchProductString) == productIndex){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Search Error"); //More professional than saying 'Ummm... thats not a thing.'
            alert.setHeaderText(searchProductString + " product not found");
            alert.setContentText(searchProductString + " does not match current records.");
            alert.showAndWait();
        } else {
            productIndex = Inventory.lookupProduct(searchProductString);
            Product tempProduct = Inventory.getProductInventory().get(productIndex);
            ObservableList<Product> tempProductList = FXCollections.observableArrayList();
            tempProductList.add(tempProduct);
            mainProductTableView.setItems(tempProductList);
        }
    }
    
    /**
     * This method deletes the selected product, asks for confirmation of delete,
     * and return whether or not the product was deleted.
     * @param event
     * @throws IOException 
     */
    public void deleteProductButtonPushed(ActionEvent event) throws IOException{
        Product product = mainProductTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Confirm?");
        alert.setContentText("Do you wish to permanently delete " + product.getProductName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            deleteProduct(product);
            updateProductTableView();
            System.out.println(product.getProductName() + " was removed.");
        } else {
            System.out.println(product.getProductName() + " was not removed");
        }
    }
    
    /**
     * This method will enable the modify, delete, and add buttons once a row in the table is selected
     */
    public void userClickedOnTable(){
        this.deletePartButton.setDisable(false);
        this.modifyPartButton.setDisable(false);
        this.addPartButton.setDisable(false);
        this.deleteProductButton.setDisable(false);
        this.modifyProductButton.setDisable(false);
        this.addProductButton.setDisable(false);
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainPartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        mainPartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        mainPartInventoryCol.setCellValueFactory(cellData -> cellData.getValue().partInventoryLevelProperty().asObject());
        mainPartPriceCol.setCellValueFactory(cellData -> cellData.getValue().partPricePerUnitProperty().asObject());
        
        mainProductIDCol.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        mainProductNameCol.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        mainProductInventoryCol.setCellValueFactory(cellData -> cellData.getValue().productInventoryLevelProperty().asObject());
        mainProductPriceCol.setCellValueFactory(cellData -> cellData.getValue().productPricePerUnitProperty().asObject());
        
        updatePartTableView();
        updateProductTableView();
    }    
    
    public void updatePartTableView(){
        mainPartsTableView.setItems(getPartInventory());
    }
    
    public void updateProductTableView() {
        mainProductTableView.setItems(getProductInventory());
    }

    public void setMainApp(MainApp mainApp) {
        updatePartTableView();
        updateProductTableView();
    }
}
