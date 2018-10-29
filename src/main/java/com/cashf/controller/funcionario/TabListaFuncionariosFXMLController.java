/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cashf.controller.funcionario;

import com.cashf.model.funcionario.Funcionario;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author joao
 */
public class TabListaFuncionariosFXMLController implements Initializable {

    @FXML
    private JFXTextField txtConsultar;
    @FXML
    private JFXButton btnPesquisar;
    @FXML
    private JFXRadioButton rdbNome;
    @FXML
    private JFXRadioButton rdbCpf;
    @FXML
    private JFXRadioButton rdbTodos;
    @FXML
    private TableView<Funcionario> tbvFuncionarios;
    @FXML
    private TableColumn<Funcionario, String> tbcNome;
    @FXML
    private TableColumn<Funcionario, String> tbcEndereco;
    @FXML
    private TableColumn<Funcionario, String> tbcEmail;
    @FXML
    private TableColumn<Funcionario, String> tbcCPf;
    private static TableView<Funcionario> _tbvFuncionarios;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setUptableView();
        setUpRadioButtons();
        loadTbv();
        _tbvFuncionarios = tbvFuncionarios;
    }

    @FXML
    private void onPesquisar(ActionEvent event) {
        switch (FuncionarioController.getInstance().getTipoConsulta()) {
            case 1:
                FuncionarioController.getInstance().buscaNome(txtConsultar.getText());
                loadTbv();
                break;
            case 2:
                FuncionarioController.getInstance().buscaCpf(txtConsultar.getText());
                loadTbv();
                break;
            default:
                FuncionarioController.getInstance().buscaTodos();
                loadTbv();
                break;
        }
    }

    @FXML
    private void onMouseClickedCliente(MouseEvent event) {
        if (tbvFuncionarios.getSelectionModel().getSelectedItem() != null) {

        }
    }

    private void setUptableView() {
        tbcNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        tbcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tbcCPf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        tbvFuncionarios.getColumns().setAll(tbcNome, tbcEndereco, tbcEmail, tbcCPf);
        tbvFuncionarios.setRowFactory(tv -> {
            TableRow<Funcionario> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    FuncionarioController.getInstance().setFuncionario(row.getItem());
                    FuncionarioController.getInstance().setListaTelefone(FXCollections.observableList(FuncionarioController.getInstance().getFuncionario().getTelefones()));
                    TabFuncionarioFXMLController.LDTS();
                    TabFuncionarioFXMLController.LDTSPhone();
                    TabFuncionarioFXMLController.setBtnEX(Boolean.FALSE);
                    FXGerenciarFuncionariosFXMLController.getTabPane().getSelectionModel().selectFirst();
                }
            });
            return row;
        });
    }

    private void loadTbv() {
        tbvFuncionarios.setItems(FuncionarioController.getInstance().getLista());
    }

    public static void loadTbvFun() {
        _tbvFuncionarios.setItems(FuncionarioController.getInstance().getLista());
        _tbvFuncionarios.refresh();
    }

    private void setUpRadioButtons() {

        rdbNome.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                rdbCpf.setSelected(false);
                rdbTodos.setSelected(false);
                FuncionarioController.getInstance().setTipoConsulta(1);//todos
            }
        });
        rdbCpf.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                rdbNome.setSelected(false);
                rdbTodos.setSelected(false);
                FuncionarioController.getInstance().setTipoConsulta(2);//todos
            }
        });

        rdbTodos.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                rdbCpf.setSelected(false);
                rdbNome.setSelected(false);
                FuncionarioController.getInstance().setTipoConsulta(0);//todos
            }

        });
    }
}
