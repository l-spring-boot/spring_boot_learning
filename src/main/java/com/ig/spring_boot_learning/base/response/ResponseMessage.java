package com.ig.spring_boot_learning.base.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {
    private int code;
    private String message;

    public ResponseMessage success() {
        return new ResponseMessage(200, "Success");
    }

    public ResponseMessage notFound() {
        return new ResponseMessage(404, "Not found");
    }
}
