package com.calefit.common.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CommonDtoList<T> {

    private final Integer count;
    private final List<T> data;

    public CommonDtoList(List<T> data) {
        this.data = data;
        this.count = data.size();
    }
}
