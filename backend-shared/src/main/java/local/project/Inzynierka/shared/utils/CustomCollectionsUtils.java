package local.project.Inzynierka.shared.utils;

import java.util.List;
import java.util.stream.Collectors;

public class CustomCollectionsUtils {

    public static List<String> mapToLowerCase(List<String> list) {
        return list.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
