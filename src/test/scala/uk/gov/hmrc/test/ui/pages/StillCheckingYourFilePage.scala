package uk.gov.hmrc.test.ui.pages

import org.openqa.selenium.By
import uk.gov.hmrc.selenium.component.PageObject
object StillCheckingYourFilePage extends BasePage with PageObject {

  override val pageUrl: String = baseUrl + "/still-checking-your-file"

  val refreshForUpdatesButton: By = By.id("refresh")

  def onPageRefreshForUpdatesById(): Unit = {
    onPage()
    click(refreshForUpdatesButton)
  }

  def navigateToStillCheckingYourFilePage(): Unit = { // TODO: Remove this after CARF-621
    navigateTo(pageUrl)
    onPage()
  }
}
