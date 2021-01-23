package com.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public
class Meta {

    private int pageNo;
    private int pageSize;
    private int itemsCount;
    private long totalItems;

    public Meta(int pageNo, int pageSize,
                Long totalItems, int itemsCount) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.itemsCount = itemsCount;
        this.totalItems = totalItems;
    }

}
