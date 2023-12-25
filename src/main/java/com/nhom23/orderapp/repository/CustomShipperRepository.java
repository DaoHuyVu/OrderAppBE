package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.ShipperDto;
import com.nhom23.orderapp.model.Shipper;

public interface CustomShipperRepository {
    ShipperDto deleteShipper(Long id);
}
