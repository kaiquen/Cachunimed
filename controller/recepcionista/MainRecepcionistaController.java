package controller.recepcionista;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.AnchorPane;
import model.Funcionario;
import model.Paciente;
import model.dao.FuncionarioDAO;
import model.dao.PacienteDAO;
import model.database.Factory;
import model.database.Idatabase;

public class MainRecepcionistaController implements Initializable{
    @FXML TextField textField;
    @FXML ListView<Paciente> listView;
    @FXML TextField textFieldCpfCreate;
    @FXML TextField textFieldNameCreate;
    @FXML TextField textFieldFoneCreate;
    @FXML TextField textFieldAddressCreate;
    @FXML TextInputControl labelIdList;

    @FXML TextInputControl textFieldFoneUpdate;
    @FXML TextInputControl textFieldNameUpdate;
    @FXML TextInputControl textFieldAddressUpdate;
    @FXML ComboBox<Funcionario> comboBoxMedicos;
    
    @FXML AnchorPane anchorPaneCreate;
    @FXML AnchorPane anchorPaneUpdate;
    @FXML AnchorPane anchorPaneConsulta;
    @FXML Group group;


    @FXML Label labelId;
    @FXML Label labelName;
    @FXML Label labelCpf;
    @FXML Label labelFone;
    @FXML Label labelAddress;

    @FXML private void searchPaciente() throws SQLException{
        
        if(textField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Search Fail");
            alert.setHeaderText("Preencha o campo ");
            alert.setContentText("Tente novamente!");
            alert.show();
        }else{
            atualizaListView();
        }
    }
    @FXML private void create(){
        Idatabase database = Factory.getDatabase("postgres");
        Connection connection = database.connect();
        PacienteDAO pacienteDAO = new PacienteDAO(connection);
        try {
         
            Paciente paciente = new Paciente(textFieldCpfCreate.getText(), textFieldNameCreate.getText(), textFieldFoneCreate.getText(), textFieldAddressCreate.getText());
            
            pacienteDAO.create(paciente);

            anchorPaneCreate.setVisible(false);
            group.setVisible(true);   
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Insert Fail");
            alert.setHeaderText("Preencha todos os campos");
            alert.setContentText(e.getMessage());
            alert.show();
        }  
    }
    @FXML private void update(){
        System.out.println(listView.getSelectionModel().getSelectedItem().getId());
     
            Idatabase database = Factory.getDatabase("postgres");
            Connection connection = database.connect();
            PacienteDAO pacienteDAO = new PacienteDAO(connection);
            try {
                System.out.println(listView.getSelectionModel().getSelectedItem().getId());
                Paciente paciente = new Paciente(Integer.valueOf(listView.getSelectionModel().getSelectedItem().getId()), textFieldNameUpdate.getText(), textFieldFoneUpdate.getText(), textFieldAddressUpdate.getText());
                pacienteDAO.update(paciente);
                
                anchorPaneUpdate.setVisible(false);
                group.setVisible(true);  
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insert Fail");
                alert.setHeaderText("Preencha todos os campos");
                alert.setContentText(e.getMessage());
                alert.show();
            }
        
    }
    @FXML private void clearUpdate(){
        anchorPaneUpdate.setVisible(false);
        group.setVisible(true);
        
    }
    @FXML private void clearCreate(){
        anchorPaneCreate.setVisible(false);
        group.setVisible(true);
    }
    @FXML private void createPaciente(){
        group.setVisible(false);
        anchorPaneCreate.setVisible(true);
    }

    @FXML private void updatePaciente(){
        if(listView.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Fail");
            alert.setHeaderText("Selecione o Paciente");
            alert.setContentText("Tente novamente!");
            alert.show();
        } else {
        group.setVisible(false);
        anchorPaneUpdate.setVisible(true);
        }
    }
    @FXML private void createConsulta(){
        System.out.println("ok");
        if(listView.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Fail");
            alert.setHeaderText("Selecione o Paciente");
            alert.setContentText("Tente novamente!");
            alert.show();
        } else {
            group.setVisible(false);
            anchorPaneConsulta.setVisible(true);
        }
    }

    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {   
        try {
            printMedicos();
        } catch (SQLException e) {
            
            e.printStackTrace();
        }
        
        listView.getSelectionModel().selectedItemProperty()
                .addListener((obeservable, oldValue, newValue) -> selectItem(newValue));
    }

 
    private void atualizaListView() throws SQLException{
        Idatabase database = Factory.getDatabase("postgres");
            Connection connection = database.connect();
            PacienteDAO pacienteDAO = new PacienteDAO(connection);
            Paciente paciente = new Paciente(textField.getText());
            
            ObservableList<Paciente> items =FXCollections.observableArrayList (
                pacienteDAO.searchPaciente(paciente));
            listView.setItems(items);
    }
    private ObservableList<Funcionario> medicos() throws SQLException {
        Idatabase database = Factory.getDatabase("postgres");
        Connection connection = database.connect();
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO(connection);
        
        return FXCollections.observableArrayList(
            funcionarioDAO.selectMedicos()
        );
    }

    private void printMedicos() throws SQLException{       
        comboBoxMedicos.setItems(medicos());
    }


    private void selectItem(Paciente paciente) {
        if (paciente != null) {
            labelId.setText(String.valueOf(paciente.getId()));
            labelName.setText(paciente.getName());
            labelCpf.setText(paciente.getCpf());
            labelFone.setText(paciente.getFone());
            labelAddress.setText(paciente.getAddress());
        } else {
            labelIdList.setText("");
            labelName.setText("");
            labelCpf.setText("");
            labelFone.setText("");
            labelAddress.setText("");
        }
    }
    
}
