package org.gbif.dwc.validator.evaluator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to identify the different implementation of RecordEvaluator.
 * 
 * @author cgendreau
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RecordEvaluatorKey {

  /**
   * @return the key to use to uniquely identify the Evaluator implementation.
   */
  String key();

}
