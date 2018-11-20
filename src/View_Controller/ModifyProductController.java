/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Inventory;
import static Model.Inventory.getPartInventory;
import static Model.Inventory.getProductInventory;
import Model.Part;
import Model.Product;
import static View_Controller.MainScreenController.productModifyIndex;
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


/**
 * FXML Controller class
 *
 * @author kyleighknight
 */
public class ModifyProductController implements Initializable {

     //product labels
    @FXML private Label modifyProductTitleLabel;
    @FXML private Label modifyProductProductIDLabel;
    @FXML private Label modifyProductProductNameLabel;
    @FXML private Label modifyProductInvLevelLabel;
    @FXML private Label modifyProductPriceLabel;
    @FXML private Label modifyProductMinLabel;
    @FXML private Label modifyProductMaxLabel;
    @FXML private Label modifyProductSearchPartLabel;
    @FXML private Label modifyProductAssociatedPartsLabel;
    
    //Product TextFields
    @FXML private TextField modifyProductIDTextField;
    @FXML private TextField modifyProductNameTextField;
    @FXML private TextField modifyProductInvLevelTextField;
    @FXML private TextField modifyProductPriceTextField;
    @FXML private TextField modifyProductMinTextField;
    @FXML private TextField modifyProductMaxTextField;
    @FXML private TextField modifyProductSearchPartTextField;
    
    //Product buttons
    @FXML private Button modifyProductSearchPartButton;
    @FXML private Button modifyProductAddButton;
    @FXML private Button modifyProductSaveButton;
    @FXML private Button modifyProductCancelButton;
    @FXML private Button ModifyProductDeleteAssociatedPartButton;
    
    //Product search parts table
    @FXML private TableView<Part> modifyProductAddTableView;
    @FXML private TableColumn<Part, Integer> modifyProductPartIDCol;
    @FXML private TableColumn<Part, String> modifyProductPartNameCol;
    @FXML private TableColumn<Part, Integer> modifyProductInvLevelCol;
    @FXML private TableColumn<Part, Double> modifyProductPriceCol;
    
    //Product associated parts table
    @FXML private TableView<Part> modifyProductCurrentTableView;
    @FXML private TableColumn<Part, Integer> modifyProductCurrentPartIDCol;
    @FXML private TableColumn<Part, String> modifyProductCurrentPartNameCol;
    @FXML private TableColumn<Part, Integer> modifyProductCurrentInvLevelCol;
    @FXML private TableColumn<Part, Double> modifyProductCurrentPriceCol;
    
    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    private int productIndex = productModifyIndex();
    private String exceptionMessage = new String();
    
    private int productID;
    
    /**
     * this method clears the search field of the add part search once add button is selected
     * @param event 
     */
    public void ClearSearchAdd(ActionEvent event){
        updateAddPartsTableView();
        modifyProductSearchPartTextField.setText("");
    }
    
    /**
     * this method queries the parts when the search button is selected
     * @param event 
     */
    public void modifyProductSearchButtonPushed(ActionEvent event) {
        String searchPartString = modifyProductSearchPartTextField.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPartString) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText(searchPartString + " not found");
            alert.setContentText(searchPartString + " does not match any known parts.");
            alert.showAndWait();
        }
        else {
            partIndex = Inventory.lookupPart(searchPartString);
            Part tempPart = Inventory.getPartInventory().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            modifyProductAddTableView.setItems(tempPartList);
        }
    }
    
    /**
     * this method adds the part to the product associated table when save button is
     * selected
     * @param event 
     */
    public void ModifyProductAddButtonPushed(ActionEvent event){
        Part part = modifyProductAddTableView.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        updateCurrentPartsTableView();
    }
    
    /**
     * this method deletes the selected part when the delete button is selected
     * @param event 
     */
    public void ModifyProductDeleteButtonPushed(ActionEvent event){
        Part part = modifyProductCurrentTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Part Deletion");
        alert.setHeaderText("Confirm Part Delete");
        alert.setContentText("Do you want to delete " + part.getPartName() + " from parts?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            currentParts.remove(part);
        } else {
            System.out.println("You have selected cancel.");
        }
    }
    
    /**
     * this method saves the product information entered into the product textFields when
     * save button is selected
     * @param event
     * @throws IOException 
     */
    public void ModifyProductSaveButtonPushed(ActionEvent event) throws IOException {
        String productName = modifyProductNameTextField.getText();
        String productInv = modifyProductInvLevelTextField.getText();
        String productPrice = modifyProductPriceTextField.getText();
        String productMin = modifyProductMinTextField.getText();
        String productMax = modifyProductMaxTextField.getText();

        try {
            exceptionMessage = Product.isProductValid(productName, Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv), Double.parseDouble(productPrice), currentParts, exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Product");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            }
            else {
                System.out.println("Product name: " + productName);
                Product newProduct = new Product();
                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setProductInventoryLevel(Integer.parseInt(productInv));
                newProduct.setProductPricePerUnit(Double.parseDouble(productPrice));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                newProduct.setProductParts(currentParts);
                Inventory.updateProduct(productIndex, newProduct);

                Parent modifyProductSaveParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(modifyProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Modifying Product");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }
    
    /**
     * this method displays a confirmation message when the cancel button is selected
     * and if select ok redirects to the main controller screen and if select cancel stays
     * on current page
     * @param event
     * @throws IOException 
     */
    public void ModifyProductCancelButtonPushed(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Do you want to cancel modifying the product?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent modifyProductCancelParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(modifyProductCancelParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else {
            System.out.println("You clicked cancel.");
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Product product = getProductInventory().get(productIndex);
        productID = getProductInventory().get(productIndex).getProductID();
        modifyProductProductIDLabel.setText("Auto-Gen: " + productID);
        modifyProductNameTextField.setText(product.getProductName());
        modifyProductInvLevelTextField.setText(Integer.toString(product.getProductInventoryLevel()));
        modifyProductPriceTextField.setText(Double.toString(product.getProductPricePerUnit()));
        modifyProductMinTextField.setText(Integer.toString(product.getProductMin()));
        modifyProductMaxTextField.setText(Integer.toString(product.getProductMax()));
        
        currentParts = product.getProductParts();
        modifyProductPartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        modifyProductPartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        modifyProductInvLevelCol.setCellValueFactory(cellData -> cellData.getValue().partInventoryLevelProperty().asObject());
        modifyProductPriceCol.setCellValueFactory(cellData -> cellData.getValue().partPricePerUnitProperty().asObject());
        modifyProductCurrentPartIDCol.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        modifyProductCurrentPartNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        modifyProductCurrentInvLevelCol.setCellValueFactory(cellData -> cellData.getValue().partInventoryLevelProperty().asObject());
        modifyProductCurrentPriceCol.setCellValueFactory(cellData -> cellData.getValue().partPricePerUnitProperty().asObject());
        
        updateAddPartsTableView();
        updateCurrentPartsTableView();
    }    
    
    public void updateAddPartsTableView() {
        modifyProductAddTableView.setItems(getPartInventory());
    }

    public void updateCurrentPartsTableView() {
        modifyProductCurrentTableView.setItems(currentParts);
    }
}
