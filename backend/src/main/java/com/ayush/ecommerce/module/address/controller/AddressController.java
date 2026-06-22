package com.ayush.ecommerce.module.address.controller;

import com.ayush.ecommerce.module.address.dto.AddressRequest;
import com.ayush.ecommerce.module.address.dto.AddressResponse;
import com.ayush.ecommerce.module.address.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public AddressResponse createAddress(
            @Valid
            @RequestBody
            AddressRequest request
    ) {
        return addressService.createAddress(request);
    }

    @GetMapping
    public List<AddressResponse> getMyAddresses() {
        return addressService.getMyAddresses();
    }

    @PutMapping("/{id}")
    public AddressResponse updateAddress(
            @PathVariable Long id,
            @Valid
            @RequestBody
            AddressRequest request
    ) {
        return addressService.updateAddress(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(
            @PathVariable Long id
    ) {
        addressService.deleteAddress(id);
    }

    @PutMapping("/{id}/default")
    public void setDefaultAddress(
            @PathVariable Long id
    ) {
        addressService.setDefaultAddress(id);
    }
}