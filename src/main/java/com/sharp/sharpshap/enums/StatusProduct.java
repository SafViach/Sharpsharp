package com.sharp.sharpshap.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum StatusProduct {
    AVAILABLE("Продаётся", null ,null), //доступен для покупки
    ARCHIVED("Продан", null, null), //продан и находится в архиве для учета
    EXAMINATION("Проверка",null,null),//ожидает проверки
    MARRIAGE("Брак", null,null), //бракованный
    RETURN("Возрат", null,null);// вернули брак


    private String description;
    private LocalDate date;
    private String nameUser;

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }
}
