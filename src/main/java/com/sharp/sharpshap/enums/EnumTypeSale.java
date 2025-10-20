package com.sharp.sharpshap.enums;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;
@Entity
@Data
@Table(name = "enum_type_sale")
public class EnumTypeSale {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
}
