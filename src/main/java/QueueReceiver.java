import com.sun.messaging.ConnectionConfiguration;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Created by Oleg on 12.04.14.
 */
public class QueueReceiver { //implements javax.jms.MessageListener {
    String mqAddressList;
    String queueName;
    Session session=null;
    QueueConnection connection=null;
    MessageProducer queueSender;
    MessageConsumer consumer=null;

    QueueReceiver(String mqAddressList, String queueName) {
        this.mqAddressList=mqAddressList;
        this.queueName=queueName;
        try {
            com.sun.messaging.ConnectionFactory factory=new com.sun.messaging.ConnectionFactory();
            factory.setProperty(ConnectionConfiguration.imqAddressList,mqAddressList);
            connection=factory.createQueueConnection("admin","admin");
            connection.start();
            session=connection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
            Queue ioQueue=session.createQueue(queueName);
            consumer=session.createConsumer(ioQueue);
            //consumer.setMessageListener(this);
        }
        catch(JMSException e) {
            System.out.println("Error: "+e.getMessage());
        }
    }

    QueueReceiver(javax.jms.ConnectionFactory factory, Queue ioQueue) {
        try {
            Connection connection=factory.createConnection("admin","admin");
            connection.start();
            Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            consumer=session.createConsumer(ioQueue);
            //consumer.setMessageListener(this);
        }
        catch(JMSException e) {
            System.out.println("Error: "+e.getMessage());
        }
    }

    //@Override
    public void onMessage(Message message) {
        String msgText;
        try {
            if(message instanceof TextMessage) {
                msgText=((TextMessage)message).getText();
                System.out.println(queueName+": "+msgText);
            } else {
                System.out.println("Non-text message.");
            }
        }
        catch(JMSException e) {
            System.out.println("Error while consuming a message: "+e.getMessage());
        }
    }

    public String receiveMessages() {
        String msgText="";
        try {
            Message message=consumer.receive();
            if(message instanceof TextMessage) {
                msgText=((TextMessage)message).getText();
            } else {
                msgText="Non-text message";
            }
        }
        catch(JMSException e) {
            msgText="Error while consuming a message: "+e.getMessage();
        }
        catch(NullPointerException e) {
            msgText="No messages!";
        }
        if(msgText.length()==0) {
            msgText="No messages!";
        }
        return msgText;
    }
}
