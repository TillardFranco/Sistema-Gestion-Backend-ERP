package com.example.farmaser.model.dto.categoryDto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@Builder
public class CategoryResponseDto implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Boolean active;
    private Date creationDate;
    private Date lastModifiedDate;
}


