package email;

import org.springframework.core.annotation.Order;

/**
 * Created by ZWH on 4/20/2014.
 */
public interface OrderManager {

    void placeOrder(Order order);

}
