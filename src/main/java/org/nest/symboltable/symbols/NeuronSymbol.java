/*
 * Copyright (c)  RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package org.nest.symboltable.symbols;

import de.monticore.symboltable.CommonScopeSpanningSymbol;
import de.monticore.symboltable.MutableScope;
import de.monticore.symboltable.Symbol;
import de.monticore.symboltable.SymbolKind;
import de.monticore.symboltable.resolving.ResolvedSeveralEntriesException;
import org.nest.symboltable.NeuronScope;
import org.nest.symboltable.symbols.references.NeuronSymbolReference;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;
import static org.nest.symboltable.symbols.VariableSymbol.BlockType.INPUT_BUFFER_CURRENT;
import static org.nest.symboltable.symbols.VariableSymbol.BlockType.INPUT_BUFFER_SPIKE;

/**
 * Represents the entire neuron or component, e.g. iaf_neuron.
 *
 * @author plotnikov
 */
public class NeuronSymbol extends CommonScopeSpanningSymbol {

  public final static NeuronSymbolKind KIND = new NeuronSymbolKind();

  private final Type type;

  private NeuronSymbol baseNeuron = null;

  public NeuronSymbol(final String name, final Type type) {
    super(name, KIND);
    this.type = type;
  }

  public Type getType() {
    return type;
  }

  @Override
  public String toString() {
    return "NeuronSymbol(" + getFullName() + "," + type + ")";
  }

  @SuppressWarnings("unused") // it is used within freemarker templates
  public List<VariableSymbol> getStateVariables() {
    return getSpannedScope().<VariableSymbol> resolveLocally(VariableSymbol.KIND)
        .stream()
        .filter(VariableSymbol::isInState)
        .collect(toList());
  }

  public Optional<VariableSymbol> getVariableByName(String variableName) {
    return spannedScope.resolveLocally(variableName, VariableSymbol.KIND);
  }


  @SuppressWarnings("unused") // it is used within freemarker templates
  public List<VariableSymbol> getCurrentBuffers() {
    final Collection<VariableSymbol> variableSymbols
        = spannedScope.resolveLocally(VariableSymbol.KIND);
    return variableSymbols.stream()
        .filter(variable -> variable.getBlockType().equals(INPUT_BUFFER_CURRENT))
        .collect(toList());
  }

  @SuppressWarnings("unused") // it is used within freemarker templates
  public List<VariableSymbol> getSpikeBuffers() {
    final Collection<VariableSymbol> variableSymbols
        = spannedScope.resolveLocally(VariableSymbol.KIND);
    return variableSymbols.stream()
        .filter(variable -> variable.getBlockType().equals(INPUT_BUFFER_SPIKE))
        .collect(toList());
  }

  @Override
  protected MutableScope createSpannedScope() {
    return new NeuronScope();
  }

  public void setBaseNeuron(NeuronSymbolReference baseNeuron) {
    checkNotNull(baseNeuron );
    this.baseNeuron = baseNeuron;
  }

  public Optional<NeuronSymbol> getBaseNeuron() {
    return Optional.ofNullable(baseNeuron);
  }

  public String printComment() {
    final StringBuffer output = new StringBuffer();
    if(getAstNode().isPresent()) {
      getAstNode().get().get_PreComments().forEach(comment -> output.append(comment.getText()).append(" "));
      getAstNode().get().get_PostComments().forEach(comment -> output.append(comment.getText()).append(" "));
    }

    return output.toString();
  }

  /**
   * The same symbol is used for neurons and components. To  distinguish between them, this enum is
   * used.
   */
  public enum Type { NEURON, COMPONENT }

  static private class NeuronSymbolKind implements SymbolKind {

    NeuronSymbolKind() {
    }

  }

}