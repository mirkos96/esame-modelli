/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cashf.controller.mesas;

import com.cashf.cashfood.MainApp;
import com.cashf.core.venda.VendaController;
import com.cashf.model.produto.Produto;
import com.cashf.model.venda.ProdutoVenda;
import com.jfoenix.controls.JFXButton;
import com.sun.prism.impl.Disposer;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author joao
 */
public class NGerenciarMesasFXMLController implements Initializable {

    @FXML
    private Label lblNumMesa;
    @FXML
    private TableView<ProdutoVenda> tbvComanda;
    @FXML
    private TableColumn<ProdutoVenda, String> tbcCod;
    @FXML
    private TableColumn<ProdutoVenda, Produto> tbcDescricao;
    @FXML
    private TableColumn<ProdutoVenda, BigDecimal> tbcQtde;
    @FXML
    private TableColumn<ProdutoVenda, BigDecimal> tbcPreco;
    @FXML
    private TableColumn<ProdutoVenda, BigDecimal> tbcTotal;
    @FXML
    private TableColumn btnCancelar;
    @FXML
    private TableColumn btnTransferirM;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblSaldoAtual;
    //----
    //----
    private static TableView<ProdutoVenda> _tbvComanda;
    @FXML
    private Label lblCLiente;
    @FXML
    private Label lblNCompras;
    @FXML
    private Label lblAvgCompras;
    @FXML
    private Label lblPax;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUptableViewProdutos();
        loadTbv();
        lblNumMesa.setText(MesaController.getInstance().getMesaAtual().getNumMesa() + "");
        loadData();
        _tbvComanda = tbvComanda;
    }


    private void loadData() {
     
        DecimalFormat df = new DecimalFormat("R$ #,##0.00;(R$ #,##0.00)");
        lblTotal.setText(df.format(VendaController.getInstance().getVendaByMesa(MesaController.getInstance().getMesaAtual()).getValorTotal()));
        lblCLiente.setText(VendaController.getInstance().getVendaByMesa(MesaController.getInstance().getMesaAtual()).getCliente().getNome());
        lblNCompras.setText(""+VendaController.getInstance().getNumVendasCliente(VendaController.getInstance().getVendaByMesa(MesaController.getInstance().getMesaAtual())));
        lblAvgCompras.setText(df.format(VendaController.getInstance().getAvgVendaCliente(VendaController.getInstance().getVendaByMesa(MesaController.getInstance().getMesaAtual()))));
        lblPax.setText(""+MesaController.getInstance().getMesaAtual().getNumPax());
    }

    private void loadTbv() {
        tbvComanda.setItems(VendaController.getInstance().getListaProduosVenda());
    }

    public static void refreshTbvComanda() {
        _tbvComanda.setItems(VendaController.getInstance().getListaProduosVenda());
        _tbvComanda.refresh();
    }

    private void setUptableViewProdutos() {
        tbcCod.setCellValueFactory((param) -> new SimpleStringProperty(param.getValue().getProduto().getCodigoReferencia()));
        tbcDescricao.setCellValueFactory(new PropertyValueFactory<>("produto"));
        tbcQtde.setCellValueFactory(new PropertyValueFactory<>("qtde"));
        tbcPreco.setCellValueFactory(new PropertyValueFactory<>("precoUnit"));

        tbcTotal.setCellValueFactory((param) -> new SimpleObjectProperty<BigDecimal>(param.getValue().getPrecoUnit().multiply(param.getValue().getQtde())));
        btnCancelar.setCellFactory(
                new Callback<TableColumn<Disposer.Record, Boolean>, TableCell<Disposer.Record, Boolean>>() {
            @Override
            public TableCell<Disposer.Record, Boolean> call(TableColumn<Disposer.Record, Boolean> p) {
                return new ButtonCellDelete();
            }
        });
        btnTransferirM.setCellFactory(
                new Callback<TableColumn<Disposer.Record, Boolean>, TableCell<Disposer.Record, Boolean>>() {
            @Override
            public TableCell<Disposer.Record, Boolean> call(TableColumn<Disposer.Record, Boolean> p) {
                return new ButtonCellShift();
            }
        });
        tbvComanda.getColumns().setAll(tbcCod, tbcDescricao, tbcQtde, tbcPreco, tbcTotal, btnCancelar, btnTransferirM);
    }

    private void loadBox(String boxPath, String title) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(boxPath));
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            MainApp.janelaAnterior = MainApp.janelaAberta;
            MainApp.janelaAberta = stage;
            stage.show();
        } catch (IOException ex) {
            System.out.println("Erro---->" + ex);
        }
    }

    public class ButtonCellDelete extends TableCell<Disposer.Record, Boolean> {

        JFXButton cellButton = new JFXButton("Excluir");
        Notifications notificationBuilder;
        FontAwesomeIconView auxff = new FontAwesomeIconView(FontAwesomeIcon.TRASH);

        public ButtonCellDelete() {
            auxff.setSize("2em");
            cellButton.setGraphic(auxff);
            cellButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            cellButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    // get Selected Item
                    ProdutoVenda currentPerson = (ProdutoVenda) ButtonCellDelete.this.getTableView().getItems().get(ButtonCellDelete.this.getIndex());
                    //remove selected item from the table list
                    if (currentPerson != null) {
                        // ... user chose OK
                        VendaController.getInstance().getListaProd().remove(currentPerson);

                    } else {
                        notificationBuilder = Notifications.create().title("Nenhum produto selecionado!").
                                text("Você deve selecionar um contato para remover.").
                                hideAfter(Duration.seconds(2)).
                                position(Pos.TOP_RIGHT).
                                darkStyle();
                        notificationBuilder.showConfirm();
                    }

                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            } else {
                setGraphic(null);
            }
        }

    }

    public class ButtonCellShift extends TableCell<Disposer.Record, Boolean> {

        JFXButton cellButton = new JFXButton("Transferir");
        Notifications notificationBuilder;
        FontAwesomeIconView auxff = new FontAwesomeIconView(FontAwesomeIcon.ARROWS_H);

        public ButtonCellShift() {
            auxff.setSize("2em");
            cellButton.setGraphic(auxff);
            cellButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            cellButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    // get Selected Item
                    ProdutoVenda currentPerson = (ProdutoVenda) ButtonCellShift.this.getTableView().getItems().get(ButtonCellShift.this.getIndex());
                    //remove selected item from the table list
                    if (currentPerson != null) {
                        // ... user chose OK
                        VendaController.getInstance().setProdutoVendaSelecionado(currentPerson);
                        loadBox("/fxml/mesas/TransferirProdutoFXML.fxml", "Transferir Ítem");

                    } else {
                        notificationBuilder = Notifications.create().title("Nenhum contato selecionado!").
                                text("Você deve selecionar um contato para transferir.").
                                hideAfter(Duration.seconds(2)).
                                position(Pos.TOP_RIGHT).
                                darkStyle();
                        notificationBuilder.showConfirm();
                    }

                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            } else {
                setGraphic(null);
            }
        }

    }
}
