package order;

import java.util.List;
import pojo.Order;

public class OrderGen {

  public static Order getListOrder() {

    List<String> ingredientIds = OrderStep.getAllIngredients().extract().path("data._id");
    Order order = new Order();
    order.setIngredients(ingredientIds);
    return order;
  }
}
