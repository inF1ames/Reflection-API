import service.JsonSerialization;
import java.time.LocalDate;
import java.time.Month;

public class App {
    public static void main(String[] args) {
        JsonSerialization json = new JsonSerialization();
        Human human = new Human();
        human.setFirstName("Bob");
        human.setLastName("Sponge");
        human.setHobby(null);
        human.setBirthDate(LocalDate.of(1996, Month.APRIL, 13));


        String s = json.toJson(human);
        System.out.println(s);

    }
}
