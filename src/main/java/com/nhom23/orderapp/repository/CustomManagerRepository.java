package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.StaffDto;

public interface CustomManagerRepository {
    StaffDto deleteManager(Long id);
}
