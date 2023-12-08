package com.minetrone.mlib.config.backend.internal.provider;

import com.minetrone.mlib.config.backend.internal.exceptions.SimplixValidationException;
import lombok.NonNull;

public abstract class ExceptionHandler {

  public RuntimeException create(
      @NonNull final Throwable throwable,
      @NonNull final String... messages) {
    return new SimplixValidationException(
        throwable,
        messages);
  }
}
