import javax.swing.*;
import java.awt.*;

class PopOutView extends JFrame{
	
	JPanel backPanel  = new JPanel();
	JPanel titlePanel = new JPanel();
	JPanel textPanel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JPanel imagePanel = new JPanel();
	
	ImageIcon userIconImage = new ImageIcon("./images/defaultFace.png");
	
	JLabel titleLbl = new JLabel("null");
	JLabel imageLbl = new JLabel( userIconImage );
	JTextArea descTextArea = new JTextArea("null information", 8, 18 );
	
	
	
	JButton closeBtn = new JButton("close");
	JButton popOutEmail = new JButton("send");
	
	PopOutView(){
		backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.Y_AXIS));
		backPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
			imageLbl.setPreferredSize(new Dimension(200, 200));
			
			descTextArea.setLineWrap(true);
			descTextArea.setWrapStyleWord(true);
			descTextArea.setEnabled(false);
			descTextArea.setDisabledTextColor(Color.black);
		setSize(350, 450);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("null");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		
		add(backPanel);
			backPanel.add(titlePanel);
				titlePanel.add(titleLbl);
				
			backPanel.add(imagePanel);
				imagePanel.add(imageLbl);
				
			backPanel.add(textPanel);
				textPanel.add(descTextArea);
				
			backPanel.add(buttonPanel);
				buttonPanel.add(popOutEmail);
				buttonPanel.add(closeBtn);
				
			
	}
}