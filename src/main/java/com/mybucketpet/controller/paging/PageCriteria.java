package com.mybucketpet.controller.paging;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PageCriteria {
    private int pageNum;    // 현재 페이지 번호
    private int amount;     // 1페이지당 보여줄 개수
    private static final int DEFAULT_AMOUNT = 20;

    public PageCriteria() {
        this(1, DEFAULT_AMOUNT);
    }

    public PageCriteria(int pageNum) {
        this(pageNum, DEFAULT_AMOUNT);
    }

    public PageCriteria(int pageNum, int amount) {
        this.pageNum = pageNum;
        this.amount = amount;
    }
}
