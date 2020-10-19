import org.apache.commons.lang3.ArrayUtils;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;

public class EmailClient extends JFrame  {
    private JPasswordField passwordField;
    private JTextField usernameField, fromField, toField, subjectField;
    private JLabel passwordLabel, usernameLabel, fromLabel, toLabel, subjectLabel,attachmentLabel, unreadLabel, totalAmountLabel, sentStatusLabel, downloadStatusLabel;
    private JButton signInOutBtn, sendBtn, attachBtn, refreshBtn, readBtn, getAttachBtn;
    private String username, password;
    private JTextArea messeageArea, readEmailArea;
    private JScrollPane scrollPane, scrollReadEmail;
    private File selectedFile;
    private EmailReceiver emailReceiver;
    private Session session;
    private DefaultListModel <Email> l1;
    private JList<Email> inboxArea;
    private ArrayList<File> attachmentsList = new ArrayList<>();
    private JFileChooser fileChooser;



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
        signInOutBtn.addActionListener(this::signInOutListener);

        //-----------------SEND AREA--------------------------
        sendBtn = new JButton("Send");
        sendBtn.setBounds(200, 100, 100, 30);
        sendBtn.setEnabled(false);
        sendBtn.addActionListener(this::sendListener);
        attachBtn = new JButton("Attachment");
        attachBtn.setBounds(200, 135, 100, 30);
        attachBtn.setEnabled(false);
        attachBtn.addActionListener(this::attachmentListener);
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

        attachmentLabel = new JLabel("Attachment: ");
        attachmentLabel.setBounds(5, 175, 400, 20);
        subjectField.setBounds(80, 150, 100, 20);
        subjectLabel.setBounds(5, 150, 100,20);
        messeageArea = new JTextArea();
        messeageArea.setBounds(5, 200, 400,330);
        messeageArea.setEnabled(false);



        //-----------------SEND AREA END-----------------------

        //-----------------LABEL AREA--------------------------

        totalAmountLabel = new JLabel("Total amount of emails: -");
        totalAmountLabel.setBounds(650, 10, 200, 20);
        unreadLabel = new JLabel("Unread: -");
        unreadLabel.setBounds(650, 40, 150, 20);
        downloadStatusLabel = new JLabel("Download status: -");
        downloadStatusLabel.setBounds(410,10,200,20);
        sentStatusLabel = new JLabel("Sending status: -");
        sentStatusLabel.setBounds(410, 40, 150, 20);


        //-----------------LABEL AREA END----------------------

        //-----------------INBOX AREA--------------------------

        refreshBtn = new JButton("Refresh");
        refreshBtn.setEnabled(false);
        refreshBtn.addActionListener(this ::refreshListener);
        refreshBtn.setBounds(410, 125, 130, 45);

        readBtn = new JButton("Read");
        readBtn.setEnabled(false);
        readBtn.setBounds(545,125, 130, 45 );
        readBtn.addActionListener(this::readListener);

        getAttachBtn = new JButton("Save attachment");
        getAttachBtn.setEnabled(false);
        getAttachBtn.addActionListener(this ::getAttachmentListener);
        getAttachBtn.setBounds(680, 125, 130, 45);

        l1 = new DefaultListModel<>();
        inboxArea = new JList<>(l1);
        scrollPane = new JScrollPane(inboxArea);
        scrollPane.setPreferredSize(new Dimension(400, 110));
        scrollPane.setBounds(410,180,400, 120);



        readEmailArea = new JTextArea();
        readEmailArea.setLineWrap(true);
        scrollReadEmail = new JScrollPane(readEmailArea);
        scrollReadEmail.setBounds(410, 310, 400, 220);

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
        this.add(attachmentLabel);
        this.add(sentStatusLabel);
        this.add(messeageArea);
        this.add(scrollReadEmail);

        this.add(downloadStatusLabel);
        this.add(readBtn);
        this.add(getAttachBtn);
        this.add(refreshBtn);
        this.add(unreadLabel);
        this.add(totalAmountLabel);
        this.add(scrollPane);
        this.setTitle("Martin Jarsäters Email-klient");
        this.setLayout(null);
        this.setSize(scrollPane.getX() + 520, 580);
        this.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void setSentStatusLabel(String msg){
        sentStatusLabel.setText("Status: "+msg);
    }

    public void getCredentials(){
        username = usernameField.getText();
        password = new String(passwordField.getPassword());
    }

    public void toggleSignInOut(){
        if (signInOutBtn.getText().equalsIgnoreCase("Sign in")) {
                signIn();
            } else if (signInOutBtn.getText().equalsIgnoreCase("Sign out")) {
                signOut();
                disableEmail();

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

    public JList getInboxArea(){
        return inboxArea;
    }

    public void enableEmail(){
        sendBtn.setEnabled(true);
        attachBtn.setEnabled(true);
        toField.setEnabled(true);
        subjectField.setEnabled(true);
        messeageArea.setEnabled(true);
        readBtn.setEnabled(true);
        getAttachBtn.setEnabled(true);
        inboxArea.setEnabled(true);
        readEmailArea.setEditable(true);

    }

    public void disableEmail(){
        sendBtn.setEnabled(false);
        attachBtn.setEnabled(false);
        toField.setEnabled(false);
        subjectField.setEnabled(false);
        messeageArea.setEnabled(false);
        readBtn.setEnabled(false);
        getAttachBtn.setEnabled(false);
        inboxArea.setEnabled(false);
        readEmailArea.setText(null);
        readEmailArea.setEditable(false);
    }

    public void signOut(){
        usernameField.setEnabled(true);
        passwordField.setEnabled(true);
        fromField.setText("");
        signInOutBtn.setText("Sign in");
        attachmentLabel.setText("Attachments: ");
        messeageArea.append("");
        l1.removeAllElements();
        totalAmountLabel.setText("");
        unreadLabel.setText("");
        setDownloadStatusLabel("-");
    }

    public void signIn(){
        usernameField.setEnabled(false);
        passwordField.setEnabled(false);
        signInOutBtn.setText("Sign out");
        fromField.setText(username);
    }

    public void clearEmailInput(){
        toField.setText(null);
        subjectField.setText(null);
        messeageArea.setText(null);
        for(int i = 0; i < attachmentsList.size(); i++){
            attachmentsList.remove(i);
        }
        attachmentLabel.setText("Attachments: ");
    }

    public void sendListener(ActionEvent e){
        EmailSender newEmail = new EmailSender(this, username, password, toField.getText(),subjectField.getText(), messeageArea.getText(), attachmentsList);
        newEmail.run();

    }


    public void signInOutListener(ActionEvent e) {
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
            emailReceiver = new EmailReceiver(this, username, password, l1);
            toggleSignInOut();
            enableEmail();
            refreshBtn.setEnabled(true);
            setDownloadStatusLabel("Signed in");
            emailReceiver.start();
        } else {
        toggleSignInOut();
    }
    }

    public void attachmentListener(ActionEvent e){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION){
            selectedFile = fileChooser.getSelectedFile();
            attachmentsList.add(selectedFile);
            updateAttachmentLabel();
        }
    }

    public void updateAttachmentLabel() {
        attachmentLabel.setText("Attachments: " +getAttachmentNames());
    }

    public String getAttachmentNames() {
        ArrayList<String> attachmentNames = new ArrayList<>();
        String attachmentName = "";
        for(int i = 0; i<attachmentsList.size(); i++){
            attachmentName = attachmentsList.get(i).getName();
            attachmentNames.add(attachmentName);

        }
        return attachmentNames.toString();
    }

    public boolean checkAttachmentAmount(Email email){
        HashMap<String, MimeBodyPart> attachments = email.getHashMap();
        String attachmentName;
        if(attachments.size() < 1){
            JOptionPane.showMessageDialog(this,"This emai has no attachment.", "Email attachment", JOptionPane.INFORMATION_MESSAGE);
            return false;
        } else if(attachments.size() > 1) {
            String[] attachmentList = attachments.keySet().toArray(new String[0]);
            attachmentName = (String) JOptionPane.showInputDialog(
                    null, "Which file?", "Save attachment"
                    , JOptionPane.QUESTION_MESSAGE, null, attachmentList, attachmentList[0]);
            fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(attachmentName));
            return true;
        } else{
            attachmentName = email.getAttachmentName();
            fileChooser.setSelectedFile(new File(attachmentName));
            return true;
        }
    }

    public void getFileChoose(MimeBodyPart part){


        int result = fileChooser.showSaveDialog(fileChooser);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                part.saveFile(fileChooser.getSelectedFile());
                setDownloadStatusLabel("Done!");
            } catch (IOException | MessagingException ioException) {
                ioException.printStackTrace();
                setDownloadStatusLabel("Failed!");
            }
        }
    }

    public void refreshListener(ActionEvent e){
        emailReceiver.refresh();
    }

    public void readListener(ActionEvent e){
            Email email = inboxArea.getSelectedValue();
            writeEmail(email);


    }

    public void getAttachmentListener(ActionEvent e) {
        Email email = inboxArea.getSelectedValue();
        MimeBodyPart part = email.getAttachemtPart();
        setDownloadStatusLabel("Fetching attachment..");

        boolean hasAttachment = checkAttachmentAmount(email);

        if(hasAttachment) {
            getFileChoose(part);
        }
    }

    public void writeEmail(Email email){
        readEmailArea.setText(null);
        if (!email.getAttachmentName().equals("")) {
            readEmailArea.append("Attachments: ");
            HashMap<String, MimeBodyPart> attachments = email.getHashMap();
            for(Map.Entry<String, MimeBodyPart> entry : attachments.entrySet()){
                readEmailArea.append(entry.getKey() +" ");
            }
        }
        readEmailArea.append("Date: "+email.getDate() + "\n");
        readEmailArea.append("\n"+ email.getMessage());
    }

    public void setDownloadStatusLabel(String msg){
        downloadStatusLabel.setText("Download status: " +msg);
    }

    public static void main(String[] args) {
        new EmailClient();

    }


}

class EmailSender extends Thread{
    EmailClient emailClient;
    ArrayList<File> selectedFile;
    String username, password, toAddress, subject, msg;
    Session session;


    EmailSender(EmailClient emailClient, String username, String password, String toAddress, String subject, String msg, ArrayList<File> selectedFile){
        this.emailClient = emailClient;
        this.username = username;
        this.password = password;
        this.toAddress = toAddress;
        this.subject = subject;
        this.msg = msg;
        this.selectedFile = selectedFile;
    }

    public void run(){
        createSession();
        prepMessage();


    }

    public void createSession(){
        emailClient.setSentStatusLabel("Creating email..");
        Properties properties = new Properties();
        properties.setProperty("mail.imap.partialfetch", "false");
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", "smtp-mail.outlook.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.port","587");
        properties.put("mail.smtp.user", username);
        properties.put("mail.smtp.password", password);
        properties.put("mail.smtp.quitwait", "false");

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendMessage(MimeMessage message){
        try{
            Transport.send(message);
            emailClient.clearEmailInput();
            setSuccessMsg();
        } catch (MessagingException me){
            me.printStackTrace();
            emailClient.setSentStatusLabel("Something went wrong, try again.");
        }
    }

    public void setSuccessMsg(){
        emailClient.setSentStatusLabel("Email sent!");
    }

    public synchronized void prepMessage(){
        try {
            emailClient.setSentStatusLabel("Sending email..");
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(username, username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            message.setSubject(subject);
            message.setSentDate(new Date());

            if(selectedFile.size() > 0) {
                Multipart multipart = new MimeMultipart();
                BodyPart messageText = new MimeBodyPart();
                messageText.setText(msg);
                multipart.addBodyPart(messageText);

                for (File file : selectedFile) {
                    MimeBodyPart attachment = new MimeBodyPart();

                    String filename = file.getAbsolutePath();
                    attachment.attachFile(filename);

                    multipart.addBodyPart(attachment);
                    message.setContent(multipart);
                }
            }
            message.setText(msg);
            sendMessage(message);

        }  catch (UnsupportedEncodingException e) {
            emailClient.setSentStatusLabel("Something went wrong, try again.");
            e.printStackTrace();
        } catch (AddressException e) {
            emailClient.setSentStatusLabel("Something went wrong, try again.");
            e.printStackTrace();
        } catch (MessagingException e) {
            emailClient.setSentStatusLabel("Something went wrong, try again.");
            e.printStackTrace();
        } catch (IOException e) {
            emailClient.setSentStatusLabel("Something went wrong, try again.");
            e.printStackTrace();
        }

    }


}

class EmailReceiver{

    private EmailClient emailClient;
    private JList inboxArea;
    private JLabel unreadLabel, totalAmountLabel;
    private int unreadCount, totalCount;
    private String username, password;
    private boolean signedIn = false;
    private MimeBodyPart part;
    private DefaultListModel <String> l1;

    public EmailReceiver(EmailClient emailClient, String username, String password, DefaultListModel l1){
        this.emailClient = emailClient;
        this.inboxArea = emailClient.getInboxArea();
        this.unreadLabel = emailClient.getUnreadLabel();
        this.totalAmountLabel = emailClient.getTotalAmountLabel();
        this.username = username;
        this.password = password;
        this.l1 = l1;
    }

    public void clearTextArea(){
        inboxArea.removeAll();
    }

    public void setLabels(int unreadCount, int totalCount){
        unreadLabel.setText("Unread: "+unreadCount);
        totalAmountLabel.setText("Total amount of emails: "+totalCount);
    }

    public void start() {
        l1.removeAllElements();
        clearTextArea();
        fetchEmails();
        new ConstantRefresher(this, emailClient);
    }

    public void refresh(){
        l1.removeAllElements();
        clearTextArea();
        fetchEmails();
        }

    public void createEmail(String fromAddress, String subject, String sentDate, String messageContent, String attachFiles, MimeBodyPart part, HashMap <String, MimeBodyPart> attachments){
        Email newEmail = new Email(fromAddress,subject,sentDate,attachFiles,messageContent,part, l1, attachments);
        newEmail.add();
    }

    public String getSubject(Message msg)throws MessagingException, IOException {
            if (msg.getSubject() != null) {
                return msg.getSubject();
            } else
                return "No subject";
    }

    public void getEmailContent(Message msg) {
    try {

        String fromAddress = msg.getFrom()[0].toString();
        String subject = getSubject(msg);
        String sentDate = msg.getSentDate().toString();
        String attachFiles = "";
        String contentType = msg.getContentType();
        String messageContent = "";
        HashMap <String, MimeBodyPart> attachments = new HashMap<>();

        if(contentType.contains("multipart")){
         Multipart multipart = (Multipart) msg.getContent();
         int numberOfParts = multipart.getCount();
         for (int i = 0; i< numberOfParts; i++){
             part = (MimeBodyPart) multipart.getBodyPart(i);
             if(Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())){
                 attachFiles = part.getFileName();
                 attachments.put(attachFiles, part);
             } else{
                 messageContent = getTextFromEmail(msg);
             }
         }
         createEmail(fromAddress,subject,sentDate,messageContent, attachFiles, part, attachments);

        }
    } catch (MessagingException me){
        me.printStackTrace();
    } catch (IOException ioe){
        ioe.printStackTrace();
    }
    }

    public String getTextFromEmail(Message msg)throws MessagingException, IOException{
        String result = "";
        if(msg.isMimeType("text/plain")){
            result = msg.getContent().toString();
        } else if(msg.isMimeType("multipart/*")){
            MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    public String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();

        for(int i = 0; i < count; i++){
           BodyPart bodyPart = mimeMultipart.getBodyPart(i);
           if(bodyPart.isMimeType("text/plain")){
               result = result+ "\n"+bodyPart.getContent();
               break;
           } else  if(bodyPart.isMimeType("text/html")){
               String html = (String) bodyPart.getContent();
               result = result +"\n"+org.jsoup.Jsoup.parse(html).text();
           } else if(bodyPart.getContent() instanceof MimeMultipart){
               result = result+getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
           }
        }
        return result;
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
            emailStore.connect("smtp-mail.outlook.com", username, password);


            Folder inbox = emailStore.getFolder("INBOX");
            unreadCount = inbox.getUnreadMessageCount();
            totalCount = inbox.getMessageCount();
            setLabels(unreadCount, totalCount);

            inbox.open(Folder.READ_ONLY);

            Message [] messages = inbox.getMessages();
            ArrayUtils.reverse(messages);
            clearTextArea();



            for(int i = 0; i < messages.length; i++){
                if(i > 10){
                    break;
                }
                getEmailContent(messages[i]);

            }

        } catch (AuthenticationFailedException e){
            JOptionPane.showMessageDialog(emailClient,"Wrong email or password!", "Authentication failed", JOptionPane.ERROR_MESSAGE);
            emailClient.toggleSignInOut();
        } catch (MessagingException me){
            me.printStackTrace();
        }
    }

}

class ConstantRefresher extends Thread{
        private EmailReceiver emailReceiver;
        private EmailClient emailClient;
        private boolean alive = true;
        public ConstantRefresher(EmailReceiver emailReceiver, EmailClient emailClient){
            this.emailClient = emailClient;
            this.emailReceiver = emailReceiver;
            start();
        }

    public void run() {
        while (alive) {
            try {
                sleep(60000);
                emailClient.setDownloadStatusLabel("Refreshing..");
                System.out.println("Refreshing...");
                emailReceiver.refresh();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }


}

class Email {
    private String from;
    private String subject;
    private String date;
    private String attachmentName;
    private String message;
    private DefaultListModel <Email> defaultListModel;
    private MimeBodyPart attachemtPart;
    private HashMap <String, MimeBodyPart> attachments;

    public Email(String from, String subject, String date, String attachment, String message, MimeBodyPart part , DefaultListModel defaultListModel, HashMap <String, MimeBodyPart> attachments){
        this.from = from;
        this.subject = subject;
        this.date = date;
        this.attachmentName = attachment;
        this.message = message;
        this.defaultListModel = defaultListModel;
        this.attachemtPart = part;
        this.attachments = attachments;
    }

    public void add(){
        defaultListModel.addElement(this);
    }

    public MimeBodyPart getAttachemtPart(){
        return attachemtPart;
    }

    public String getAttachmentName(){
        return attachmentName.trim();
    }

    public String getMessage(){
        return message;
    }

    public String toString(){
        return "Subject: "+subject  + " From:" +from;
    }

    public HashMap getHashMap(){
        return attachments;
    }

    public String getDate(){
        return date;
    }
}
