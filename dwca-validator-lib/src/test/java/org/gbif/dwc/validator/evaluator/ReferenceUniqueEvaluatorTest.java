package org.gbif.dwc.validator.evaluator;

import org.gbif.dwc.record.Record;
import org.gbif.dwc.record.RecordImpl;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.text.ArchiveField;
import org.gbif.dwc.text.ArchiveField.DataType;
import org.gbif.dwc.validator.TestEvaluationResultHelper;
import org.gbif.dwc.validator.evaluator.integrity.ReferenceEvaluatorBuilder;
import org.gbif.dwc.validator.result.EvaluationContext;
import org.gbif.dwc.validator.result.impl.InMemoryResultAccumulator;
import org.gbif.dwc.validator.result.type.ContentValidationType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test ReferentialIntegrityEvaluator with mock records.
 * 
 * @author cgendreau
 */
public class ReferenceUniqueEvaluatorTest {

  private Record buildMockRecord(String taxonID, String acceptedNameUsageID) {
    // create a mock Record
    ArchiveField idField = new ArchiveField(0, DwcTerm.taxonID, null, DataType.string);
    ArchiveField acceptedNameUsageIDField = new ArchiveField(1, DwcTerm.acceptedNameUsageID, null, DataType.string);
    List<ArchiveField> fieldList = new ArrayList<ArchiveField>();
    fieldList.add(idField);
    fieldList.add(acceptedNameUsageIDField);

    RecordImpl testRecord = new RecordImpl(idField, fieldList, DwcTerm.Taxon.qualifiedName(), false);
    testRecord.setRow(new String[] {taxonID, acceptedNameUsageID});
    return testRecord;
  }

  @Test
  public void referentialIntegrityEvaluatorCorrectId() {

    InMemoryResultAccumulator resultAccumulator = new InMemoryResultAccumulator();

    try {

      StatefulRecordEvaluator referenceEvaluator =
        ReferenceEvaluatorBuilder
          .builder()
          .termRefersToUnique(DwcTerm.acceptedNameUsageID, EvaluationContext.CORE, DwcTerm.Taxon.qualifiedName(),
            DwcTerm.taxonID, EvaluationContext.CORE, DwcTerm.Taxon.qualifiedName()).build();

      referenceEvaluator.handleEval(buildMockRecord("1", "2b"), EvaluationContext.CORE);
      referenceEvaluator.handleEval(buildMockRecord("2a", "1"), EvaluationContext.CORE);
      // add blank acceptedNameUsageID, should be ignored.
      referenceEvaluator.handleEval(buildMockRecord("2b", ""), EvaluationContext.CORE);

      referenceEvaluator.handlePostIterate(resultAccumulator);
      referenceEvaluator.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertTrue(resultAccumulator.getValidationResultList().isEmpty());
  }

  @Test
  public void referentialIntegrityEvaluatorIncorrectId() {

    InMemoryResultAccumulator resultAccumulator = new InMemoryResultAccumulator();

    try {
      StatefulRecordEvaluator referenceEvaluator =
        ReferenceEvaluatorBuilder
          .builder()
          .termRefersToUnique(DwcTerm.acceptedNameUsageID, EvaluationContext.CORE, DwcTerm.Taxon.qualifiedName(),
            DwcTerm.taxonID, EvaluationContext.CORE, DwcTerm.Taxon.qualifiedName()).build();

      referenceEvaluator.handleEval(buildMockRecord("1", "4"), EvaluationContext.CORE);
      referenceEvaluator.handleEval(buildMockRecord("2", "1"), EvaluationContext.CORE);

      referenceEvaluator.handlePostIterate(resultAccumulator);
      referenceEvaluator.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertTrue(TestEvaluationResultHelper.containsValidationType(resultAccumulator.getValidationResultList(), "4",
      ContentValidationType.FIELD_REFERENTIAL_INTEGRITY));
  }

  @Test
  public void referentialIntegrityEvaluatorMultipleCorrectId() {
    InMemoryResultAccumulator resultAccumulator = new InMemoryResultAccumulator();

    try {
      StatefulRecordEvaluator referenceEvaluator =
        ReferenceEvaluatorBuilder
          .builder()
          .termRefersToUnique(DwcTerm.acceptedNameUsageID, EvaluationContext.CORE, DwcTerm.Taxon.qualifiedName(),
            DwcTerm.taxonID, EvaluationContext.CORE, DwcTerm.Taxon.qualifiedName()).supportMultipleValues("|").build();

      referenceEvaluator.handleEval(buildMockRecord("1", "3|4"), EvaluationContext.CORE);
      referenceEvaluator.handleEval(buildMockRecord("3", ""), EvaluationContext.CORE);
      referenceEvaluator.handleEval(buildMockRecord("4", ""), EvaluationContext.CORE);
      referenceEvaluator.handlePostIterate(resultAccumulator);
      referenceEvaluator.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertTrue(resultAccumulator.getValidationResultList().isEmpty());
  }

  @Test
  public void referentialIntegrityEvaluatorMultipleIncorrectId() {
    InMemoryResultAccumulator resultAccumulator = new InMemoryResultAccumulator();

    try {
      StatefulRecordEvaluator referenceEvaluator =
        ReferenceEvaluatorBuilder
          .builder()
          .termRefersToUnique(DwcTerm.acceptedNameUsageID, EvaluationContext.CORE, DwcTerm.Taxon.qualifiedName(),
            DwcTerm.taxonID, EvaluationContext.CORE, DwcTerm.Taxon.qualifiedName()).supportMultipleValues("|").build();
      referenceEvaluator.handleEval(buildMockRecord("1", "3|5"), EvaluationContext.CORE);
      referenceEvaluator.handleEval(buildMockRecord("3", ""), EvaluationContext.CORE);
      referenceEvaluator.handleEval(buildMockRecord("4", ""), EvaluationContext.CORE);

      referenceEvaluator.handlePostIterate(resultAccumulator);
      referenceEvaluator.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertTrue(TestEvaluationResultHelper.containsValidationType(resultAccumulator.getValidationResultList(), "5",
      ContentValidationType.FIELD_REFERENTIAL_INTEGRITY));
  }
}
