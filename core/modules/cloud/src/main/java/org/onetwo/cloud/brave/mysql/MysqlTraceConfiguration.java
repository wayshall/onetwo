package org.onetwo.cloud.brave.mysql;

import org.springframework.cloud.sleuth.zipkin2.ZipkinProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

/**
 * @author weishao zeng <br/>
 */
@Configuration
public class MysqlTraceConfiguration {
	@Bean
	public Sender sender(ZipkinProperties zipkinProperties) {
		return OkHttpSender.create(zipkinProperties.getEndpoint());
	}

	@Bean
	public Reporter<Span> reporter() {
		return AsyncReporter.builder(sender()).build();
	}
}
