/*
 * This file is a part of Turbo Gherkin Parser.
 *
 * Copyright © 2020-2020
 * Valery Maximov <maximovvalery@gmail.com>, 1c-syntax team <www.github.com/1c-syntax>, BIA Technologies team <www.bia-tech.ru> and contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * Turbo Gherkin Parser is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * Turbo Gherkin Parser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Turbo Gherkin Parser.
 */

import com.github._1c_syntax.bsl.parser.CaseChangingCharStream;
import com.github._1c_syntax.bsl.parser.UnicodeBOMInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Tree;
import org.apache.commons.io.IOUtils;
import com.github._1c_syntax.turbo.gherkin.parser.GherkinParserRuleContext;
import com.github._1c_syntax.turbo.gherkin.parser.TurboGherkinLexer;
import com.github._1c_syntax.turbo.gherkin.parser.TurboGherkinParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Утилиты для тестов
 */
public final class TestUtils {

  public TestUtils() {
    // utils
  }

  /**
   * Проверяет входную строку на соответствие списку ожидаемых токенов
   *
   * @param inputString строка с примером
   * @param tokensIds   список идентификаторов токенов
   */
  public static void assertThatTokens(String inputString, Integer... tokensIds) {
    assertThat(getTokens(inputString)).extracting(Token::getType).containsExactly(tokensIds);
  }

  /**
   * Проверяет входную строку на наличие нужного токена
   *
   * @param inputString строка с примером
   * @param tokensIds   список идентификаторов токенов
   */
  public static void assertThatContains(String inputString, Integer... tokensIds) {
    assertThat(getTokens(inputString)).extracting(Token::getType).contains(tokensIds);
  }

  /**
   * Проверяет наличие в дереве нужных узлов
   *
   * @param tree  Исходное дерево
   * @param index Индекс узла
   */
  public static void assertThatContains(ParseTree tree, Integer... index) {
    Set<Integer> indexes = new HashSet<>(Arrays.asList(index));
    assertThat(IntStream.range(0, tree.getChildCount())
      .mapToObj(tree::getChild)
      .filter((Tree child) ->
        child instanceof GherkinParserRuleContext
          && indexes.contains(((GherkinParserRuleContext) child).getRuleIndex())
          && !((GherkinParserRuleContext) child).getText().isBlank())
      .map((Tree child) -> ((GherkinParserRuleContext) child).getRuleIndex())).contains(index);
  }

  /**
   * Проверяет входную строку на отсутствие нужного токена
   *
   * @param inputString строка с примером
   * @param tokensIds   список идентификаторов токенов
   */
  public static void assertThatNotContains(String inputString, Integer... tokensIds) {
    assertThat(getTokens(inputString)).extracting(Token::getType).doesNotContain(tokensIds);
  }

  /**
   * Возвращает все дочерние узны от переданного корня дерева
   *
   * @param tree Корень дерева
   * @return найденные дочерние узлы
   */
  protected static Stream<GherkinParserRuleContext> getAllChildren(Tree tree) {
    return IntStream.range(0, tree.getChildCount())
      .mapToObj(tree::getChild)
      .filter((Tree child) -> child instanceof GherkinParserRuleContext)
      .map(child -> (GherkinParserRuleContext) child);
  }

  /**
   * Возвращает список токенов в строке
   *
   * @param inputString исходная строка для анализа
   * @return список найденных токенов
   */
  public static List<Token> getTokens(String inputString) {
    CharStream input;

    try (
      InputStream inputStream = IOUtils.toInputStream(inputString, StandardCharsets.UTF_8);
      UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(inputStream);
      Reader inputStreamReader = new InputStreamReader(ubis, StandardCharsets.UTF_8)
    ) {
      ubis.skipBOM();
      CodePointCharStream inputTemp = CharStreams.fromReader(inputStreamReader);
      input = new CaseChangingCharStream(inputTemp);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    var lexer = createLexer(input);
    CommonTokenStream tempTokenStream = new CommonTokenStream(lexer);
    tempTokenStream.fill();

    return tempTokenStream.getTokens();
  }

  /**
   * Анализирует дерево на наличие ошибок парсинга
   *
   * @param tree корень анализируемого дерева
   * @return признак наличия ошибки
   */
  public static boolean treeContainsErrors(ParseTree tree) {
    if (!(tree instanceof GherkinParserRuleContext)) {
      return false;
    }

    var ruleContext = (GherkinParserRuleContext) tree;

    if (ruleContext.exception != null) {
      return true;
    }

    return ruleContext.children != null
      && ruleContext.children.stream().anyMatch(TestUtils::treeContainsErrors);
  }

  protected static TurboGherkinParser createParser(String inputString) {
    CharStream input;

    try (
      InputStream inputStream = IOUtils.toInputStream(inputString, StandardCharsets.UTF_8);
      UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(inputStream);
      Reader inputStreamReader = new InputStreamReader(ubis, StandardCharsets.UTF_8)
    ) {
      ubis.skipBOM();
      CodePointCharStream inputTemp = CharStreams.fromReader(inputStreamReader);
      input = new CaseChangingCharStream(inputTemp);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    var lexer = new TurboGherkinLexer(input);
    CommonTokenStream tokenStream = new CommonTokenStream(lexer);
    tokenStream.fill();

    var parser = new TurboGherkinParser(tokenStream);
    parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
    return parser;
  }

  protected static TurboGherkinLexer createLexer(CharStream inputStream) {
    var lexer = new TurboGherkinLexer(inputStream);
    lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);
    lexer.pushMode(TurboGherkinLexer.DEFAULT_MODE);
    return lexer;
  }

}
