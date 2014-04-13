import com.sun.messaging.ConnectionFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by Oleg on 12.04.14.
 */
public class CoursesMessanger {
    public static void main(String[] args) throws IOException, NamingException {
//        initForm();
        Properties properties=new Properties();
        properties.setProperty("java.naming.factory.initial","com.sun.enterprise.naming.SerialInitContextFactory");

        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
        properties.put("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
        properties.put("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
        properties.put("org.omg.CORBA.ORBInitialHost", "192.168.1.110");
        properties.put("org.omg.CORBA.ORBInitialPort", "3700");

        InitialContext ic=new InitialContext(properties);

//        // If Java client is running inside Java EE server that runs the naming service
//        InitialContext ic=new InitialContext();

        Context jndiContext=new InitialContext();
        ConnectionFactory factory=(ConnectionFactory)jndiContext.lookup("MyTestConnectionFactory");
        Queue ioQueue=(Queue)jndiContext.lookup("MyJMSTestQueue");


        String command="";
        String mqAddressList="mq://192.168.7.157:7676";
        String queueName1="OT";
        QueueSender qs1=new QueueSender(mqAddressList,queueName1);
        String queueName2="TO";
        QueueReceiver qs2=new QueueReceiver(mqAddressList,queueName2);
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        command = bufferRead.readLine();
        while(!command.equals("/exit")) {
            if (command.startsWith(queueName1)) {
                qs1.sendMessage(command.substring(queueName1.length()));
            }
            command = bufferRead.readLine();
        }
    }

    public static void initForm() {
        JFrame frame=new JFrame("Chat");
        frame.setSize(400,400);
        JPanel jp1=new JPanel();
        jp1.setLayout(new GridLayout(2,1));
        JPanel jp2=new JPanel();
        jp1.setLayout(new GridLayout(2,1));

        JTextField tf1=new JTextField(100);
        jp1.add(tf1);
        JTextField tf2=new JTextField(100);
        jp2.add(tf2);

        frame.setContentPane(jp1);
        frame.setVisible(true);
    }
}
