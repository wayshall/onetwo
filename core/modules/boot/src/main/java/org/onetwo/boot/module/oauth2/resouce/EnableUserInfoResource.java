package org.onetwo.boot.module.oauth2.resouce;

import org.springframework.context.annotation.Import;

@Import(UserInfoResourceContextConfig.class)
public @interface EnableUserInfoResource {

}
