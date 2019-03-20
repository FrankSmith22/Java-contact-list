import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.*;

class Controller {
    Model m;
    View v;
    PopOutView pv;
    java.util.Timer timerAdd = new java.util.Timer();
    java.util.Timer timerPopOut = new java.util.Timer();
    java.util.Timer timerPopOutActive = new java.util.Timer();
    java.util.Timer timerDuplicate = new java.util.Timer();
    TimerTask taskAdd;
    TimerTask taskPopOut;
    TimerTask taskPopOutActive;
    TimerTask taskDuplicate;
    ActionListener openImageUploader;
    ActionListener submitContact;
    ActionListener deleteContactListener;
    ActionListener selectContactListener;
    ActionListener sendEmailListener;
    ActionListener closeWindow;
    WindowListener serializePreClose;
    WindowListener popOutClose;
    KeyListener searchContactsListener;
    MouseListener dblClickListener;
    
    boolean popOutIsActive = false;
    JFileChooser chooser = new JFileChooser();
    String base64Image = "";
    String popOutEmail = "";
    
    Controller(Model model, View view){
        m = model;
        v = view;
        
        /////////////////////////////////
        alphabetizeContactList();
        //System.out.println(v.listModel);
        searchContacts();
        //initListModel();
        //v.listModel.clear();
        //^^uncomment to initiate with blank searchresult
        /////////////////////////////////
        
        openImageUploader = new ActionListener(){
        	public void actionPerformed(ActionEvent ae){
        		popOutFileUploader();
        	}
        };
        
        submitContact = new ActionListener(){
        	public void actionPerformed(ActionEvent ae){
        		serializeContact();
        	}
        };
        
        deleteContactListener = new ActionListener(){
         	public void actionPerformed(ActionEvent ae){
         		//System.out.println("Hello World this is the delete method");
         		//grab selected item from combobox
         		deleteContact();
         	}
        };
        
        selectContactListener = new ActionListener(){
        	public void actionPerformed(ActionEvent ae){
        		selectContact();
        	}
        };
        
        sendEmailListener = new ActionListener(){
        	public void actionPerformed(ActionEvent ae){
        		sendEmail();
        	}
        };
        
        closeWindow = new ActionListener(){
         	public void actionPerformed(ActionEvent ae){
         		pv.dispose();
         		popOutIsActive = false;
         	}
        };
        
        serializePreClose = new WindowAdapter(){
			public void windowClosing( WindowEvent e ) {
				Storage.serialize("contactsModel.ser", m);
			}
		};
		popOutClose = new WindowAdapter(){
			public void windowClosing( WindowEvent e){
				popOutIsActive = false;
			}
		};
		
		searchContactsListener = new KeyAdapter(){
			public void keyReleased(KeyEvent key){
				searchContacts();
			}
		};
		
		dblClickListener = new MouseAdapter(){
        	public void mouseClicked(MouseEvent me){
        		JListClick(me);
        	}
        };
        
        v.imageUploaderBtn.addActionListener(openImageUploader);
		v.addBtn.addActionListener(submitContact);
		v.deleteBtn.addActionListener(deleteContactListener);
		v.popOutBtn.addActionListener(selectContactListener);
		v.searchResultList.addMouseListener(dblClickListener);
		
		v.addWindowListener(serializePreClose);
		
		v.searchField.addKeyListener(searchContactsListener);
		
		fillComboBox();
    }
    
    public void JListClick(MouseEvent me){
    	String selectedContact;
    	String[] selectedContactArr;
    	String email;
        if (me.getClickCount() == 2) {
            // Double-click detected
            //System.out.println(v.searchResultList.getSelectedValue());
            if(v.searchResultList.getSelectedValue() != null){
	            selectedContact = (String)v.searchResultList.getSelectedValue();
	            selectedContactArr = selectedContact.split(" ", 5);
	            email = selectedContactArr[2];
	            for(int i=0; i<m.contactList.size(); i++){
					if(email.equals(m.contactList.get(i).get("email"))){
						//the map with the selected email from the jlist has been found
						pv = new PopOutView();
						popOutIsActive = true;
						pv.addWindowListener(popOutClose);
						pv.closeBtn.addActionListener(closeWindow);
						pv.popOutEmail.addActionListener(sendEmailListener);
						fillPopOut(i);
					}
					else{
						continue;
					}
	            }
            }
        }
        else{
        	if(v.searchResultList.getSelectedValue() != null){
        		selectedContact = (String)v.searchResultList.getSelectedValue();
	            selectedContactArr = selectedContact.split(" ", 5);
	            
	            v.contactComboBox.setSelectedItem(selectedContactArr[0] + " " + selectedContactArr[1]);
        	}
        }
    }
    
    public void selectContact(){
    		//check if combobox even has content
        		if(m.contactList.size() != 0){
        			//then check if a popout is already active
        			if(!popOutIsActive){
        				pv = new PopOutView();
        				popOutIsActive = true;
        				pv.addWindowListener(popOutClose);
						pv.closeBtn.addActionListener(closeWindow);
						pv.popOutEmail.addActionListener(sendEmailListener);
						fillPopOut(v.contactComboBox.getSelectedIndex());
        			}
        			else{
        				/////////////////////////////////////////////////////////
        				v.titleLbl.setText("Popout is already active");
						taskPopOutActive = new TimerTask(){
							public void run(){
								v.titleLbl.setText("Contacts!");
								timerPopOutActive.cancel();
							}
						};
						timerPopOutActive.schedule(taskPopOutActive, 3000);
        				/////////////////////////////////////////////////////////
        			}
        		}
        		else{
        			/////////////////////////////////////////////////////////
        			v.titleLbl.setText("Please add some contacts");
					taskPopOut = new TimerTask(){
						public void run(){
							v.titleLbl.setText("Contacts!");
							timerPopOut.cancel();
						}
					};
					timerPopOut.schedule(taskPopOut, 3000);
					/////////////////////////////////////////////////////////
        		}
     }
     
    public void searchContacts(){
    	//empty our JList 
    	v.listModel.clear();
    	//grab our search field value (as string)
    	String searchString = v.searchField.getText().toLowerCase();
    	
    	String firstSearchResult;
    	//iterate through our arraylist of maps
    	/////////////////////////////////////////////////
    	/////////////////////////////////////////////////
    	for(int i=0; i<m.contactList.size(); i++){
    		String currentFirstName = (String) m.contactList.get(i).get("fName");
    		String currentLastName = (String) m.contactList.get(i).get("lName");
    		String currentEmail = (String) m.contactList.get(i).get("email");
    		if(currentFirstName.toLowerCase().contains(searchString) || currentLastName.toLowerCase().contains(searchString) || currentEmail.toLowerCase().contains(searchString)){
    			String listMember = currentFirstName + " " + currentLastName + " " + currentEmail;
    			v.listModel.addElement(listMember);
    			continue;
    		}
    	}
    	/////////////////////////////////////////////////
    	/////////////////////////////////////////////////
    	//update combobox to select first found search result
    	if(v.listModel.getSize() > 0 ){
    		firstSearchResult = (String)v.listModel.elementAt(0);
    	}
    	else{
    		String firstName = (String)m.contactList.get(0).get("fName");
    		String lastName = (String)m.contactList.get(0).get("lName");
    		firstSearchResult = firstName + " " + lastName;
    	}
    	String[] selectedContactArr = firstSearchResult.split(" ", 3);
    	String fullName = (String)selectedContactArr[0] + " " + selectedContactArr[1];
    	v.contactComboBox.setSelectedItem(fullName);
    }
    
    public void fillComboBox(){
    	if(m.contactList.size() == 0){
    		v.contactComboBox.removeAllItems();
    		v.contactComboBox.addItem("Your List is empty!");
    	}
    	else{
    		v.contactComboBox.removeAllItems();
    		for(int i = 0; i < m.contactList.size(); i++){
	    		v.contactComboBox.addItem( (String) (m.contactList.get(i).get("fName") + " " + m.contactList.get(i).get("lName") ) );
	    	}
    	}
    }
    
    public void popOutFileUploader(){
    	
    	javax.swing.filechooser.FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
    		"Image Files", ImageIO.getReaderFileSuffixes()
    	);
    	chooser.setFileFilter(imageFilter);
    	int returnValue = chooser.showOpenDialog( v );
    	if(returnValue == JFileChooser.APPROVE_OPTION ){
    		File selectedFile = chooser.getSelectedFile();
    		String filePath = selectedFile.getAbsolutePath();
    		
			File file = new File(filePath);
			try (FileInputStream imageInFile = new FileInputStream(file)) {
				// Reading a Image file from file system
				byte imageData[] = new byte[(int) file.length()];
				imageInFile.read(imageData);
				base64Image = java.util.Base64.getEncoder().encodeToString(imageData);
			} catch (FileNotFoundException e) {
				System.out.println("Image not found" + e);
			} catch (IOException ioe) {
				System.out.println("Exception while reading the Image " + ioe);
			}
    	}
    }
    
    public void deleteContact(){
    	//remove selected contact from Arraylist
    	if(!(m.contactList.size() < (v.contactComboBox.getSelectedIndex()+1))){
    		m.contactList.remove(v.contactComboBox.getSelectedIndex());
    		//clear out and then repopulate JList with new m.contactList
    		v.listModel.clear();
	    	for(int i=0; i<m.contactList.size(); i++){
	    		String currentFirstName = (String) m.contactList.get(i).get("fName");
	    		String currentLastName = (String) m.contactList.get(i).get("lName");
	    		String currentEmail = (String) m.contactList.get(i).get("email");
	    		
    			String listMember = currentFirstName + " " + currentLastName + " " + currentEmail;
    			v.listModel.addElement(listMember);
	    	}
	    	
    		fillComboBox();
    	}
    }
    
    public static Image decodeImage( String string ) {
		
		System.out.println(string);
		BufferedImage image = null;//null might be placeholder instead
		
		try {
			
			byte[] imageByteArray = java.util.Base64.getDecoder().decode( string );
			InputStream stream = new ByteArrayInputStream( imageByteArray );
			image = ImageIO.read( stream );
		} catch( Exception e ) {
			//even tho we dont handle this error, the catch is required because
			//Java is dumb and didnt fix this bug :)
		}
		return ( Image ) image;
    }
    
    public void fillPopOut(int user){
    	//grab selected index from when popout button was pressed
    	Map modelUser = m.contactList.get(user);
    	pv.setTitle(modelUser.get("fName") + " " + modelUser.get("lName"));
    	pv.titleLbl.setText(modelUser.get("fName") + " " + modelUser.get("lName"));
    	pv.descTextArea.setText( (String)modelUser.get("description") );
    	popOutEmail = (String)modelUser.get("email");
    	
    	try{
    		Image userImage = decodeImage((String)modelUser.get("image")).getScaledInstance( 200, 200, Image.SCALE_DEFAULT );
    		java.awt.image.BufferedImage newImage = new java.awt.image.BufferedImage( 200, 200, BufferedImage.TYPE_INT_ARGB );
    		pv.imageLbl.setIcon( new ImageIcon( userImage ) );
    	}
    	catch(Exception e){
    		System.out.println("error" + e);
    	}
    }
    
    public boolean checkForDuplicate() {
    	
    	Boolean isDuplicate = false;
    	String submittedEmail = v.emailField.getText();
    	for(int i=0; i<m.contactList.size(); i++){
    		if(submittedEmail.equals(m.contactList.get(i).get("email"))){
    			System.out.println("Email duplicate has been found, will not submit form");
    			isDuplicate = true;
    			break;
    		}
    	}
    	return isDuplicate;
    }
    
    public void serializeContact(){
    	//first check if all fields are completed
    	if(
    		!(
	    		v.lNameField.getText().isEmpty()
	    		|| v.fNameField.getText().isEmpty()
	    		|| v.emailField.getText().isEmpty()
	    		|| v.infoTextArea.getText().isEmpty()
	    	)
		){
    		//check if the email attempting to register is already registered.
    		if(!checkForDuplicate()){
    			//fill contact array with input field values
		    	Map<String, String> contact = new HashMap<String, String>();
		    	
		    	//grab and store first name
		    	contact.put("fName", v.fNameField.getText());
		    	
		    	//grab and store last name
		    	contact.put("lName", v.lNameField.getText());
		    	
		    	//grab and store email
		    	contact.put("email", v.emailField.getText());
		    	
		    	//grab and store description
		    	contact.put("description", v.infoTextArea.getText());
		    	
		    	//store "base64"d image
		    	contact.put("image", base64Image);
		    	
		    	v.fNameField.setText("");
		    	v.lNameField.setText("");
		    	v.emailField.setText("");
		    	v.infoTextArea.setText("");
		    	chooser.setSelectedFile(null);
		    	
		    	m.contactList.add(contact);
		    	alphabetizeContactList();
		    	fillComboBox();
		    	//dont forget to separate these lines underneath into a separate function!
		    	v.listModel.clear();
		    	for(int i=0; i<m.contactList.size(); i++){
		    		String currentFirstName = (String) m.contactList.get(i).get("fName");
		    		String currentLastName = (String) m.contactList.get(i).get("lName");
		    		String currentEmail = (String) m.contactList.get(i).get("email");
					String listMember = currentFirstName + " " + currentLastName + " " + currentEmail;
					v.listModel.addElement(listMember);
		    	}
    		}
    		else{
    			/////////////////////////////////////////////////////////
    			v.titleLbl.setText("email already registered");
				taskDuplicate = new TimerTask(){
					public void run(){
						v.titleLbl.setText("Contacts!");
						timerDuplicate.cancel();
					}
				};
				timerDuplicate.schedule(taskDuplicate, 3000);
				/////////////////////////////////////////////////////////
    		}
		    	
    	}
    	else{
    		/////////////////////////////////////////////////////////
    		v.titleLbl.setText("Please fill in all the fields; including an image");
    		taskAdd = new TimerTask(){
    			public void run(){
    				v.titleLbl.setText("Contacts!");
    				timerAdd.cancel();
    			}
    		};
    		timerAdd.schedule(taskAdd, 3000);
    		/////////////////////////////////////////////////////////
    	}
    }
    
    public void sendEmail(){
    	try{
    		System.out.println("button was clicked");
    		mailto(popOutEmail);
    	}
    	catch(IOException | URISyntaxException error){
    		System.out.println("IOException: " + error);
    	}
    	
    }
    
    public static void mailto(String recipient) throws IOException, URISyntaxException {
    	String uriStr = String.format("mailto:%s",
            recipient);
        System.out.println(uriStr);
    	Desktop.getDesktop().mail(new URI(uriStr));
	}
	
	public void alphabetizeContactList(){
    	for( int i=0; i<(m.contactList.size()-1); i++ ){
    		String currentLastName = (String)(m.contactList.get(i).get("lName"));
	    	char currentCharacter = currentLastName.charAt( 0 );
	    	int j = i;
	    	++j;
    		String nextLastName = (String)((m.contactList.get(j)).get("lName"));
    		char nextCharacter = nextLastName.charAt( 0 );
    		
    		//now check who has the higher last name
    		if(Character.toLowerCase(nextCharacter) < Character.toLowerCase(currentCharacter)){
    			Map person = m.contactList.get(j);
    			m.contactList.remove(m.contactList.get(j));
    			m.contactList.add(0, person);
    			
    			alphabetizeContactList();
    		}
    		else if(Character.toLowerCase(nextCharacter) > Character.toLowerCase(currentCharacter) || Character.toLowerCase(nextCharacter) == (Character.toLowerCase(currentCharacter))){
    			continue;
    		}
    	}
    }
    
    /*
    //alphabetizeContactList with comments and System print outs
    //AKA, a debug version of my sorting method
    public void alphabetizeContactList(){
    	//Next step is to seperate out condition checking for equality so then
    	//I can compare the next characters for a true alphabet sort
    	
    	//for each map(person) in our arraylist(contactList)...
    	for( int i=0; i<(m.contactList.size()-1); i++ ){
    		//grab the first letter of the first persons last name
    		String currentLastName = (String)(m.contactList.get(i).get("lName"));
	    	char currentCharacter = currentLastName.charAt( 0 );
	    	System.out.println("currentLastName:   ");
	    	System.out.print(currentLastName);
	    	System.out.println("");
	    	
	    	//increment to check what would be next in loop without changing i
	    	int j = i;
	    	++j;
	    	//grab the first letter of the second persons last name
    		String nextLastName = (String)((m.contactList.get(j)).get("lName"));
    		char nextCharacter = nextLastName.charAt( 0 );
    		System.out.println("nextLastName:   ");
    		System.out.print(nextLastName);
    		System.out.println("\n");
    		
    		//now check who has the higher last name
    		if(Character.toLowerCase(nextCharacter) < Character.toLowerCase(currentCharacter)){
    			System.out.println("nextCharacter is alphabetically higher than the next");
    			//save the person before we remove from the array
    			Map<String, String> person = m.contactList.get(j);
    			//now move the person with the currentlastName to the front of the arraylist
    			//by removing them from the list..
    			m.contactList.remove(m.contactList.get(j));
    			//and putting them back in at the top
    			m.contactList.add(0, person);
    			
    			//now we reinvoke to reevaluate m.contactList
    			alphabetizeContactList();
    		}
    		else if(Character.toLowerCase(nextCharacter) > Character.toLowerCase(currentCharacter) || Character.toLowerCase(nextCharacter) == (Character.toLowerCase(currentCharacter))){
    			//the two compared values are in proper order, continue onto the next pair
    			continue;
    		}
    		else{
    			System.out.println("m.contactList has now been sorted.. hopefully");
    		}
    	}
    }
    */
}