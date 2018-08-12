package com.fizal.domain.module;

import com.fizal.domain.api.HelloService;
import com.fizal.domain.api.ProfileService;
import com.fizal.domain.impl.HelloServiceImpl;
import com.fizal.domain.impl.ProfileServiceImpl;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

/**
 * The module that binds the HelloService so that it can be served.
 */
public class HelloModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
//        bindService(HelloService.class, HelloServiceImpl.class);
        bindService(ProfileService.class, ProfileServiceImpl.class);
    }
}
