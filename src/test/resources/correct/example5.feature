# from https://cucumber.io/docs/gherkin/reference/
Feature: params
  Scenario: eat 5 out of 12
    Given there are 12 cucumbers
    When I eat 5 cucumbers
    Then I should have 7 cucumbers

    Given a blog post named "Random" with Markdown body
  """
  Some Title, Eh?
  ===============
  Here is the first paragraph of my blog post. Lorem ipsum dolor sit amet,
  consectetur adipiscing elit.
  """

  Scenario: eat 5 out of 20
    Given there are 20 cucumbers
    When I eat 5 cucumbers
    Then I should have 15 cucumbers

  Scenario Outline: eating
    Given there are <start> cucumbers
    When I eat <eat> cucumbers
    Then I should have <left> cucumbers

    Given the following users exist:
      | name   | email              | twitter         |
      | Aslak  | aslak@cucumber.io  | @aslak_hellesoy |
      | Julien | julien@cucumber.io | @jbpros         |
      | Matt   | matt@cucumber.io   | @mattwynne      |

    Examples:
      | start | eat | left |
      |    12 |   5 |    7 |
      |    20 |   5 |   15 |