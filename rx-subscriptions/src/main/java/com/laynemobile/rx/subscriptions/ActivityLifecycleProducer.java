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

package com.laynemobile.rx.subscriptions;


public final class ActivityLifecycleProducer extends LifecycleProducer {
    private ActivityLifecycleProducer() {}

    public static ActivityLifecycleProducer create() {
        return new ActivityLifecycleProducer();
    }

    @Override public void onCreate() {
        super.onCreate();
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    @Override public void onPause() {
        super.onPause();
    }

    @Override public void onResume() {
        super.onResume();
    }

    @Override public void onStart() {
        super.onStart();
    }

    @Override public void onStop() {
        super.onStop();
    }

    @Override protected void onAttach() {
        super.onAttach();
    }

    @Override protected void onDestroyView() {
        super.onDestroyView();
    }

    @Override protected void onDetach() {
        super.onDetach();
    }

    @Override protected void onViewCreated() {
        super.onViewCreated();
    }
}
