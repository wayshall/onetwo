package org.onetwo.common.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import org.junit.Test;


public class RxJavaTest {

	@Test
	public void test() {
		Observable<Integer> observableString = Observable
				.create(new ObservableOnSubscribe<Integer>() {
					@Override
					public void subscribe(ObservableEmitter<Integer> observer) {
						for (int i = 0; i < 5; i++) {
							observer.onNext(i);
						}
						observer.onComplete();
					}
				});

		observableString
				.subscribe(new Observer<Integer>() {
					
					@Override
					public void onSubscribe(Disposable disposable) {
						System.out.println("Observable onSubscribe");
					}

					@Override
					public void onComplete() {
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
		
		observableString
				.subscribe(new Observer<Integer>() {
					
					@Override
					public void onSubscribe(Disposable arg0) {
						System.out.println("Observable onSubscribe");
					}

					@Override
					public void onComplete() {
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
