package com.ig.spring_boot_learning.base.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
public class ObjectResponse<T> {
    T results;
    ResponseMessage response = new ResponseMessage().success();

    public ObjectResponse(T results) {
        this.results = results;
    }
}
