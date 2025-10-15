package com.sharp.sharpshap.dto;

import com.sharp.sharpshap.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProductSlice {
    List<ResponseOfTheProductFoundDTO> products;
    boolean hasNext;

}
