package com.example.flexiMed.mapper;

import com.example.flexiMed.dto.RequestDTO;
import com.example.flexiMed.model.AmbulanceEntity;
import com.example.flexiMed.model.RequestEntity;
import com.example.flexiMed.enums.RequestStatus;
import com.example.flexiMed.model.UserEntity;

/**
 * Mapper class for converting between {@link RequestEntity} and {@link RequestDTO} objects.
 * This class provides static methods to map data between the entity and DTO representations.
 */
public class RequestMapper {

    /**
     * Converts a RequestEntity object to a RequestDTO object.
     *
     * @param request The RequestEntity object to convert.
     * @return The corresponding RequestDTO object, or null if the input is null.
     */
    public static RequestDTO toDTO(RequestEntity request) {
        if (request == null) {
            return null;
        }
        return new RequestDTO(
                request.getId(),
                request.getUser() != null ? request.getUser().getUserId() : null, // Ensure user is not null
                request.getAmbulance() != null ? request.getAmbulance().getId() : null,
                request.getRequestStatus() != null ? request.getRequestStatus() : null,
                request.getRequestTime(),
                request.getDispatchTime(),
                request.getArrivalTime(),
                request.getLatitude(),
                request.getLongitude(),
                request.getDescription()
        );
    }

    /**
     * Converts a RequestDTO object to a RequestEntity object.
     *
     * @param dto       The RequestDTO object to convert.
     * @param user      The UserEntity associated with the request.
     * @param ambulance The AmbulanceEntity associated with the request.
     * @return The corresponding RequestEntity object, or null if the input is null.
     * @throws IllegalArgumentException If an invalid RequestStatus is provided in the DTO.
     */
    public static RequestEntity toEntity(RequestDTO dto, UserEntity user, AmbulanceEntity ambulance) {
        if (dto == null) {
            return null;
        }
        RequestEntity request = new RequestEntity();
        request.setId(dto.getId());
        request.setUser(user);
        request.setAmbulance(ambulance);
        if (dto.getRequestStatus() != null) {
            try {
                request.setRequestStatus(RequestStatus.valueOf(dto.getRequestStatus().toString()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid Request Status: " + dto.getRequestStatus(), e);
            }
        }
        request.setRequestTime(dto.getRequestTime());
        request.setDispatchTime(dto.getDispatchTime());
        request.setArrivalTime(dto.getArrivalTime());
        request.setLatitude(dto.getLatitude());
        request.setLongitude(dto.getLongitude());
        request.setDescription(dto.getDescription());
        return request;
    }
}