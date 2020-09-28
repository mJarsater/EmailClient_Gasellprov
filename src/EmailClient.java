import org.apache.commons.lang3.ArrayUtils;

import javax.mail.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Properties;

public class EmailClient extends JFrame  {
    private JPasswordField passwordField;
    private JTextField usernameField, fromField, toField, subjectField;
    private JLabel passwordLabel, usernameLabel, fromLabel, toLabel, subjectLabel, unreadLabel, totalAmountLabel;
    private JButton signInOutBtn, sendBtn, attachBtn, refreshBtn;
    private String username, password;
    private JTextArea messeageArea, inboxArea;
    private JScrollPane scrollPane;
    private EmailReciver emailReciver;
    private Session session;



    public EmailClient(){
        usernameField = new JTextField();
        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(5,5,80,20);
        usernameField.setBounds(80,5,100,20);
        passwordField = new JPasswordField();
        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(5,30,80,20);
        passwordField.setBounds(80,30,100,20);
        signInOutBtn = new JButton("Sign in");
        signInOutBtn.setBounds(200, 5, 100, 45);
        signInOutBtn.addActionListener(this:: signInOutListner);

        //-----------------SEND AREA--------------------------
        sendBtn = new JButton("Send");
        sendBtn.setBounds(200, 100, 100, 30);
        sendBtn.setEnabled(false);
        attachBtn = new JButton("Attachment");
        attachBtn.setBounds(200, 135, 100, 30);
        attachBtn.setEnabled(false);
        fromField = new JTextField();
        fromLabel = new JLabel("From:");
        fromLabel.setBounds(5,100,50, 20);
        fromField.setBounds(80, 100, 100, 20);
        fromField.setEnabled(false);
        toField = new JTextField();
        toField.setEnabled(false);
        toLabel = new JLabel("To:");
        toLabel.setBounds(5, 125, 100,20);
        toField.setBounds(80, 125, 100,20);
        subjectField = new JTextField();
        subjectField.setEnabled(false);
        subjectLabel = new JLabel("Subject:");
        subjectField.setBounds(80, 150, 100, 20);
        subjectLabel.setBounds(5, 150, 100,20);
        messeageArea = new JTextArea();
        messeageArea.setBounds(5, 180, 400,350);
        messeageArea.setEnabled(false);
        //-----------------SEND AREA END----------------------


        //-----------------INBOX AREA--------------------------

        refreshBtn = new JButton("Refresh");
        refreshBtn.setEnabled(false);
        refreshBtn.addActionListener(this :: refreshListner);
        refreshBtn.setBounds(550, 100, 100, 45);
        inboxArea = new JTextArea();
        scrollPane = new JScrollPane(inboxArea);
        scrollPane.setPreferredSize(new Dimension(400, 350));
        scrollPane.setBounds(410,180,400, 350);
        totalAmountLabel = new JLabel("Total amount of emails: -");
        totalAmountLabel.setBounds(450, 150, 150, 20);
        unreadLabel = new JLabel("Unread: -");
        unreadLabel.setBounds(650, 150, 150, 20);

        //-----------------INBOX AREA END----------------------



        this.add(signInOutBtn);
        this.add(usernameLabel);
        this.add(usernameField);
        this.add(passwordLabel);
        this.add(passwordField);
        this.add(sendBtn);
        this.add(attachBtn);
        this.add(fromField);
        this.add(fromLabel);
        this.add(toField);
        this.add(toLabel);
        this.add(subjectField);
        this.add(subjectLabel);
        this.add(messeageArea);

        this.add(refreshBtn);
        this.add(unreadLabel);
        this.add(totalAmountLabel);
        this.add(scrollPane);
        this.setTitle("Martin Jarsäters Email-klient");
        this.setLayout(null);
        this.setSize(scrollPane.getX() + 420, 580);
        this.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    public void getCredentials(){
        username = usernameField.getText();
        password = new String(passwordField.getPassword());
    }

    public void toggleSignInOut(){
        if (signInOutBtn.getText().equalsIgnoreCase("Sign in")) {
                usernameField.setEnabled(false);
                passwordField.setEnabled(false);
                signInOutBtn.setText("Sign out");
                fromField.setText(username);
            } else if (signInOutBtn.getText().equalsIgnoreCase("Sign out")) {
                usernameField.setEnabled(true);
                passwordField.setEnabled(true);
                fromField.setText("");
                signInOutBtn.setText("Sign in");
        } else{
            System.out.println("No input");
        }
    }

    public JLabel getTotalAmountLabel(){
        return totalAmountLabel;
    }

    public JLabel getUnreadLabel(){
        return unreadLabel;
    }

    public JTextArea getInboxArea(){
        return inboxArea;
    }

    public void enableEmail(){
        sendBtn.setEnabled(true);
        attachBtn.setEnabled(true);
        toField.setEnabled(true);
        subjectField.setEnabled(true);
        messeageArea.setEnabled(true);

    }

    public void disableEmail(){
        sendBtn.setEnabled(false);
        attachBtn.setEnabled(false);
        toField.setEnabled(false);
        subjectField.setEnabled(false);
        messeageArea.setEnabled(false);
    }

    public void signInOutListner(ActionEvent e) {
        if(signInOutBtn.getText().equals("Sign in")) {
            getCredentials();
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp-mail.outlook.com");
            session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            // emailReciver = new EmailReciver(this, username, password); TODO - Funkar inte med brandväggen?
            // emailReciver.start();
            toggleSignInOut();
            enableEmail();
            refreshBtn.setEnabled(true);
        } else {
        toggleSignInOut();
        disableEmail();
    }
    }

        public static void main(String[] args) {
        EmailClient emailClient = new EmailClient();

    }

    public void refreshListner(ActionEvent e){
        emailReciver.start();
    }

}

class EmailReciver extends Thread{
    private boolean alive = true;
    private EmailClient emailClient;
    private JTextArea inboxArea;
    private JLabel unreadLabel, totalAmountLabel;
    private int unreadCount, totalCount;
    private String username, password;
    private boolean signedIn = false;

    public EmailReciver(EmailClient emailClient, String username, String password){
        this.emailClient = emailClient;
        this.inboxArea = emailClient.getInboxArea();
        this.unreadLabel = emailClient.getUnreadLabel();
        this.totalAmountLabel = emailClient.getTotalAmountLabel();
        this.username = username;
        this.password = password;
    }

    public void clearTextArea(){
        inboxArea.setText(null);
    }

    public void setLabels(int unreadCount, int totalCount){
        unreadLabel.setText("Unread: "+unreadCount);
        totalAmountLabel.setText("Total amount of emails: "+totalCount);
    }


    public void start(){
                System.out.println("Clearin");
                clearTextArea();
                System.out.println("Fetching");
                fetchEmails();
        }




    public void print(String msg){
        inboxArea.append(msg+ "\n");
    }

    public void printEmail(Message msg){
        try{
            inboxArea.append("\n");
            inboxArea.append("From: " +msg.getFrom()[0]+"\n");
            inboxArea.append("Subject: "+msg.getSubject()+"\n");
            inboxArea.append("Date: "+msg.getSentDate()+"\n");
            inboxArea.append("------------------------------------");
            inboxArea.append("\n");
    } catch (MessagingException me){
            print("Ooops.. something went wrong, please try again.");
        }
    }

    public synchronized void fetchEmails(){
        try{
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp-mail.outlook.com");
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            Store emailStore = session.getStore("imaps");
            System.out.println("Connecting");
            emailStore.connect("smtp-mail.outlook.com", username, password);


            System.out.println("Opening folder");
            Folder inbox = emailStore.getFolder("INBOX");
            unreadCount = inbox.getUnreadMessageCount();
            totalCount = inbox.getMessageCount();
            setLabels(unreadCount, totalCount);

            inbox.open(Folder.READ_ONLY);

            Message [] messages = inbox.getMessages();
            ArrayUtils.reverse(messages);
            clearTextArea();
            for(int i = 0; i < messages.length; i++){
                if(i >= 10){
                    inbox.close();
                    emailStore.close();
                }
                printEmail(messages[i]);
            }

        } catch (AuthenticationFailedException e){
            print("Authentication failed");
            print("Wrong email/password");
        } catch (MessagingException me){
            System.out.println(me);
        }
    }

    public boolean isSignedIn() {
        return signedIn;
    }

    public void kill(){
        alive = false;
    }
}
