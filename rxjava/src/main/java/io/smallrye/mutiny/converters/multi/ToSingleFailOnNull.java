package io.smallrye.mutiny.converters.multi;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import io.reactivex.Single;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.converters.uni.UniRxConverters;
import io.smallrye.mutiny.helpers.ParameterValidation;

public class ToSingleFailOnNull<T> implements Function<Multi<T>, Single<T>> {
    private final Supplier<? extends Throwable> supplier;

    ToSingleFailOnNull(Supplier<? extends Throwable> supplier) {
        this.supplier = ParameterValidation.nonNull(supplier, "supplier");
    }

    @Override
    public Single<T> apply(Multi<T> multi) {
        return multi.collectItems().first()
                .onItem().ifNull().failWith(supplier)
                .convert().with(UniRxConverters.toSingle())
                .map(Optional::get);
    }
}
