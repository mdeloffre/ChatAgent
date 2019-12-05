package GUI;

import Software.mainWindowActions;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class userActions extends JPanel {

    public JButton startSession = new JButton("Begin chat session");
    public JLabel userName = new JLabel();
    public JButton changeUsername = new JButton("Change username");
    public static int port = 5000;

    public userActions(JList L, String name, JFrame parent){
        super();
        userName.setText("Logged in as " + name);
        mainWindowActions mainWindowActions = new mainWindowActions(name);

        //Starts a session with the selected user when the button is clicked
        startSession.addActionListener(e -> {
            if (L.getSelectedIndex() != -1){
                try {
                    //When the user starts a session, a new Window appears
                    //The second argument starts a new UDP session
                    new chatWindow((String) L.getSelectedValue(), mainWindowActions.beginSession(port));
                    port++;
                } catch (Exception ex) {
                    System.out.println("aled");
                }
            }
            else {
                JOptionPane.showMessageDialog(parent, "Please choose a user in the list");
            }
        });

        //Open the change username window when the button is clicked
        changeUsername.addActionListener(e -> new changeUsernameWindow(userName));

        add(startSession, BorderLayout.NORTH);
        add(userName, BorderLayout.CENTER);
        add(changeUsername, BorderLayout.SOUTH);

    }
}
