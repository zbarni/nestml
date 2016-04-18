package org.nest.spl.parsing;

import de.monticore.antlr4.MCConcreteParser;
import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;
import org.nest.base.ModelbasedTest;

import org.nest.ode._ast.ASTEq;
import org.nest.ode._ast.ASTODE;
import org.nest.ode._ast.ASTOdeDeclaration;
import org.nest.spl._parser.SPLParser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

/**
 * TODO
 *
 * @author (last commit) $$Author$$
 * @version $$Revision$$, $$Date$$
 * @since 0.0.1
 */
public class ODEParsingTest extends ModelbasedTest {

  final SPLParser parser = new SPLParser();

  @Test
  public void testOdeDefinition() throws IOException {
    final String odeDeclarationAsString =
            "I = w * (E/tau_in) * t * exp(-1/tau_in*t)\n" +
            "V' = -1/Tau * V + 1/C*I\n" ;
    Optional<ASTOdeDeclaration> res = parseOdeDeclaration(odeDeclarationAsString);
    assertTrue(res.isPresent());
  }

  @Test
  public void testOde() throws IOException {
    Optional<ASTODE> res = parseOde("V' = -1/Tau * V + 1/C*I");
    assertTrue(res.isPresent());

  }

  @Test
  public void testEq() throws IOException {
    Optional<ASTEq> res = parseEq("I = w * (E/tau_in) * t * exp(-1/tau_in*t)");
    assertTrue(res.isPresent());

  }

  public Optional<ASTODE> parseOde(String input) throws RecognitionException, IOException {

    parser.setParserTarget(MCConcreteParser.ParserExecution.EOF);
    return parser.parseODE(new StringReader(input));
  }

  public Optional<ASTEq> parseEq(String input) throws RecognitionException, IOException {

    return parser.parseEq(new StringReader(input));
  }


  public Optional<ASTOdeDeclaration> parseOdeDeclaration(String input) throws RecognitionException, IOException {

    return parser.parseOdeDeclaration(new StringReader(input));
  }


}