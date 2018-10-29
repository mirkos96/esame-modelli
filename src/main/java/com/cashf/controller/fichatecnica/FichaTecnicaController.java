/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cashf.controller.fichatecnica;

import com.cashf.dao.fichatecnica.FichaTecnicaDAO;
import com.cashf.dao.produto.ProdutoDAO;
import com.cashf.model.fichatecnica.FichaTecnica;
import com.cashf.model.fichatecnica.ProdutoFichaTecnica;
import com.cashf.model.produto.Produto;
import com.cashf.model.produto.UnidadeMedida;
import controller.GenericController;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author joao
 */
public class FichaTecnicaController implements GenericController<FichaTecnica> {

    public static FichaTecnicaController controller;
    private final FichaTecnicaDAO fichaTecnicaDAO;
    private final ProdutoDAO produtoDAO;
    private FichaTecnica fichaTecnica;
    private Produto itemAtual = new Produto();
    private Produto produtoPrincipal = new Produto();
    private ObservableList<ProdutoFichaTecnica> listaItensFicha;
    private ObservableList<FichaTecnica> lista;
    private ObservableList<Produto> comboBoxItensFicha;
    private UnidadeMedida unidadeMedida;
    private BigDecimal custoTotal;
    private int tipoConsulta;

    private FichaTecnicaController() {
        this.fichaTecnicaDAO = new FichaTecnicaDAO(FichaTecnica.class);
        this.produtoDAO = new ProdutoDAO(Produto.class);
        this.lista = FXCollections.observableList(fichaTecnicaDAO.listAll());
        this.comboBoxItensFicha = FXCollections.observableList(produtoDAO.listProdNotFicha());
        this.fichaTecnica = new FichaTecnica();
        this.itemAtual = new Produto();
        this.produtoPrincipal = new Produto();
        this.fichaTecnica.setIdFichaTecnica(0l);
        this.custoTotal = BigDecimal.ZERO;
        this.listaItensFicha = FXCollections.observableList(new ArrayList<>());
    }

    public static synchronized FichaTecnicaController getInstance() {
        if (controller == null) {
            controller = new FichaTecnicaController();
        }
        return controller;
    }

    @Override
    public void insert() {
        fichaTecnicaDAO.update(fichaTecnica);
    }

    @Override
    public void update() {
        fichaTecnicaDAO.update(fichaTecnica);
    }

    @Override
    public void delete() {
        fichaTecnicaDAO.delete(fichaTecnica);
    }

    @Override
    public void flushObject() {
        this.fichaTecnica = new FichaTecnica();
        this.itemAtual = new Produto();
        this.produtoPrincipal = new Produto();
        this.fichaTecnica.setIdFichaTecnica(0l);
        this.custoTotal = BigDecimal.ZERO;
        this.listaItensFicha = FXCollections.observableList(new ArrayList<>());
        this.lista = FXCollections.observableList(fichaTecnicaDAO.listAll());
        this.comboBoxItensFicha = FXCollections.observableList(produtoDAO.listProdNotFicha());
    }

    @Override
    public ObservableList<FichaTecnica> getLista() {
        return lista;
    }

    @Override
    public void setLista(ObservableList<FichaTecnica> lista) {

    }

    @Override
    public void setItenLista(FichaTecnica obj) {

    }

    public void setListaItens(BigDecimal qtdeProduto, BigDecimal valorPorcao) {
        ProdutoFichaTecnica pf= new ProdutoFichaTecnica();
        pf.setIdProdutoFichaTecnica(0l);
        pf.setFichaTecnica(fichaTecnica);
        pf.setProduto(itemAtual);
        pf.setUnidadeMedida(unidadeMedida);
        pf.setQtdeProduto(qtdeProduto);
        pf.setValorPorcao(valorPorcao);
        //new ProdutoFichaTecnica(0l, fichaTecnica, itemAtual, unidadeMedida, qtdeProduto, valorPorcao)
        this.listaItensFicha.add(pf);
    }

    public FichaTecnica getFichaTecnica() {
        return fichaTecnica;
    }

    public Produto getItemAtual() {
        return itemAtual;
    }

    public void setItemAtual(Produto itemAtual) {
        this.itemAtual = itemAtual;
    }

    public void setFichaTecnica(FichaTecnica fichaTecnica) {
        this.fichaTecnica = fichaTecnica;
    }

    public void setFichaTecnica(long idFichaTecnica, Produto produtoPrincipal, Boolean status) {
        this.fichaTecnica.setIdFichaTecnica(idFichaTecnica);
        this.fichaTecnica.setProdutoPrincipal(produtoPrincipal);
        this.fichaTecnica.setDataProducao(LocalDate.now());
        this.fichaTecnica.setCustoTotal(getCustoTotal());
        this.fichaTecnica.setListaProdutos(listaItensFicha);
        this.fichaTecnica.setStatus(status);
    }

    public ObservableList<Produto> getComboBoxItensFicha() {
        return comboBoxItensFicha;
    }

    public ObservableList<Produto> getComboBoxFichaTecnica() {
        return FXCollections.observableList(produtoDAO.listProdFichaTecnica());
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public ObservableList<ProdutoFichaTecnica> getListaItensFicha() {
        return listaItensFicha;
    }

    public void setListaItensFicha(ObservableList<ProdutoFichaTecnica> listaItensFicha) {
        this.listaItensFicha = listaItensFicha;
    }

    public Produto getProdutoPrincipal() {
        return produtoPrincipal;
    }

    public void setProdutoPrincipal(Produto produtoPrincipal) {
        this.produtoPrincipal = produtoPrincipal;
    }

    public BigDecimal getCustoTotal() {
        custoTotal = BigDecimal.ZERO;
        listaItensFicha.forEach((custUnit) -> {
            custoTotal = custoTotal.add(custUnit.getValorPorcao());
        });
        return custoTotal;
    }

    public int getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(int tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    void buscaCodRef(String text) {
        this.comboBoxItensFicha = FXCollections.observableList(produtoDAO.listProdInsumosCodRef(text));
    }

    void buscaInsumosDesc(String text) {
        this.comboBoxItensFicha = FXCollections.observableList(produtoDAO.listProdInsumosDesc(text));
    }

    void buscaInsumosTodos() {
        this.comboBoxItensFicha = FXCollections.observableList(produtoDAO.listProdInsumos());
    }

    
}
