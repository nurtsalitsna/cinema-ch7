package com.spring.binar.challenge_5.utils;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ResponseHandler {

    private ResponseHandler(){}
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("message", message);
        map.put("status", status.value());

        if(responseObj == null){
            map.put("data", null);
            map.put(Constants.TOTAL_RECORDS, 0);
        } else if(responseObj instanceof List<?> list){
            map.put(Constants.TOTAL_RECORDS, list.size());
            map.put("data", responseObj);
        }else {
            var list = new ArrayList<>();
            list.add(responseObj);
            map.put("data", list);
            map.put(Constants.TOTAL_RECORDS, list.size());
        }

        return new ResponseEntity<>(map, status);
    }

    public static ResponseEntity<Object> generatePagingResponse(String message, HttpStatus status, Page<?> responseObj) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("status", status.value());
        map.put("message", message);

        if(responseObj == null){
            map.put("data", null);
            map.put(Constants.TOTAL_RECORDS, 0);
            map.put("pageNumber", 0);
            map.put("pageSize", 0);
        } else{
            map.put("data", responseObj.getContent());
            map.put(Constants.TOTAL_RECORDS, responseObj.getTotalElements());
            map.put("pageNumber", responseObj.getNumber());
            map.put("pageSize", responseObj.getSize());
        }


        return new ResponseEntity<>(map, status);
    }
}
