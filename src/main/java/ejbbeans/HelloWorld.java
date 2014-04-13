package ejbbeans;

import javax.ejb.Stateless;

/**
 * Created by Oleg on 13.04.14.
 */
@Stateless
public class HelloWorld {
    public HelloWorld() {
    }

    public String sayHello() {
        return "Hello World!";
    }
}
