parser grammar TurboGherkinParser;

options {
    tokenVocab = TurboGherkinLexer;
    contextSuperClass = 'GherkinParserRuleContext';
}

// структура фиче-файла
feature: white tags FEATURE_KEYWORD space* featureName white featureDescription featureBody EOF;

// теги
tags: white (tag (SPACE | TAB | EOL)+)*;
tag: AT (~(AT | SPACE | TAB | EOL))+;

// название фичи
featureName: (~EOL)*; // символы до конца строки
// описание фичи
featureDescription:
    (white
        (~(SCENARIO_OUTLINE_KEYWORD | SCENARIO_KEYWORD | BACKGROUND_KEYWORD | EXAMPLE_KEYWORD | RULE_KEYWORD | EOL))* EOL
    )*;
// содержимое фичи
featureBody: background? (businessRules | scenarios);

// бизнес-правила (GHERKIN 6)
businessRules: businessRule+;
businessRule: white RULE_KEYWORD space* name white description background? scenarios;

// общий контекст фичи или бизнес правила, если используются они
background: white BACKGROUND_KEYWORD white description steps;

// элементы
scenarios: (scenario | scenarioOutline)+;
scenario: white (SCENARIO_KEYWORD | EXAMPLE_KEYWORD) space* name white description steps;

scenarioOutline: white SCENARIO_OUTLINE_KEYWORD space* name white description steps examples;

// примеры
examples: white (EXAMPLES_KEYWORD | SCENARIOS_KEYWORD) space* (EOL+ | EOF) table;

// шаги
steps: step*;
step: white stepKeyword SPACE name (EOL+ | EOF) (docStrings | table)?;

// пустая строка
white: (SPACE | TAB | EOL)*;
// допустимый пробел
space: SPACE | TAB;

// общие
name:
    (parameter | partName | space+)+;
parameter: DECIMAL | FLOAT | STRING | (LABRACKET ~(EOL | LABRACKET | RABRACKET)+ RABRACKET);
partName: ~(EOL | DECIMAL | STRING | LABRACKET | SPACE | TAB)+;

description:
    (white
        (~(GIVEN_KEYWORD | WHEN_KEYWORD | THEN_KEYWORD | AND_KEYWORD | BUT_KEYWORD | EOL | SPACE)) (~EOL)+ EOL
    )*;

// таблица Gherkin
table: white tableHead tableRows;
tableHead: space* BAR tableCell+ space* (EOL+ | EOF);
tableRows: tableRow+;
tableRow: space* BAR tableCell+ space* (EOL+ | EOF);
tableCell: tableCellValue BAR;
tableCellValue: (~(EOL | BAR))*;

docStrings: space* DOC_STRINGS space* EOL docStringsValue space* DOC_STRINGS space* (EOL+ | EOF);
docStringsValue: (~DOC_STRINGS)*;

// ключевые слова для шагов
stepKeyword:
    GIVEN_KEYWORD
    | WHEN_KEYWORD
    | THEN_KEYWORD
    | AND_KEYWORD
    | BUT_KEYWORD
    ;
