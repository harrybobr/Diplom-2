package settings;

import net.datafaker.Faker;

public class DataGen {

  private static final Faker faker = new Faker();

  public static String genName() {
    return faker.name().firstName();

  }

  public static String genPassword() {
    return faker.internet().password(8, 16);
  }

  public static String genEmail() {
    return faker.internet().emailAddress();
  }
}
