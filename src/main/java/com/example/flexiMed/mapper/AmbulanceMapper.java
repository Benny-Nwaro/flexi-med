package com.example.flexiMed.mapper;

import com.example.flexiMed.dto.AmbulanceDTO;
import com.example.flexiMed.model.AmbulanceEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between {@link AmbulanceEntity} and {@link AmbulanceDTO} objects.
 * This class provides static methods to map data between the entity and DTO representations.
 */
@Component
public class AmbulanceMapper {

    /**
     * Converts an AmbulanceEntity object to an AmbulanceDTO object.
     *
     * @param ambulance The AmbulanceEntity object to convert.
     * @return The corresponding AmbulanceDTO object, or null if the input is null.
     */
    public static AmbulanceDTO toDTO(AmbulanceEntity ambulance) {
        if (ambulance == null) {
            return null;
        }
        return new AmbulanceDTO(
                ambulance.getId(),
                ambulance.getPlateNumber(),
                ambulance.getLatitude(),
                ambulance.getLongitude(),
                ambulance.isAvailabilityStatus(),
                ambulance.getDriverName(),
                ambulance.getDriverContact(),
                ambulance.getLastUpdatedAt()
        );
    }

    /**
     * Converts an AmbulanceDTO object to an AmbulanceEntity object.
     *
     * @param dto The AmbulanceDTO object to convert.
     * @return The corresponding AmbulanceEntity object, or null if the input is null.
     */
    public static AmbulanceEntity toEntity(AmbulanceDTO dto) {
        if (dto == null) {
            return null;
        }
        AmbulanceEntity ambulance = new AmbulanceEntity();
        ambulance.setId(dto.getId());
        ambulance.setPlateNumber(dto.getPlateNumber());
        ambulance.setLatitude(dto.getLatitude());
        ambulance.setLongitude(dto.getLongitude());
        ambulance.setAvailabilityStatus(dto.isAvailabilityStatus());
        ambulance.setDriverName(dto.getDriverName());
        ambulance.setDriverContact(dto.getDriverContact());
        ambulance.setLastUpdatedAt(dto.getLastUpdatedAt());
        return ambulance;
    }
}