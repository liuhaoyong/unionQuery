package com.wac.query.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.TypeReference;

/**
 * @author huangjisnheng
 */
public class JsonTool {
    private static final Logger log = Logger.getLogger(JsonTool.class);
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    static {
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    public static void main(String[] args) throws Exception {
//
    }
    
    public static String writeListValueAsString(Collection<? extends Jsonable> jsonableList) {
        List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
        for (Jsonable jsonable : jsonableList) {
            list.add(jsonable.getMap4Json());
        }
        return writeValueAsString(list);
    }
    
    public static String writeValueAsString(Jsonable jsonable) {
        return writeValueAsString(jsonable.getMap4Json());
    }
    
    /**JSON���л�*/
    public static String writeValueAsString(Object obj) {
        if (obj instanceof Jsonable) {
            return writeValueAsString((Jsonable) obj);
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonGenerationException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex.getMessage());
        } catch (JsonMappingException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
    
    /**JSON�����л�*/
    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonParseException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex.getMessage());
        } catch (JsonMappingException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    /**JSON�����л�*/
    public static ArrayList<Map<String, Object>> readValue2List(String json) {
        ArrayList<Map<String, Object>> list = null;
        try {
            list = mapper.readValue(json, TypeFactory.collectionType(ArrayList.class, Map.class));
        } catch (JsonParseException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex.getMessage());
        } catch (JsonMappingException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex.getMessage());
        }
        return list;
    }
    
    /**JSON�����л�*/
    public static <T> ArrayList<T> readValue2List(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, TypeFactory.collectionType(ArrayList.class, clazz));
        } catch (JsonParseException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex.getMessage());
        } catch (JsonMappingException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    /**JSON�����л�*/
    public static <T> ArrayList<T> readValue2List(String json, TypeReference<List<T>> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (JsonParseException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex.getMessage());
        } catch (JsonMappingException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

	public static <K, V> Map<K, V> readValue2Map(String json, Class<K> keyClazz, Class<V> valueClazz) {
		try {
			return mapper.readValue(json, TypeFactory.mapType(Map.class, keyClazz, valueClazz));
		} catch (JsonParseException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		} catch (JsonMappingException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		}
	}
    public ObjectMapper getMapper() {
        return mapper;
    }
    
    
    

}
