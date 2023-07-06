package space.obminyashka.items_exchange.util;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicate {

    private final Set<Predicate> predicates = new HashSet<>();

    public <T> QPredicate add(T object, Function<T, Predicate> function) {
        Optional.ofNullable(object)
                .filter(java.util.function.Predicate.not(o -> o instanceof Collection && ((Collection<?>) object).isEmpty()))
                .ifPresent(o -> predicates.add(function.apply(o)));
        return this;
    }

    public Predicate buildAnd() {
        return Optional.ofNullable(ExpressionUtils.allOf(predicates))
                .orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }

    public static QPredicate builder() {
        return new QPredicate();
    }
}