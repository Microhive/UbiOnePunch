import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by JonathanLab on 19-05-2016.
 */
public class ServerBoard extends JFrame {
    private JTextArea messagesArea;
    private JButton sendButton;
    private JTextField message;
    private JButton startServer;
    private TCPServer mServer;

    private JButton gesture1;
    private JButton gesture2;
    private JButton gesture3;
    private JButton gesture4;
    private JButton gesture5;
    private JButton gesture6;
    private JButton gesture7;

    public ServerBoard() {

        super("ServerBoard");

        JPanel panelFields = new JPanel();
        panelFields.setLayout(new BoxLayout(panelFields,BoxLayout.X_AXIS));


        JPanel panelFields2 = new JPanel();
        panelFields2.setLayout(new BoxLayout(panelFields2,BoxLayout.X_AXIS));

        //here we will have the text messages screen
        messagesArea = new JTextArea();
        messagesArea.setColumns(30);
        messagesArea.setRows(10);
        messagesArea.setEditable(false);

        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get the message from the text view
                String messageText = message.getText();
                // add message to the message area
                messagesArea.append("\n" + messageText);
                // send the message to the client
                mServer.sendMessage(messageText);
                // clear text
                message.setText("");
            }
        });
        gesture1 = new JButton("Gesture 1");
        gesture1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageText = "1";
                messagesArea.append("\n" + "input to android: (" + messageText +")");
                mServer.sendMessage(messageText);
            }
        });
        gesture2 = new JButton("Gesture 2");
        gesture2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageText = "2";
                messagesArea.append("\n" + "input to android: (" + messageText +")");
                mServer.sendMessage(messageText);
            }
        });
        gesture3 = new JButton("Gesture 3");
        gesture3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageText = "3";
                messagesArea.append("\n" + "input to android: (" + messageText +")");
                mServer.sendMessage(messageText);
            }
        });
        gesture4 = new JButton("Gesture 4");
        gesture4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageText = "4";
                messagesArea.append("\n" + "input to android: (" + messageText +")");
                mServer.sendMessage(messageText);
            }
        });
        gesture5 = new JButton("Gesture 5");
        gesture5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageText = "5";
                messagesArea.append("\n" + "input to android: (" + messageText +")");
                mServer.sendMessage(messageText);
            }
        });
        gesture6 = new JButton("Gesture 6");
        gesture6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageText = "6";
                messagesArea.append("\n" + "input to android: (" + messageText +")");
                mServer.sendMessage(messageText);
            }
        });
        gesture7 = new JButton("Gesture 7");
        gesture7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageText = "7";
                messagesArea.append("\n" + "input to android: (" + messageText +")");
                mServer.sendMessage(messageText);
            }
        });

        startServer = new JButton("Start");
        startServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // disable the start button
                startServer.setEnabled(false);

                //creates the object OnMessageReceived asked by the TCPServer constructor
                mServer = new TCPServer(new TCPServer.OnMessageReceived() {
                    @Override
                    //this method declared in the interface from TCPServer class is implemented here
                    //this method is actually a callback method, because it will run every time when it will be called from
                    //TCPServer class (at while)
                    public void messageReceived(String message) {
                        messagesArea.append("\n "+message);
                    }
                });
                mServer.start();

            }
        });


        //the box where the user enters the text (EditText is called in Android)
        message = new JTextField();
        message.setSize(200, 20);

        //add the buttons and the text fields to the panel
        panelFields.add(messagesArea);
        panelFields.add(startServer);

        panelFields.add(gesture1);
        panelFields.add(gesture2);
        panelFields.add(gesture3);
        panelFields.add(gesture4);
        panelFields.add(gesture5);
        panelFields.add(gesture6);
        panelFields.add(gesture7);


        panelFields2.add(message);
        panelFields2.add(sendButton);

        getContentPane().add(panelFields);
        getContentPane().add(panelFields2);

        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));

        setSize(300, 170);
        setVisible(true);
    }
}
