import javax.swing.*;
import java.awt.*;
class BKView extends JFrame{
	Model m;
	
	JPanel backPanel  = new JPanel();
	JPanel titlePanel = new JPanel();
	JPanel popOutBtnPanel = new JPanel();
	JPanel comboBoxPanel = new JPanel();
	JPanel fNamePanel = new JPanel();
	JPanel lNamePanel = new JPanel();
	JPanel emailPanel = new JPanel();
	JPanel buttonsPanel = new JPanel();
	JPanel textAreaPanel = new JPanel();
	JPanel imageUploaderPanel = new JPanel();
	JPanel searchPanel = new JPanel();
	JPanel searchListPanel = new JPanel();
	
	JButton imageUploaderBtn = new JButton("Choose Image");
	JButton addBtn = new JButton("Add");
	JButton deleteBtn = new JButton("Delete");
	JButton popOutBtn = new JButton("PopOut");
	JButton popOutBtnEmail = new JButton("Email");
	
	JLabel titleLbl = new JLabel("Contacts!");
	JLabel fNameLbl = new JLabel("First Name");
	JLabel lNameLbl = new JLabel("Last Name");
	JLabel emailLbl = new JLabel("Email Address");
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
	
	BKView(Model model){
		m = model;
		searchResultList.setModel(listModel);
		JScrollPane searchScrollPane = new JScrollPane(searchResultList);
		/**
		 * This View constructor is for the main view
		 * */
		backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.Y_AXIS));
		backPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
			infoTextArea.setLineWrap(true);
			infoTextArea.setWrapStyleWord(true);
			
			
			searchResultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			searchResultList.setVisibleRowCount(5);
		setSize(275, 550);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Contact List!");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		
		
		
		add(backPanel);
			backPanel.add(titlePanel);
				titlePanel.add(titleLbl);
				
			backPanel.add(comboBoxPanel);
				comboBoxPanel.add(contactComboBox);
				
			backPanel.add(popOutBtnPanel);
				popOutBtnPanel.add(popOutBtn);
				
			backPanel.add(fNamePanel);
				fNamePanel.add(fNameLbl);
				fNamePanel.add(fNameField);
				
			backPanel.add(lNamePanel);
				lNamePanel.add(lNameLbl);
				lNamePanel.add(lNameField);
			
			backPanel.add(emailPanel);
				emailPanel.add(emailLbl);
				emailPanel.add(emailField);
				
			backPanel.add(imageUploaderPanel);
				imageUploaderPanel.add(imageUploaderBtn);
				
			backPanel.add(textAreaPanel);
				textAreaPanel.add(infoLbl);
				textAreaPanel.add(taScrollPane);
				
			backPanel.add(buttonsPanel);
				buttonsPanel.add(addBtn);
				buttonsPanel.add(deleteBtn);
				
			backPanel.add(searchPanel);
				searchPanel.add(searchLabel);
				searchPanel.add(searchField);
			
			backPanel.add(searchListPanel);
				searchListPanel.add(searchScrollPane);
	}
}