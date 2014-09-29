package com.github.abel533.easyxls.generater;

import com.github.abel533.easyxls.bean.Column;
import com.github.abel533.easyxls.bean.Columns;
import com.github.abel533.easyxls.bean.ExcelConfig;
import com.github.abel533.easyxls.common.XmlConfig;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class GenXml extends JFrame {
    private JPanel contentPane;
    private CardLayout card = null;
    private JPanel btnpanel;
    private JButton prevBtn;
    private JButton nextBtn;
    private JButton doneBtn;
    private JPanel pane;
    private JPanel step1;
    private JPanel step2;
    private JPanel step3;
    private JTextField classPath;
    private int step = 0;
    private JLabel label_1;
    private JTable table;
    private JButton delBtn;
    private JButton mvT;
    private JButton mvD;
    private JButton reset;
    private boolean hasRead = false;
    private JLabel label_2;
    private JLabel lblsheet;
    private JLabel label_3;
    private JLabel lblSheet;
    private JLabel label_4;
    private JCheckBox cache;
    private JTextField sheet;
    private JTextField sheetNum;
    private JTextField startRow;
    private JSlider sliderSheet;
    private JSlider sliderRow;
    private JPanel panel_1;
    private JPanel panel_2;
    private JScrollPane scrollPane_1;
    private String clasz;
    /**
     * 完成，保存xml
     */
    private ActionListener doneListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ExcelConfig dlExcel = new ExcelConfig();
            List<Column> columns = new ArrayList<Column>();
            dlExcel.setColumns(new Columns());
            dlExcel.getColumns().setColumns(columns);
            dlExcel.setClazz(clasz);
            dlExcel.setCache(cache.isSelected());
            dlExcel.setSheet(sheet.getText());
            dlExcel.setSheetNum(Integer.parseInt(sheetNum.getText()));
            dlExcel.setStartRow(Integer.parseInt(startRow.getText()));
            //列
            int rows = table.getRowCount();
            Column column = null;
            for (int i = 0; i < rows; i++) {
                column = new Column();
                column.setName(String.valueOf(table.getValueAt(i, 0)));
                column.setType(String.valueOf(table.getValueAt(i, 1)));
                column.setHeader(String.valueOf(table.getValueAt(i, 2)));
                columns.add(column);
            }
            XmlConfig config = new XmlConfig();
            //filepath
            String xmlPath = filePath.getText();
            String fileName = clasz.substring(clasz.lastIndexOf('.') + 1);
            String fullPath = xmlPath + "/" + fileName + ".xml";
            try {
                config.WriteXml(dlExcel, fullPath);
                if (JOptionPane.showConfirmDialog(GenXml.this, "保存完成，是否退出向导?", "成功", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            } catch (Exception e2) {
                JOptionPane.showMessageDialog(GenXml.this, "保存出错：" + e2.getMessage());
            }
        }
    };
    private JTextField filePath;

    /**
     * Create the frame.
     */
    public GenXml() {
        setResizable(false);
        setTitle("XML生成向导");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 670, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new LineBorder(Color.DARK_GRAY, 4));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        btnpanel = new JPanel();
        btnpanel.setBackground(Color.DARK_GRAY);
        btnpanel.setBorder(new LineBorder(Color.DARK_GRAY));
        btnpanel.setPreferredSize(new Dimension(10, 60));
        contentPane.add(btnpanel, BorderLayout.SOUTH);
        btnpanel.setLayout(null);

        prevBtn = new JButton("上一步");
        prevBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                prev();
            }
        });
        prevBtn.setEnabled(false);
        prevBtn.setBounds(294, 10, 113, 40);
        btnpanel.add(prevBtn);

        nextBtn = new JButton("下一步");
        nextBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                next();
            }
        });
        nextBtn.setBounds(417, 10, 113, 40);
        btnpanel.add(nextBtn);

        doneBtn = new JButton("完成");
        doneBtn.addActionListener(doneListener);
        doneBtn.setEnabled(false);
        doneBtn.setBounds(540, 10, 113, 40);
        btnpanel.add(doneBtn);

        card = new CardLayout(0, 0);
        pane = new JPanel(card);
        contentPane.add(pane, BorderLayout.CENTER);

        step1 = new JPanel();
        step1.setBackground(Color.LIGHT_GRAY);
        step1.setBorder(new LineBorder(new Color(0, 0, 0)));
        pane.add(step1, "name_287780550285180");
        step1.setLayout(null);

        JLabel label = new JLabel("请输入类名（全路径）：");
        label.setFont(new Font("宋体", Font.PLAIN, 18));
        label.setBounds(10, 42, 344, 43);
        step1.add(label);

        classPath = new JTextField();
        classPath.setForeground(Color.BLACK);
        classPath.setBackground(Color.WHITE);
        classPath.setFont(new Font("宋体", Font.BOLD, 20));
        classPath.setBounds(10, 95, 636, 52);
        step1.add(classPath);
        classPath.setColumns(10);

        JLabel lblxml = new JLabel("保存xml路径（全路径）：");
        lblxml.setFont(new Font("宋体", Font.PLAIN, 18));
        lblxml.setBounds(10, 218, 344, 43);
        step1.add(lblxml);

        filePath = new JTextField();
        //默认使用当前路径
        String path = GenXml.class.getResource("/").getPath();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        filePath.setText(path);
        filePath.setForeground(Color.BLACK);
        filePath.setFont(new Font("宋体", Font.BOLD, 20));
        filePath.setColumns(10);
        filePath.setBackground(Color.WHITE);
        filePath.setBounds(10, 271, 636, 52);
        step1.add(filePath);

        step2 = new JPanel();
        step2.setBackground(Color.LIGHT_GRAY);
        step2.setBorder(new LineBorder(new Color(0, 0, 0)));
        pane.add(step2, "name_287788958231594");
        step2.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        step2.add(scrollPane);

        table = new JTable();
        table.setRowHeight(30);
        table.setAutoCreateRowSorter(true);
        table.setDragEnabled(true);
        table.setFont(new Font("宋体", Font.PLAIN, 18));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setForeground(Color.BLACK);
        table.setBackground(Color.WHITE);
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
                "\u5B57\u6BB5\u540D", "\u7C7B\u578B",
                "\u5217\u540D"}));
        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        scrollPane.setViewportView(table);

        panel_1 = new JPanel();
        panel_1.setPreferredSize(new Dimension(10, 40));
        step2.add(panel_1, BorderLayout.NORTH);
        panel_1.setLayout(null);

        label_1 = new JLabel("需要显示的属性：");
        label_1.setBounds(10, 10, 157, 22);
        panel_1.add(label_1);

        panel_2 = new JPanel();
        panel_2.setPreferredSize(new Dimension(100, 10));
        step2.add(panel_2, BorderLayout.EAST);
        panel_2.setLayout(null);

        reset = new JButton("重置");
        reset.setBounds(3, 0, 93, 25);
        panel_2.add(reset);

        mvT = new JButton("向上");
        mvT.setBounds(3, 30, 93, 25);
        panel_2.add(mvT);

        mvD = new JButton("向下");
        mvD.setBounds(3, 60, 93, 25);
        panel_2.add(mvD);

        delBtn = new JButton("删除");
        delBtn.setBounds(3, 90, 93, 25);
        panel_2.add(delBtn);
        delBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 删除选中列
                DefaultTableModel tableModel = (DefaultTableModel) table
                        .getModel();
                int row = table.getSelectedRow();
                if (row > -1) {
                    tableModel.removeRow(row);
                    if (row < table.getRowCount()) {
                        table.getSelectionModel()
                                .setSelectionInterval(row, row);
                    } else if (row > 1) {
                        table.getSelectionModel()
                                .setSelectionInterval(row, row);
                    }
                }
            }
        });
        mvD.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 移动选中列
                DefaultTableModel tableModel = (DefaultTableModel) table
                        .getModel();
                int row = table.getSelectedRow();
                if (row < table.getRowCount()) {
                    tableModel.moveRow(row, row, ++row);
                    table.getSelectionModel().setSelectionInterval(row, row);
                }
            }
        });
        mvT.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 移动选中列
                DefaultTableModel tableModel = (DefaultTableModel) table
                        .getModel();
                int row = table.getSelectedRow();
                if (row > 0) {
                    tableModel.moveRow(row, row, --row);
                    table.getSelectionModel().setSelectionInterval(row, row);
                }
            }
        });
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReadInTable(classPath.getText());
            }
        });

        step3 = new JPanel();
        step3.setBackground(Color.LIGHT_GRAY);
        step3.setBorder(new LineBorder(new Color(0, 0, 0)));
        pane.add(step3, "name_287798491067114");
        step3.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(new LineBorder(new Color(0, 0, 0)));
        step3.add(panel);
        panel.setLayout(null);

        lblsheet = new JLabel("sheet序号：");
        lblsheet.setBounds(106, 226, 99, 15);
        panel.add(lblsheet);

        label_3 = new JLabel("开始行号：");
        label_3.setBounds(106, 291, 99, 24);
        panel.add(label_3);

        lblSheet = new JLabel("sheet名称：");
        lblSheet.setBounds(106, 261, 99, 15);
        panel.add(lblSheet);

        label_4 = new JLabel("是否缓存：");
        label_4.setBounds(106, 191, 99, 15);
        panel.add(label_4);

        cache = new JCheckBox("选中为缓存（不选为不缓存）");
        cache.setSelected(true);
        cache.setBounds(215, 184, 312, 30);
        panel.add(cache);

        sheet = new JTextField();
        sheet.setText("Sheet0");
        sheet.setColumns(10);
        sheet.setBounds(215, 254, 312, 30);
        panel.add(sheet);

        sheetNum = new JTextField();
        sheetNum.setHorizontalAlignment(SwingConstants.CENTER);
        sheetNum.setBackground(Color.DARK_GRAY);
        sheetNum.setForeground(Color.WHITE);
        sheetNum.setEnabled(false);
        sheetNum.setBounds(461, 219, 66, 30);
        panel.add(sheetNum);
        sheetNum.setColumns(10);

        startRow = new JTextField();
        startRow.setHorizontalAlignment(SwingConstants.CENTER);
        startRow.setForeground(Color.WHITE);
        startRow.setBackground(Color.DARK_GRAY);
        startRow.setEnabled(false);
        startRow.setColumns(10);
        startRow.setBounds(461, 289, 66, 30);
        panel.add(startRow);

        sliderRow = new JSlider();
        sliderRow.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                startRow.setText("" + sliderRow.getValue());
            }
        });
        sliderRow.setMaximum(10);
        sliderRow.setValue(0);
        sliderRow.setBounds(215, 289, 240, 30);
        panel.add(sliderRow);

        sliderSheet = new JSlider();
        sliderSheet.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                sheetNum.setText("" + sliderSheet.getValue());
            }
        });
        sliderSheet.setValue(0);
        sliderSheet.setMaximum(10);
        sliderSheet.setBounds(215, 219, 240, 30);
        panel.add(sliderSheet);

        label_2 = new JLabel("           基本信息：");
        label_2.setPreferredSize(new Dimension(60, 35));
        step3.add(label_2, BorderLayout.NORTH);
    }

    /**
     * 运行xml生成器
     */
    public static void run() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GenXml frame = new GenXml();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 处理
    private void next() {
        switch (step) {
            case 0:
                next0();
                break;
            case 1:
                next1();
                break;
            case 2:
                done();
                break;
            default:
                break;
        }
    }

    // 处理
    private void prev() {
        switch (step) {
            case 1:
                prev0();
                break;
            case 2:
                prev1();
                break;
            default:
                break;
        }
    }

    private void next0() {
        prevBtn.setEnabled(true);
        String clasz = classPath.getText();
        String xmlPath = filePath.getText();
        if (!clasz.equals("") && !xmlPath.equals("")) {
            try {
                Class.forName(clasz).newInstance();
                card.next(pane);
                prevBtn.setEnabled(true);
                if (!hasRead || !this.clasz.equals(clasz)) {
                    ReadInTable(clasz);
                }
                step++;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(GenXml.this, "类路径不存在，请重新输入!");
            }
        } else {
            JOptionPane.showMessageDialog(GenXml.this, "请输入路径!");
        }
    }

    private void next1() {
        // 显示xml基本信息
        card.next(pane);
        nextBtn.setEnabled(false);
        doneBtn.setEnabled(true);
        step++;
    }

    private void done() {

    }

    private void prev0() {
        prevBtn.setEnabled(false);
        doneBtn.setEnabled(false);
        card.previous(pane);
        step--;
    }

    private void prev1() {
        prevBtn.setEnabled(true);
        nextBtn.setEnabled(true);
        doneBtn.setEnabled(false);
        card.previous(pane);
        step--;
    }

    // 读入文件夹列表
    private void ReadInTable(String clasz) {
        this.clasz = clasz;
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        try {
            BeanInfo sourceBean = Introspector.getBeanInfo(Class.forName(clasz), Object.class);
            PropertyDescriptor[] ps = sourceBean.getPropertyDescriptors();
            for (int i = 0; i < ps.length; i++) {
                if (ps[i].getPropertyType().equals(Class.class)) {
                    continue;
                }
                tableModel.addRow(new Object[]{ps[i].getName(),
                        ps[i].getPropertyType().getName(), ps[i].getName()});
            }
            hasRead = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(GenXml.this, "发生错误:" + e.getMessage());
        }
    }
}
