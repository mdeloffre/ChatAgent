package Session;

import Software.userData;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class session extends JFrame {

    //Attributes for the TCP protocol
    private PrintWriter out;

    //Attributes for the UI
    public String username;
    public userData otherUserData;
    private JTextArea messageDisplay;
    private JTextField text = new JTextField("Write a message...");

    //Attributes for the message fetching
    public File messagesFile;

    //Constructor. Will display the new window and start the reception thread.
    public session(String username, userData otherUser, int port,  boolean isServer){
        super();
        otherUserData = otherUser;
        String otherUsername = otherUser.getUsername();

        //*****************************************************
        // THIS IS THE PART CONCERNING THE WINDOW DISPLAY
        //*****************************************************
        JButton sendButton = new JButton("Send");
        JPanel textAreaPanel = new JPanel();
        messageDisplay = new JTextArea("This is the start of your conversation with " + otherUsername + ".\n");
        messageDisplay.setEditable(false);
        JScrollPane messageArea = new JScrollPane(messageDisplay);

        this.setTitle(otherUsername);

        sendButton.setMnemonic(KeyEvent.VK_ENTER);
        sendButton.setPreferredSize(new Dimension(100, 40));
        text.setPreferredSize(new Dimension(250,40));

        textAreaPanel.add(text, BorderLayout.WEST);
        textAreaPanel.add(sendButton, BorderLayout.EAST);
        textAreaPanel.setPreferredSize(new Dimension(350,40));

        messageArea.setPreferredSize(new Dimension(400, 300));
        setPreferredSize(new Dimension(600,500));

        add(messageArea, BorderLayout.NORTH);
        add(textAreaPanel, BorderLayout.SOUTH);

        text.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    if (text.getText() != null){
                        try {
                            sendMessage(text.getText());
                            messageDisplay.append("\n" + username + " : " + text.getText());
                        } catch (Exception ex) {
                            messageDisplay.append("\nSorry, there was an error while trying to send the message.");
                            System.out.println("Failed to send the message");
                        }
                        text.setText("");
                    }
                }
            }
        });
        sendButton.addActionListener(e -> {
            if (text.getText() != null){
                try {
                    sendMessage(text.getText());
                    messageDisplay.append("\n" + username + " : " + text.getText());
                } catch (Exception ex) {
                    messageDisplay.append("\nSorry, there was an error while trying to send the message.");
                    System.out.println("Failed to send the message");
                }
                text.setText("");
            }
        });
        //*****************************************************

        //*****************************************************
        // THIS IS THE PART CONCERNING THE TCP PROTOCOL
        //*****************************************************
        try {
            Socket connectionSocket;
            if (isServer) {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Message before accept");
                connectionSocket = serverSocket.accept();
                System.out.println("Message after accept");
            } else {
                connectionSocket = new Socket(InetAddress.getByName(otherUser.getIPAddress()), port);
            }
            this.out = new PrintWriter(connectionSocket.getOutputStream(), true);
            //Attributes for the TCP connection and the connection Thread
            Thread connectionThread = new Thread(() -> {
                try {
                    BufferedReader bufferIn = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    while (true) {
                        String message = bufferIn.readLine();
                        messageDisplay.append("\n" + otherUsername + " : " + message);
                        System.out.println("Message received : " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            connectionThread.start();
            System.out.println("Connection thread started");
        }catch (IOException e) {
            e.printStackTrace();
        }
        //*****************************************************

        //*****************************************************
        // THIS IS THE PART CONCERNING THE MESSAGE FETCH
        //*****************************************************
        if (!messagesFile.exists()){

        }
    }


    private void sendMessage(String message){
        out.println(message);
        System.out.println("Message sent : " + message);
    }

    //Getters/Setters
    public userData getOtherUserData(){
        return otherUserData;
    }

    public String getUsername(){
        return username;
    }


}
