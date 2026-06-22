package com.ayush.ecommerce.module.address.service;

import com.ayush.ecommerce.common.security.SecurityUtils;
import com.ayush.ecommerce.exception.AddressNotFoundException;
import com.ayush.ecommerce.exception.UserNotFoundException;
import com.ayush.ecommerce.module.address.dto.AddressRequest;
import com.ayush.ecommerce.module.address.dto.AddressResponse;
import com.ayush.ecommerce.module.address.entity.Address;
import com.ayush.ecommerce.module.address.repository.AddressRepository;
import com.ayush.ecommerce.module.auth.entity.User;
import com.ayush.ecommerce.module.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public AddressResponse createAddress(
            AddressRequest request
    ) {

        User user = getCurrentUser();

        Address address = Address.builder()
                .user(user)
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .postalCode(request.getPostalCode())
                .build();

        Address savedAddress =
                addressRepository.save(address);

        return mapToResponse(savedAddress);
    }

    @Override
    public List<AddressResponse> getMyAddresses() {

        User user = getCurrentUser();

        return addressRepository
                .findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AddressResponse updateAddress(
            Long addressId,
            AddressRequest request
    ) {

        User user = getCurrentUser();

        Address address =
                addressRepository
                        .findByIdAndUser(
                                addressId,
                                user
                        )
                        .orElseThrow(() ->
                                new AddressNotFoundException(
                                        "Address not found"
                                )
                        );

        address.setFullName(
                request.getFullName()
        );

        address.setPhoneNumber(
                request.getPhoneNumber()
        );

        address.setAddressLine1(
                request.getAddressLine1()
        );

        address.setAddressLine2(
                request.getAddressLine2()
        );

        address.setCity(
                request.getCity()
        );

        address.setState(
                request.getState()
        );

        address.setCountry(
                request.getCountry()
        );

        address.setPostalCode(
                request.getPostalCode()
        );

        Address updatedAddress =
                addressRepository.save(address);

        return mapToResponse(updatedAddress);
    }

    @Override
    public void deleteAddress(
            Long addressId
    ) {

        User user = getCurrentUser();

        Address address =
                addressRepository
                        .findByIdAndUser(
                                addressId,
                                user
                        )
                        .orElseThrow(() ->
                                new AddressNotFoundException(
                                        "Address not found"
                                )
                        );

        addressRepository.delete(address);
    }

    @Override
    public void setDefaultAddress(
            Long addressId
    ) {

        User user = getCurrentUser();

        Address address =
                addressRepository
                        .findByIdAndUser(
                                addressId,
                                user
                        )
                        .orElseThrow(() ->
                                new AddressNotFoundException(
                                        "Address not found"
                                )
                        );

        addressRepository
                .findByUserAndDefaultAddressTrue(user)
                .ifPresent(existingDefault -> {

                    existingDefault.setDefaultAddress(false);

                    addressRepository.save(
                            existingDefault
                    );
                });

        address.setDefaultAddress(true);

        addressRepository.save(address);
    }

    private User getCurrentUser() {

        String email =
                SecurityUtils
                        .getCurrentUserEmail();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        )
                );
    }

    private AddressResponse mapToResponse(
            Address address
    ) {

        return AddressResponse.builder()
                .id(address.getId())
                .fullName(address.getFullName())
                .phoneNumber(address.getPhoneNumber())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .defaultAddress(
                        address.isDefaultAddress()
                )
                .build();
    }
}