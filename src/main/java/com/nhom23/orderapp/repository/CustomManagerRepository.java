package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.ManagerDto;
import com.nhom23.orderapp.model.Manager;

public interface CustomManagerRepository {
    ManagerDto deleteManager(Long id);
}
