package com.jackinjava.qywx.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserCompareResult {

    private List<UserCompare> userCompares;

    private List<Integer> cacheIndex;



    public UserCompareResult(List<UserCompare> userCompares, List<Integer> cacheIndex) {
        this.userCompares = userCompares;
        this.cacheIndex = cacheIndex;
    }
}
