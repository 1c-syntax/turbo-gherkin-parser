# from https://github.com/Pr-Mex/vanessa-automation/blob/develop/features/Core/CucumberReport/%D0%A4%D0%BE%D1%80%D0%BC%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5%D0%9E%D1%82%D1%87%D0%B5%D1%82%D0%B0Cucumber.feature
# language: ru
#parent uf:
@UF5_формирование_результатов_выполнения_сценариев
#parent ua:
@UA19_формировать_отчет_Cucumber

@IgnoreOn82Builds
@IgnoreOnOFBuilds
@IgnoreOnWeb

Функционал: Проверка формирования отчета Cucumber

  Как разработчик
  Я хочу чтобы корректно формировался отчет Cucumber
  Чтобы я мог видеть результат работы сценариев

  Контекст:
    Когда Я открываю VanessaAutomation в режиме TestClient со стандартной библиотекой

  Сценарий: Проверка отчета Cucumber
    Когда В поле с именем "КаталогФичСлужебный" я указываю путь к служебной фиче "ФичаДляПроверкиОтчетаCucumber"
    И в открытой форме я перехожу к закладке с заголовком "Сервис"
    И я перехожу к закладке с именем "СтраницаОтчетыОЗапуске"
    И я разворачиваю группу с именем "ГруппаОтчеты"
    И я перехожу к закладке с именем "ГруппаCucumber"
    И я устанавливаю флаг с именем 'ДелатьОтчетВФорматеCucumberJson'
    И в поле каталог отчета Cucumber я указываю путь к относительному каталогу "tools\Cucumber"
    И Я нажимаю на кнопку перезагрузить сценарии в Vanessa-Automation TestClient
    И Я нажимаю на кнопку выполнить сценарии в Vanessa-Automation TestClient
    И в каталоге Cucumber появился 1 файл json