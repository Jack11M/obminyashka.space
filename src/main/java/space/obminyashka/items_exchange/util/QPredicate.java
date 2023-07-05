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
        if (object != null) {
            if (object instanceof Collection && ((Collection<?>) object).isEmpty()) {
                return this;
            }
            predicates.add(function.apply(object));}
        return this;
    }

    public Predicate buildAnd() {
        Predicate predicate = ExpressionUtils.allOf(predicates);
        return predicate == null ? Expressions.asBoolean(true).isTrue() : predicate;
    }

    public static QPredicate builder() {
        return new QPredicate();
    }
}