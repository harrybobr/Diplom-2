package user;

import pojo.User;
import settings.DataGen;

public class UserGen {
  public static User getRandomUser() {
    return new User(DataGen.genEmail(), DataGen.genPassword(), DataGen.genName());
  }
}
