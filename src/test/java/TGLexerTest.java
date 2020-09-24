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

import org.junit.jupiter.api.Test;
import com.github._1c_syntax.turbo.gherkin.parser.TurboGherkinLexer;

class TGLexerTest {

  @Test
  void testWhitespaces() {
    TestUtils.assertThatTokens("  @А\t\t",
      TurboGherkinLexer.SPACE,
      TurboGherkinLexer.SPACE,
      TurboGherkinLexer.AT,
      TurboGherkinLexer.BUT_KEYWORD,
      TurboGherkinLexer.TAB,
      TurboGherkinLexer.TAB,
      TurboGherkinLexer.EOF
    );

    TestUtils.assertThatTokens("  \"@А\t\t\"",
      TurboGherkinLexer.SPACE,
      TurboGherkinLexer.SPACE,
      TurboGherkinLexer.STRING,
      TurboGherkinLexer.EOF
    );
  }

  @Test
  void testBOM() {
    TestUtils.assertThatTokens('\uFEFF' + "Feature:",
      TurboGherkinLexer.FEATURE_KEYWORD,
      TurboGherkinLexer.EOF);
  }

  @Test
  void testFEATURE_KEYWORD() {
    var checkToken = TurboGherkinLexer.FEATURE_KEYWORD;
    TestUtils.assertThatContains("Feature:", checkToken);
    TestUtils.assertThatContains("Функция:", checkToken);
    TestUtils.assertThatContains("Функционал:", checkToken);
    TestUtils.assertThatContains("Функциональность:", checkToken);
    TestUtils.assertThatContains("Свойство:", checkToken);
    TestUtils.assertThatNotContains("Feature", checkToken);
    TestUtils.assertThatNotContains("Функция :", checkToken);
    TestUtils.assertThatNotContains("Функционал\t:", checkToken);
    TestUtils.assertThatNotContains("Функциональность\n:", checkToken);
    TestUtils.assertThatNotContains("Свойство#:", checkToken);
  }

  @Test
  void testBACKGROUND_KEYWORD() {
    var checkToken = TurboGherkinLexer.BACKGROUND_KEYWORD;
    TestUtils.assertThatContains("Background:", checkToken);
    TestUtils.assertThatContains("Контекст:", checkToken);
    TestUtils.assertThatContains("Предыстория:", checkToken);
    TestUtils.assertThatNotContains("Background", checkToken);
    TestUtils.assertThatNotContains("Контекст :", checkToken);
    TestUtils.assertThatNotContains("Предыстория\t:", checkToken);
    TestUtils.assertThatNotContains("Предыстория\n:", checkToken);
    TestUtils.assertThatNotContains("Предыстория#:", checkToken);
  }

  @Test
  void testSCENARIO_KEYWORD() {
    var checkToken = TurboGherkinLexer.SCENARIO_KEYWORD;
    TestUtils.assertThatContains("Scenario:", checkToken);
    TestUtils.assertThatContains("Сценарий:", checkToken);
    TestUtils.assertThatNotContains("Scenario", checkToken);
    TestUtils.assertThatNotContains("Сценарий :", checkToken);
    TestUtils.assertThatNotContains("Scenario\t:", checkToken);
    TestUtils.assertThatNotContains("Scenario\n:", checkToken);
    TestUtils.assertThatNotContains("Scenario#:", checkToken);
  }

  @Test
  void testSCENARIOS_KEYWORD() {
    var checkToken = TurboGherkinLexer.SCENARIOS_KEYWORD;
    TestUtils.assertThatContains("Scenarios:", checkToken);
    TestUtils.assertThatContains("Сценарии:", checkToken);
    TestUtils.assertThatNotContains("Scenari", checkToken);
    TestUtils.assertThatNotContains("Сценарии :", checkToken);
    TestUtils.assertThatNotContains("Scenarios\t:", checkToken);
    TestUtils.assertThatNotContains("Scenarios\n:", checkToken);
    TestUtils.assertThatNotContains("Scenarios#:", checkToken);
  }

  @Test
  void testSCENARIO_OUTLINE_KEYWORD() {
    var checkToken = TurboGherkinLexer.SCENARIO_OUTLINE_KEYWORD;
    TestUtils.assertThatContains("Scenario outline:", checkToken);
    TestUtils.assertThatContains("Scenario template:", checkToken);
    TestUtils.assertThatContains("Структура сценария:", checkToken);
    TestUtils.assertThatNotContains("Scenario outline", checkToken);
    TestUtils.assertThatNotContains("Scenario outline :", checkToken);
    TestUtils.assertThatNotContains("Scenario  outline:", checkToken);
    TestUtils.assertThatNotContains("Scenario\toutline:", checkToken);
    TestUtils.assertThatNotContains("Scenariooutline:", checkToken);
  }

  @Test
  void testEXAMPLE_KEYWORD() {
    var checkToken = TurboGherkinLexer.EXAMPLE_KEYWORD;
    TestUtils.assertThatContains("Example:", checkToken);
    TestUtils.assertThatContains("Пример:", checkToken);
    TestUtils.assertThatNotContains("Example", checkToken);
    TestUtils.assertThatNotContains("Примеры:", checkToken);
    TestUtils.assertThatNotContains("Пример :", checkToken);
    TestUtils.assertThatNotContains("Пример\t:", checkToken);
  }

  @Test
  void testEXAMPLES_KEYWORD() {
    var checkToken = TurboGherkinLexer.EXAMPLES_KEYWORD;
    TestUtils.assertThatContains("Examples:", checkToken);
    TestUtils.assertThatContains("Примеры:", checkToken);
    TestUtils.assertThatNotContains("Examples", checkToken);
    TestUtils.assertThatNotContains("Пример:", checkToken);
    TestUtils.assertThatNotContains("Примеры :", checkToken);
    TestUtils.assertThatNotContains("Примеры\t:", checkToken);
  }

  @Test
  void testGIVEN_KEYWORD() {
    var checkToken = TurboGherkinLexer.GIVEN_KEYWORD;
    TestUtils.assertThatContains("Given", checkToken);
    TestUtils.assertThatContains("Дано", checkToken);
    TestUtils.assertThatContains("Допустим", checkToken);
    TestUtils.assertThatContains("Пусть", checkToken);
    TestUtils.assertThatContains("Пусть будет", checkToken);
    TestUtils.assertThatContains("Пусть\t Будет \n #Мир", checkToken);
    TestUtils.assertThatContains("Ну и пусть", checkToken);
    TestUtils.assertThatNotContains("#Пусть:", checkToken);
    TestUtils.assertThatNotContains("Данo", checkToken);
  }

  @Test
  void testWHEN_KEYWORD() {
    var checkToken = TurboGherkinLexer.WHEN_KEYWORD;
    TestUtils.assertThatContains("When", checkToken);
    TestUtils.assertThatContains("КОгда", checkToken);
    TestUtils.assertThatContains("КОгда нибудь", checkToken);
    TestUtils.assertThatNotContains("#КОгда:", checkToken);
  }

  @Test
  void testIF_KEYWORD() {
    var checkToken = TurboGherkinLexer.IF_KEYWORD;
    TestUtils.assertThatContains("If", checkToken);
    TestUtils.assertThatContains("Если", checkToken);
    TestUtils.assertThatContains("Если мы будем", checkToken);
    TestUtils.assertThatNotContains("#If:", checkToken);
  }

  @Test
  void testTHEN_KEYWORD() {
    var checkToken = TurboGherkinLexer.THEN_KEYWORD;
    TestUtils.assertThatContains("Then", checkToken);
    TestUtils.assertThatContains("Тогда", checkToken);
    TestUtils.assertThatContains("То", checkToken);
    TestUtils.assertThatContains("Затем", checkToken);
    TestUtils.assertThatContains("Тогда или не тогда", checkToken);
    TestUtils.assertThatNotContains("#Потому", checkToken);
  }

  @Test
  void testAND_KEYWORD() {
    var checkToken = TurboGherkinLexer.AND_KEYWORD;
    TestUtils.assertThatContains("And", checkToken);
    TestUtils.assertThatContains("И", checkToken);
    TestUtils.assertThatContains("К тому же", checkToken);
    TestUtils.assertThatContains("И так", checkToken);
    TestUtils.assertThatContains("Также", checkToken);
    TestUtils.assertThatNotContains("К тому\tже", checkToken);
  }

  @Test
  void testBUT_KEYWORD() {
    var checkToken = TurboGherkinLexer.BUT_KEYWORD;
    TestUtils.assertThatContains("But", checkToken);
    TestUtils.assertThatContains("Но", checkToken);
    TestUtils.assertThatContains("А", checkToken);
    TestUtils.assertThatContains("Иначе", checkToken);
    TestUtils.assertThatNotContains("не", checkToken);
  }

  @Test
  void testRULE_KEYWORD() {
    var checkToken = TurboGherkinLexer.RULE_KEYWORD;
    TestUtils.assertThatContains("Rule:", checkToken);
    TestUtils.assertThatContains("Правило:", checkToken);
    TestUtils.assertThatNotContains("Правила", checkToken);
    TestUtils.assertThatNotContains("Правило", checkToken);
    TestUtils.assertThatNotContains("Правило :", checkToken);
    TestUtils.assertThatNotContains("Правило\t:", checkToken);
  }

  @Test
  void testString() {
    var checkToken = TurboGherkinLexer.STRING;
    TestUtils.assertThatContains("'Строка в одинарных кавычках'", checkToken);
    TestUtils.assertThatContains("\"Строка в двойных кавычках\"", checkToken);
    TestUtils.assertThatNotContains("\"Неправльная строка'", checkToken);
  }

  @Test
  void testMultiString() {
    TestUtils.assertThatTokens(" \"\"\"\n bb \"бла\" bb\n\"\"\"",
      TurboGherkinLexer.SPACE,
      TurboGherkinLexer.DOC_STRINGS,
      TurboGherkinLexer.EOL,
      TurboGherkinLexer.SPACE,
      TurboGherkinLexer.ANYSYMBOL,
      TurboGherkinLexer.ANYSYMBOL,
      TurboGherkinLexer.SPACE,
      TurboGherkinLexer.STRING,
      TurboGherkinLexer.SPACE,
      TurboGherkinLexer.ANYSYMBOL,
      TurboGherkinLexer.ANYSYMBOL,
      TurboGherkinLexer.EOL,
      TurboGherkinLexer.DOC_STRINGS,
      TurboGherkinLexer.EOF
    );
  }
}
