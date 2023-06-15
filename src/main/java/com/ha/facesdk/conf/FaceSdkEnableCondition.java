package com.ha.facesdk.conf;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class FaceSdkEnableCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();
        // 根据face-sdk.enable属性来决定是否创建 Face bean
        return "true".equals(env.getProperty("face-sdk.enable"));
    }
}
