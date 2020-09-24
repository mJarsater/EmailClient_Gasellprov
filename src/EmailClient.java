import javax.mail.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Properties;

public class EmailClient extends JFrame  {
    private JPasswordField passwordField;
    private JTextField usernameField, fromField, toField, subjectField;
    private JLabel passwordLabel, usernameLabel, fromLabel, toLabel, subjectLabel;
    private JButton signInOutBtn, sendBtn, attachBtn;
    private String username, password;
    private JTextArea messeageArea, inboxArea;
    private Session session;
    private boolean signedIn = false;


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
        messeageArea.setBounds(5, 180, 300,350);
        messeageArea.setEnabled(false);

        inboxArea = new JTextArea();


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
        this.setTitle("Martin Jarsäters Email-klient");
        this.setLayout(null);
        this.setSize(900, 580);
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
            toggleSignInOut();
            enableEmail();
            EmailReciver emailReciver = new EmailReciver();
            emailReciver.run();
        } else {
        toggleSignInOut();
        disableEmail();
    }
    }
        public static void main(String[] args) {
        EmailClient emailClient = new EmailClient();

    }

}

class EmailReciver extends Thread{
    private boolean alive = true;


    public EmailReciver(){

    }

    public void start(){
        while(alive) {
            try {
                sleep(30000);
                //Hämta email
                fetchEmails();
            } catch (InterruptedException ie){
                System.out.println(ie);
            }
        }
    }

    public void fetchEmails(){

    }

    public void kill(){
        alive = false;
    }
}
