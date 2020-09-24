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
package com.github._1c_syntax.turbo.gherkin.parser;

import com.github._1c_syntax.bsl.parser.Tokenizer;
import org.antlr.v4.runtime.CharStreams;

/**
 * Токенайзер
 * Расширяет класс Tokenizer из bsl-parser
 */
public class GherkinTokenizer extends Tokenizer<TurboGherkinParser.FeatureContext, TurboGherkinParser> {
  public GherkinTokenizer(String content) {
    super(content, new TurboGherkinLexer(CharStreams.fromString(""), true), TurboGherkinParser.class);
  }

  @Override
  protected TurboGherkinParser.FeatureContext rootAST() {
    return parser.feature();
  }
}
