import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import weka.core.Instances;


import static java.lang.System.out;

/**
 * Created by JonathanLab on 19-05-2016.
 */
public class ServerBoard extends JFrame {
    private JTextArea messagesArea;
    private JButton sendButton;
    private JButton predict;
    private JTextField message;
    private JButton startServer;
    private JButton restartServer;
    private TCPServer mServer;
    private WekaTrain wekatrain;

    private JButton gesture1;
    private JButton gesture2;
    private JButton gesture3;
    private JButton gesture4;
    private JButton gesture5;
    private JButton gesture6;
    private JButton gesture7;
    private String ipAdress;

    public ServerBoard() {

        super("ServerBoard");

        JPanel panelFields = new JPanel();
        panelFields.setLayout(new BoxLayout(panelFields, BoxLayout.X_AXIS));


        JPanel panelFields2 = new JPanel();
        panelFields2.setLayout(new BoxLayout(panelFields2, BoxLayout.X_AXIS));

        wekatrain = new WekaTrain();
        wekatrain.LoadTrainset();
        readfile FR = new readfile();
        FR.readfile();
        wekatrain.sendGesture();


        //here we will have the text messages screen
/*        messagesArea = new JTextArea();
        messagesArea.setColumns(30);
        messagesArea.setRows(10);
        messagesArea.setEditable(false);

        messagesArea = new JTextArea ( 16, 58 );
        messagesArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(messagesArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


        */
        final JTextArea messageArea;
        messagesArea = new JTextArea(16, 58);
        DefaultCaret caret = (DefaultCaret) messagesArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane sampleScrollPane = new JScrollPane(messagesArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


        // create the middle panel components
        //messagesArea.setEditable ( false ); // set textArea non-editable
        //JScrollPane sp = new JScrollPane(ta);


        //frame.setVisible ( true );

        // getting the ip adress of this host


        predict = new JButton("Predict");
        predict.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              int gesture =  wekatrain.sendGesture();
                messagesArea.append("\n" + "gesture id: (" + gesture +")");
                mServer.sendMessage(""+gesture);
            }
        });

       // messagesArea.append(ipAdress);
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
        restartServer = new JButton("restart");
        restartServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get the message from the text view
                mServer.restart();
                // add message to the message area
                messagesArea.append("\n" + "restarted");
                // send the message to the client
                mServer.sendMessage("connected from server");
                // clear text

                //listNets();
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
                messagesArea.append("\n" + "Started server on" + ipAdress);

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
                listNets();
            }
        });


        //the box where the user enters the text (EditText is called in Android)
        message = new JTextField();
        message.setSize(200, 20);

        //add the buttons and the text fields to the panel
        panelFields.add(sampleScrollPane);
        //panelFields.add(sampleScrollPane);
        panelFields.add(startServer);
        panelFields.add(restartServer);
        panelFields.add(gesture1);
        panelFields.add(gesture2);
        panelFields.add(gesture3);
        panelFields.add(gesture4);
        panelFields.add(gesture5);
        panelFields.add(gesture6);
        panelFields.add(gesture7);


        panelFields2.add(message);
        panelFields2.add(sendButton);
        panelFields2.add(predict);

        getContentPane().add(panelFields);
        getContentPane().add(panelFields2);

        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));

        setSize(300, 170);
        setVisible(true);
    }
    public void listNets(){
        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();

            for (NetworkInterface netint : Collections.list(nets))
                displayInterfaceInformation(netint);


        } catch (SocketException Se) {
            return;
        }
    }



    public void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        out.printf("Display name: %s\n", netint.getDisplayName());
        out.printf("Name: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {

            out.printf("InetAddress: %s\n", inetAddress);
            messagesArea.append("InetAddress: %s\n" + inetAddress.toString());
        }
        out.printf("\n");
    }
}
