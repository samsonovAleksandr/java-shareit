package ru.practicum.gateway.item.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonSerialize
public class RequestItemDto {


    private long id;

    private String name;

    private String description;

    private Boolean available;

    private long requestId;


}
