package org.onetwo.common.rxjava;

import org.junit.Test;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

public class RxJavaTest {

	@Test
	public void test() {
		Observable<Integer> observableString = Observable
				.create(new Observable.OnSubscribe<Integer>() {
					@Override
					public void call(Subscriber<? super Integer> observer) {
						for (int i = 0; i < 5; i++) {
							observer.onNext(i);
						}
						observer.onCompleted();
					}
				});

		Subscription subscriptionPrint = observableString
				.subscribe(new Observer<Integer>() {
					@Override
					public void onCompleted() {
						System.out.println("Observable completed");
					}

					@Override
					public void onError(Throwable e) {
						System.out.println("Oh,no! Something wrong happened！");
					}

					@Override
					public void onNext(Integer item) {
						System.out.println("Item is " + item);
					}
				});
		
		subscriptionPrint = observableString
				.subscribe(new Observer<Integer>() {
					@Override
					public void onCompleted() {
						System.out.println("Observable completed");
					}

					@Override
					public void onError(Throwable e) {
						System.out.println("Oh,no! Something wrong happened！");
					}

					@Override
					public void onNext(Integer item) {
						System.out.println("Item is " + item);
					}
				});
	}

}
