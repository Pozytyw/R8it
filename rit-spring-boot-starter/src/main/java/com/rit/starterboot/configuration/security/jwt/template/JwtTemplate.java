package com.rit.starterboot.configuration.security.jwt.template;

import java.time.Instant;

public abstract class JwtTemplate {

    public abstract String getSubject();

    public abstract Instant getExpiresAt();
}
