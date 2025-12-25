package com.github.yajatkaul.mega_showdown.render;

import com.github.yajatkaul.mega_showdown.MegaShowdown;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class LoaderInitializer {

    @SuppressWarnings("unchecked")
    public static <T> T getImplInstance(Class<T> abstractClss, String... impls) {
        if (impls == null || impls.length == 0)
            throw new IllegalStateException("Couldn't create an instance of " + abstractClss + ". No implementations provided!");
        Class<?> clss = null;
        int i = 0;
        while (clss == null && i < impls.length) {
            try {
                clss = Class.forName(impls[i]);
            } catch (ClassNotFoundException ignored) {
            }
            i++;
        }
        if (clss == null)
            MegaShowdown.LOGGER.error("No Implementation of {} found with given paths {}", abstractClss, Arrays.toString(impls));
        else if (abstractClss.isAssignableFrom(clss)) {
            try {
                Constructor<T> constructor = (Constructor<T>) clss.getDeclaredConstructor();
                return constructor.newInstance();
            } catch (NoSuchMethodException e) {
                MegaShowdown.LOGGER.error("Implementation of {} needs to provide an no arg constructor", clss);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                MegaShowdown.LOGGER.error(e.getMessage());
            }
        }
        throw new IllegalStateException("Couldn't create an instance of " + abstractClss);
    }
}