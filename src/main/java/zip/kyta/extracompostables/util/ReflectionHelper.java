package zip.kyta.extracompostables.util;

public class ReflectionHelper {
    public static <T> T getStaticValue(Class<?> clazz, String fieldName) {
        try {
            var field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);
            return (T) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
