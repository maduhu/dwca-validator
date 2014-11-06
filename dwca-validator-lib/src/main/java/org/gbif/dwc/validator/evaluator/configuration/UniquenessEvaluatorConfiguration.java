package org.gbif.dwc.validator.evaluator.configuration;

import org.gbif.dwc.terms.ConceptTerm;
import org.gbif.dwc.validator.evaluator.annotation.RecordEvaluatorConfigurationKey;
import org.gbif.dwc.validator.result.EvaluationContext;

import java.io.File;

/**
 * Container object holding UniquenessEvaluator configurations.
 * 
 * @author cgendreau
 */
@RecordEvaluatorConfigurationKey
public class UniquenessEvaluatorConfiguration {

  private EvaluationContext evaluatorContext;
  private ConceptTerm term;
  private File workingFolder;

  public EvaluationContext getEvaluatorContext() {
    return evaluatorContext;
  }

  public void setEvaluatorContext(EvaluationContext evaluatorContext) {
    this.evaluatorContext = evaluatorContext;
  }

  public ConceptTerm getTerm() {
    return term;
  }

  public void setTerm(ConceptTerm term) {
    this.term = term;
  }

  public File getWorkingFolder() {
    return workingFolder;
  }

  public void setWorkingFolder(File workingFolder) {
    this.workingFolder = workingFolder;
  }

}
