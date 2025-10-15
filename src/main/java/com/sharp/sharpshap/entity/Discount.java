//package com.sharp.sharpshap.entity;
//
//
//import jakarta.persistence.Entity;
//import lombok.Data;
//
//import java.math.BigDecimal;
//import java.util.Set;
//import java.util.UUID;
//
//import jakarta.persistence.*;
//import lombok.ToString;
//
//@Entity
//@Data
//public class Discount{
//    @Id
//    @GeneratedValue
//    private UUID id;
//
//    @Column(nullable = false , precision = 15 , scale = 2)
//    private BigDecimal discountAmount;
//
//    @OneToMany(mappedBy = "discount" , cascade = CascadeType.ALL)
//    private Set<Product> products;
//
//    @Override
//    public String toString() {
//        return "Discount{" +
//                "id=" + id +
//                ", discountAmount=" + discountAmount +
//                '}';
//    }
//}
