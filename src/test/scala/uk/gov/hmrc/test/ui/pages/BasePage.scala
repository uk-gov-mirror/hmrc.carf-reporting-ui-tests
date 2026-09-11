/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.test.ui.pages

import org.openqa.selenium.*
import org.openqa.selenium.support.ui.{ExpectedConditions, FluentWait, Select, Wait}
import org.scalatest.matchers.should.Matchers
import uk.gov.hmrc.selenium.component.PageObject
import uk.gov.hmrc.selenium.webdriver.Driver
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.driver.BrowserDriver
import uk.gov.hmrc.test.ui.utils.IdGenerators

import java.time.Duration
import scala.jdk.CollectionConverters.*

trait BasePage extends BrowserDriver with Matchers with IdGenerators with PageObject {

  val pageUrl: String
  val baseUrl: String = TestConfiguration.url("carf-reporting-frontend")

  def navigateTo(url: String): Unit = driver.navigate().to(url)

  val continueButtonId: By = By.id("continue")
  val submitButtonId: By   = By.id("submit")
  val yesRadioId: By       = By.id("value")
  val noRadioId: By        = By.id("value-no")
  val inputId: By          = By.id("value")
  val fileUploadId: By     = By.id("file-upload-input")

  private def fluentWait(timeoutSeconds: Long): Wait[WebDriver] = new FluentWait[WebDriver](Driver.instance)
    .withTimeout(Duration.ofSeconds(timeoutSeconds))
    .pollingEvery(Duration.ofMillis(200))
    .ignoring(classOf[org.openqa.selenium.StaleElementReferenceException])
    .ignoring(classOf[org.openqa.selenium.NoSuchElementException])

  def onPage(timeoutSeconds: Long = 3): Unit =
    fluentWait(timeoutSeconds).until(ExpectedConditions.urlToBe(pageUrl))

  def waitForElementDisappear(timeoutSeconds: Long = 20, locator: By): Unit =
    fluentWait(timeoutSeconds).until(ExpectedConditions.invisibilityOfElementLocated(locator))

  def clickOnLink(link: By): Unit = {
    onPage()
    click(link)
  }

  def selectDropdownById(id: By): Select = new Select(driver.findElement(id: By))

  def fillFields(fieldData: (By, String)*): Unit = {
    onPage()
    fieldData.foreach { case (locator, value) => sendKeys(locator, value) }
  }

  def fillFieldsAndContinue(fieldData: (By, String)*): Unit = {
    fillFields(fieldData: _*)
    click(continueButtonId)
  }

  def select(option: String): Unit = {
    onPage()

    val radioId = option.trim.toLowerCase match {
      case "yes" => yesRadioId
      case "no"  => noRadioId
      case other => throw new IllegalArgumentException(s"Invalid option: '$other'. Use 'Yes' or 'No'.")
    }

    assertLocatorPresent(radioId)
    click(radioId)
    click(continueButtonId)
  }

  def selectRadioAndContinue(id: By): Unit = {
    onPage()
    click(id)
    click(continueButtonId)
  }

  protected def assertLocatorPresent(locator: By): Unit = {
    val elements = Driver.instance.findElements(locator).asScala
    require(
      elements.nonEmpty,
      s"Expected element with locator [$locator] to be present, but none was found"
    )
  }

  def onPageSubmitById(): Unit = {
    onPage()
    click(submitButtonId)
  }

  def onPageContinueById(): Unit = {
    onPage()
    click(continueButtonId)
  }

  def uploadAnyFile(file: String): Unit =
    if (file.nonEmpty) {
      val filePath      = s"${System.getProperty("user.dir")}/src/test/resources/files/$file"
      fluentWait(8).until(ExpectedConditions.presenceOfElementLocated(fileUploadId))
      val uploadElement = driver.findElement(fileUploadId)
      uploadElement.sendKeys(filePath)
    }
}
