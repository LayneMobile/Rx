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

package com.laynemobile.rx.requests;

public final class Result<T> {
    private final boolean success;
    private final T value;
    private final Throwable error;

    private Result(T value) {
        this.success = true;
        this.value = value;
        this.error = null;
    }

    private Result(Throwable error) {
        this.success = false;
        this.value = null;
        this.error = error;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value);
    }

    public static <T> Result<T> error(Throwable error) {
        return new Result<>(error);
    }

    public boolean isSuccess() {
        return success;
    }

    public T value() {
        return value;
    }

    public Throwable error() {
        return error;
    }
}
