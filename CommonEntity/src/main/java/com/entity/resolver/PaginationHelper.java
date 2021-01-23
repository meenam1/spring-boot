package com.entity.resolver;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaginationHelper<T> {

    public List<T> doPagination(List<T> collection, int pageNo, int pageSize) {
        collection = collection.subList((pageNo * pageSize),
                (pageNo + 1) * pageSize);
        return collection;
    }
}
