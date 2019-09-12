package com.belonk.api;

import com.belonk.entity.Order;
import com.belonk.service.OrderTccService;
import com.belonk.service.impl.OrderServiceImpl;
import org.bytesoft.compensable.Compensable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sun on 2019/9/9.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/order")
@Compensable(interfaceClass = OrderTccService.class, confirmableKey = "orderConfirmService", cancellableKey = "orderCancelService")
public class OrderApi implements OrderTccService {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(OrderApi.class);

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Resource
    private OrderServiceImpl orderService;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @PostMapping("/create")
    public Map<String, Object> create(Long userId, Long productId, Integer buyNumber) {
        Assert.notNull(userId);
        Assert.notNull(productId);
        Assert.notNull(buyNumber);
        Order order = orderService.create(userId, productId, buyNumber);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rtnCode", 0);
        resultMap.put("rtnMsg", "success");
        resultMap.put("rtnData", order);
        return resultMap;
    }

    // @PostMapping("/paySuccess")
    // public Map<String, Object> paySuccess(String orderNo) {
    //     orderService.paySuccessWithoutTcc(orderNo);
    //     System.err.println("Order pay success.");
    //     Map<String, Object> resultMap = new HashMap<>();
    //     resultMap.put("rtnCode", 0);
    //     resultMap.put("rtnMsg", "success");
    //     resultMap.put("rtnData", orderNo);
    //     return resultMap;
    // }

    @PostMapping("/paySuccess")
    @Override
    @Transactional
    public Order paySuccess(String orderNo) {
        Assert.hasLength(orderNo);
        Order order = orderService.paySuccess(orderNo);
        return order;
    }
}