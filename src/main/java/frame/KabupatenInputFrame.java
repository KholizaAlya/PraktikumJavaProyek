package frame;

import helpers.Koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KabupatenInputFrame extends JFrame{
    private JPanel buttonPanel;
    private JButton simpanButton;
    private JButton batalButton;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JPanel mainPanel;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public KabupatenInputFrame() {
        batalButton.addActionListener(e -> {
            dispose();
        });

        //simpan
        simpanButton.addActionListener(e -> {
                    String nama = namaTextField.getText();
                        if (nama.equals("")) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Isi Nama Kabupaten",
                                    "Validasi data kosong",
                                    JOptionPane.WARNING_MESSAGE
                            );
                            return;
                        }


                    Connection c = Koneksi.getConnection();
                    PreparedStatement ps;
                    try {
                        if(id == 0) {
                            String cekSQL = "SELECT * FROM kabupaten WHERE nama = ?";
                            ps = c.prepareStatement(cekSQL);
                            ps.setString(1, nama);
                            ResultSet rs = ps.executeQuery();
                            if (rs.next()) {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Data sama sudah ada"
                                );
                            } else {
                                String insertSQL = "INSERT INTO kabupaten (id, nama) VALUES (NULL, ?)";
                                ps = c.prepareStatement(insertSQL);
                                ps.setString(1, nama);
                                ps.executeUpdate();
                                dispose();
                            }
                    } else {
                            String cekSQL = "SELECT * FROM kabupaten WHERE nama = ? AND id != ?";
                            ps = c.prepareStatement(cekSQL);
                            ps.setString(1, nama);
                            ps.setInt(2, id);
                            ResultSet rs = ps.executeQuery();
                            if (rs.next()) {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Data sama sudah ada"
                                );
                            } else {
                                String updateSQL = "UPDATE kabupaten SET nama = ? WHERE id = ?";
                                ps = c.prepareStatement(updateSQL);
                                ps.setString(1, nama);
                                ps.setInt(2, id);
                                ps.executeUpdate();
                                dispose();
                            }
            }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });
        init();
    }


    public void init() {
        setContentPane(mainPanel);
        setTitle("Input Kabupaten");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiKomponen() {
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM kabupaten WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idTextField.setText(String.valueOf(rs.getInt("id")));
                namaTextField.setText(rs.getString("nama"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
