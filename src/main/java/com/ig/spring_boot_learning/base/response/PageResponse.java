package com.ig.spring_boot_learning.base.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> results;
    private long total;
    private ResponseMessage response = new ResponseMessage().success();

    public PageResponse(List<T> results, long total) {
        this.results = results;
        this.total = total;
    }
}
