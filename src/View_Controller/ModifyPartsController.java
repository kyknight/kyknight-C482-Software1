/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.InhousePart;
import Model.Inventory;
import static Model.Inventory.getPartInventory;
import Model.OutsourcedPart;
import Model.Part;
import static View_Controller.MainScreenController.partModifyIndex;
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
public class ModifyPartsController implements Initializable {

    @FXML private Label modifyPartLabel;
    @FXML private Label modifyPartIDLabel;
    @FXML private Label modifyPartNameLabel;
    @FXML private Label modifyPartInvLevelLabel;
    @FXML private Label modifyPartPriceLabel;
    @FXML private Label modifyPartMinLabel;
    @FXML private Label modifyPartMaxLabel;
    @FXML private Label modifyPartMachineCompanyLabel;
    
    //In-House and Outsourced RadioButton
    @FXML private RadioButton modifyPartInhouseRadioButton;
    @FXML private RadioButton modifyPartOutsourcedRadioButton;
    
    //Update and Cancel Button
    @FXML private Button modifyPartUpdateButton;
    @FXML private Button modifyPartCancelButton;
    
    ////In-House and Outsourced TextField
    @FXML private TextField modifyPartIDTextField;
    @FXML private TextField modifyPartNameTextField;
    @FXML private TextField modifyPartInvTextField;
    @FXML private TextField modifyPartPriceTextField;
    @FXML private TextField modifyPartMinTextField;
    @FXML private TextField modifyPartMaxTextField;
    @FXML private TextField modifyPartMachineCompanyTextField;
    
    //
    private boolean isOutsourced;
    private boolean isInhouse;
    private String exceptionMessage = new String();
    private int partID;
    int partIndex = partModifyIndex();
    
    /**
     * this method displays that the inhouse radio button is selected and changes the
     * MachineCompany label and text field to Machine ID when selected
     * @param event 
     */
    public void modifyPartInHouseRadio(ActionEvent event) {
        isInhouse = true;
        modifyPartOutsourcedRadioButton.setSelected(false);
        modifyPartInhouseRadioButton.setSelected(true);
        modifyPartMachineCompanyLabel.setText("Machine ID");
    }

    /**
     * this method displays that the outsourced radio button is selected and changes the
     * MachineCompany label and text field to Company when selected
     * @param event 
     */
    public void modifyPartOutsourcedRadio(ActionEvent event) {
        isOutsourced = true;
        modifyPartOutsourcedRadioButton.setSelected(true);
        modifyPartInhouseRadioButton.setSelected(false);
        modifyPartMachineCompanyLabel.setText("Company Name");
    }

    /**
     * this method saves the part information entered into the part textFields when
     * update button is selected
     * @param event
     * @throws IOException 
     */
    public void modifyPartUpdateButtonPushed(ActionEvent event) throws IOException {
        String partName = modifyPartNameTextField.getText();
        String partInventory = modifyPartInvTextField.getText();
        String partPrice = modifyPartPriceTextField.getText();
        String partMin = modifyPartMinTextField.getText();
        String partMax = modifyPartMaxTextField.getText();
        String partMachComp = modifyPartMachineCompanyTextField.getText();

        try {
            exceptionMessage = Part.isPartValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInventory), Double.parseDouble(partPrice), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Part");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            } else {
                if (isInhouse == true) {
                    System.out.println("Part name: " + partName + " 1 " + partMachComp);
                    InhousePart inPart = new InhousePart();
                    inPart.setPartID(partID);
                    inPart.setPartName(partName);
                    inPart.setPartInventoryLevel(Integer.parseInt(partInventory));
                    inPart.setPartPricePerUnit(Double.parseDouble(partPrice));
                    inPart.setPartMin(Integer.parseInt(partMin));
                    inPart.setPartMax(Integer.parseInt(partMax));
                    inPart.setMachineID(Integer.parseInt(partMachComp));
                    Inventory.updatePart(partIndex, inPart);
                } else if (isOutsourced == true) {
                    System.out.println("Part name: " + partName + " 2 " + partMachComp);
                    OutsourcedPart outPart = new OutsourcedPart();
                    outPart.setPartID(partID);
                    outPart.setPartName(partName);
                    outPart.setPartInventoryLevel(Integer.parseInt(partInventory));
                    outPart.setPartPricePerUnit(Double.parseDouble(partPrice));
                    outPart.setPartMin(Integer.parseInt(partMin));
                    outPart.setPartMax(Integer.parseInt(partMax));
                    outPart.setCompanyName(partMachComp);
                    Inventory.updatePart(partIndex, outPart);
                }

                Parent modifyProductSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(modifyProductSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Modifying Part");
            alert.setContentText("Form contains blank fields.");
            System.out.println("this is it!" + partMachComp);
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
    public void modifyPartCancelButtonPushed(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to cancel modifying the part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent modifyPartCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(modifyPartCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else {
            System.out.println("You clicked cancel.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Part part = getPartInventory().get(partIndex);
        partID = getPartInventory().get(partIndex).getPartID();
        modifyPartIDTextField.setText("Auto Gen - " + partID);
        modifyPartNameTextField.setText(part.getPartName());
        
        modifyPartInvTextField.setText(Integer.toString(part.getPartInventoryLevel()));
        modifyPartPriceTextField.setText(Double.toString(part.getPartPricePerUnit()));
        modifyPartMinTextField.setText(Integer.toString(part.getPartMin()));
        modifyPartMaxTextField.setText(Integer.toString(part.getPartMax()));
        
        if (part instanceof InhousePart) {
            modifyPartMachineCompanyLabel.setText("Machine ID");
            modifyPartMachineCompanyTextField.setText(Integer.toString(((InhousePart) getPartInventory().get(partIndex)).getMachineID()));
            modifyPartInhouseRadioButton.setSelected(true);
        }
        else {
            modifyPartMachineCompanyLabel.setText("Company Name");
            modifyPartMachineCompanyTextField.setText(((OutsourcedPart) getPartInventory().get(partIndex)).getCompanyName());
            modifyPartOutsourcedRadioButton.setSelected(true);
        }
    }
}
