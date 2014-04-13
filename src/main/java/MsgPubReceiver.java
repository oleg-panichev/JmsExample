import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

/**
 * Created by Oleg on 12.04.14.
 */
public class MsgPubReceiver implements MessageListener {
    ConnectionFactory factory=new ConnectionFactory();
    TopicConnection connection;

    MsgPubReceiver() {
        try {
            factory.setProperty(ConnectionConfiguration.imqAddressList,"mq://localhost:7676");
            connection=factory.createTopicConnection("admin","admin");
            TopicSession pubSession=connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic myTopic=pubSession.createTopic("Price_Drop_Alerts");
            TopicSubscriber subscriber=pubSession.createSubscriber(myTopic);
            connection.start();
            subscriber.setMessageListener(this);
        } catch(JMSException e){
            System.out.println("Error: "+e.getMessage());
        }
        finally {
//            try {
//                connection.close();
//            }
//            catch (JMSException e) {
//                System.out.println(e.getMessage());
//            }
        }
    }

    @Override
    public void onMessage(Message msg) {
        String msgText;
        try {
            if(msg instanceof TextMessage) {
                msgText=((TextMessage)msg).getText();
                System.out.println("Got from the queue "+msgText);
            } else {
                System.out.println("Got nontext");
            }
        }
        catch(JMSException e) {
            System.out.println("Error while consuming a message: "+e.getMessage());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MsgPubReceiver r=new MsgPubReceiver();
        Thread.sleep(10000);
    }
}
