package com.sharp.sharpshap.enums;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class EnumStatusProduct{

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false , unique = true)
    private String status;
}
