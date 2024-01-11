package org.onetwo.ext.security.method;

import java.util.function.Supplier;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.ObservationAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.util.function.SingletonSupplier;

import io.micrometer.observation.ObservationRegistry;

/***
 * @see org.springframework.security.config.annotation.method.configuration.DeferringObservationAuthorizationManager
 */
public class DeferringObservationAuthorizationManagerDelegate<T> implements AuthorizationManager<T> {

	private final Supplier<AuthorizationManager<T>> delegate;

	DeferringObservationAuthorizationManagerDelegate(ObjectProvider<ObservationRegistry> provider,
			AuthorizationManager<T> delegate) {
		this.delegate = SingletonSupplier.of(() -> {
			ObservationRegistry registry = provider.getIfAvailable(() -> ObservationRegistry.NOOP);
			if (registry.isNoop()) {
				return delegate;
			}
			return new ObservationAuthorizationManager<>(registry, delegate);
		});
	}

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, T object) {
		return this.delegate.get().check(authentication, object);
	}


}
