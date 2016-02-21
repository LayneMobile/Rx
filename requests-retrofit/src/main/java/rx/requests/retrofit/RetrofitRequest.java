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

package rx.requests.retrofit;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.requests.Request;
import rx.requests.retry.RetryPolicy;
import rx.subscriptions.Subscriptions;

public final class RetrofitRequest<T> extends Request<Response<T>> {
    private RetrofitRequest(Call<T> call) {
        this(call, null);
    }

    private RetrofitRequest(Call<T> call, RetryPolicy retryPolicy) {
        this(Single.create(new CallOnSubscribe<>(call)), retryPolicy);
    }

    private RetrofitRequest(Single<Response<T>> single) {
        this(single, null);
    }

    private RetrofitRequest(Single<Response<T>> single, RetryPolicy retryPolicy) {
        super(retryPolicy == null ? single : retryResponse(single, retryPolicy));
    }

    public static <T> RetrofitRequest<T> from(Call<T> call) {
        return new RetrofitRequest<>(call);
    }

    public static <T> RetrofitRequest<T> from(Call<T> call, RetryPolicy retryPolicy) {
        return new RetrofitRequest<>(call, retryPolicy);
    }

    public static <T> RetrofitRequest<T> just(Response<T> value) {
        return new RetrofitRequest<>(Single.just(value));
    }

    public Request<T> map() {
        return Request.from(map(asSingle()));
    }

    private static <T> Single<Response<T>> retryResponse(Single<Response<T>> single, RetryPolicy retryPolicy) {
        return single.toObservable()
                // If we get a response that is an error, we want to retry
                // so create an observable error with the response inside
                .flatMap(new Func1<Response<T>, Observable<Response<T>>>() {
                    @Override public Observable<Response<T>> call(Response<T> response) {
                        if (response.isSuccess()) {
                            return Observable.just(response);
                        }
                        return Observable.error(new HttpException(response));
                    }
                })
                /* Now we retry the request if there are errors */
                .retry(retryPolicy)
                /* Now if we still have an error after retrying, we either have the response
                   or something else happened. If we have the response, then return it
                   and if not, we need to throw the error */
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<T>>>() {
                    @SuppressWarnings("unchecked")
                    @Override public Observable<? extends Response<T>> call(Throwable throwable) {
                        if (throwable instanceof HttpException) {
                            HttpException e = (HttpException) throwable;
                            Response<T> response = (Response<T>) e.response();
                            return Observable.just(response);
                        }
                        return Observable.error(throwable);
                    }
                })
                .toSingle();
    }

    private static <T> Single<T> map(Single<Response<T>> single) {
        return single.flatMap(new Func1<Response<T>, Single<T>>() {
            @Override public Single<T> call(Response<T> response) {
                if (response.isSuccess()) {
                    return Single.just(response.body());
                }
                return Single.error(new HttpException(response));
            }
        });
    }

    private static final class CallOnSubscribe<T> implements Single.OnSubscribe<Response<T>> {
        private final Call<T> originalCall;

        private CallOnSubscribe(Call<T> originalCall) {
            this.originalCall = originalCall;
        }

        @Override public void call(final SingleSubscriber<? super Response<T>> subscriber) {
            // Since Call is a one-shot type, clone it for each new subscriber.
            final Call<T> call = originalCall.clone();

            // Attempt to cancel the call if it is still in-flight on unsubscription.
            subscriber.add(Subscriptions.create(new Action0() {
                @Override public void call() {
                    call.cancel();
                }
            }));

            try {
                Response<T> response = call.execute();
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onSuccess(response);
                }
            } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(t);
                }
            }
        }
    }
}
