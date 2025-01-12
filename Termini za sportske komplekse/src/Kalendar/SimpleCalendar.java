package Kalendar;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SimpleCalendar extends JFrame {
    private JLabel monthLabel;
    private JPanel daysPanel;
    private LocalDate currentDate;
    private JComboBox<String> sportComboBox;
    private JComboBox<String> kompleksComboBox;
    private JComboBox<String> hallComboBox;
    private JButton prevButton;
    private JButton nextButton;
    String[] months= {"Siječanj","Veljača","Ožujak","Travanj","Svibanj","Lipanj","Srpanj","Kolovoz","Rujan","Listopad","Studeni","Prosinac"};
    private String korisnickoIme;

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public SimpleCalendar(String korisnickoIme) {
    	this.korisnickoIme=korisnickoIme;
        currentDate = LocalDate.now();
        setTitle("Simple Calendar");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        monthLabel=new JLabel();
        monthLabel.setFont(new Font("Arial",Font.BOLD,24));
        JPanel gornjiPanel=new JPanel(new GridLayout(0,3, 20,0));
        gornjiPanel.setBorder(new EmptyBorder(5,10,10,10));
        gornjiPanel.add(new JLabel("Odaberi sport: "));
        gornjiPanel.add(new JLabel("Odaberi kompleks: "));
        gornjiPanel.add(new JLabel("Odaberi dvoranu: "));
        sportComboBox=new JComboBox<>();
        loadSports();
        sportComboBox.setFont(new Font("Arial",Font.BOLD,20));
        sportComboBox.setBackground(new Color(255,204,255));
        sportComboBox.addActionListener(e->{loadComplexes();
        loadHalls();
        });
        kompleksComboBox=new JComboBox<>();
        kompleksComboBox.setFont(new Font("Arial",Font.BOLD,20));
        kompleksComboBox.setBackground(new Color(255,204,255));
        loadComplexes();
        kompleksComboBox.addActionListener(e->loadHalls());
        hallComboBox=new JComboBox<>();
        hallComboBox.setFont(new Font("Arial",Font.BOLD,20));
        hallComboBox.setBackground(new Color(255,204,255));
        loadHalls();
        gornjiPanel.add(sportComboBox);
        gornjiPanel.add(kompleksComboBox);
        gornjiPanel.add(hallComboBox);
        gornjiPanel.add(monthLabel);
        gornjiPanel.setBackground(new Color(153,255,204));
        add(gornjiPanel, BorderLayout.NORTH);

        daysPanel = new JPanel(new GridLayout(0, 7,5,5));
        add(daysPanel, BorderLayout.CENTER);

        String[] weekDays = {"Pon", "Uto", "Sri", "Čet", "Pet", "Sub", "Ned"};
        for (String day : weekDays) {
            daysPanel.add(new JLabel(day, SwingConstants.CENTER));
        }

        JPanel navigationPanel = new JPanel();
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setBorder(new EmptyBorder(10,10,10,10));

        prevButton = new JButton("<");
        nextButton = new JButton(">");
        prevButton.setBackground(new Color(255,204,255));
        nextButton.setBackground(new Color(255,204,255));
        prevButton.setEnabled(false);
        prevButton.addActionListener(e -> changeMonth(-1));
        nextButton.addActionListener(e -> changeMonth(1));

        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(prevButton);
        navigationPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        navigationPanel.add(nextButton);
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.setBackground(new Color(153,255,204));

        add(navigationPanel, BorderLayout.SOUTH);

        updateCalendar();
        setLocationRelativeTo(null);
        System.out.println(java.sql.Date.valueOf(currentDate.withDayOfMonth(1))); 
    }

    private void changeMonth(int increment) {
    	LocalDate nextMonth=currentDate.plusMonths(1);
    	 if ((increment == 1 && currentDate.isBefore(nextMonth.plusMonths(1)))){
    		 currentDate = currentDate.plusMonths(increment);
    		 nextButton.setEnabled(false);
    		 prevButton.setEnabled(true);
    	}
    	if(increment == -1 && currentDate.isAfter(currentDate.minusMonths(2))){
    		currentDate = currentDate.plusMonths(increment);
    		prevButton.setEnabled(false);
    		nextButton.setEnabled(true);
    	}
    	updateCalendar(); 
    }

    private void updateCalendar() {
        monthLabel.setText(months[currentDate.getMonthValue()-1]+" "+String.valueOf(currentDate.getYear()));
        daysPanel.removeAll();
        daysPanel.setBackground(new Color(153,255,204));
        daysPanel.setBorder(new EmptyBorder(0,10,10,10));

        String[] weekDays = {"Pon", "Uto", "Sri", "Čet", "Pet", "Sub", "Ned"};
        for (String day : weekDays) {
        	JLabel dayname=new JLabel(day, SwingConstants.CENTER);
        	dayname.setFont(new Font("Arial",Font.PLAIN,24));
            daysPanel.add(dayname);
        }

        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();
        int daysInMonth = currentDate.lengthOfMonth();
        LocalDate currentDateCopy = LocalDate.now();

        for (int i = 1; i < dayOfWeek; i++) {
            daysPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= daysInMonth; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setFont(new Font("Arial",Font.PLAIN,24));
            dayButton.addActionListener(new DayButtonActionListener(day));

            LocalDate buttonDate = currentDate.withDayOfMonth(day);
            if (buttonDate.isBefore(currentDateCopy)) {
                dayButton.setEnabled(false);
                dayButton.setBackground(Color.LIGHT_GRAY);
            } else {
                dayButton.setBackground(new Color(255,204,255));
            }

            daysPanel.add(dayButton);
        }

        daysPanel.revalidate();
        daysPanel.repaint();
    }

    private class DayButtonActionListener implements ActionListener {
        private final int day;

        public DayButtonActionListener(int day) {
            this.day = day;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            showTimeSelection(day);
        }
    }

    private void showTimeSelection(int day) {
        JFrame timeFrame = new JFrame("Odabir termina za " + day + ". " + (months[currentDate.getMonthValue()-1])+ "; "+ (String)sportComboBox.getSelectedItem() +
        		" : "+(String)hallComboBox.getSelectedItem()+" : "+ (String)kompleksComboBox.getSelectedItem());
        timeFrame.setSize(600, 800);
        timeFrame.setLayout(new BorderLayout());
        timeFrame.setBackground(new Color(153,255,204));
        timeFrame.setLocationRelativeTo(null);
        
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new GridLayout(0, 3, 30, 5));
        timePanel.setBackground(new Color(153,255,204));
        timePanel.setBorder(new EmptyBorder(5, 5, 5, 45));

        timePanel.add(new JLabel("Vrijeme", SwingConstants.CENTER));
        timePanel.add(new JLabel("Klijent", SwingConstants.CENTER));
        timePanel.add(new JLabel("Status", SwingConstants.CENTER));

        LocalDateTime currentTime = LocalDateTime.now();
        int currentHour = currentTime.getHour();

        for (int hour = 7; hour <= 21; hour++) {
            String timeSlot = String.format("%02d:00 - %02d:00", hour, hour + 1);

            timePanel.add(new JLabel(timeSlot, SwingConstants.CENTER));

            JButton clientField = new JButton("Rezerviraj termin");
            JTextField statusField = new JTextField();
            statusField.setEnabled(false);
            clientField.setBackground(new Color(255, 255, 255));
            clientField.setEnabled(true);
            statusField.setBackground(Color.GREEN);

            if (day==currentDate.getDayOfMonth() && hour < currentHour+1) {
                clientField.setEnabled(false);
                clientField.setBackground(Color.LIGHT_GRAY);
                statusField.setBackground(Color.red);
            }

            timePanel.add(clientField);
            timePanel.add(statusField);

            clientField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int response = JOptionPane.showConfirmDialog(
                            timeFrame,
                            "Jeste li sigurni da želite rezervirati ovaj termin?",
                            "Potvrda rezervacije",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (response == JOptionPane.YES_OPTION) {
                        clientField.setText(korisnickoIme);
                        clientField.setEnabled(false);
                        statusField.setBackground(Color.RED);
                        saveAppointments(day, timePanel, clientField, timeSlot);
                    } else {
                        clientField.setText("Rezerviraj termin");
                    }
                }
            });

        }

        timeFrame.add(timePanel, BorderLayout.CENTER);
        timeFrame.revalidate();
        timeFrame.repaint();
        timeFrame.setVisible(true);
        loadAppointments(day, timePanel);
    }

    private void loadSports() {
    	try(Connection conn=DBConnection.getConnection()){
    		String sql="SELECT sport_name FROM sports";
    		try(PreparedStatement pstmt=conn.prepareStatement(sql)){
    			ResultSet rs=pstmt.executeQuery();
    			DefaultComboBoxModel<String> model=new DefaultComboBoxModel();
    			while(rs.next()) {
    				model.addElement(rs.getString("sport_name"));
    			}
    			sportComboBox.setModel(model);
    		}
    	}catch(SQLException ex) {
    		JOptionPane.showMessageDialog(this, "Došlo je do greške prilikom učitavanja sportova: "+ex.getMessage());
    	}
    }
    
    private void loadComplexes() {
    	try(Connection conn=DBConnection.getConnection()){
    		String sql="SELECT DISTINCT complex_name FROM complexes c JOIN sport_complex_hall scs ON scs.complex_id=c.complex_id "
    	+"JOIN sports s ON scs.sport_id=s.sport_id WHERE s.sport_name=?";
    		try(PreparedStatement pstmt=conn.prepareStatement(sql)){
   			pstmt.setString(1, (String)sportComboBox.getSelectedItem());
    			ResultSet rs=pstmt.executeQuery();
    			DefaultComboBoxModel<String> model=new DefaultComboBoxModel();
    			
    			while(rs.next()) {
    				model.addElement(rs.getString("complex_name"));
    			}
    			kompleksComboBox.setModel(model);
    		}
    	}catch(SQLException ex) {
    		JOptionPane.showMessageDialog(this, "Došlo je do greške prilikom učitavanja kompleksa "+ ex.getMessage());
    	}
    }
    
    private void loadHalls() {
    	try(Connection conn=DBConnection.getConnection()){
    		String sql="SELECT hall_name FROM halls h JOIN sport_complex_hall scs ON scs.hall_id=h.hall_id "+
    	"JOIN sports s ON scs.sport_id=s.sport_id JOIN complexes c ON scs.complex_id=c.complex_id "+
    	"WHERE c.complex_name=? AND s.sport_name=?";
    	try(PreparedStatement pstmt=conn.prepareStatement(sql)){
    		pstmt.setString(1, (String)kompleksComboBox.getSelectedItem());
    		pstmt.setString(2, (String)sportComboBox.getSelectedItem());
    		ResultSet rs=pstmt.executeQuery();
    		DefaultComboBoxModel<String> model=new DefaultComboBoxModel();
    		while(rs.next()) {
    			model.addElement(rs.getString("hall_name"));
    		}
    		hallComboBox.setModel(model);
    	}
    }catch(SQLException ex) {
    	JOptionPane.showMessageDialog(this, "Došlo je do greške prilikom učitavanja dvorana "+ ex.getMessage());
    }
}

    private void loadAppointments(int day, JPanel timePanel) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT a.time_slot, a.client_name, s.sport_name " +
                         "FROM appointments a " +
                         "JOIN halls h ON h.hall_id = a.hall_id " +
                         "JOIN sports s ON s.sport_id = a.sport_id " +
                         "JOIN complexes c ON c.complex_id = a.complex_id " +
                         "WHERE a.date = ? AND c.complex_name = ? AND h.hall_name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDate(1, java.sql.Date.valueOf(currentDate.withDayOfMonth(day)));
                pstmt.setString(2, (String)kompleksComboBox.getSelectedItem());
                pstmt.setString(3, (String)hallComboBox.getSelectedItem());
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    String timeSlot = rs.getString("time_slot");
                    String clientName = rs.getString("client_name");
                    for (Component comp : timePanel.getComponents()) {
                        if (comp instanceof JButton) {
                            JButton button = (JButton) comp;
                            if (button.getText().contains(timeSlot)) {
                                button.setText(clientName);
                                button.setEnabled(false);
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Došlo je do greške prilikom učitavanja termina: " + ex.getMessage());
        }
    }

    private void saveAppointments(int day, JPanel timePanel, JButton clientField, String timeSlot) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO appointments (date, time_slot, client_name, sport_id, hall_id, complex_id) " +
                         "SELECT ?, ?, ?, s.sport_id, h.hall_id, c.complex_id " +
                         "FROM sports s " +
                         "JOIN halls h ON h.hall_id = ? " +
                         "JOIN complexes c ON c.complex_id = ? " +
                         "WHERE s.sport_name = ? AND h.hall_name = ? AND c.complex_name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDate(1, java.sql.Date.valueOf(currentDate.withDayOfMonth(day)));
                pstmt.setString(2, timeSlot);
                pstmt.setString(3, korisnickoIme);
                pstmt.setInt(4, Integer.parseInt(clientField.getText()));
                pstmt.setString(5, (String)sportComboBox.getSelectedItem());
                pstmt.setString(6, (String)hallComboBox.getSelectedItem());
                pstmt.setString(7, (String)kompleksComboBox.getSelectedItem());
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Došlo je do greške prilikom spremanja termina: " + ex.getMessage());
        }
    }


    public static void main(String[] args) { 
    	SwingUtilities.invokeLater(()->{
			Login login=new Login();
			login.pack();
			login.setVisible(true);
			});
    }
}

