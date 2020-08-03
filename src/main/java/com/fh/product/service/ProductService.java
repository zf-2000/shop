package com.fh.product.service;

import com.fh.common.ResponseCode;
import com.fh.product.model.Product;

public interface ProductService {
    ResponseCode queryIsHotProductList();

    ResponseCode queryProductList();

    ResponseCode queryListPage(long CurrentPage , long pageSize);

    Product selectById(Integer productId);

    Long updateAmount(Integer id, Integer count);
}
