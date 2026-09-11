package uk.gov.hmrc.test.ui.pages

import org.openqa.selenium.By

object SendYourFilePage extends BasePage {

  override val pageUrl: String = baseUrl + "/send-your-file"

  val loadingSpinner: By = By.cssSelector("#processing svg circle")

  def loadingSpinnerDisappear(): Unit =
    waitForElementDisappear(25, loadingSpinner)

}
