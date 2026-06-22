package com.ayush.ecommerce.module.address.service;

import com.ayush.ecommerce.module.address.dto.AddressRequest;
import com.ayush.ecommerce.module.address.dto.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse createAddress(
            AddressRequest request
    );

    List<AddressResponse> getMyAddresses();

    AddressResponse updateAddress(
            Long addressId,
            AddressRequest request
    );

    void deleteAddress(
            Long addressId
    );

    void setDefaultAddress(
            Long addressId
    );
}