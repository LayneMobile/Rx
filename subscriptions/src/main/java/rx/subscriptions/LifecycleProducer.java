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

package rx.subscriptions;


import rx.Observable;
import rx.subjects.BehaviorSubject;

public abstract class LifecycleProducer {
    private final BehaviorSubject<Integer> subject = BehaviorSubject.create();

    protected LifecycleProducer() {}

    protected void onAttach() {
        subject.onNext(Lifecycle.OnAttach);
    }

    protected void onCreate() {
        subject.onNext(Lifecycle.OnCreate);
    }

    protected void onViewCreated() {
        subject.onNext(Lifecycle.OnViewCreated);
    }

    protected void onStart() {
        subject.onNext(Lifecycle.OnStart);
    }

    protected void onResume() {
        subject.onNext(Lifecycle.OnResume);
    }

    protected void onPause() {
        subject.onNext(Lifecycle.OnPause);
    }

    protected void onStop() {
        subject.onNext(Lifecycle.OnStop);
    }

    protected void onDestroyView() {
        subject.onNext(Lifecycle.OnDestroyView);
    }

    protected void onDestroy() {
        subject.onNext(Lifecycle.OnDestroy);
    }

    protected void onDetach() {
        subject.onNext(Lifecycle.OnDetach);
    }

    public final Observable<Integer> asObservable() {
        return subject.asObservable();
    }
}
