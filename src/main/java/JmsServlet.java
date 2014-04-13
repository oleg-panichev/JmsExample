import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Oleg on 13.04.14.
 */
public class JmsServlet extends HttpServlet {
    private Context jndiContext;
    private ConnectionFactory factory;
    private Queue ioQueue;
    private QueueReceiver qs;
    @Override
    public void init() throws ServletException {
        try {
            jndiContext = new InitialContext();
            factory = (ConnectionFactory) jndiContext.lookup("MyConnectionFactory");
            ioQueue = (Queue) jndiContext.lookup("MyJMSTestQueue");
            qs=new QueueReceiver(factory,ioQueue);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out=response.getWriter();
        out.print(qs.receiveMessages());
    }
}
