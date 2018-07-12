package org.onetwo.cloud.bugfix;

import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.ClassUtils;

/**
 * @author wayshall
 * <br/>
 */
public class AllEncompassingFormHttpMessageConverter extends FormHttpMessageConverter {

	private static final boolean jaxb2Present =
			ClassUtils.isPresent("javax.xml.bind.Binder",
					AllEncompassingFormHttpMessageConverter.class.getClassLoader());

	private static final boolean jackson2Present =
			ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper",
					AllEncompassingFormHttpMessageConverter.class.getClassLoader()) &&
			ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator",
					AllEncompassingFormHttpMessageConverter.class.getClassLoader());

	private static final boolean jackson2XmlPresent =
			ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper",
					AllEncompassingFormHttpMessageConverter.class.getClassLoader());

	private static final boolean jackson2SmilePresent =
			ClassUtils.isPresent("com.fasterxml.jackson.dataformat.smile.SmileFactory",
					AllEncompassingFormHttpMessageConverter.class.getClassLoader());

	private static final boolean gsonPresent =
			ClassUtils.isPresent("com.google.gson.Gson",
					AllEncompassingFormHttpMessageConverter.class.getClassLoader());

	private static final boolean jsonbPresent =
			ClassUtils.isPresent("javax.json.bind.Jsonb",
					AllEncompassingFormHttpMessageConverter.class.getClassLoader());


	public AllEncompassingFormHttpMessageConverter() {
		addPartConverter(new SourceHttpMessageConverter<>());

		if (jaxb2Present && !jackson2XmlPresent) {
			addPartConverter(new Jaxb2RootElementHttpMessageConverter());
		}

		if (jackson2Present) {
			addPartConverter(new MappingJackson2HttpMessageConverter());
		}
		else if (gsonPresent) {
			addPartConverter(new GsonHttpMessageConverter());
		}
		/*else if (jsonbPresent) {
			addPartConverter(new JsonbHttpMessageConverter());
		}*/

		if (jackson2XmlPresent) {
			addPartConverter(new MappingJackson2XmlHttpMessageConverter());
		}

		/*if (jackson2SmilePresent) {
			addPartConverter(new MappingJackson2SmileHttpMessageConverter());
		}*/
	}

}
