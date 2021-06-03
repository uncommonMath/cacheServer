package cacheServer.repository;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public final class RepositoryFactory {
    public static Repository create()
    {
        try {
            Properties properties = new Properties();
            properties.load(RepositoryFactory.class.getResourceAsStream("/datastructures.properties"));
            return (Repository)Class.
                    forName(properties.getProperty("dataStructureClass")).
                    getDeclaredConstructor().
                    newInstance();
        } catch (InstantiationException |
                IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException |
                ClassNotFoundException |
                IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
