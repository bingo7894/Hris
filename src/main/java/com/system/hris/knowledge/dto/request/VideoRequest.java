package com.system.hris.knowledge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class VideoRequest {

    @NotBlank(message = "Title is required")
    private  String title;

    @Size(max = 4000,message = "Description must not exceed 4   000 characters")
    private String description;

    private Integer year;
    private Integer duration;
    private String poster;
    private String src;
    private boolean published;
    private List<String> categories;
}
