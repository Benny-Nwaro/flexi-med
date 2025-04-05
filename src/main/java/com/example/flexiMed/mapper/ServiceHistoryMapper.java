package com.example.flexiMed.mapper;

import com.example.flexiMed.dto.ServiceHistoryDTO;
import com.example.flexiMed.enums.EventType;
import com.example.flexiMed.model.RequestEntity;
import com.example.flexiMed.model.ServiceHistoryEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between {@link ServiceHistoryEntity} and {@link ServiceHistoryDTO} objects.
 * This class provides static methods to map data between the entity and DTO representations.
 */
@Component
public class ServiceHistoryMapper {

    /**
     * Converts a ServiceHistoryEntity object to a ServiceHistoryDTO object.
     *
     * @param history The ServiceHistoryEntity object to convert.
     * @return The corresponding ServiceHistoryDTO object, or null if the input is null.
     */
    public static ServiceHistoryDTO toDTO(ServiceHistoryEntity history) {
        if (history == null) {
            return null;
        }
        return new ServiceHistoryDTO(
                history.getId(),
                history.getRequest().getId(),
                history.getEventType().name(),
                history.getEventTime(),
                history.getDetails()
        );
    }

    /**
     * Converts a ServiceHistoryDTO object to a ServiceHistoryEntity object.
     *
     * @param dto     The ServiceHistoryDTO object to convert.
     * @param request The RequestEntity associated with the service history.
     * @return The corresponding ServiceHistoryEntity object, or null if the input is null.
     * @throws IllegalArgumentException If an invalid EventType is provided in the DTO.
     */
    public static ServiceHistoryEntity toEntity(ServiceHistoryDTO dto, RequestEntity request) {
        if (dto == null) {
            return null;
        }
        ServiceHistoryEntity history = new ServiceHistoryEntity();
        history.setId(dto.getId());
        history.setRequest(request);
        try {
            history.setEventType(EventType.valueOf(dto.getEventType()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid EventType: " + dto.getEventType(), e);
        }
        history.setEventTime(dto.getEventTime());
        history.setDetails(dto.getDetails());
        return history;
    }
}