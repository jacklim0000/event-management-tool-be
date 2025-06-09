package com.unitask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.List;

public class PageResponse<T, U> {

    private final Page<T> page;

    private final List<U> content;

    public PageResponse(Page<T> page, List<U> content) {
        Assert.notNull(page, "Page must not be null");
        this.page = page;
        this.content = content;
    }

    @JsonProperty
    public List<U> getContent() {
        return this.content;
    }

    @Nullable
    @JsonProperty("page")
    public PagedModel.PageMetadata getMetadata() {
        return new PagedModel.PageMetadata((long) this.page.getSize(), (long) this.page.getNumber(), this.page.getTotalElements(), (long) this.page.getTotalPages());
    }

    public static record PageMetadata(long size, long number, long totalElements, long totalPages) {
        public PageMetadata(long size, long number, long totalElements, long totalPages) {
            Assert.isTrue(size > -1L, "Size must not be negative!");
            Assert.isTrue(number > -1L, "Number must not be negative!");
            Assert.isTrue(totalElements > -1L, "Total elements must not be negative!");
            Assert.isTrue(totalPages > -1L, "Total pages must not be negative!");
            this.size = size;
            this.number = number;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
        }

        public long size() {
            return this.size;
        }

        public long number() {
            return this.number;
        }

        public long totalElements() {
            return this.totalElements;
        }

        public long totalPages() {
            return this.totalPages;
        }
    }
}
