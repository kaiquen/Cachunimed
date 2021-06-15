package controller.diretor;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Types;

public class CreateFuncionarioController implements Initializable{


    @FXML ComboBox<Types> comboBox;
    @FXML TextField textFieldCpf;
    @FXML TextField textFieldName;
    @FXML PasswordField passwordField;

    @FXML private void create(){
        System.out.println(verifyField());
    } 

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {  
        printTypes();
    }

    private boolean verifyField(){
        String name =  textFieldName.getText();
        String password = passwordField.getText();
        boolean verifyField = (!name.isEmpty() && !password.isEmpty() && verifyCpf() && verifyComboBox()) ? true : false;
        return verifyField;
    }

    private Boolean verifyCpf(){
        String cpf = textFieldCpf.getText();
        boolean verifyCPF = (!cpf.isEmpty() && cpf.length() == 11 && cpf.matches("[0-9]+")) ? true : false;
        return verifyCPF;
    }

    private Boolean verifyComboBox(){
        boolean verifyComboBox = !(comboBox.getValue().toString().isEmpty()) ? true : false;
        return verifyComboBox;
    }

    private void printTypes(){
        List<Types> type = new ArrayList<>();
        ObservableList<Types> types;
    
        Types type1 = new Types("Diretor");
        Types type2 = new Types("Médico");
        Types type3 = new Types("Recepcionista");

        type.add(type1);
        type.add(type2);
        type.add(type3);

        types = FXCollections.observableArrayList(type);
        comboBox.setItems(types);
    }
}
