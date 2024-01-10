package com.umc.lifesharing.product.service;

import com.umc.lifesharing.product.dto.ProductRequestDTO;
import com.umc.lifesharing.product.entity.Product;

public interface ProductCommandService {
    Product ProductRegister(ProductRequestDTO.RegisterProductDTO request);

}
