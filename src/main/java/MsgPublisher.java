import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.jmq.jmsserver.core.Session;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

/**
 * Created by Oleg on 12.04.14.
 */
public class MsgPublisher {
    ConnectionFactory factory=new ConnectionFactory();
    TopicConnection connection;

    MsgPublisher() {
        try {
            factory.setProperty(ConnectionConfiguration.imqAddressList,"mq://localhost:7676");
            connection=factory.createTopicConnection("admin","admin");
            TopicSession pubSession=connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic myTopic=pubSession.createTopic("Price_Drop_Alerts");
            TopicPublisher publisher=pubSession.createPublisher(myTopic);
            connection.start();
            TextMessage msg=pubSession.createTextMessage();
            msg.setText("The sale Apple stores bla bla bla");
            publisher.publish(msg);
        }
        catch(JMSException e){
            System.out.println("Error: "+e.getMessage());
        }
        finally {
            try {
                //Thread.sleep(10000);
                //connection.close();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        MsgPublisher msgPub=new MsgPublisher();
    }
}
