/*
 * Copyright 2016 Layne Mobile, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rx.requests;


import rx.Single;
import rx.functions.Func1;
import rx.requests.retry.RetryPolicy;
import rx.schedulers.Schedulers;
import rx.subscriptions.RxSubscriptions;
import rx.subscriptions.SubscriptionBuilder;

public class Request<T> {
    private final Single<T> single;

    protected Request(Single<T> single) {
        this.single = single;
    }

    protected Request(Single<T> single, RetryPolicy retryPolicy) {
        if (retryPolicy == null) {
            this.single = single;
        } else {
            this.single = retry(single, retryPolicy);
        }
    }

    public static <T> Request<T> from(Single<T> single) {
        return new Request<>(single);
    }

    public static <T> Request<T> from(Single<T> single, RetryPolicy retryPolicy) {
        return new Request<>(single, retryPolicy);
    }

    public static <T> Request<T> just(T value) {
        return new Request<>(Single.just(value));
    }

    public static <T> Request<T> error(Throwable exception) {
        return new Request<>(Single.<T>error(exception));
    }

    public final Result<T> execute() {
        try {
            return result(single)
                    .toBlocking()
                    .value();
        } catch (Throwable e) {
            return Result.error(e);
        }
    }

    public final Single<T> asSingle() {
        return single;
    }

    public final Single<T> asAsyncSingle() {
        return single.subscribeOn(Schedulers.io());
    }

    public final <R> Request<R> map(Func1<? super T, ? extends R> func) {
        return Request.from(single.map(func));
    }

    public final <R> Request<R> flatMap(final Func1<? super T, ? extends Single<? extends R>> func) {
        return Request.from(single.flatMap(func));
    }

    public final SubscriptionBuilder<T> with(RxSubscriptions subscriptions) {
        return subscriptions.with(asAsyncSingle())
                .observeOnMainThread();
    }

    private static <T> Single<T> retry(Single<T> single, RetryPolicy retryPolicy) {
        return single.toObservable()
                /* Retry the request if there are errors */
                .retry(retryPolicy)
                .toSingle();
    }

    /**
     * Maps the single to emit a result value with success/failure instead of calling onError.
     *
     * @param single
     *         the single
     * @param <T>
     *         the type of result
     *
     * @return the single that emits a result
     */
    private static <T> Single<Result<T>> result(Single<T> single) {
        return single.map(new Func1<T, Result<T>>() {
            @Override public Result<T> call(T t) {
                return Result.success(t);
            }
        }).onErrorReturn(new Func1<Throwable, Result<T>>() {
            @Override public Result<T> call(Throwable throwable) {
                return Result.error(throwable);
            }
        });
    }
}
