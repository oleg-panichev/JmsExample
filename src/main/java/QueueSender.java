import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;

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
public class QueueSender {
    String mqAddressList;
    String queueName;
    Session session=null;
    ConnectionFactory factory;
    QueueConnection connection=null;
    MessageProducer queueSender;
    MessageConsumer consumer=null;

    QueueSender(String mqAddressList, String queueName) {
        this.mqAddressList=mqAddressList;
        this.queueName=queueName;
        try {
            factory=new com.sun.messaging.ConnectionFactory();
            factory.setProperty(ConnectionConfiguration.imqAddressList,mqAddressList);
            connection=factory.createQueueConnection("admin","admin");
            connection.start();
            session=connection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
            Queue ioQueue=session.createQueue(queueName);
            queueSender=session.createProducer(ioQueue);
        }
        catch(JMSException e) {
            System.out.println("Error: "+e.getMessage());
        }
    }

    public void sendMessage(String message) {
        try {
            TextMessage outMsg=session.createTextMessage(message);
            queueSender.send(outMsg);
        }
        catch(JMSException e) {
            System.out.println("Exception: "+e.getMessage());
        }
    }
}
