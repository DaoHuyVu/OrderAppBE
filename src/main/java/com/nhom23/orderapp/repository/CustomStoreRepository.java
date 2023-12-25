package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.model.Store;

public interface CustomStoreRepository {
    Store deleteByStoreId(Long id);
}
