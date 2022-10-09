package com.portfolio.gascharge.utils.querydsl;

import com.portfolio.gascharge.domain.charge.Charge;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class QueryDslUtil {
    public static List<OrderSpecifier> getOrderSpecifiers(Pageable pageable, Class<?> clazz, String variable) {
        List<OrderSpecifier> orders = new ArrayList<>();
        pageable.getSort().stream().forEach(order -> {
            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder pathBuilder = new PathBuilder(clazz, variable);
            orders.add(new OrderSpecifier(direction, pathBuilder.get(prop)));
        });
        return orders;
    }
}
