package com.nhom23.orderapp.response;

import com.nhom23.orderapp.model.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MenuResponse<T> {
    private T menuResponse;
}
