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

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import rx.subscriptions.FragmentLifecycleProducer;
import rx.subscriptions.RxSubscriptions;

public class RxsFragment extends android.support.v4.app.Fragment {
    private final FragmentLifecycleProducer producer = FragmentLifecycleProducer.create();
    private final RxSubscriptions subscriptions;

    public RxsFragment() {
        this.subscriptions = createSubscriptions();
        if (!subscriptions.setProducer(producer)) {
            throw new IllegalStateException("subclass must not set producer");
        }
    }

    protected RxSubscriptions createSubscriptions() {
        return RxSubscriptions.observeFragment();
    }

    @NonNull public RxSubscriptions subscriptions() {
        return subscriptions;
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        producer.onAttach();
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        producer.onCreate();
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        producer.onViewCreated();
    }

    @Override public void onStart() {
        super.onStart();
        producer.onStart();
    }

    @Override public void onResume() {
        super.onResume();
        producer.onResume();
    }

    @Override public void onPause() {
        producer.onPause();
        super.onPause();
    }

    @Override public void onStop() {
        producer.onStop();
        super.onStop();
    }

    @Override public void onDestroyView() {
        producer.onDestroyView();
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        producer.onDestroy();
        super.onDestroy();
    }

    @Override public void onDetach() {
        producer.onDetach();
        super.onDetach();
    }
}
