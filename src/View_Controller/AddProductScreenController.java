/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Inventory;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static Model.Inventory.getPartInventory;

/**
 * FXML Controller class
 *
 * @author kyleighknight
 */
public class AddProductScreenController implements Initializable {

    //product labels
    @FXML private Label addProductTitleLabel;
    @FXML private Label addProductProductIDLabel;
    @FXML private Label addProductProductNameLabel;
    @FXML private Label addProductInvLevelLabel;
    @FXML private Label addProductPriceLabel;
    @FXML private Label addProductMinLabel;
    @FXML private Label addProductMaxLabel;
    @FXML private Label addProductSearchPartLabel;
    @FXML private Label addProductAssociatedPartsLabel;
    
    //Product TextFields
    @FXML private TextField addProductIDTextField;
    @FXML private TextField addProductNameTextField;
    @FXML private TextField addProductInvLevelTextField;
    @FXML private TextField addProductPriceTextField;
    @FXML private TextField addProductMinTextField;
    @FXML private TextField addProductMaxTextField;
    @FXML private TextField addProductSearchPartTextField;
    
    //Product buttons
    @FXML private Button addProductSearchPartButton;
    @FXML private Button addProductPartButton;
    @FXML private Button addProductSaveButton;
    @FXML private Button addProductCancelButton;
    @FXML private Button deleteAssociatedPartButton;
    
    //Product search parts table
    @FXML private TableView<Part> addProductAddTableView;
    @FXML private TableColumn<Part, Integer> addProductPartIDCol;
    @FXML private TableColumn<Part, String> addProductPartNameCol;
    @FXML private TableColumn<Part, Integer> addProductInvLevelCol;
    @FXML private TableColumn<Part, Double> addProductPriceCol;
    
    //Product associated parts table
    @FXML private TableView<Part> addProductCurrentTableView;
    @FXML private TableColumn<Part, Integer> addProductCurrentPartIDCol;
    @FXML private TableColumn<Part, String> addProductCurrentPartNameCol;
    @FXML private TableColumn<Part, Integer> addProductCurrentInvLevelCol;
    @FXML private TableColumn<Part, Double> addProductCurrentPriceCol;
    
    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    private String exceptionMessage = new String();
    private int productID;
    
    public AddProductScreenController(){
    }
    
    /**
     * this method clears the search field of the add part search once add button is selected
     * @param event 
     */
    public void ClearSearchAdd(ActionEvent event){
        PartUpdateTableView();
        addProductSearchPartTextField.setText("");
    }
    
    /**
     * this method queries the parts table to find the entered search field when Search button
     * is selected
     * @param event 
     */
    public void AddProductSearchButtonPushed(ActionEvent event){
        String searchPartString = addProductSearchPartTextField.getText();
        int partIndex = -1;
        
        if(Inventory.lookupPart(searchPartString) == -1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search");
            alert.setHeaderText(searchPartString + " is not found");
            alert.setContentText(searchPartString + " does not match any known parts.");
            alert.showAndWait();
        } else{
            partIndex = Inventory.lookupPart(searchPartString);
            Part tempPart = Inventory.getPartInventory().get(partIndex);
            
            ObservableList<Part> tempProdList = FXCollections.observableArrayList();
            tempProdList.add(tempPart);
            addProductAddTableView.setItems(tempProdList);
        }
    }
    
    /**
     * this method adds the selected part to the product when add button is selected
     * @param event 
     */
    public void AddProductAddButtonPushed(ActionEvent event){
        Part part = addProductAddTableView.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        PartUpdateCurrentTableView();
    }
    
    /**
     * this method deletes the selected associated part from the product when the 
     * delete button is selected
     * @param event 
     */
    public void AddProductDeleteButtonPushed(ActionEvent event){
        Part part = addProductCurrentTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete Part");
        alert.setHeaderText("Confirm");
        alert.setContentText("Do you wish to delete " + part.getPartName() + " from parts?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK){
            System.out.println(part.getPartName() + " part deleted.");
            currentParts.remove(part);
        } else {
            System.out.println("You have selected cancel.");
        }
    }
    
    /**
     * this method saves the product information entered in the product textFields 
     * and the save button is selected
     * @param event
     * @throws IOException 
     */
    public void AddProductSaveButtonPushed(ActionEvent event) throws IOException{
        String productName = addProductNameTextField.getText();
        String productInventory = addProductInvLevelTextField.getText();
        String productPricePerUnit = addProductPriceTextField.getText();
        String productMin = addProductMinTextField.getText();
        String productMax = addProductMaxTextField.getText();
        
        try{
            exceptionMessage = Product.isProductValid(productName, Integer.parseInt(productMin), 
                    Integer.parseInt(productMax), Integer.parseInt(productInventory),
                    Double.parseDouble(productPricePerUnit), currentParts, exceptionMessage);
            
            if(exceptionMessage.length() > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Adding Product Error");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            } else {
                System.out.println("Product name: " + productName);
                Product newProduct = new Product();
                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setProductInventoryLevel(Integer.parseInt(productInventory));
                newProduct.setProductPricePerUnit(Double.parseDouble(productPricePerUnit));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                newProduct.setProductParts(currentParts);
                Inventory.addProduct(newProduct);

                Parent addProductSaveParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(addProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Adding Product Error");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }
    
    /**
     * this method asks for confirmation before redirecting to the MainScreen when the cancel
     * button is selected
     * @param event
     * @throws IOException 
     */
    public void AddProductCancelButtonPushed(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Do you wish to cancel adding a new product?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent addProductCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(addProductCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("You selected cancel.");
        }
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addProductPartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        addProductPartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        addProductInvLevelCol.setCellValueFactory(cellData -> cellData.getValue().partInventoryLevelProperty().asObject());
        addProductPriceCol.setCellValueFactory(cellData -> cellData.getValue().partPricePerUnitProperty().asObject());
        
        addProductCurrentPartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        addProductCurrentPartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        addProductCurrentInvLevelCol.setCellValueFactory(cellData -> cellData.getValue().partInventoryLevelProperty().asObject());
        addProductCurrentPriceCol.setCellValueFactory(cellData -> cellData.getValue().partPricePerUnitProperty().asObject());
        
        PartUpdateTableView();
        PartUpdateCurrentTableView();
        
        productID = Inventory.getProductIdCount();
        addProductIDTextField.setText("Auto Gen - " + productID);
    }  
    
    public void PartUpdateTableView(){
        addProductAddTableView.setItems(getPartInventory());
    }
    
    public void PartUpdateCurrentTableView(){
        addProductCurrentTableView.setItems(currentParts);
    }
    
}
