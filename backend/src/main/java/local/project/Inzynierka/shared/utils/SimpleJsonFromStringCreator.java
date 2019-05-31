package local.project.Inzynierka.shared.utils;

public class SimpleJsonFromStringCreator {

    private SimpleJsonFromStringCreator(){}

    public static String toJson(String string) {
        return "{\"data\":\""+string+"\"}";
    }
}
