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

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import rx.requests.Request;

public final class RetrofitRequestCallAdapterFactory extends CallAdapter.Factory {
    private static final RetrofitRequestCallAdapterFactory Instance = new RetrofitRequestCallAdapterFactory();

    private RetrofitRequestCallAdapterFactory() {}

    public static RetrofitRequestCallAdapterFactory instance() {
        return Instance;
    }

    @Override public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        if (rawType != RetrofitRequest.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            String name = "Request";
            throw new IllegalStateException(name + " return type must be parameterized"
                    + " as " + name + "<Foo> or " + name + "<? extends Foo>");
        }
        Type requestType = getParameterUpperBound(0, (ParameterizedType) returnType);
        return new RequestCallAdapter(requestType);
    }

    private static final class RequestCallAdapter implements CallAdapter<Request<?>> {
        private final Type responseType;

        private RequestCallAdapter(Type responseType) {
            this.responseType = responseType;
        }

        @Override public <R> RetrofitRequest<R> adapt(Call<R> call) {
            return RetrofitRequest.from(call);
        }

        @Override public Type responseType() {
            return responseType;
        }
    }
}
