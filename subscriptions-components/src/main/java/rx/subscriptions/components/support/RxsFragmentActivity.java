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

package rx.subscriptions.components.support;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import rx.subscriptions.ActivityLifecycleProducer;
import rx.subscriptions.RxSubscriptions;

public abstract class RxsFragmentActivity extends FragmentActivity {
    private final ActivityLifecycleProducer producer = ActivityLifecycleProducer.create();
    private final RxSubscriptions subscriptions;

    public RxsFragmentActivity() {
        this.subscriptions = createSubscriptions();
        if (!subscriptions.setProducer(producer)) {
            throw new IllegalStateException("subclass must not set producer");
        }
    }

    protected RxSubscriptions createSubscriptions() {
        return RxSubscriptions.observeActivity();
    }

    @NonNull public RxSubscriptions subscriptions() {
        return subscriptions;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        producer.onCreate();
    }

    @Override protected void onStart() {
        super.onStart();
        producer.onStart();
    }

    @Override protected void onResume() {
        super.onResume();
        producer.onResume();
    }

    @Override protected void onPause() {
        producer.onPause();
        super.onPause();
    }

    @Override protected void onStop() {
        producer.onStop();
        super.onStop();
    }

    @Override protected void onDestroy() {
        producer.onDestroy();
        super.onDestroy();
    }
}
