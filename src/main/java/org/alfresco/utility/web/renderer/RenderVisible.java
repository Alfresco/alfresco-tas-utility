package org.alfresco.utility.web.renderer;

import org.alfresco.utility.web.browser.WebBrowser;
import org.alfresco.utility.web.annotation.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Render one element using selenium's expectedCondition.
 * Just annotate your {@link PageObject} with
 * {code}
 *  @RenderWebElement
 *  @FindBy(...)
 *  WebElement element;
 * {code}
 * 
 * @author Paul.Brodner
 */
public class RenderVisible extends RenderElement
{
    @Override
    public void doWork(By locator, WebBrowser browser, long timeOutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(browser, timeOutInSeconds);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
