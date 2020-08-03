package com.fh.product.controller;

import com.fh.common.Ignore;
import com.fh.common.ResponseCode;
import com.fh.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("product")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("queryIsHotProductList")
    @Ignore
    public ResponseCode queryIsHotProductList(){
        return productService.queryIsHotProductList();
    }

    @RequestMapping("queryProductList")
    @Ignore
    public ResponseCode queryProductList(){
        return productService.queryProductList();
    }

    @RequestMapping("queryListPage")
    @Ignore
    public ResponseCode queryListPage(long CurrentPage , long pageSize){
        return productService.queryListPage(CurrentPage,pageSize);
    }
}
