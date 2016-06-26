package com.meshtasks.utils;

import java.io.StringReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

/**
 * This Utility class performs JSON operations.
 * @author dipeshkumar mistry
 *
 */

public class JsonUtils {

    private static Gson gsonObject = new GsonBuilder().create();

    /**
     * Method deserialize the given object and create the String.
     * @param object
     * @return {@link String}
     */
    public static String createJSONDataFromObject(Object object) {
        if ( object == null ) return null;
        return gsonObject.toJson(object);
    }
    
    /**
     * Method serialize the given String and create object of given Class type.
     * @param data
     * @param valueType
     * @return T
     */
    public static <T> T createObjectFromJsonData(String data, Class<T> valueType) {
        JsonReader reader = new JsonReader(new StringReader(data));
        reader.setLenient(true);
        return gsonObject.fromJson(reader, valueType);
    }
    
    /**
     * Method serialize the given String and create object of given Class type.
     * @param data
     * @param valueType
     * @return T
     */
    public static <T> T createObjectFromTree(Object obj, Class<T> valueType) {
        return createObjectFromJsonData(gsonObject.toJsonTree(obj).toString(), valueType);
    }
    
    /**
     * Method create single element for given Key and its value.
     * @param key
     * @param value
     * @return {@link String}
     */
    public static String getSingleElement(String key, String value) {
        JsonObject object = new JsonObject();
        object.add(key, new JsonPrimitive(value));
        return gsonObject.toJson(object);
    }
    
    /**
     * Method will read given JSON data and read specific given node value.
     * @param json
     * @param nodeName
     * @return {@link String}
     */
    public static String getNodeValue(String json, String nodeName) {
        if ( CommonUtils.isEmpty(json) || CommonUtils.isEmpty(nodeName) ) return null;
        JsonObject jsonObject = gsonObject.fromJson(json, JsonObject.class);
        JsonElement element = jsonObject.get(nodeName);
        if ( element != null ) {
            return element.getAsString();
        }
        return null;
    }
}