package models.services;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RuntimeTypeAdapterFactory<T> implements TypeAdapterFactory {
    private final Class<?> baseType;
    private final String typeFieldName;
    private final Map<String, Class<? extends T>> labelToSubtype = new HashMap<>();
    private final Map<Class<? extends T>, String> subtypeToLabel = new HashMap<>();

    private RuntimeTypeAdapterFactory(Class<?> baseType, String typeFieldName) {
        this.baseType = baseType;
        this.typeFieldName = typeFieldName;
    }

    public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> baseType, String typeFieldName) {
        return new RuntimeTypeAdapterFactory<>(baseType, typeFieldName);
    }

    public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> type, String label) {
        labelToSubtype.put(label, type);
        subtypeToLabel.put(type, label);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> TypeAdapter<R> create(Gson gson, TypeToken<R> type) {
        if (!baseType.isAssignableFrom(type.getRawType())) {
            return null;
        }

        return (TypeAdapter<R>) new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }

                Class<?> srcClass = value.getClass();
                String label = subtypeToLabel.get(srcClass);
                if (label == null) {
                    throw new JsonParseException("Unknown subtype: " + srcClass);
                }

                // گرفتن TypeAdapter اختصاصی این زیرکلاس
                @SuppressWarnings("unchecked")
                TypeAdapter<T> delegate = (TypeAdapter<T>) gson.getDelegateAdapter(RuntimeTypeAdapterFactory.this, TypeToken.get(srcClass));

                // تولید JSON بدون ورود به چرخه بازگشتی
                JsonObject jsonObject = delegate.toJsonTree(value).getAsJsonObject();

                // افزودن نوع (type field)
                jsonObject.addProperty(typeFieldName, label);

                // نوشتن در JSON
                Streams.write(jsonObject, out);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                JsonObject jsonObject = Streams.parse(in).getAsJsonObject();
                JsonElement labelJsonElement = jsonObject.get(typeFieldName);
                if (labelJsonElement == null) {
                    throw new JsonParseException("Missing type field: " + typeFieldName);
                }

                String label = labelJsonElement.getAsString();
                Class<? extends T> subtype = labelToSubtype.get(label);
                if (subtype == null) {
                    throw new JsonParseException("Unknown subtype label: " + label);
                }

                @SuppressWarnings("unchecked")
                TypeAdapter<T> delegate = (TypeAdapter<T>) gson.getDelegateAdapter(RuntimeTypeAdapterFactory.this, TypeToken.get(subtype));
                return delegate.fromJsonTree(jsonObject);
            }
        };
    }
}
