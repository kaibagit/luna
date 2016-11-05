package org.luna.rpc.filter;

import java.util.Comparator;

/**
 * Filter排序
 * Created by luliru on 2016/11/4.
 */
public class FilterComparator implements Comparator<Filter> {

    @Override
    public int compare(Filter filter1, Filter filter2) {
        return filter2.getOrder() - filter1.getOrder();
    }
}
