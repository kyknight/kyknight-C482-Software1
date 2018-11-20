/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author kyleighknight
 */
public class Inventory {
    
    //Part inventory list and count 
    private static ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private static int partIDCount = 0;
    
    //Product inventory list and count
    private static ObservableList<Product> productInventory = FXCollections.observableArrayList();
    private static int productIDCount = 0;
    
    public Inventory(){
    }
    
    /**
     * this method gets the part inventory to be displayed
     * @return 
     */
    public static ObservableList<Part> getPartInventory(){
        return partInventory;
    }
    
    /**
     * this method adds a part into the inventory list
     * @param part 
     */
    public static void addPart(Part part){
        partInventory.add(part);
    }
    
    /**
     * this method deletes the part from the inventory list
     * @param part 
     */
    public static void deletePart(Part part){
        partInventory.remove(part);
    }
    
    /**
     * this method updates the part information from the modify part scene
     * @param index
     * @param part 
     */
    public static void updatePart(int index, Part part){
        partInventory.set(index, part);
    }
    
    /**
     * this method gets the part count from the inventory list
     * @return 
     */
    public static int getPartIdCount(){
        partIDCount++;
        return partIDCount;
    }
    
    /**
     * this method validates that the part was deleted
     * @param part
     * @return 
     */
    public static boolean partDeleteValidation(Part part){
        boolean isFound = false;
        
        for (int i=0; i<productInventory.size(); i++) {
            if(productInventory.get(i).getProductParts().contains(part)){
                isFound = true;
            } 
        }
        return isFound;
    }
    
    /**
     * this method searches the parts inventory list for the user-defined search
     * criteria
     * @param searchTerm
     * @return 
     */
    public static int lookupPart(String searchTerm){
        boolean isFound = false;
        int index = 0;
        
        if(isInteger(searchTerm)){
            for(int i=0; i<partInventory.size(); i++){
                if(Integer.parseInt(searchTerm) == partInventory.get(i).getPartID()){
                    index = i;
                    isFound = true;
                }
            }
        } else {
            for(int i=0; i<partInventory.size(); i++){
                if(searchTerm.equals(partInventory.get(i).getPartName())) {
                    index = i;
                    isFound  = true;
                }
            }
        }
        
        if(isFound == true){
            return index;
        } else {
            System.out.println("There are no parts found.");
            return -1;
        }    
    }

    
    /**
     * this method gets the product inventory to be displayed
     * @return 
     */
    public static ObservableList<Product> getProductInventory(){
        return productInventory;
    }
    
    /**
     * this method adds a product into the inventory list
     * @param product
     */
    public static void addProduct(Product product){
        productInventory.add(product);
    }
    
    /**
     * this method deletes the product from the inventory list
     * @param product 
     */
    public static void deleteProduct(Product product){
        productInventory.remove(product);
    }
    
    /**
     * this method updates the product information from the modify product scene
     * @param index
     * @param product 
     */
    public static void updateProduct(int index, Product product){
        productInventory.set(index, product);
    }
    
    /**
     * this method gets the product count from the inventory list
     * @return 
     */
    public static int getProductIdCount(){
        productIDCount++;
        return productIDCount;
    }
    
    /**
     * this method searches the product inventory list for the user-defined search
     * criteria
     * @param searchTerm
     * @return 
     */
    public static int lookupProduct(String searchTerm){
        boolean isFound = false;
        int index = 0;
        
        if(isInteger(searchTerm)){
            for(int i=0; i<productInventory.size(); i++){
                if(Integer.parseInt(searchTerm) == productInventory.get(i).getProductID()){
                    index = i;
                    isFound = true;
                }
            }
        } else {
            for(int i=0; i<productInventory.size(); i++){
                if(searchTerm.equals(productInventory.get(i).getProductName())){
                    index = i;
                    isFound  = true;
                }
            }
        }
        
        if(isFound == true){
            return index;
        } else {
            System.out.println("There are no products found.");
            return -1;
        }
    }
    
    /**
     * this method parses the input into a integer
     * @param input
     * @return 
     */
    public static boolean isInteger(String input){
        try{
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
