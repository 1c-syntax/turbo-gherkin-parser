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

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import com.github._1c_syntax.turbo.gherkin.parser.GherkinParserRuleContext;
import com.github._1c_syntax.turbo.gherkin.parser.TurboGherkinLexer;
import com.github._1c_syntax.turbo.gherkin.parser.TurboGherkinParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class TGParserTest {

  @Test
  void testFeature() {
    var parser = TestUtils.createParser(
      "Функционал: тестовый пример\n" +
        "Описание тестового сценария\n" +
        "Сценарий: тестовый сценарий");
    var feature = parser.feature();
    assertThat(TestUtils.treeContainsErrors(feature)).isFalse();
    TestUtils.assertThatContains(feature,
      TurboGherkinParser.RULE_featureName,
      TurboGherkinParser.RULE_featureDescription,
      TurboGherkinParser.RULE_featureBody);

    assertThat(feature.featureName()).isNotNull();
    assertThat(feature.featureName().getText())
      .isNotNull()
      .isEqualTo("тестовый пример");
    assertThat(feature.featureDescription()).isNotNull();
    assertThat(feature.featureDescription().getText())
      .isNotNull()
      .isEqualTo("Описание тестового сценария\n");
  }

  @Test
  void testTags() {
    var parser = TestUtils.createParser(
      "@Тег1\n\n" +
        "@Тег2\n" +
        "Функционал: тестовый пример\n" +
        "Описание тестового сценария\n" +
        "Сценарий: тестовый сценарий");
    var feature = parser.feature();
    assertThat(TestUtils.treeContainsErrors(feature)).isFalse();
    TestUtils.assertThatContains(feature,
      TurboGherkinParser.RULE_featureName,
      TurboGherkinParser.RULE_tags,
      TurboGherkinParser.RULE_featureDescription,
      TurboGherkinParser.RULE_featureBody);

    var tags = feature.tags();
    TestUtils.assertThatContains(tags,
      TurboGherkinParser.RULE_tag);
    assertThat(TestUtils.getAllChildren(tags))
      .filteredOn(child -> child.getRuleIndex() == TurboGherkinParser.RULE_tag)
      .hasSize(2)
      .anyMatch(child -> "@Тег1".equals(child.getText()))
      .anyMatch(child -> "@Тег2".equals(child.getText()));
  }

  @Test
  void testFeatureBody() {
    var parser = TestUtils.createParser(
      "Функционал: тестовый пример\n" +
        "Контекст: \n" +
        "Тестовое описание контекста\n" +
        "Дано что-to \n" +
        "И что-то еще\n" +
        "Сценарий: тестовый сценарий\n" +
        "У сценария тоже бывает описание, перед шагами\n" +
        "Когда первый шаг сценария\n" +
        "Затем второй");
    var feature = parser.feature();
    assertThat(TestUtils.treeContainsErrors(feature)).isFalse();
    TestUtils.assertThatContains(feature,
      TurboGherkinParser.RULE_featureName,
      TurboGherkinParser.RULE_featureBody);

    var featureBody = feature.featureBody();
    TestUtils.assertThatContains(featureBody,
      TurboGherkinParser.RULE_background,
      TurboGherkinParser.RULE_scenarios);

    parser = TestUtils.createParser(
      "Функционал: тестовый пример\n" +
        "Контекст: \n" +
        "Тестовое описание контекста\n" +
        "Дано что-to \n" +
        "И что-то еще\n" +
        "Правило: бизнес правило номер 1\n" +
        "Сценарий: тестовый сценарий\n" +
        "У сценария тоже бывает описание, перед шагами\n" +
        "Когда первый шаг сценария\n" +
        "Затем второй");
    feature = parser.feature();
    assertThat(TestUtils.treeContainsErrors(feature)).isFalse();
    TestUtils.assertThatContains(feature,
      TurboGherkinParser.RULE_featureName,
      TurboGherkinParser.RULE_featureBody);

    featureBody = feature.featureBody();
    TestUtils.assertThatContains(featureBody,
      TurboGherkinParser.RULE_background,
      TurboGherkinParser.RULE_businessRules);
  }

  @Test
  void testBackground() {
    var parser = TestUtils.createParser(
      "Функционал: тестовый пример\n" +
        "Контекст: \n" +
        "Тестовое описание контекста\n" +
        "Дано что-to \n" +
        "И что-то еще\n" +
        "Сценарий: тестовый сценарий\n" +
        "У сценария тоже бывает описание, перед шагами\n" +
        "Когда первый шаг сценария\n" +
        "Затем второй");
    var feature = parser.feature();
    assertThat(TestUtils.treeContainsErrors(feature)).isFalse();
    TestUtils.assertThatContains(feature, TurboGherkinParser.RULE_featureBody);
    var featureBody = feature.featureBody();
    TestUtils.assertThatContains(featureBody, TurboGherkinParser.RULE_background);

    var background = featureBody.background();
    TestUtils.assertThatContains(background,
      TurboGherkinParser.RULE_description,
      TurboGherkinParser.RULE_steps);

    assertThat(background.description()).isNotNull();
    assertThat(background.description().getText())
      .isNotNull()
      .isEqualTo("Тестовое описание контекста\n");

    assertThat(TestUtils.getAllChildren(background.steps()))
      .filteredOn(child -> child.getRuleIndex() == TurboGherkinParser.RULE_step)
      .hasSize(2)
      .allMatch(child -> !child.getText().isBlank());

    parser = TestUtils.createParser(
      "Функционал: тестовый пример\n" +
        "Контекст: \n" +
        "Дано что-to \n" +
        "Сценарий: тестовый сценарий\n" +
        "У сценария тоже бывает описание, перед шагами\n" +
        "Когда первый шаг сценария\n" +
        "Затем второй");

    background = parser.feature().featureBody().background();
    TestUtils.assertThatContains(background, TurboGherkinParser.RULE_steps);

  }

  @Test
  void testScenarios() {
    var parser = TestUtils.createParser(
      "Функционал: тестовый пример\n" +
        "Контекст: \n" +
        "Тестовое описание контекста\n" +
        "Дано что-to \n" +
        "И что-то еще\n" +
        "Сценарий: тестовый сценарий\n" +
        "У сценария тоже бывает описание\n, перед шагами\n" +
        "Когда первый шаг сценария\n" +
        "Затем второй\n" +
        "Сценарий: второй тестовый сценарий\n" +
        "Когда второй сценарий без описания\n" +
        "Тогда разбор тоже работает корректно\n" +
        "And no problem\n\n" +
        "Структура Сценария: Шаблонный тестовый сценарий\n" +
        "Шаблонный сценарий может иметь описание, как и все остальные\n" +
        "Когда <Парам1> сценарий без описания\n" +
        "Тогда <Значение2>\n" +
        "Примеры:\n" +
        "|Парам1|Значение2|\n" +
        "|ЫЫ|1|");
    var feature = parser.feature();
    assertThat(TestUtils.treeContainsErrors(feature)).isFalse();
    TestUtils.assertThatContains(feature, TurboGherkinParser.RULE_featureBody);
    var featureBody = feature.featureBody();
    TestUtils.assertThatContains(featureBody,
      TurboGherkinParser.RULE_background,
      TurboGherkinParser.RULE_scenarios);

    var scenarios = featureBody.scenarios();
    TestUtils.assertThatContains(scenarios,
      TurboGherkinParser.RULE_scenario,
      TurboGherkinParser.RULE_scenarioOutline);
    assertThat(scenarios.scenario()).hasSize(2);
    assertThat(scenarios.scenarioOutline()).hasSize(1);

    // первый сценарий
    var scenario = (TurboGherkinParser.ScenarioContext)
      ((GherkinParserRuleContext) scenarios.getChild(0)).getRuleContext();

    assertThat(scenario.description()).isNotNull();
    assertThat(scenario.description().getText())
      .isNotNull()
      .isEqualTo("У сценария тоже бывает описание\n, перед шагами\n");

    assertThat(scenario.name().getText())
      .isNotNull()
      .isEqualTo("тестовый сценарий");

    assertThat(TestUtils.getAllChildren(scenario.steps()))
      .filteredOn(child -> child.getRuleIndex() == TurboGherkinParser.RULE_step)
      .hasSize(2)
      .allMatch(child -> !child.getText().isBlank());

    // второй сценарий
    scenario = (TurboGherkinParser.ScenarioContext)
      ((GherkinParserRuleContext) scenarios.getChild(1)).getRuleContext();

    assertThat(scenario.description()).isNotNull();
    assertThat(scenario.description().getText())
      .isNotNull()
      .isEmpty();

    assertThat(scenario.name().getText())
      .isNotNull()
      .isEqualTo("второй тестовый сценарий");

    assertThat(TestUtils.getAllChildren(scenario.steps()))
      .filteredOn(child -> child.getRuleIndex() == TurboGherkinParser.RULE_step)
      .hasSize(3)
      .allMatch(child -> !child.getText().isBlank());

    // шаблонный сценарий
    var scenarioOutline = (TurboGherkinParser.ScenarioOutlineContext)
      ((GherkinParserRuleContext) scenarios.getChild(2)).getRuleContext();

    TestUtils.assertThatContains(scenarioOutline,
      TurboGherkinParser.RULE_steps,
      TurboGherkinParser.RULE_examples,
      TurboGherkinParser.RULE_name,
      TurboGherkinParser.RULE_description);

    assertThat(TestUtils.getAllChildren(scenarioOutline.steps()))
      .filteredOn(child -> child.getRuleIndex() == TurboGherkinParser.RULE_step)
      .hasSize(2)
      .allMatch(child -> !child.getText().isBlank());

    assertThat(scenarioOutline.examples().table()).isNotNull();
    assertThat(scenarioOutline.examples().table().tableHead()).isNotNull();
    assertThat(scenarioOutline.examples().table().tableRows()).isNotNull();
    assertThat(scenarioOutline.examples().table().tableRows().tableRow()).hasSize(1);
  }

  @Test
  void testBusinessRules() {
    var parser = TestUtils.createParser(
      "Функционал: тестовый пример\n" +
        "Контекст: \n" +
        "Тестовое описание контекста\n" +
        "Дано что-to \n" +
        "И что-то еще\n" +
        "Rule: Business rule №1\n" +
        "Description rule\n" +
        "Сценарий: тестовый сценарий\n" +
        "У сценария тоже бывает описание\n, перед шагами\n" +
        "Когда первый шаг сценария\n" +
        "Затем второй\n" +
        "Правило: Бизнес правило 2\n" +
        "Контекст: \n" +
        "У бизнес правила может быть контекст\n" +
        "Хотя и считается деприкейтом\n" +
        "Дано что-to важное\n" +
        "Структура Сценария: Шаблонный тестовый сценарий\n" +
        "Когда <Парам1> сценарий без описания\n" +
        "Тогда <Значение2>\n" +
        "Примеры:\n" +
        "|Парам1|Значение2|\n" +
        "|ЫЫ|1|");
    var feature = parser.feature();
    assertThat(TestUtils.treeContainsErrors(feature)).isFalse();
    TestUtils.assertThatContains(feature, TurboGherkinParser.RULE_featureBody);
    var featureBody = feature.featureBody();
    TestUtils.assertThatContains(featureBody,
      TurboGherkinParser.RULE_background,
      TurboGherkinParser.RULE_businessRules);

    var businessRules = featureBody.businessRules();
    TestUtils.assertThatContains(businessRules, TurboGherkinParser.RULE_businessRule);
    assertThat(businessRules.businessRule()).hasSize(2);

    // первое правило
    var rule = (TurboGherkinParser.BusinessRuleContext)
      ((GherkinParserRuleContext) businessRules.getChild(0)).getRuleContext();

    assertThat(rule.description()).isNotNull();
    assertThat(rule.description().getText())
      .isNotNull()
      .isEqualTo("Description rule\n");

    assertThat(rule.name()).isNotNull();
    assertThat(rule.name().getText())
      .isNotNull()
      .isEqualTo("Business rule №1");

    TestUtils.assertThatContains(rule, TurboGherkinParser.RULE_scenarios);

    assertThat(rule.scenarios().scenario()).isNotNull().hasSize(1);

    // второе правило
    rule = (TurboGherkinParser.BusinessRuleContext)
      ((GherkinParserRuleContext) businessRules.getChild(1)).getRuleContext();

    assertThat(rule.description()).isNotNull();
    assertThat(rule.description().getText())
      .isNotNull()
      .isEmpty();

    assertThat(rule.name()).isNotNull();
    assertThat(rule.name().getText())
      .isNotNull()
      .isEqualTo("Бизнес правило 2");

    TestUtils.assertThatContains(rule,
      TurboGherkinParser.RULE_background,
      TurboGherkinParser.RULE_scenarios);
  }

  @Test
  void testTable() {
    var parser = TestUtils.createParser(
      "Функционал: тестовый пример\n" +
        "Структура Сценария: Шаблонный тестовый сценарий\n" +
        "Когда <Парам1> сценарий без описания\n" +
        "Тогда <Значение2>\n" +
        "Примеры:\n" +
        "|Парам1|Значение2|\n" +
        "|Парам1|||||||||||\n" +
        "|||||знач|||||||\n" +
        "||\n" +
        "\n" +
        "|ЫЫ|1|");
    var feature = parser.feature();
    assertThat(TestUtils.treeContainsErrors(feature)).isFalse();
    TestUtils.assertThatContains(feature, TurboGherkinParser.RULE_featureBody);
    assertThat(feature.featureBody().scenarios()).isNotNull();

    var scenarioOutlines = feature.featureBody().scenarios().scenarioOutline();
    assertThat(scenarioOutlines).isNotNull().hasSize(1);
    var scenarioOutline = scenarioOutlines.get(0);
    assertThat(scenarioOutline.examples()).isNotNull();
    assertThat(scenarioOutline.examples().table()).isNotNull();
    var table = scenarioOutline.examples().table();
    assertThat(table.tableHead()).isNotNull();
    assertThat(table.tableHead().tableCell())
      .isNotNull()
      .hasSize(2)
      .allMatch(cell -> cell.tableCellValue() != null && cell.tableCellValue().getText() != null)
      .extracting(TurboGherkinParser.TableCellContext::tableCellValue)
      .anyMatch(cell -> "Парам1".equals(cell.getText()))
      .anyMatch(cell -> "Значение2".equals(cell.getText()));

    assertThat(table.tableRows()).isNotNull();
    assertThat(table.tableRows().tableRow()).hasSize(4);

    assertThat(table.tableRows().tableRow().get(0).tableCell()).isNotNull().hasSize(11);
    assertThat(table.tableRows().tableRow().get(1).tableCell()).isNotNull().hasSize(11);
    assertThat(table.tableRows().tableRow().get(2).tableCell()).isNotNull().hasSize(1);
    assertThat(table.tableRows().tableRow().get(3).tableCell()).isNotNull().hasSize(2);

  }

  @Test
  void testParameters() {
    var parser = TestUtils.createParser(
      "Функционал: тестовый пример\n" +
        "Сценарий: Сценарий с \"Параметром\"\n" +
        "Когда параметр равен 'строковый параметр' или \n" +
        "\"\"\"\n" +
        "многострочный \n" +
        "сложный параметр\n" +
        "\"\"\"\n" +
        "Тогда Значение больше 1.23 но меньше 0.99\n" +
        "И Значение не равно 1\n" +
        "И Значение нет в табличке\n" +
        "        |Парам1|Значение2|\n" +
        "        |ЫЫ|1|");

    var feature = parser.feature();
    assertThat(TestUtils.treeContainsErrors(feature)).isFalse();
    TestUtils.assertThatContains(feature, TurboGherkinParser.RULE_featureBody);
    assertThat(feature.featureBody().scenarios()).isNotNull();
    assertThat(feature.featureBody().scenarios().scenario()).hasSize(1);

    var scenario = feature.featureBody().scenarios().scenario().get(0);
    assertThat(scenario.name()).isNotNull();
    assertThat(scenario.name().parameter())
      .isNotNull()
      .hasSize(1);
    assertThat(scenario.name().parameter().get(0)).isNotNull();
    var param = scenario.name().parameter().get(0).getTokens().get(0);
    assertThat(param.getType()).isEqualTo(TurboGherkinLexer.STRING);
    assertThat(param.getText()).isEqualTo("\"Параметром\"");

    assertThat(scenario.steps()).isNotNull();
    assertThat(scenario.steps().step()).hasSize(4);

    var step = scenario.steps().step().get(0);
    assertThat(step.name()).isNotNull();
    assertThat(step.name().parameter())
      .isNotNull()
      .hasSize(1);

    assertThat(step.docStrings()).isNotNull();
    assertThat(step.docStrings().docStringsValue()).isNotNull();
    assertThat(step.docStrings().docStringsValue().getText())
      .isNotNull()
      .isEqualTo("многострочный \nсложный параметр\n");

    param = step.name().parameter().get(0).getTokens().get(0);
    assertThat(param.getType()).isEqualTo(TurboGherkinLexer.STRING);
    assertThat(param.getText()).isEqualTo("'строковый параметр'");

    step = scenario.steps().step().get(1);
    assertThat(step.name()).isNotNull();
    assertThat(step.name().parameter())
      .isNotNull()
      .hasSize(2);

    param = step.name().parameter().get(0).getTokens().get(0);
    assertThat(param.getType()).isEqualTo(TurboGherkinLexer.FLOAT);
    assertThat(param.getText()).isEqualTo("1.23");

    step = scenario.steps().step().get(2);
    assertThat(step.name()).isNotNull();
    assertThat(step.name().parameter())
      .isNotNull()
      .hasSize(1);

    param = step.name().parameter().get(0).getTokens().get(0);
    assertThat(param.getType()).isEqualTo(TurboGherkinLexer.DECIMAL);
    assertThat(param.getText()).isEqualTo("1");

    step = scenario.steps().step().get(3);
    assertThat(step.name()).isNotNull();
    assertThat(step.name().parameter())
      .isNotNull()
      .isEmpty();

    assertThat(step.table()).isNotNull();
  }

  @Test
  void testSmokyNoParsingError() {
    var srcDir = "./src/test/resources/correct";
    FileUtils.listFiles(Paths.get(srcDir).toAbsolutePath().toFile(), new String[]{"feature"}, true)
      .forEach(filePath -> {
        try {
          var fileSource = FileUtils.readFileToString(filePath, StandardCharsets.UTF_8);
          var parser = TestUtils.createParser(fileSource);
          var feature = parser.feature();
          assertThat(TestUtils.treeContainsErrors(feature)).isFalse();

        } catch (IOException e) {
          e.printStackTrace();
        }
      });
  }

  @Test
  void testSmokyParsingError() {
    var srcDir = "./src/test/resources/uncorrect";
    FileUtils.listFiles(Paths.get(srcDir).toAbsolutePath().toFile(), new String[]{"feature"}, true)
      .forEach(filePath -> {
        try {
          var fileSource = FileUtils.readFileToString(filePath, StandardCharsets.UTF_8);
          var parser = TestUtils.createParser(fileSource);
          var feature = parser.feature();
          assertThat(TestUtils.treeContainsErrors(feature)).isTrue();

        } catch (IOException e) {
          e.printStackTrace();
        }
      });
  }
}
