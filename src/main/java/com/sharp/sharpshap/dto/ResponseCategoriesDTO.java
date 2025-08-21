package com.sharp.sharpshap.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharp.sharpshap.entity.Category;
import lombok.Data;

import java.util.List;
@Data
public class ResponseCategoriesDTO {
    private List<Category> categories;
}
