/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cashf.controller.cliente;

import com.cashf.model.cliente.Cliente;
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
public class TabListaClientesFXMLController implements Initializable {

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
    private TableView<Cliente> tbvClientes;
    @FXML
    private TableColumn<Cliente, String> tbcNome;
    @FXML
    private TableColumn<Cliente, String> tbcEmail;
    @FXML
    private TableColumn<Cliente, String> tbcEndereco;
    @FXML
    private TableColumn<Cliente, String> tbcCpf;

    private static TableView<Cliente> _tbvClientes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setUptableView();
        loadTbv();
        setUpRadioButtons();
        _tbvClientes = tbvClientes;
    }

    @FXML
    private void onPesquisar(ActionEvent event) {
        switch (ClienteController.getInstance().getTipoConsulta()) {
            case 1:
                ClienteController.getInstance().buscaNome(txtConsultar.getText());
                loadTbv();
                break;
            case 2:
                ClienteController.getInstance().buscaCpf(txtConsultar.getText());
                loadTbv();
                break;
            default:
                ClienteController.getInstance().buscaTodos();
                loadTbv();
                break;
        }

    }

    public static void loadTbvCli() {
        _tbvClientes.setItems(ClienteController.getInstance().getLista());
        _tbvClientes.refresh();
    }

    @FXML
    private void onMouseClickedCliente(MouseEvent event) {
        

    }

    private void setUptableView() {
        tbcNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        tbcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tbcCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        tbvClientes.getColumns().setAll(tbcNome, tbcEndereco, tbcEmail, tbcCpf);
        tbvClientes.setRowFactory(tv -> {
            TableRow<Cliente> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ClienteController.getInstance().setCliente(row.getItem());
                    ClienteController.getInstance().setListaTelefone(FXCollections.observableList(ClienteController.getInstance().getCliente().getTelefones()));
                    TabClientesFXMLController.LDTSFone();
                    TabClientesFXMLController.LDTS();
                    TabClientesFXMLController.setBtnEX(Boolean.FALSE);
                    FXGerenciarClientesFXMLController.getTabPane().getSelectionModel().selectFirst();
                }
            });
            return row;
        });
    }

    private void loadTbv() {
        tbvClientes.setItems(ClienteController.getInstance().getLista());
    }

    private void setUpRadioButtons() {

        rdbNome.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                rdbCpf.setSelected(false);
                rdbTodos.setSelected(false);
                ClienteController.getInstance().setTipoConsulta(1);//nome
            }
        });
        rdbCpf.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                rdbNome.setSelected(false);
                rdbTodos.setSelected(false);
                ClienteController.getInstance().setTipoConsulta(2);//cpf
            }
        });

        rdbTodos.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                rdbCpf.setSelected(false);
                rdbNome.setSelected(false);
                ClienteController.getInstance().setTipoConsulta(0);//todos
            }
        });
    }
}
