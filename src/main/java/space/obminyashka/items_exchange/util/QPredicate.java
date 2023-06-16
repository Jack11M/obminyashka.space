package space.obminyashka.items_exchange.util;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicate {

    private final Set<Predicate> predicates = new HashSet<>();

    public <T> QPredicate add(T object, Function<T, Predicate> function) {
        if (object instanceof Collection && !((Collection<?>) object).isEmpty()) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    public Predicate buildAnd() {
        return ExpressionUtils.allOf(predicates);
    }

    public static QPredicate builder() {
        return new QPredicate();
    }
}