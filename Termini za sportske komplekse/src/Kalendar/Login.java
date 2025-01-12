package Kalendar;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
public class Login extends JFrame{
	private JTextField korisnik;
	private JPasswordField password;
	public Login() {
		setTitle("Login");
		setSize(300,300);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel mainPanel=new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.X_AXIS));
		mainPanel.setBackground(new Color(153,255,204));
		mainPanel.setBorder(new EmptyBorder(10,10,10,10));
		JPanel labelPanel=new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel,BoxLayout.Y_AXIS));
		labelPanel.setBackground(new Color(153,255,204));
		JPanel textPanel=new JPanel();
		textPanel.setBackground(new Color(153,255,204));
		textPanel.setLayout(new BoxLayout(textPanel,BoxLayout.Y_AXIS));
		JLabel labelKorisnik=new JLabel("Korisnik: ");
		JLabel labelPassword=new JLabel("Password: ");
		korisnik=new JTextField(10);
		password=new JPasswordField(10);
		labelPanel.add(labelKorisnik);
		labelPanel.add(Box.createRigidArea(new Dimension(0,1)));
		labelPanel.add(labelPassword);
		textPanel.add(korisnik);
		textPanel.add(Box.createRigidArea(new Dimension(0,1)));
		textPanel.add(password);
		add(mainPanel);
		mainPanel.add(labelPanel);
		mainPanel.add(textPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(5,0)));
		JButton logintipka=new JButton("Login");
		logintipka.setBackground(new Color(255,204,255));
		mainPanel.add(logintipka);
		setLocationRelativeTo(null);
		
		logintipka.addActionListener(e->{
			if(Provjera(getKorisnik(),getPassword())) {
			dispose();
			SimpleCalendar calendar=new SimpleCalendar(getKorisnik());
			calendar.setVisible(true);}
		});
		password.addActionListener(e -> {
		    if (Provjera(getKorisnik(), getPassword())) {
		        dispose();
		        SimpleCalendar calendar = new SimpleCalendar(getKorisnik());
		        calendar.setVisible(true);
		    }
		});
	}
	
	public String getKorisnik(){
		return korisnik.getText();
	}
	public String getPassword() {
		return password.getText();
	}
	public boolean Provjera(String korisnik,String password) {
		String sql="SELECT * FROM client WHERE client_name=? and client_password=?;";
		try(Connection conn = DBConnection.getConnection();
			PreparedStatement stmt=conn.prepareStatement(sql)){
			
			stmt.setString(1, korisnik);
			stmt.setString(2, password);
			
			try(ResultSet rs=stmt.executeQuery()){
				if(rs.next()) {
					return true;
				}
			else {
				JOptionPane.showMessageDialog(this, "Krivi korisnik ili lozinka");
				return false;
			}
		}
	}
	catch(SQLException e){
		e.printStackTrace();
		JOptionPane.showMessageDialog(this, "Greška u povezivanju s bazom");
		return false;
		}
	
	}
}