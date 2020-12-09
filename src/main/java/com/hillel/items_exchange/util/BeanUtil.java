package com.hillel.items_exchange.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanUtil {

    public static void copyProperties(Object source, Object target, String... properties) {
        BeanWrapper srcWrap = PropertyAccessorFactory.forBeanPropertyAccess(source);
        BeanWrapper trgWrap = PropertyAccessorFactory.forBeanPropertyAccess(target);
        for (String property : properties) {
            trgWrap.setPropertyValue(property, srcWrap.getPropertyValue(property));
        }
    }
}
