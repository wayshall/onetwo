package org.onetwo.boot.module.security.oauth2;

import org.springframework.context.annotation.Import;

@Import(UserInfoResourceContextConfig.class)
public @interface EnableUserInfoResource {

}
