package Kalendar;
import javax.swing.*; // Uvoz Swing biblioteke za GUI
import javax.swing.border.EmptyBorder;

import java.awt.*; // Uvoz AWT biblioteke za raspored i boje
import java.awt.event.ActionEvent; // Uvoz za rad s događajima akcija
import java.awt.event.ActionListener; // Uvoz za slušače akcija
import java.awt.event.KeyAdapter; // Uvoz za prilagodbu tipkovnice
import java.awt.event.KeyEvent; // Uvoz za događaje tipkovnice
import java.sql.*;
import java.time.LocalDate; // Uvoz za rad s datumima
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SimpleCalendar extends JFrame { // Definicija klase koja nasljeđuje JFrame
    private JLabel monthLabel; // Label za prikaz mjeseca
    private JPanel daysPanel; // Panel za dane u mjesecu
    private LocalDate currentDate; // Trenutni datum 
    private JComboBox<String> sportComboBox;	//Dropdown lista za sport
    private JComboBox<String> kompleksComboBox; //Dropdown lista za kompleks
    private JComboBox<String> hallComboBox; //Dropdown lista za dvorane
    private JButton prevButton; //Button za mjesec prije
    private JButton nextButton; //Button za mjesec poslije
    String[] months= {"Siječanj","Veljača","Ožujak","Travanj","Svibanj","Lipanj","Srpanj","Kolovoz","Rujan","Listopad","Studeni","Prosinac"}; //Mjeseci na hrvatskom jeziku
    private String korisnickoIme; //Korisničko ime prosljeđeno od logina

    public SimpleCalendar(String korisnickoIme) { // Konstruktor glavne klase
    	this.korisnickoIme=korisnickoIme;
        currentDate = LocalDate.now(); // Postavlja trenutni datum
        setTitle("Kalendar"); // Postavlja naslov prozora
        setSize(800, 800); // Postavlja dimenzije prozora
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Postavlja zatvaranje aplikacije
        setLayout(new BorderLayout()); // Postavlja raspored prozora
        monthLabel=new JLabel(); //Pravi label za naziv mjeseca
        monthLabel.setFont(new Font("Arial",Font.BOLD,24)); //Postavlja font labela mjeseca
        JPanel gornjiPanel=new JPanel(new GridLayout(0,3, 20,0)); //Pravi grid za dropdown liste
        gornjiPanel.setBorder(new EmptyBorder(5,10,10,10)); //Odmiče od ruba prozora
        gornjiPanel.add(new JLabel("Odaberi sport: ")); //Dodaje label poviše sport dropdown liste
        gornjiPanel.add(new JLabel("Odaberi kompleks: "));//Dodaje label poviše kompleks dropdown liste
        gornjiPanel.add(new JLabel("Odaberi dvoranu: "));//Dodaje label poviše dvorane dropdown liste
        sportComboBox=new JComboBox<>(); //pravi dropdown listu
        loadSports(); //učitava sportove iz baze u sport dropdown listu
        sportComboBox.setFont(new Font("Arial",Font.BOLD,20)); //postavlja font sportdropdown liste
        sportComboBox.setBackground(new Color(255,204,255)); //postavlja boju sport dropdown liste
        sportComboBox.addActionListener(e->{loadComplexes(); //odabirom sporta mjenjaju se kompleksi (promjenom sporta poziva se funkcija loadComplexes())
        loadHalls(); //učitava dvorane iz baze u dvorane dropdown listu
        });
        kompleksComboBox=new JComboBox<>();
        kompleksComboBox.setFont(new Font("Arial",Font.BOLD,20));
        kompleksComboBox.setBackground(new Color(255,204,255));
        loadComplexes(); //ovo je za početni zaslon kad se još nije odabrao sport, automatski se loadaju svi kompleksi 
        kompleksComboBox.addActionListener(e->loadHalls()); //ako smo promjenili kompleks mjenjaju se dvorane koje su u tom kompleksu
        hallComboBox=new JComboBox<>();
        hallComboBox.setFont(new Font("Arial",Font.BOLD,20));
        hallComboBox.setBackground(new Color(255,204,255));
        loadHalls();
        gornjiPanel.add(sportComboBox); //dodavanje elemenata na gornji panel
        gornjiPanel.add(kompleksComboBox);
        gornjiPanel.add(hallComboBox);
        gornjiPanel.add(monthLabel);
        gornjiPanel.setBackground(new Color(153,255,204));
        
        

       
        add(gornjiPanel, BorderLayout.NORTH);  // Dodaje panel na vrh kalendara

        daysPanel = new JPanel(new GridLayout(0, 7,5,5)); // Panel za dane s mrežnim rasporedom
        add(daysPanel, BorderLayout.CENTER); // Dodaje panel u središte

        // Dodaj red s danima u tjednu
        String[] weekDays = {"Pon", "Uto", "Sri", "Čet", "Pet", "Sub", "Ned"}; // Dani u tjednu
        for (String day : weekDays) {
            daysPanel.add(new JLabel(day, SwingConstants.CENTER)); // Dodaje dane u grid tako da su u sredini kućice
        }

        JPanel navigationPanel = new JPanel(); // Panel za navigaciju mjeseci
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS)); // Raspored u horizontalnom smjeru
        navigationPanel.setBorder(new EmptyBorder(10,10,10,10)); //odmiče od ruba prozora

        prevButton = new JButton("<"); // Gumb za prethodni mjesec
        nextButton = new JButton(">"); // Gumb za sljedeći mjesec
        prevButton.setBackground(new Color(255,204,255));
        nextButton.setBackground(new Color(255,204,255));
        prevButton.setEnabled(false);
        prevButton.addActionListener(e -> changeMonth(-1)); // Dodaje slušača za prethodni mjesec
        nextButton.addActionListener(e -> changeMonth(1)); // Dodaje slušača za sljedeći mjesec

        navigationPanel.add(Box.createHorizontalGlue()); // Dodaje razmak
        navigationPanel.add(prevButton); // Dodaje gumb za prethodni mjesec
        navigationPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Razmak između gumba
        navigationPanel.add(nextButton); // Dodaje gumb za sljedeći mjesec
        navigationPanel.add(Box.createHorizontalGlue()); // Dodaje razmak
        navigationPanel.setBackground(new Color(153,255,204));

        add(navigationPanel, BorderLayout.SOUTH); // Dodaje navigacijski panel na dno
        

        updateCalendar(); // Ažurira prikaz kalendara
        setLocationRelativeTo(null); // Centriraj prozor na ekranu
        System.out.println(java.sql.Date.valueOf(currentDate.withDayOfMonth(1))); 
    }


    
    private void changeMonth(int increment) { // Metoda za promjenu mjeseca
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

    private void updateCalendar() { // Metoda za ažuriranje kalendara

        monthLabel.setText(months[currentDate.getMonthValue()-1]+" "+String.valueOf(currentDate.getYear())); // Ažurira labelu s mjesecom i godinom
        daysPanel.removeAll(); // Uklanja sve prethodne dane iz panela
        daysPanel.setBackground(new Color(153,255,204));
        daysPanel.setBorder(new EmptyBorder(0,10,10,10));
        
        // Dodaj red s danima u tjednu
        String[] weekDays = {"Pon", "Uto", "Sri", "Čet", "Pet", "Sub", "Ned"}; // Dani u tjednu
        for (String day : weekDays) {
            daysPanel.add(new JLabel(day, SwingConstants.CENTER)); // Dodaje oznake za dane
        }

        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1); // Prvi dan trenutnog mjeseca
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // Dan u tjednu prvog dana
        int daysInMonth = currentDate.lengthOfMonth(); // Ukupan broj dana u mjesecu
        LocalDate currentDateCopy = LocalDate.now(); // Trenutni datum

        for (int i = 1; i < dayOfWeek; i++) {
            daysPanel.add(new JLabel("")); // Dodaje prazne oznake za dane koji prethode prvom danu
        }

        for (int day = 1; day <= daysInMonth; day++) {
            JButton dayButton = new JButton(String.valueOf(day)); // Gumb za svaki dan
            dayButton.addActionListener(new DayButtonActionListener(day)); // Dodaje slušača za gumb dana

            // Onemogućiti gumbe za prošle dane
            LocalDate buttonDate = currentDate.withDayOfMonth(day); // Kreira datum za gumb
            if (buttonDate.isBefore(currentDateCopy)) {
                dayButton.setEnabled(false); // Onemogućava gumb ako je prošli dan
                dayButton.setBackground(Color.LIGHT_GRAY); // Promijeniti boju gumba za prošli dan
            } else {
                dayButton.setBackground(new Color(255,204,255)); // Postavlja boju gumba za aktualni i budući dan
            }
            
            daysPanel.add(dayButton); // Dodaje gumb u panel
        }

        daysPanel.revalidate(); // Ponovno učitava panel
        daysPanel.repaint(); // Osvježava panel
    }


    private class DayButtonActionListener implements ActionListener { // Unutarnja klasa za upravljanje događajem gumba dana
        private final int day; // Privatna varijabla za pohranu dana

        public DayButtonActionListener(int day) { // Konstruktor
        	
            this.day = day; // Postavlja dan
        }

        @Override
        public void actionPerformed(ActionEvent e) {// Kada se pritisne gumb
            showTimeSelection(day); // Poziva metodu za odabir vremena
        }
    }

    private void showTimeSelection(int day) {
        JFrame timeFrame = new JFrame("Odabir termina za " + day + ". " + (months[currentDate.getMonthValue()-1])+", "+(String)kompleksComboBox.getSelectedItem()+", "+(String)hallComboBox.getSelectedItem());
        timeFrame.setSize(600, 800);
        timeFrame.setLayout(new BorderLayout());
        timeFrame.setBackground(new Color(153,255,204));
        timeFrame.setLocationRelativeTo(null);
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new GridLayout(0, 3, 30, 5));
        timePanel.setBackground(new Color(153,255,204));
        timePanel.setBorder(new EmptyBorder(5, 5, 5, 45));

        // Dodajemo oznake za vrijeme, klijenta i status
        timePanel.add(new JLabel("Vrijeme", SwingConstants.CENTER));
        timePanel.add(new JLabel("Klijent", SwingConstants.CENTER));
        timePanel.add(new JLabel("Status", SwingConstants.CENTER));

        LocalDateTime currentTime = LocalDateTime.now(); // Trenutni datum i vrijeme
        int currentHour = currentTime.getHour(); // Trenutni sat
        
        ArrayList<String> timeSlots=new ArrayList<>();
        
        // Petlja kroz sate
        for (int hour = 7; hour <= 21; hour++) {
            String timeSlot = String.format("%02d:00 - %02d:00", hour, hour + 1);

            timePanel.add(new JLabel(timeSlot, SwingConstants.CENTER)); // Dodajemo slot vremena

            JButton clientField = new JButton("Rezerviraj termin");
            JTextField statusField = new JTextField();
            statusField.setEnabled(false); // Status nije uređiv
            clientField.setBackground(new Color(255, 255, 255));
            clientField.setEnabled(true);
            statusField.setBackground(Color.GREEN);

            // Onemogući gumbe za prošle sate
            if (day == currentDate.getDayOfMonth() && hour < currentHour + 1) {
                clientField.setEnabled(false); // Onemogućiti gumb ako je sat prošao
                clientField.setBackground(Color.LIGHT_GRAY); // Promjena boje za prošle sate
                statusField.setBackground(Color.red);
            }

            timePanel.add(clientField); // Dodajemo polje za klijenta
            timePanel.add(statusField); // Dodajemo polje za status
            
            clientField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	if(clientField.getBackground()!=Color.gray) {
                	timeSlots.add(timeSlot);
                	clientField.setBackground(Color.gray);
                	statusField.setBackground(Color.gray);}
                	else {
                	timeSlots.remove(timeSlot);
                	clientField.setBackground(Color.white);
                	statusField.setBackground(Color.green);
                	}
                }
            });

        }

        // Dodavanje 'Potvrdi' gumba na dno prozora
        JButton confirmButton = new JButton("Potvrdi");
        confirmButton.setPreferredSize(new Dimension(200, 40)); // Postavljanje veličine gumba
        confirmButton.setFont(new Font("Arial", Font.BOLD, 16)); // Promjena fonta za gumb
        
        // ActionListener za 'Potvrdi' gumb
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(timeSlots.isEmpty()) {
            		JOptionPane.showMessageDialog(timePanel, "Niste odabrali nijedan termin!","Obavijest",JOptionPane.INFORMATION_MESSAGE);  
            	}
            	else {
            		UIManager.put("OptionPane.yesButtonText", "Da");
            		UIManager.put("OptionPane.noButtonText", "Ne");
                int response = JOptionPane.showConfirmDialog(
                        timeFrame, // Prozor na kojem se dijalog prikazuje
                        "Jeste li sigurni da želite rezervirati ove termine?", // Poruka dijaloga
                        "Potvrda rezervacije", // Naslov dijaloga
                        JOptionPane.YES_NO_OPTION, // Opcije za Da/Ne
                        JOptionPane.QUESTION_MESSAGE // Tip dijaloga (pitajte)
                );

                // Provjerava odgovor korisnika
                if (response == JOptionPane.YES_OPTION) {
                    for (String timeSlot : timeSlots) {
						saveAppointments(day,timeSlot);
				}
                    JOptionPane.showMessageDialog(timePanel, "Podaci su spremljeni!","Obavijest",JOptionPane.INFORMATION_MESSAGE);
                    timeFrame.dispose();
                    showTimeSelection(day);    
                }
            }}
        });
    
        // Kreiramo panel za dno prozora, gdje ćemo smjestiti gumb
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(confirmButton); // Dodajemo gumb na panel

        timeFrame.add(timePanel, BorderLayout.CENTER); // Dodavanje timePanel na prozor
        timeFrame.add(bottomPanel, BorderLayout.SOUTH); // Dodavanje bottomPanel sa 'Potvrdi' gumbom
        timeFrame.revalidate(); // Osvježavanje prozora
        timeFrame.repaint(); // Prikazivanje promjena
        timeFrame.setVisible(true); // Vidljivost prozora
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
    		JOptionPane.showMessageDialog(this, "Došlo je do greške prilikom učitavanja sportova: "+ex.getMessage(),"Greška",JOptionPane.ERROR_MESSAGE);
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
    		JOptionPane.showMessageDialog(this, "Došlo je do greške prilikom učitavanja kompleksa "+ ex.getMessage(),"Greška",JOptionPane.ERROR_MESSAGE);
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
    	JOptionPane.showMessageDialog(this, "Došlo je do greške prilikom učitavanja dvorana "+ ex.getMessage(),"Greška",JOptionPane.ERROR_MESSAGE);
    }
}


    private void loadAppointments(int day, JPanel timePanel) {
        try (Connection conn = DBConnection.getConnection()) { // Otvara vezu s bazom
            String sql = "SELECT a.time_slot, a.client_name, s.sport_name " +
                         "FROM appointments a " +
                         "JOIN halls h ON h.hall_id = a.hall_id " +
                         "JOIN sports s ON s.sport_id = a.sport_id " +
                         "JOIN complexes c ON c.complex_id = a.complex_id " +
                         "WHERE a.date = ? AND c.complex_name = ? AND h.hall_name = ?"; // SQL upit
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) { // Priprema SQL upit
                pstmt.setDate(1, java.sql.Date.valueOf(currentDate.withDayOfMonth(day))); // Postavlja datum
                pstmt.setString(2, (String)kompleksComboBox.getSelectedItem());
                pstmt.setString(3, (String)hallComboBox.getSelectedItem());// Filtrira prema dvorani
                ResultSet rs = pstmt.executeQuery(); // Izvršava upit
                while (rs.next()) { // Petlja kroz rezultate
                    String timeSlot = rs.getString("time_slot"); // Dohvaća vremenski slot
                    String client = rs.getString("client_name"); // Dohvaća ime klijenta
                    String sport = rs.getString("sport_name"); // Dohvaća sport
                    System.out.println("Loaded appointment: " + timeSlot + ", " + client + ", " + sport); // Logiranje

                    // Petlja kroz sate uključujući 21:00 - 22:00
                    for (int hour = 7; hour <= 21; hour++) { // Petlja od 7 do 21
                        String slot = String.format("%02d:00 - %02d:00", hour, hour + 1); // Formatira vremenski slot

                        if (timeSlot.equals(slot)) { // Ako se vremenski slot poklapa
                            int index = (hour - 6) * 3 + 1; // Izračunava indeks za polje klijenta

                            if (timePanel.getComponent(index) instanceof JButton) { // Ako je komponenta tekstualno polje
                                JButton clientField = (JButton) timePanel.getComponent(index); // Dohvaća polje klijenta
                                clientField.setText(client); // Postavlja ime klijenta
                                clientField.setEnabled(false); // Onemogućava gumb

                                JTextField statusField = (JTextField) timePanel.getComponent(index + 1); // Dohvaća polje statusa
                                if (!clientField.getText().equals("Rezerviraj termin")) {
                                    statusField.setBackground(Color.RED); // Ako klijent nije prazan, postavlja crvenu pozadinu
                                    statusField.setText(sport);
                                } else {
                                    statusField.setBackground(Color.GREEN); // Inače, postavlja zelenu pozadinu
                                }
                            }
                        }
                    }
                }
            }

            // Osvježi prikaz nakon učitavanja podataka
            timePanel.revalidate(); 
            timePanel.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ispisuje greške u konzolu
            JOptionPane.showMessageDialog(this, "Došlo je do greške prilikom učitavanja podataka: " + ex.getMessage(),"Greška",JOptionPane.ERROR_MESSAGE); // Obavještava korisnika o grešci
        }
    }



    private void saveAppointments(int day, String Timeslot) { // Metoda za spremanje termina
        try (Connection conn = DBConnection.getConnection()) { // Otvara vezu s bazom
            String selectedSport = (String) sportComboBox.getSelectedItem(); // Uzimanje trenutne dvorane
            String selectedKompleks = (String) kompleksComboBox.getSelectedItem();
            String selectedHall = (String) hallComboBox.getSelectedItem();
            
            // Dobijemo koji sat je odabrano za clickedButton
            String timeSlot = Timeslot; // Gumb ima tekst u formatu "HH:00 - HH:00"

                String client = korisnickoIme; 

                String sql = "INSERT INTO appointments (sport_id, complex_id, hall_id, client_name, date, time_slot) " +
                             "VALUES ((SELECT sport_id FROM sports WHERE sport_name LIKE ? LIMIT 1), " +
                             "(SELECT complex_id FROM complexes WHERE complex_name=? LIMIT 1), " +
                             "(SELECT hall_id FROM halls WHERE hall_name=? LIMIT 1), ?, ?, ?)"; // SQL upit za umetanje termina
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) { // Pripremamo SQL upit
                    pstmt.setDate(5, java.sql.Date.valueOf(currentDate.withDayOfMonth(day))); // Postavljamo datum
                    pstmt.setString(6, timeSlot); // Postavljamo vremenski slot
                    pstmt.setString(4, client); // Postavljamo ime klijenta
                    pstmt.setString(1, selectedSport); // Dvorana
                    pstmt.setString(2, selectedKompleks); // Kompleks
                    pstmt.setString(3, selectedHall); // Dvorana
                    pstmt.executeUpdate(); // Izvršava upit
                }
            

            
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ispisuje greške u konzolu
            JOptionPane.showMessageDialog(this, "Došlo je do greške prilikom spremanja podataka: " + ex.getMessage(),"Greška",JOptionPane.ERROR_MESSAGE); // Obavještava korisnika o grešci
        }
    }




    public static void main(String[] args) { // Glavna metoda
    	SwingUtilities.invokeLater(()->{
			Login login=new Login();
			if(login.testDBConnection()) {
			login.pack();
			login.setVisible(true);}
			});
    }
}

