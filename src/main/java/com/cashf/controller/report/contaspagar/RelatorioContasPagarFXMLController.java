/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cashf.controller.report.contaspagar;

import com.cashf.model.contasPagar.StatusPagto;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author joao
 */
public class RelatorioContasPagarFXMLController implements Initializable {

    @FXML
    private JFXButton btnImprimir;
    @FXML
    private JFXButton btnLimpar;
    @FXML
    private JFXTextField txtTotalInt;
    @FXML
    private JFXTextField txtQtdeTitulos;
    @FXML
    private JFXDatePicker dtpDataIni;
    @FXML
    private Label lblSaldo;
    @FXML
    private JFXDatePicker dtpDataFin;
    @FXML
    private JFXButton btnPesquisar;
    //----
    String erros = "";
    RelatorioContasPagarController relatorioController;
    @FXML
    private JFXComboBox<StatusPagto> cbbFiltro;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        relatorioController = new RelatorioContasPagarController();
        loadCbbFiltro();
    }

    @FXML
    private void onSalvar(ActionEvent event) {
        relatorioController.testaDois();
    }

    @FXML
    private void onLimpar(ActionEvent event) {
        dtpDataFin.setValue(LocalDate.now());
        dtpDataIni.setValue(LocalDate.now());
        txtTotalInt.clear();
        txtQtdeTitulos.clear();
    }

    @FXML
    private void onKeyReleasedQtdeAjuste(KeyEvent event) {
    }

    @FXML
    private void onPesquisar(ActionEvent event) {
        getData();
        relatorioController.gerarDados();
        txtQtdeTitulos.setText(relatorioController.getLista().size() + "");
        txtTotalInt.setText(relatorioController.totalPeriodo().toString());

    }

    private void getData() {
        relatorioController.setDataInicio(dtpDataIni.getValue());
        relatorioController.setDataFim(dtpDataFin.getValue());
        relatorioController.setStatusPagto(cbbFiltro.getSelectionModel().getSelectedItem());
    }

    private void loadCbbFiltro() {
        cbbFiltro.getItems().addAll(Arrays.asList(StatusPagto.values()));
    }
}
