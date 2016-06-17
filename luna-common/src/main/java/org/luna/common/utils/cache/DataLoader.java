package org.luna.common.utils.cache;

/**
 * Created by luliru on 2016/6/3.
 */
public interface DataLoader {

    public <T> T load(String key, Class<T> clazz);
}
