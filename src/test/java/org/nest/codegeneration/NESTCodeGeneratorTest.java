/*
 * Copyright (c) 2015 RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package org.nest.codegeneration;

import org.junit.Test;
import org.nest.base.GenerationBasedTest;
import org.nest.mocks.PSCMock;
import org.nest.nestml._ast.ASTNESTMLCompilationUnit;
import org.nest.utils.FilesHelper;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Generates entire NEST implementation for several NESTML models. Uses MOCKs or works with models without ODEs.
 *
 * @author plotnikov
 */
public class NESTCodeGeneratorTest extends GenerationBasedTest {
  private static final PSCMock pscMock = new PSCMock();

  private static final String PSC_MODEL_WITH_ODE = "models/iaf_psc_alpha.nestml";
  private static final String PSC_MODEL_IMPERATIVE = "src/test/resources/codegeneration/imperative/iaf_psc_alpha_imperative.nestml";
  private static final String PSC_MODEL_THREE_BUFFERS = "src/test/resources/codegeneration/iaf_psc_alpha_three_buffers.nestml";
  private static final String COND_MODEL_IMPLICIT = "models/iaf_cond_alpha_implicit.nestml";
  private static final String MODEL_PATH = "src/test/resources";

  @Test
  public void testPSCModelWithoutOde() {
    final ASTNESTMLCompilationUnit root = parseNESTMLModel(PSC_MODEL_IMPERATIVE, MODEL_PATH);
    scopeCreator.runSymbolTableCreator(root);
    final NESTCodeGenerator generator = new NESTCodeGenerator(scopeCreator, pscMock);

    generator.analyseAndGenerate(root, CODE_GEN_OUTPUT);
    generator.generateNESTModuleCode(newArrayList(root), MODULE_NAME, CODE_GEN_OUTPUT);
  }

  @Test
  public void testPSCModelWithOde() {
    final ASTNESTMLCompilationUnit root = parseNESTMLModel(PSC_MODEL_WITH_ODE, MODEL_PATH);
    scopeCreator.runSymbolTableCreator(root);
    final NESTCodeGenerator generator = new NESTCodeGenerator(scopeCreator, pscMock);

    FilesHelper.deleteFilesInFolder(CODE_GEN_OUTPUT);
    generator.analyseAndGenerate(root, CODE_GEN_OUTPUT);
    generator.generateNESTModuleCode(newArrayList(root), MODULE_NAME, CODE_GEN_OUTPUT);
  }

  @Test
  public void testCondModelWithImplicitOdes() {
    final ASTNESTMLCompilationUnit root = parseNESTMLModel(COND_MODEL_IMPLICIT, MODEL_PATH);
    scopeCreator.runSymbolTableCreator(root);
    final NESTCodeGenerator generator = new NESTCodeGenerator(scopeCreator, pscMock);

    FilesHelper.deleteFilesInFolder(CODE_GEN_OUTPUT);
    generator.analyseAndGenerate(root, CODE_GEN_OUTPUT);
    generator.generateNESTModuleCode(newArrayList(root), MODULE_NAME, CODE_GEN_OUTPUT);
  }

  @Test
  public void testPSCModelWithThreeBuffers() {
    final ASTNESTMLCompilationUnit root = parseNESTMLModel(PSC_MODEL_THREE_BUFFERS, MODEL_PATH);
    scopeCreator.runSymbolTableCreator(root);
    final NESTCodeGenerator generator = new NESTCodeGenerator(scopeCreator, pscMock);

    FilesHelper.deleteFilesInFolder(CODE_GEN_OUTPUT);
    generator.analyseAndGenerate(root, CODE_GEN_OUTPUT);
    generator.generateNESTModuleCode(newArrayList(root), MODULE_NAME, CODE_GEN_OUTPUT);
  }

}
