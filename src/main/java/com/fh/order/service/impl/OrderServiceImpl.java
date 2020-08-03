package com.fh.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.Settle.model.Settle;
import com.fh.cart.model.Cart;
import com.fh.common.ResponseCode;
import com.fh.member.model.Member;
import com.fh.order.mapper.OrderInfoMapper;
import com.fh.order.mapper.OrderMapper;
import com.fh.order.model.Order;
import com.fh.order.model.OrderInfo;
import com.fh.order.service.OrderService;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import com.fh.util.BigDecimalUtil;
import com.fh.util.IdUtil;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private ProductService productService;

    @Override
    public ResponseCode buildOrder(List<Cart> cartList, List<Settle> settleList, Integer payType, Member member) {

        //生成订单号
        String orderId = IdUtil.createId();
        List<OrderInfo> orderInfoList = new ArrayList<>();
        //商品总价格
        BigDecimal totalPrice = new BigDecimal("0.00");
        //我们吧内存不足的数据 放到集合中
        List<String> amountNotFull = new ArrayList<>();

        for (Cart cart : cartList) {
            //判断内存是否充足
            Product product = productService.selectById(cart.getProductId());
            if(product.getAmount()<cart.getCount()){
                //库存不足
                amountNotFull.add(cart.getName());
            }

            //订单不足，减库存 判断库存是否充足
            Long res = productService.updateAmount(product.getId(),cart.getCount());
            if(res==1){
                //订单充足 生成订单详情页
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setName(cart.getName());
                orderInfo.setFilgImage(cart.getFileImage());
                orderInfo.setPrice(cart.getPrice());
                orderInfo.setOrderId(orderId);
                orderInfo.setProductId(cart.getProductId());
                orderInfo.setCount(cart.getCount());
                orderInfoList.add(orderInfo);
                BigDecimal thisTotal = BigDecimalUtil.mul(cart.getPrice().toString(),cart.getCount().toString());
                totalPrice = BigDecimalUtil.add(totalPrice,thisTotal);
                //在我们添加订单完成之后  需要更新Redis购物车的数据
                String cartJson = RedisUtil.hget(SystemConstant.CATR_KEY + member.getId(), orderInfo.getProductId().toString());
                if(StringUtils.isNotEmpty(cartJson)){
                    Cart cart1 = JSONObject.parseObject(cartJson, Cart.class);
                    if(cart1.getCount()<=orderInfo.getCount()){
                        //删除购物车中该商品
                        RedisUtil.hdel(SystemConstant.CATR_KEY + member.getId(), orderInfo.getProductId().toString());

                    }else{
                        //更新购物车
                        cart1.setCount(cart1.getCount()-orderInfo.getCount());
                        String s = JSONObject.toJSONString(cart1);
                        RedisUtil.hset(SystemConstant.CATR_KEY + member.getId(), orderInfo.getProductId().toString(),s);
                    }
                }
            }else {
                //库存不足
                amountNotFull.add(cart.getName());
            }
        }

        if(orderInfoList !=null && orderInfoList.size()==cartList.size()){
            //在确保订单都足的情况下 保存订单详情信息
            for (OrderInfo orderInfo : orderInfoList) {
                orderInfoMapper.insert(orderInfo);
            }
            //生成订单
            Order order  = new Order();
            order.setMemberId(member.getId());
            for (Settle settle : settleList) {
                order.setAreaId(settle.getId());
            }
            order.setCreateDate(new Date());
            order.setId(orderId);
            order.setPayId(payType);
            order.setTotalPrice(totalPrice);
            order.setStatus(SystemConstant.ORDER_STATUS_WAIT);
            orderMapper.insert(order);
            return ResponseCode.success(orderId);
        }else {
            return ResponseCode.error(amountNotFull);
        }

    }
}
