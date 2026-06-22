package com.ayush.ecommerce.module.address.repository;

import com.ayush.ecommerce.module.address.entity.Address;
import com.ayush.ecommerce.module.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository
        extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);

    Optional<Address> findByIdAndUser(
            Long id,
            User user
    );

    Optional<Address> findByUserAndDefaultAddressTrue(
            User user
    );
}