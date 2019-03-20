import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
class View extends JFrame{
	Model m;
	
	JPanel backPanel = new JPanel();
	JPanel northPanel = new JPanel();
		JPanel titlePanel = new JPanel();
		JPanel comboBoxPanel = new JPanel();
		JPanel popOutDelPanel = new JPanel();
		JPanel searchPanel = new JPanel();
		JPanel searchResultPanel = new JPanel();
	JPanel westPanel = new JPanel();
		JPanel firstLastEmailPanel = new JPanel();
			JPanel fNamePanel = new JPanel();
			JPanel lNamePanel = new JPanel();
			JPanel emailPanel = new JPanel();
			JPanel imageChooserPanel = new JPanel();
	JPanel eastPanel = new JPanel();
	JPanel southPanel = new JPanel();
	
	JButton imageUploaderBtn = new JButton("Choose Image");
	JButton addBtn = new JButton("Add");
	JButton deleteBtn = new JButton("Delete");
	JButton popOutBtn = new JButton("PopOut");
	JButton popOutBtnEmail = new JButton("Email");
	
	JLabel titleLbl = new JLabel("Contacts!");
	JLabel fNameLbl = new JLabel("First Name");
	JLabel lNameLbl = new JLabel("Last Name");
	JLabel emailLbl = new JLabel("Email Address");
	JLabel imageUploaderLbl = new JLabel("Choose an image");
	JLabel infoLbl = new JLabel("Contact Description");
	JLabel searchLabel = new JLabel("Search:");
	
	JTextField lNameField = new JTextField(12);
	JTextField fNameField = new JTextField(12);
	JTextField emailField = new JTextField(12);
	JTextField searchField = new JTextField(12);
	
	JTextArea infoTextArea = new JTextArea(4, 15);
	JScrollPane taScrollPane = new JScrollPane(infoTextArea);
	
	JList<String> searchResultList = new JList<>();
	
	JComboBox<String> contactComboBox = new JComboBox<>();
	
	DefaultListModel<String> listModel = new DefaultListModel<>();
	
	View(Model model){
		m = model;
		searchResultList.setModel(listModel);
		JScrollPane searchScrollPane = new JScrollPane(searchResultList);
		/**
		 * This View constructor is for the main view
		 * */
		
		infoTextArea.setLineWrap(true);
		infoTextArea.setWrapStyleWord(true);
		
		infoTextArea.setBorder( new LineBorder(new Color(197, 163, 253)) );
		
		taScrollPane.setBorder( new EmptyBorder(20,20,20,20) );
		
		searchResultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		searchResultList.setVisibleRowCount(5);
		
		setSize(500, 450);
		//setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Contact List!");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		backPanel.setLayout(new BorderLayout());
		
		add(backPanel);
			backPanel.add(northPanel, BorderLayout.NORTH);
				northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
				northPanel.add(titlePanel);
					titlePanel.add(titleLbl);
				northPanel.add(comboBoxPanel);
					contactComboBox.setPreferredSize(new Dimension(210, 20));
					comboBoxPanel.add(contactComboBox);
				northPanel.add(popOutDelPanel);
					popOutDelPanel.add(popOutBtn);
					popOutDelPanel.add(deleteBtn);
				northPanel.add(searchPanel);
					searchPanel.add(searchLabel);
					searchPanel.add(searchField);
				northPanel.add(searchResultPanel);
					searchResultPanel.add(searchScrollPane);
			backPanel.add(westPanel, BorderLayout.WEST);
				westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
				westPanel.add(firstLastEmailPanel);
					firstLastEmailPanel.setLayout(new BoxLayout(firstLastEmailPanel, BoxLayout.Y_AXIS));
					firstLastEmailPanel.add(fNamePanel);
						fNamePanel.add(fNameLbl);
						fNamePanel.add(fNameField);
					firstLastEmailPanel.add(lNamePanel);
						lNamePanel.add(lNameLbl);
						lNamePanel.add(lNameField);
					firstLastEmailPanel.add(emailPanel);
						emailPanel.add(emailLbl);
						emailPanel.add(emailField);
					firstLastEmailPanel.add(imageChooserPanel);
						imageChooserPanel.add(imageUploaderLbl);
						imageChooserPanel.add(imageUploaderBtn);
			backPanel.add(eastPanel, BorderLayout.EAST);
				eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
				eastPanel.add(taScrollPane);
				eastPanel.add(addBtn);
			backPanel.add(southPanel, BorderLayout.SOUTH);
	}
}