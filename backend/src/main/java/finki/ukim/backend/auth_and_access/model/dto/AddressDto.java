package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.Address;

import jakarta.validation.constraints.NotBlank;

public record AddressDto(
        @NotBlank(message = "Street is required")
        String street,
        
        @NotBlank(message = "City is required")
        String city,
        
        String postalCode
) {
    public Address toAddress() {
        return new Address(street, city, postalCode);
    }

    public static AddressDto from(Address address) {
        return new AddressDto(
                address.getStreet(),
                address.getCity(),
                address.getPostalCode()
        );
    }
}
