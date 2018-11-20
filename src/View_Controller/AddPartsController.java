/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.InhousePart;
import Model.Inventory;
import Model.OutsourcedPart;
import Model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kyleighknight
 */
public class AddPartsController implements Initializable {

    @FXML private Label addPartLabel;
    @FXML private Label addPartIDLabel;
    @FXML private Label addPartNameLabel;
    @FXML private Label addPartInvLevelLabel;
    @FXML private Label addPartPriceLabel;
    @FXML private Label addPartMinLabel;
    @FXML private Label addPartMaxLabel;
    @FXML private Label addPartMachineCompanyLabel;
    
    //In-House and Outsourced RadioButton
    @FXML private RadioButton addPartInhouseRadioButton;
    @FXML private RadioButton addPartOutsourcedRadioButton;
    
    //Save and Cancel Button
    @FXML private Button addPartSaveButton;
    @FXML private Button addPartCancelButton;
    
    ////In-House and Outsourced TextField
    @FXML private TextField addPartIDTextField;
    @FXML private TextField addPartNameTextField;
    @FXML private TextField addPartInvTextField;
    @FXML private TextField addPartPriceTextField;
    @FXML private TextField addPartMinTextField;
    @FXML private TextField addPartMaxTextField;
    @FXML private TextField addPartMachineCompanyTextField;
    
    //
    private boolean isOutsourced;
    private boolean isInhouse;
    private String exceptionMessage = new String();
    private int partID;
    
    /**
     * this method changes the last label to Machine ID if InHouse RadioButton
     * is selected
     * @param event 
     */
    public void addPartInHouseRadioPushed(ActionEvent event){
        isInhouse = true;
        addPartMachineCompanyLabel.setText("Machine ID");
        addPartMachineCompanyTextField.setPromptText("Machine ID");
        addPartInhouseRadioButton.setSelected(true);
        addPartOutsourcedRadioButton.setSelected(false);
    }
    
    /**
     * this method changes the last label to Company Name if Outsourced RadioButton
     * is selected
     * @param event 
     */
    public void addPartOutsourcedRadioPushed(ActionEvent event){
        isOutsourced = true;
        addPartMachineCompanyLabel.setText("Company Name");
        addPartMachineCompanyTextField.setPromptText("Company Name");
        addPartOutsourcedRadioButton.setSelected(true);
        addPartInhouseRadioButton.setSelected(false);
    }
    
    public void addPartSaveButtonPushed(ActionEvent event) throws IOException{
        String partName = addPartNameTextField.getText();
        String partInventory = addPartInvTextField.getText();
        String partPrice = addPartPriceTextField.getText();
        String partMin = addPartMinTextField.getText();
        String partMax = addPartMaxTextField.getText();
        String partMachComp = addPartMachineCompanyTextField.getText();
        
        try {
            exceptionMessage = Part.isPartValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInventory), Double.parseDouble(partPrice), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Part");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            } else {
                if (isOutsourced == false) {
                    System.out.println("Part name: " + partName);
                    InhousePart inPart = new InhousePart();
                    inPart.setPartID(partID);
                    inPart.setPartName(partName);
                    inPart.setPartInventoryLevel(Integer.parseInt(partInventory));
                    inPart.setPartPricePerUnit(Double.parseDouble(partPrice));
                    inPart.setPartMin(Integer.parseInt(partMin));
                    inPart.setPartMax(Integer.parseInt(partMax));
                    inPart.setMachineID(Integer.parseInt(partMachComp));
                    Inventory.addPart(inPart);
                } else {
                    System.out.println("Part name: " + partName);
                    OutsourcedPart outPart = new OutsourcedPart();
                    outPart.setPartID(partID);
                    outPart.setPartName(partName);
                    outPart.setPartInventoryLevel(Integer.parseInt(partInventory));
                    outPart.setPartPricePerUnit(Double.parseDouble(partPrice));
                    outPart.setPartMin(Integer.parseInt(partMin));
                    outPart.setPartMax(Integer.parseInt(partMax));
                    outPart.setCompanyName(partMachComp);
                    Inventory.addPart(outPart);
                }

                Parent addPartSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(addPartSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Adding Part");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }
    
    /**
     * this method will prompt the user to confirm cancel and if select ok, user is redirected
     * to the main screen
     * @param event
     * @throws IOException 
     */
    public void addPartCancelButtonPushed(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Do you wish to cancel adding the new part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent addPartCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(addPartCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else {
            System.out.println("You have selected cancel.");
        }
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partID = Inventory.getPartIdCount();
        addPartIDTextField.setText("Auto Gen - " + partID);
    }    
    
}
