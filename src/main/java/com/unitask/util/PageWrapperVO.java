package com.unitask.util;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@JsonPropertyOrder({"pageSize", "pageNumber", "numberOfElements", "totalElements", "totalPages",
        "first", "last", "hasNext", "hasPrevious", "hasContent", "content"})
@Data
public class PageWrapperVO<T> {

    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final List<T> content;
    private final boolean first;
    private final boolean last;
    private final boolean hasPrevious;
    private final boolean hasNext;
    private final boolean hasContent;


    public PageWrapperVO(@SuppressWarnings("rawtypes") Page pageDomain, List<T> content) {
        super();
        this.pageSize = pageDomain.getSize();
        this.pageNumber = pageDomain.getNumber() + 1;
        this.totalElements = pageDomain.getTotalElements();
        this.totalPages = pageDomain.getTotalPages();
        this.content = content;
        this.first = pageDomain.isFirst();
        this.last = pageDomain.isLast();
        this.hasPrevious = pageDomain.hasPrevious();
        this.hasNext = pageDomain.hasNext();
        this.hasContent = pageDomain.hasContent();
    }
}
