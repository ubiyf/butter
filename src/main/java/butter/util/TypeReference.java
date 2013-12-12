package butter.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class TypeReference<T> {

    private final Type type;

    protected TypeReference() {
        Type superClass = getClass().getGenericSuperclass();

        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }

    public final static Type LIST_BYTES = new TypeReference<List<byte[]>>() {
    }.getType();
}