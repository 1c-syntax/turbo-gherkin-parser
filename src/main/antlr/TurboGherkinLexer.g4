lexer grammar TurboGherkinLexer;

@members {
public TurboGherkinLexer(CharStream input, boolean crAwareCostructor) {
  super(input);
  _interp = new CRAwareLexerATNSimulatorWrapper(this, _ATN);
  validateInputStream(_ATN, input);
}
}

// COMMON
EOL     : '\r'? '\n';
SPACE   : ' ';
TAB     : '\t';
HASH    : '#' ~[\r\n]* -> channel(HIDDEN);
AT      : '@';
BAR     : '|';
LABRACKET: '<';
RABRACKET: '>';
DOLLAR: '$';
DDOLLAR: '$$';
TDOLLAR: '$$$';
DOC_STRINGS  : '"""';
COMMENT_LINE : '//' ~[\r\n]* -> channel(HIDDEN);

DECIMAL: [0-9]+;

FLOAT : [0-9]+ '.' [0-9]*;
STRING: ('"' (~[\r\n"] | '""')* '"')
        | ('\'' (~[\r\n'])* '\'');

// KEYWORDS (https://cucumber.io/docs/gherkin/languages/)
// Хотя Gherkin поддерживает использование звездочки ( *) вместо обычных ключевых слов шагов,
// в турбогеркине они используются для иных целей
FEATURE_KEYWORD             : (F E A T U R E        | RU_F RU_U RU_N RU_K RU_C RU_I RU_YA
                                                    | RU_F RU_U RU_N RU_K RU_C RU_I RU_O RU_N RU_A RU_L RU_SOFT_SIGN RU_N RU_O RU_S RU_T RU_SOFT_SIGN
                                                    | RU_F RU_U RU_N RU_K RU_C RU_I RU_O RU_N RU_A RU_L
                                                    | RU_S RU_V RU_O RU_J RU_S RU_T RU_V RU_O
                            ) ':';
BACKGROUND_KEYWORD          : (B A C K G R O U N D  | RU_K RU_O RU_N RU_T RU_E RU_K RU_S RU_T
                                                    | RU_P RU_R RU_E RU_D RU_Y RU_S RU_T RU_O RU_R RU_I RU_YA
                            ) ':';
SCENARIO_KEYWORD            : (S C E N A R I O      | RU_S RU_C RU_E RU_N RU_A RU_R RU_I RU_J
                            ) ':';
SCENARIOS_KEYWORD           : (S C E N A R I O S    | RU_S RU_C RU_E RU_N RU_A RU_R RU_I RU_I
                            ) ':';
SCENARIO_OUTLINE_KEYWORD    : (  S C E N A R I O ' ' O U T L I N E
                               | S C E N A R I O ' ' T E M P L A T E
                                                    | RU_S RU_T RU_R RU_U RU_K RU_T RU_U RU_R RU_A ' ' RU_S RU_C RU_E RU_N RU_A RU_R RU_I RU_YA
                            ) ':';
EXAMPLE_KEYWORD             : (E X A M P L E        | RU_P RU_R RU_I RU_M RU_E RU_R
                            ) ':';
EXAMPLES_KEYWORD            : (E X A M P L E S      | RU_P RU_R RU_I RU_M RU_E RU_R RU_Y
                            ) ':';
GIVEN_KEYWORD               : G I V E N             | RU_D RU_A RU_N RU_O
                                                    | RU_D RU_O RU_P RU_U RU_S RU_T RU_I RU_M
                                                    | RU_P RU_U RU_S RU_T RU_SOFT_SIGN
                            ;
WHEN_KEYWORD                : W H E N               | RU_K RU_O RU_G RU_D RU_A
                            ;
IF_KEYWORD                  : I F                   | RU_E RU_S RU_L RU_I
                            ;
THEN_KEYWORD                : T H E N               | RU_T RU_O RU_G RU_D RU_A
                                                    | RU_T RU_O
                                                    | RU_Z RU_A RU_T RU_E RU_M
                            ;
AND_KEYWORD                 : A N D                 | RU_I
                                                    | RU_K ' ' RU_T RU_O RU_M RU_U ' ' RU_ZH RU_E
                                                    | RU_T RU_A RU_K RU_ZH RU_E
                            ;
BUT_KEYWORD                 : B U T                 | RU_N RU_O
                                                    | RU_A
                                                    | RU_I RU_N RU_A RU_CH RU_E
                            ;
RULE_KEYWORD                : (R U L E              | RU_P RU_R RU_A RU_V RU_I RU_L RU_O
                            ) ':';

// OTHER
ANYSYMBOL: .;

// LETTERS
fragment RU_A: 'А' | 'а';
fragment RU_B: 'Б' | 'б';
fragment RU_V: 'В' | 'в';
fragment RU_G: 'Г' | 'г';
fragment RU_D: 'Д' | 'д';
fragment RU_YO: 'Ё' | 'ё';
fragment RU_E: 'Е' | 'е';
fragment RU_ZH: 'Ж' | 'ж';
fragment RU_Z: 'З' | 'з';
fragment RU_I: 'И' | 'и';
fragment RU_J: 'Й' | 'й';
fragment RU_K: 'К' | 'к';
fragment RU_L: 'Л' | 'л';
fragment RU_M: 'М' | 'м';
fragment RU_N: 'Н' | 'н';
fragment RU_O: 'О' | 'о';
fragment RU_P: 'П' | 'п';
fragment RU_R: 'Р' | 'р';
fragment RU_S: 'С' | 'с';
fragment RU_T: 'Т' | 'т';
fragment RU_U: 'У' | 'у';
fragment RU_F: 'Ф' | 'ф';
fragment RU_H: 'Х' | 'х';
fragment RU_C: 'Ц' | 'ц';
fragment RU_CH: 'Ч' | 'ч';
fragment RU_SH: 'Ш' | 'ш';
fragment RU_SCH: 'Щ' | 'щ';
fragment RU_SOLID_SIGN: 'Ъ' | 'ъ';
fragment RU_Y: 'Ы' | 'ы';
fragment RU_SOFT_SIGN: 'Ь' | 'ь';
fragment RU_EH: 'Э' | 'э';
fragment RU_YU: 'Ю' | 'ю';
fragment RU_YA: 'Я' | 'я';
fragment A: 'A' | 'a';
fragment B: 'B' | 'b';
fragment C: 'C' | 'c';
fragment D: 'D' | 'd';
fragment I: 'I' | 'i';
fragment J: 'J' | 'j';
fragment E: 'E' | 'e';
fragment F: 'F' | 'f';
fragment G: 'G' | 'g';
fragment U: 'U' | 'u';
fragment K: 'K' | 'k';
fragment L: 'L' | 'l';
fragment M: 'M' | 'm';
fragment N: 'N' | 'n';
fragment O: 'O' | 'o';
fragment P: 'P' | 'p';
fragment Q: 'Q' | 'q';
fragment R: 'R' | 'r';
fragment S: 'S' | 's';
fragment T: 'T' | 't';
fragment V: 'V' | 'v';
fragment H: 'H' | 'h';
fragment W: 'W' | 'w';
fragment X: 'X' | 'x';
fragment Y: 'Y' | 'y';
