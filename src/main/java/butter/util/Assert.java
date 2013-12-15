package butter.util;

/**
 * Created with IntelliJ IDEA.
 * User: Lizhongyuan
 * Date: 13-12-13
 * Time: 上午11:29
 */
public abstract class Assert {
    /**
     * Assert that an object is not <code>null</code> .
     * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
     *
     * @param object  the object to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object is <code>null</code>
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that an object is not {@code null} .
     * <pre class="code">Assert.notNull(clazz);</pre>
     *
     * @param object the object to check
     * @throws IllegalArgumentException if the object is {@code null}
     */
    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    /**
     * Assert that an array has elements; that is, it must not be
     * {@code null} and must have at least one element.
     * <pre class="code">Assert.notEmpty(array, "The array must have elements");</pre>
     *
     * @param array   the array to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object array is {@code null} or has no elements
     */
    public static void notEmpty(Object[] array, String message) {
        if (isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Determine whether the given array is empty:
     * i.e. {@code null} or of zero length.
     *
     * @param array the array to check
     */
    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

    /**
     * Assert that an array has elements; that is, it must not be
     * {@code null} and must have at least one element.
     * <pre class="code">Assert.notEmpty(array);</pre>
     *
     * @param array the array to check
     * @throws IllegalArgumentException if the object array is {@code null} or has no elements
     */
    public static void notEmpty(Object[] array) {
        notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
    }

    /**
     * Assert that an array has no null elements.
     * Note: Does not complain if the array is empty!
     * <pre class="code">Assert.noNullElements(array, "The array must have non-null elements");</pre>
     *
     * @param array   the array to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object array contains a {@code null} element
     */
    public static void noNullElements(Object[] array, String message) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    /**
     * Assert that an array has no null elements.
     * Note: Does not complain if the array is empty!
     * <pre class="code">Assert.noNullElements(array);</pre>
     *
     * @param array the array to check
     * @throws IllegalArgumentException if the object array contains a {@code null} element
     */
    public static void noNullElements(Object[] array) {
        noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
    }
}
