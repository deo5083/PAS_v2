package pas_v2.Controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import pas_v2.Models.Employee;
import pas_v2.Models.EmployeeList;
import pas_v2.Models.EmployeeRoleEnum;
import pas_v2.Models.FieldTypeEnum;
import pas_v2.Models.Storage;
import pas_v2.Models.Validator;

/**
 * FXML Controller class
 *
 * @author David Ortiz
 */
public class NewEmployeeController implements Initializable {

    @FXML Label MsgLabel;
    //private javax.swing.JList<String> RoleList;
    @FXML Button cnclBtn;
    @FXML PasswordField empPWField;
    @FXML TextField fNameTxtField;
   
    @FXML TextField lNameTxtField;
    @FXML Button submitBtn;
    
    @FXML
    ToggleGroup roleSelector;

    @FXML
    RadioButton rbAdmin;
    @FXML
    RadioButton rbOperator;
    
    
    private EmployeeList employeeList;
    private Employee currentEmployee;
    private Storage storage;
    private EmployeeRoleEnum role;
    
    public void initData(Employee emp, EmployeeList employeeList){
        this.employeeList = employeeList;
        this.currentEmployee = emp;
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //validation rules
        fNameTxtField.textProperty().addListener(new Validator(fNameTxtField, FieldTypeEnum.NAME));
        lNameTxtField.textProperty().addListener(new Validator(lNameTxtField, FieldTypeEnum.NAME));
        
        storage = new Storage();
        
//        try { 
//            employeeList = new EmployeeList();
//            employeeList.refreshEmployeeList();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(NewEmployeeController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    } 
    
    
    public void submitButtonClicked(ActionEvent ae) throws IOException{ 
        //System.out.println("submit clicked");

          
        if(checkFields())
        {    
           MsgLabel.setText("One or more fields are not completed");     
        }
        else
        {
           if (containsForbiddenCharacters())
            {
                MsgLabel.setText(" the characters ~ and ; cannot be contained in any field");
                resetFields();
            }

            else                
            {    
                role = (rbAdmin.isSelected())?  EmployeeRoleEnum.Admin : EmployeeRoleEnum.Operator;
                Employee newEmployee = new Employee(fNameTxtField.getText(), 
                        lNameTxtField.getText(), role);
                newEmployee.setCredential(empPWField.getText());
                employeeList.addEmployee(newEmployee);
                storage.write(employeeList.getEmployees(), Employee.class);
                MsgLabel.setText("New Employee saved Successfully");
                navigateToStaffUI(ae);
                
                
                //RadioButton selectedRadioButton = (RadioButton) role.getSelectedToggle();
                //String toogleGroupValue = selectedRadioButton.getText();
                //MsgLabel.setText("Submitted Successfully");
                //employeeList.saveEmployee(toogleGroupValue.toLowerCase(), fNameTxtField.getText(), lNameTxtField.getText(), empPWField.getText());            
                
            }     
        }    
       
    }
    
    public void navigateToStaffUI(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/pas_v2/Views/StaffUI.fxml"));
        Parent tableViewParent = loader.load();
        
        Scene tableViewScene = new Scene(tableViewParent);
        
        //access the controller and call a method
        StaffUIController controller = loader.getController();
        controller.initData(currentEmployee, employeeList);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
        
    }
    
    public void resetFields()
    {
        fNameTxtField.setText("");
        lNameTxtField.setText("");
        empPWField.setText("");
        

    }  
    
    public boolean containsForbiddenCharacters()
    {
        boolean badchar; 
        badchar = fNameTxtField.getText().contains(";") || lNameTxtField.getText().contains(";") || empPWField.getText().contains(";") || fNameTxtField.getText().contains("~") || lNameTxtField.getText().contains("~") || empPWField.getText().contains("~");

        return badchar;
    } 
    
    private boolean checkFields()
    {
        boolean anyFieldEmpty; 

        String pass = new String (empPWField.getText());
        if (fNameTxtField.getText().equals("") || lNameTxtField.getText().equals("") || pass.equals(""))
        {
            anyFieldEmpty = true;
        }    
        else
        {
            anyFieldEmpty = false; 
        }
        return anyFieldEmpty;
    } 
    
}
