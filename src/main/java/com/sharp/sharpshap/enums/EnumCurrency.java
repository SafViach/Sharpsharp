package com.sharp.sharpshap.enums;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class EnumCurrency{

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false , unique = true)
    private String description;

    @Column(precision = 4, scale = 2)
    private BigDecimal rate;

    private LocalDateTime lastUpdate;

    public EnumCurrency(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public void setRate(BigDecimal rate) {
        this.rate = rate;
        this.lastUpdate = LocalDateTime.now();
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
}