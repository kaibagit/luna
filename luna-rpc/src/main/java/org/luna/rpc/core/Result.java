package org.luna.rpc.core;

/**
 * Created by luliru on 2016/11/1.
 */
public interface Result {

    /**
     * Get invoke result.
     *
     * @return result. if no result return null.
     */
    Object getValue();

    /**
     * Get exception.
     *
     * @return exception. if no exception return null.
     */
    Exception getException();

    /**
     * Has exception.
     *
     * @return has exception.
     */
    boolean hasException();

}
