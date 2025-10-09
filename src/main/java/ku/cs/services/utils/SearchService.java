package ku.cs.services.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SearchService<T> {

    /**
     * @param items list of items
     * @param keyword something to search
     * @param extractors filter
     * @return {@link List<T>} of item matches
     */
    @SafeVarargs
    public final List<T> search(List<T> items, String keyword, Function<T, String>... extractors) {
        if (keyword == null || keyword.isBlank()) {
            return items;
        }
        String lower = keyword.trim().toLowerCase();

        return items.stream()
                .filter(item -> {
                    for (Function<T, String> extractor : extractors) {
                        String value = extractor.apply(item);
                        if (value != null && value.toLowerCase().contains(lower)) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }
}
