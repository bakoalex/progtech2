/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.progtech2.frontend.components;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import hu.elte.progtech2.frontend.GuiManager;
import hu.elte.progtech2.frontend.components.factory.SwingComponentFactory;
import hu.elte.progtech2.frontend.enums.Action;
import hu.elte.progtech2.frontend.windows.DashboardWindow;

/**
 * Főpanel
 *
 * @author <Andó Sándor Zsolt>
 */
public class DashboardPanel extends JPanel {

    private JButton listOrdersButton, listProductsButton, listRetailersButton;
    private JTable resultTable;
    private JPanel buttonPanel, tablePanel;
    private final DashboardWindow window;
    private Action state;

    public DashboardPanel(DashboardWindow frame) {
        this.window = frame;
        initDashBoardPanel();
        initButtons();
    }

    private void initDashBoardPanel() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        buttonPanel = new JPanel(new FlowLayout());
        tablePanel = new JPanel(new FlowLayout());

        resultTable = new JTable(5, 5);
        
        /**
         * this::newSelection => method reference
         * https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html
         *
         * https://stackoverflow.com/questions/41069817/making-an-action-listener-for-a-jbutton-as-a-method
         *
         */
        resultTable.getSelectionModel().addListSelectionListener(this::newSelection);
        tablePanel.add(new JScrollPane(resultTable));
        add(buttonPanel, gbc);
        add(tablePanel, gbc);
    }

    private void initButtons() {
        listOrdersButton = SwingComponentFactory.generateButton(buttonPanel, "Rendelések");
        
        listOrdersButton.addActionListener(this::doListOrders);

        listProductsButton = SwingComponentFactory.generateButton(buttonPanel, "Termékek");
        listProductsButton.addActionListener(this::doListProducts);

        listRetailersButton = SwingComponentFactory.generateButton(buttonPanel, "Kiskereskedők");
        listRetailersButton.addActionListener(this::doListRetailers);
    }

    private void doListOrders(ActionEvent e) {
        this.setState(Action.ORDER);
        window.doListOrders();
    }

    private void doListProducts(ActionEvent e) {
        this.setState(Action.PRODUCT);
        window.doListProducts();
    }

    private void doListRetailers(ActionEvent e) {
        this.setState(Action.RETAILER);
        window.doListRetailers();
    }

    private void setState(Action state) {
        this.state = state;
    }

    private void newSelection(ListSelectionEvent event) {
        if (event.getValueIsAdjusting() && resultTable.getSelectedRow() > -1) {
            if (state.equals(Action.ORDER)) {
                window.listOrderLines(resultTable.getValueAt(resultTable.getSelectedRow(), 0));
            } else if (state.equals(Action.PRODUCT)) {
                int row = resultTable.getSelectedRow();
                GuiManager.showProductWindow(resultTable.getValueAt(row, 0), resultTable.getValueAt(row, 1), resultTable.getValueAt(row, 2));
            } else if (state.equals(Action.RETAILER)) {
                int row = resultTable.getSelectedRow();
                GuiManager.showRetailerWindow(resultTable.getValueAt(row, 0), resultTable.getValueAt(row, 1), resultTable.getValueAt(row, 2), resultTable.getValueAt(row, 3));
            }
        }
    }

    public <E> void addContentToTable(List<E> content, Object[] columnNames) {
        resultTable.removeAll();
        DefaultTableModel dtm = new DefaultTableModel(columnNames, 0);
        content.forEach(row -> dtm.addRow((Object[]) row));
        resultTable.setModel(dtm);
    }

    public String getSelectedRowIndex() {
        return (String) resultTable.getValueAt(resultTable.getSelectedRow(), 0);
    }

}
