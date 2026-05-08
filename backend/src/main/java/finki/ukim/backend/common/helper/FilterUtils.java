package finki.ukim.backend.common.helper;

public class FilterUtils {
    public static String normalizeTextFilter(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    public static String[] normalizeTextFilters(String... values) {
        String[] normalized = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            normalized[i] = normalizeTextFilter(values[i]);
        }

        return normalized;
    }
}