package local.project.Inzynierka.shared.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CustomCollectionsUtils {

    public static List<String> mapToLowerCase(List<String> list) {
        return list.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    public static  <T> List<T> convertToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    public static  <T,R> List<R> convertToListMapping(Iterable<T> iterable, Function<? super T,? extends R> function ) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(function)
                .collect(Collectors.toList());
    }
}
