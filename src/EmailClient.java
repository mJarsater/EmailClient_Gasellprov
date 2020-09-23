import javax.mail.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Properties;

public class EmailClient extends JFrame {
    private JPasswordField passwordField;
    private JTextField usernameField, fromField, toField, subjectField;
    private JLabel passwordLabel, usernameLabel, fromLabel, toLabel, subjectLabel;
    private JButton signInOutBtn;
    private String username, password;
    private JTextArea messeageArea, inboxArea;
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


        fromField = new JTextField();
        fromLabel = new JLabel("From:");
        fromLabel.setBounds(5,200,50, 20);
        fromField.setBounds(50, 200, 100, 20);
        fromField.setEnabled(false);
        toField = new JTextField();
        toLabel = new JLabel("To:");
        toLabel.setBounds(5, 225, 100,20);
        toField.setBounds(50, 225, 100,20);
        subjectField = new JTextField();
        subjectLabel = new JLabel("Subject:");
        subjectField.setBounds(50, 250, 100, 20);
        subjectLabel.setBounds(5, 250, 100,20);
        messeageArea = new JTextArea();
        messeageArea.setBounds(5, 280, 300,370);
        messeageArea.setEnabled(false);

        inboxArea = new JTextArea();


        this.add(signInOutBtn);
        this.add(usernameLabel);
        this.add(usernameField);
        this.add(passwordLabel);
        this.add(passwordField);
        this.add(fromField);
        this.add(fromLabel);
        this.add(toField);
        this.add(toLabel);
        this.add(subjectField);
        this.add(subjectLabel);
        this.add(messeageArea);
        this.setTitle("Martin Jarsäters Email-klient");
        this.setLayout(null);
        this.setSize(900, 700);
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
            } else if (signInOutBtn.getText().equalsIgnoreCase("Sign out")) {
                usernameField.setEnabled(true);
                passwordField.setEnabled(true);
                signInOutBtn.setText("Sign in");
        } else{
            System.out.println("No input");
        }
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
        } else {

        //TODO - Ta bort info
        toggleSignInOut();
    }
    }

        public static void main(String[] args) {
        EmailClient emailClient = new EmailClient();
    }

}
