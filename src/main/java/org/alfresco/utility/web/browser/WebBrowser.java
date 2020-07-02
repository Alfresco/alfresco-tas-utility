package org.alfresco.utility.web.browser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.alfresco.utility.TasProperties;
import org.alfresco.utility.exception.PageOperationException;
import org.alfresco.utility.web.common.Parameter;
import org.apache.commons.httpclient.HttpState;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wrapper around an arbitrary WebDriver instance which supports registering
 * of a {@link WebDriverEventListener} for logging purposes.
 * 
 * @author Paul.Brodner
 */
public class WebBrowser extends EventFiringWebDriver
{
    protected static final Logger LOG = LoggerFactory.getLogger(WebBrowser.class);
    protected TasProperties properties;

    String mainWindow;

    public WebBrowser(WebDriver driver, TasProperties properties)
    {
        super(driver);
        this.properties = properties;
        LOG.info("Initialising driver '{}'", driver.toString());
    }

    /**
     * Inject auth cookie in current session so you don't need to login with LoginPage
     */
    public WebBrowser authenticatedSession(HttpState httpState)
    {
        if (httpState == null)
        {
            throw new RuntimeException("Failed to login");
        }
        navigate().to(properties.getShareUrl());
        manage().addCookie(new Cookie(httpState.getCookies()[0].getName(), httpState.getCookies()[0].getValue()));
        navigate().refresh();
        return this;
    }

    /**
     * This is working in combination with {@link #authenticatedSession(HttpState)}
     */
    public void cleanUpAuthenticatedSession()
    {
        manage().deleteAllCookies();
    }

    /**
     * Recreating the action of hovering over a particular HTML element on a
     * page.
     * 
     * @param element
     *            {@link WebElement} target
     */
    public void mouseOver(WebElement element)
    {
        try
        {
            Parameter.checkIsMandotary("WebElement", element);
            new Actions(this).moveToElement(element).perform();
        }
        catch(MoveTargetOutOfBoundsException ex)
        {
            ((JavascriptExecutor) this).executeScript("arguments[0].scrollIntoView(true);", element);
            new Actions(this).moveToElement(element).perform();
        }
    }

    /**
     * Recreating the action of hovering over with offset coordinates a
     * particular HTML element on a page.
     *
     * @param element
     *            {@link WebElement} target
     */
    public void mouseOver(WebElement element, int xOffset, int yOffset)
    {
        Parameter.checkIsMandotary("WebElement", element);
        new Actions(this).moveToElement(element, xOffset, yOffset).perform();
    }

    /**
     * Acts as a refresh page action similar to F5 key.
     */
    public void refresh()
    {
        this.navigate().refresh();
    }

    /**
     * Wait until the element has attribute for the specified amount of time.
     *
     * @param element
     */
    public void waitUntilElementHasAttribute(WebElement element, String attribute, String value)
    {
        Parameter.checkIsMandotary("Element", element);
        WebDriverWait wait = new WebDriverWait(this, properties.getExplicitWait());
        wait.until(ExpectedConditions.attributeContains(element, attribute, value));
    }

    /**
     * Wait until the element is visible for the specified amount of time.
     * 
     * @param locator {@link By} locator
     */
    public WebElement waitUntilElementVisible(By locator)
    {
        return waitUntilElementVisible(locator, properties.getExplicitWait());
    }

    /**
     * Wait until the element is visible for the specified amount of time.
     * 
     * @param locator {@link By} locator
     * @param timeOutInSeconds timeout in seconds
     */
    public WebElement waitUntilElementVisible(By locator, long timeOutInSeconds)
    {
        Parameter.checkIsMandotary("Locator", locator);
        WebDriverWait wait = new WebDriverWait(this, timeOutInSeconds);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait until element is present on the DOM of a page.
     * 
     * @param locator {@link By} locator
     * @param timeOutInSeconds seconds to wait
     * @return {@link WebElement} 
     */
    public WebElement waitUntilElementIsPresent(By locator, long timeOutInSeconds)
    {
        Parameter.checkIsMandotary("Locator", locator);
        WebDriverWait wait = new WebDriverWait(this, timeOutInSeconds);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Wait until element is present on the DOM of a page.
     * 
     * @param locator {@link By} locator
     * @return {@link WebElement} 
     */
    public WebElement waitUntilElementIsPresent(By locator)
    {
        return waitUntilElementIsPresent(locator, properties.getExplicitWait());
    }
    
    /**
     * Wait for child WebElement as a part of parent element to present
     * 
     * @param parentLocator {@link By} parent locator
     * @param childLocator {@link By} child locator
     * @param timeOutInSeconds seconds to wait
     * @return {@link WebElement} 
     */
    public WebElement waitUntilChildElementIsPresent(By parentLocator, By childLocator, long timeOutInSeconds)
    {
        Parameter.checkIsMandotary("Parent locator", parentLocator);
        Parameter.checkIsMandotary("Child locator", childLocator);
        WebDriverWait wait = new WebDriverWait(this, timeOutInSeconds);
        return wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parentLocator, childLocator));
    }
    
    /**
     * Wait for child WebElement as a part of parent element to present
     * 
     * @param parentLocator {@link By} parent locator
     * @param childLocator {@link By} child locator
     * @return {@link WebElement} 
     */
    public WebElement waitUntilChildElementIsPresent(By parentLocator, By childLocator)
    {
        Parameter.checkIsMandotary("Parent locator", parentLocator);
        Parameter.checkIsMandotary("Child locator", childLocator);
        WebDriverWait wait = new WebDriverWait(this, properties.getExplicitWait());
        return wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parentLocator, childLocator));
    }
    
    /**
     * Wait for child WebElement as a part of parent element to present
     * 
     * @param parentLocator {@link WebElement} parent locator
     * @param childLocator {@link By} child locator
     * @param timeOutInSeconds seconds to wait
     * @return {@link WebElement} 
     */
    public WebElement waitUntilChildElementIsPresent(WebElement parentLocator, By childLocator, long timeOutInSeconds)
    {
        Parameter.checkIsMandotary("Parent locator", parentLocator);
        Parameter.checkIsMandotary("Child locator", childLocator);
        WebDriverWait wait = new WebDriverWait(this, timeOutInSeconds);
        return wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parentLocator, childLocator));
    }
    
    /**
     * Wait for child WebElement as a part of parent element to present
     * 
     * @param parentLocator {@link WebElement} parent locator
     * @param childLocator {@link By} child locator
     * @return {@link WebElement} 
     */
    public WebElement waitUntilChildElementIsPresent(WebElement parentLocator, By childLocator)
    {
        Parameter.checkIsMandotary("Parent locator", parentLocator);
        Parameter.checkIsMandotary("Child locator", childLocator);
        WebDriverWait wait = new WebDriverWait(this, properties.getExplicitWait());
        return wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parentLocator, childLocator));
    }

    /**
     * Wait until the element is visible for the specified amount of time.
     *
     * @param element {@link WebElement} web element
     */
    public WebElement waitUntilElementVisible(WebElement element)
    {
        return waitUntilElementVisible(element, properties.getExplicitWait());
    }

    /**
     * Wait until the element is visible for the specified amount of time.
     *
     * @param element {@link WebElement} web element
     * @param timeOutInSeconds timeout in seconds
     */
    public WebElement waitUntilElementVisible(WebElement element, long timeOutInSeconds)
    {
        Parameter.checkIsMandotary("Element", element);
        WebDriverWait wait = new WebDriverWait(this, timeOutInSeconds);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Helper method to find and return a slow loading collection of {@link WebElement}.
     * 
     * @param locator {@link By} search criteria
     * @return Collection of {@link WebElement} HTML elements
     */
    public List<WebElement> waitUntilElementsVisible(By locator)
    {
        Parameter.checkIsMandotary("Locator", locator);
        WebDriverWait wait = new WebDriverWait(this, properties.getExplicitWait());
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /**
     * Helper method to find and return a slow loading collection of {@link WebElement}.
     * 
     * @param elements {@link WebElement} search criteria
     * @return Collection of {@link WebElement} HTML elements
     */
    public List<WebElement> waitUntilElementsVisible(List<WebElement> elements)
    {
        WebDriverWait wait = new WebDriverWait(this, properties.getExplicitWait());
        return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    /**
     * Wait the element is displayed by refreshing the page
     * 
     * @param locator
     *            {@link By} query
     */
    public void waitUntilElementIsDisplayedWithRetry(By locator)
    {
        waitUntilElementIsDisplayedWithRetry(locator, 0);
    }

    /**
     * Wait the element is displayed by refreshing the page
     * 
     * @param locator {@link By} query
     * @param secondsToWait
     */
    public void waitUntilElementIsDisplayedWithRetry(By locator, int secondsToWait)
    {
        Parameter.checkIsMandotary("Locator", locator);
        int counter = 1;
        int retryRefreshCount = 3;

        while (counter <= retryRefreshCount)
        {
            if (isElementDisplayed(locator))
            {
                break;
            }
            else
            {
                LOG.info(String.format("Wait for element %s seconds after refresh: %s", secondsToWait, counter));
                refresh();
                waitInSeconds(secondsToWait);
                counter++;
            }
        }
    }

    /**
     * Wait the element to disappear by refreshing the page
     * 
     * @param locator {@link By} query
     * @param secondsToWait
     */
    public void waitUntilElementDisappearsWithRetry(By locator, int secondsToWait)
    {
        Parameter.checkIsMandotary("Locator", locator);
        int counter = 1;
        int retryRefreshCount = 3;
        while (counter <= retryRefreshCount && isElementDisplayed(locator))
        {
            LOG.info(String.format("Wait for element %s seconds after refresh: %s", secondsToWait, counter));
            refresh();
            waitInSeconds(secondsToWait);
            counter++;
        }
    }

    /**
     * Wait until one element is displayed by refreshing the page
     * 
     * @param webElement
     */
    public void waitUntilWebElementIsDisplayedWithRetry(WebElement webElement)
    {
        waitUntilWebElementIsDisplayedWithRetry(webElement, 0);
    }

    /**
     * Wait until one element is displayed by refreshing the page
     * 
     * @param webElement
     * @param secondsToWait
     */
    public void waitUntilWebElementIsDisplayedWithRetry(WebElement webElement, int secondsToWait)
    {
        Parameter.checkIsMandotary("WebElement", webElement);
        int counter = 1;
        int retryRefreshCount = 3;
        while (counter <= retryRefreshCount)
        {
            if (isElementDisplayed(webElement))
            {
                break;
            }
            else
            {
                LOG.info(String.format("Wait for web element %s seconds after refresh: %s", secondsToWait, counter));
                refresh();
                waitInSeconds(secondsToWait);
                counter++;
            }
        }
    }

    /**
     * Wait until the Clickable of given Element for given seconds.
     *
     * @param element WebElement
     */
    public WebElement waitUntilElementClickable(WebElement element)
    {
        WebDriverWait wait = new WebDriverWait(this, properties.getExplicitWait());
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait until the Clickable of given Element for given seconds.
     * 
     * @param locator CSS Locator
     * @param timeOutInSeconds Timeout In Seconds
     */
    public WebElement waitUntilElementClickable(By locator, long timeOutInSeconds)
    {
        Parameter.checkIsMandotary("Locator", locator);
        WebDriverWait wait = new WebDriverWait(this, timeOutInSeconds);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait until the Clickable of given Element for given seconds.
     *
     * @param element WebElement
     * @param timeOutInSeconds Timeout In Seconds
     */
    public WebElement waitUntilElementClickable(WebElement element, long timeOutInSeconds)
    {
        Parameter.checkIsMandotary("Element", element);
        WebDriverWait wait = new WebDriverWait(this, timeOutInSeconds);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait until the given text is present in the given Element
     *
     * @param element WebElement
     */
    public void waitUntilElementContainsText(WebElement element, String text)
    {
        Parameter.checkIsMandotary("Element", element);
        WebDriverWait wait = new WebDriverWait(this, properties.getExplicitWait());
        wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    /**
     * Wait until url contains the given URL fraction.
     *
     * @param URLfraction
     * @param timeOutInSeconds
     */
    public void waitUrlContains(String URLfraction, long timeOutInSeconds)
    {
        Parameter.checkIsMandotary("Element", URLfraction);
        WebDriverWait wait = new WebDriverWait(this, timeOutInSeconds);
        wait.until(ExpectedConditions.urlContains(URLfraction));
    }

    /**
     * Wait Until element successfully deleting from DOM.
     * 
     * @param locator {@link By} locator
     */
    public void waitUntilElementDeletedFromDom(By locator)
    {
        waitUntilElementDeletedFromDom(locator, properties.getExplicitWait());
    }

    /**
     * Wait Until element successfully deleting from DOM.
     * 
     * @param locator {@link By} locator
     * @param timeOutInSeconds time to wait
     */
    public void waitUntilElementDeletedFromDom(By locator, long timeOutInSeconds)
    {
        Parameter.checkIsMandotary("Locator", locator);
        WebDriverWait wait = new WebDriverWait(this, timeOutInSeconds);
        try
        {
            wait.until(ExpectedConditions.stalenessOf(this.findElement(locator)));
        }
        catch (NoSuchElementException e)
        {
            /* if element already not in DOM! */}
    }

    /**
     * Wait until the invisibility of given Element for given seconds.
     * 
     * @param locator CSS Locator
     */
    public void waitUntilElementDisappears(By locator)
    {
        waitUntilElementDisappears(locator, properties.getExplicitWait());
    }

    /**
     * Wait until the invisibility of given Element for given seconds.
     * 
     * @param locator {@link By} Locator
     * @param timeOutInSeconds timeout In Seconds
     */
    public void waitUntilElementDisappears(By locator, long timeOutInSeconds)
    {
        Parameter.checkIsMandotary("Locator", locator);
        WebDriverWait wait = new WebDriverWait(this, timeOutInSeconds);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait until the invisibility of given Element for given seconds.
     *
     * @param locator {@link By} Locator
     * @param timeOutInSeconds timeout In Seconds
     */
    public void waitUntilElementDisappears(WebElement locator, long timeOutInSeconds)
    {
        Parameter.checkIsMandotary("Locator", locator);
        WebDriverWait wait = new WebDriverWait(this, timeOutInSeconds);
        wait.until(ExpectedConditions.invisibilityOf(locator));
    }

    /**
     * Wait until the invisibility of given Element for given seconds.
     *
     * @param locator CSS Locator
     */
    public void waitUntilElementDisappears(WebElement locator)
    {
        waitUntilElementDisappears(locator, properties.getExplicitWait());
    }

    /**
     * Returns true if the element is displayed else false.
     * 
     * @param locator
     *            {@link By} query
     * @return boolean true if displayed
     */
    public boolean isElementDisplayed(By locator)
    {
        Parameter.checkIsMandotary("Locator", locator);
        try
        {
            return this.findElement(locator).isDisplayed();
        }
        catch (NoSuchElementException nse)
        {
            // no log needed due to negative cases.
        }
        catch (TimeoutException toe)
        {
            // no log needed due to negative cases.
        }
        catch (StaleElementReferenceException ste)
        {

            // no log needed due to negative cases.
        }
        return false;
    }

    /**
     * Returns true if the element is displayed else false.
     * 
     * @param element
     *            {@link By} query
     * @return boolean true if displayed
     */
    public boolean isElementDisplayed(WebElement element)
    {
        try
        {
            Parameter.checkIsMandotary("WebElement", element);
            return element.isDisplayed();
        }
        catch (NoSuchElementException nse)
        {
            // no log needed due to negative cases.
        }
        catch (TimeoutException toe)
        {
            // no log needed due to negative cases.
        }
        catch (StaleElementReferenceException ste)
        {
            // no log needed due to negative cases.
        }
        catch (IllegalArgumentException il)
        {
            // element does not exist
        }
        return false;
    }

    /**
     * Returns true if the element is displayed else false.
     * 
     * @param locator
     *            {@link WebElement} query
     *            {@link By} query
     * @return boolean true if displayed
     */
    public boolean isElementDisplayed(WebElement element, By locator)
    {
        try
        {
            Parameter.checkIsMandotary("WebElement", element);
            return element.findElement(locator).isDisplayed();
        }
        catch (NoSuchElementException nse)
        {
            // no log needed due to negative cases.
        }
        catch (TimeoutException toe)
        {
            // no log needed due to negative cases.
        }
        catch (StaleElementReferenceException ste)
        {
            // no log needed due to negative cases.
        }
        catch (IllegalArgumentException il)
        {
            // element does not exist
        }
        return false;
    }

    /**
     * Gets the URL of the previously visited page. The previous url is only
     * displayed once interacted with the page. If user uses drone.navigetTo it
     * will treat it as an entry page and will not have a previous page url.
     * 
     * @return String previous page URL
     */
    public String getPreviousUrl()
    {
        String url = (String) executeScript("return document.referrer;");
        if (url == null || url.isEmpty())
        {
            throw new UnsupportedOperationException("There is no previous url value");
        }
        return url;
    }

    /**
     * Method to switch on to frames of content object.
     * 
     * @param frameId
     *            String identifier
     */
    public void switchToFrame(String frameId)
    {
        Parameter.checkIsMandotary("FrameId", frameId);
        this.switchTo().frame(frameId);
    }

    /**
     * Method to switch back from i-frame to default content.
     */
    public void switchToDefaultContent()
    {
        this.switchTo().defaultContent();
    }

    /**
     * Switches to the newly created window.
     */
    public void switchWindow()
    {
        mainWindow = this.getWindowHandle();
        Set<String> windows = this.getWindowHandles();
        windows.remove(mainWindow);
        this.switchToWindow(windows.iterator().next());
    }

    /**
     * Waits and switches to window based on index
     */
    public void switchWindow(int windowIndex)
    {
        mainWindow = this.getWindowHandle();
        Set<String> windows = this.getWindowHandles();
        int windowsNumber = windows.size();
        int counter = 1;
        int retryRefreshCount = 5;

        while (counter <= retryRefreshCount)
        {
            if (windowsNumber == windowIndex + 1)
            {
                windows.remove(mainWindow);
                this.switchToWindow(windows.iterator().next());
                break;
            }
            LOG.info("Wait for window: " + counter);
            waitInSeconds(2);
            windowsNumber = this.getWindowHandles().size();
            counter++;
        }
    }

    /**
     * Switches to window with specified url.
     */
    public void switchWindow(String winHandler)
    {
        mainWindow = this.getWindowHandle();
        for (String winHandle : this.getWindowHandles())
        {
            this.switchTo().window(winHandle);
            if (this.getCurrentUrl().contains(winHandler))
            {
                break;
            }
            else
            {
                this.switchTo().window(mainWindow);
            }
        }
    }

    /**
     * Closes the newly created win and swithes back to main
     */
    public void closeWindowAndSwitchBack()
    {
        this.close();
        this.switchToWindow(mainWindow);
    }

    /**
     * Closes the window and returns to selected window
     */

    public void closeWindowAndSwitchBackParametrized(String windowToSwitchTo, String windowToClose)
    {
        String currentWindow = this.getWindowHandle();

        if (currentWindow.equals(windowToClose))
        {
            this.close();
            this.switchToWindow(windowToSwitchTo);
        }
        else
        {
            LOG.info("You are not on the expected page, you are on: " + this.getCurrentUrl());
        }
    }

    /**
     * Closes the window opened leaving the browser session opened
     */

    public void closeWindowAcceptingModalDialog()
    {
        this.close();
        Alert alert = this.switchTo().alert();
        String alertText = alert.getText().trim();
        LOG.info("Alert data: " + alertText);
        alert.accept();
        this.switchToWindow(mainWindow);
    }

    /**
     * This method transfers the control to the specific window as per the given
     * window handle.
     * 
     * @param windowHandle
     *            identifier
     */
    public void switchToWindow(String windowHandle)
    {
        Parameter.checkIsMandotary("windowHandle", windowHandle);
        this.switchTo().window(windowHandle);
    }

    /**
     * Get a cookie that matches the name.
     * 
     * @param name
     *            String cookie identifier
     * @return Cookie object
     */
    public Cookie getCookie(final String name)
    {
        if (name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("Cookie identifier is required.");
        }
        Set<Cookie> cookies = this.manage().getCookies();
        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if (name.equalsIgnoreCase(cookie.getName()))
                {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * Function to delete the cookies in the browser
     */
    public void deleteCookies()
    {

        this.manage().deleteAllCookies();
    }

    /**
     * Delete cookie based on given cookie.
     * 
     * @param cookie
     *            {@link Cookie} object to be deleted.
     */
    public void deleteCookie(Cookie cookie)
    {
        Parameter.checkIsMandotary("Cookie", cookie);
        this.manage().deleteCookie(cookie);
    }

    /**
     * Maximizes the current window if it is not already maximized
     */
    public void maximize()
    {
        this.manage().window().maximize();
    }

    /**
     * Drag the source element and drop into target element.
     * 
     * @param source Source {@link WebElement}
     * @param target Target {@link WebElement}
     */
    public void dragAndDrop(WebElement source, WebElement target)
    {
        Parameter.checkIsMandotary("source element", source);
        Parameter.checkIsMandotary("target element", target);
        Actions builder = new Actions(this);
        builder.dragAndDrop(source, target).perform();
    }

    /**
     * Drag web element by x,y coordinates
     * 
     * @param source html {@link WebElement}
     * @param x coordinate
     * @param y coordinate
     */
    public void dragAndDrop(WebElement source, int x, int y)
    {
        Parameter.checkIsMandotary("source element", source);
        Actions builder = new Actions(this);
        Action dragAndDrop = builder.dragAndDropBy(source, x, y).build();
        dragAndDrop.perform();
    }

    /**
     * Double click on an element
     * 
     * @param element {@link WebElement}
     */
    public void doubleClickOnElement(WebElement element)
    {
        Parameter.checkIsMandotary("doubleclick element", element);
        Actions builder = new Actions(this);
        Action doubleClick = builder.doubleClick(element).build();
        doubleClick.perform();
    }

    /**
     * Right click on an element
     * 
     * @param element {@link WebElement}
     */
    public void rightClickOnElement(WebElement element)
    {
        Parameter.checkIsMandotary("right element", element);
        Actions builder = new Actions(this);
        Action rightClick = builder.contextClick(element).build();
        rightClick.perform();
    }

    /**
     * This function will return 1st visible element found with the specified selector
     * 
     * @param locator {@link By} selector type
     * @return {@link WebElement} element to interact
     */
    public WebElement findFirstDisplayedElement(By locator)
    {
        List<WebElement> elementList = findDisplayedElementsFromLocator(locator);
        if (elementList.size() != 0)
            return elementList.get(0);
        else
            return null;
    }

    /**
     * This function will return 1st element found with a specified value
     * 
     * @param locator {@link By} identifier
     * @return {@link List} of {@link WebElement}
     */
    public WebElement findFirstElementWithValue(By locator, String value)
    {
        List<WebElement> elementList = waitUntilElementsVisible(locator);
        for (WebElement element : elementList)
        {
            if (element.getText().contains(value))
                return element;
        }
        return null;
    }

    /**
     * This function will return 1st element found which contains a specified value
     * 
     * @param list {@link List<WebElement>} identifier
     * @return {@link List} of {@link WebElement}
     */
    public WebElement findFirstElementWithValue(List<WebElement> list, String value)
    {
        for (WebElement element : list)
        {
            if (element.getText().contains(value))
                return element;
        }
        return null;
    }

    /**
     * This function will return 1st element found with the exact specified value
     * 
     * @param list
     * @param value
     * @return
     */
    public WebElement findFirstElementWithExactValue(List<WebElement> list, String value)
    {
        for (WebElement element : list)
        {
            if (element.getText().equals(value))
                return element;
        }
        return null;
    }

    /**
     * This function will return list of visible elements found with the specified selector
     * 
     * @param selector {@link By} identifier
     * @return {@link List} of {@link WebElement}
     */
    public List<WebElement> findDisplayedElementsFromLocator(By selector)
    {
        Parameter.checkIsMandotary("Locator", selector);
        List<WebElement> elementList = this.findElements(selector);
        List<WebElement> displayedElementList = new ArrayList<WebElement>();
        for (WebElement elementSelected : elementList)
        {
            if (elementSelected.isDisplayed())
            {
                displayedElementList.add(elementSelected);
            }
        }
        return displayedElementList;
    }

    /**
     * This function selects specified filter option from filter options list
     * 
     * @param option Option to be selected
     * @param filterOptionsList The filter Option List
     */
    public void selectOptionFromFilterOptionsList(String option, List<WebElement> filterOptionsList)
    {
        Parameter.checkIsMandotary("Filter options", filterOptionsList);
        for (WebElement webElement : filterOptionsList)
        {
            if (webElement.getText().contains(option))
            {
                webElement.click();
                break;
            }
        }
    }

    /*
     * Method returns if the specified option is selected for Alfresco "dropdown" (the button element with an arrow)
     * @param myActivitiesOption String
     * @return boolean
     */
    public boolean isOptionSelectedForFilter(String option, WebElement filterElement)
    {
        try
        {
            String actualOption = filterElement.getText();
            actualOption = actualOption.substring(0, actualOption.length() - 2);
            if (actualOption.equals(option))
                return true;
            return false;
        }
        catch (TimeoutException te)
        {
            throw new PageOperationException("Unable to retrieve the '" + filterElement + "' button", te);
        }
    }

    /**
     * Scroll to WebElement
     * 
     * @param element WebElement
     */
    public void scrollToElement(WebElement element)
    {
        executeJavaScript(String.format("window.scrollTo(0, '%s')", element.getLocation().getY()));
    }

    /**
     * Scroll to WebElement
     *
     * @param element WebElement
     */
    public void scrollIntoView(WebElement element)
    {
        executeJavaScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Method to wait for given seconds.
     * 
     * @param seconds time in seconds
     */
    public void waitInSeconds(int seconds)
    {
        long time0;
        long time1;
        time0 = System.currentTimeMillis();
        do
        {
            time1 = System.currentTimeMillis();
        }
        while (time1 - time0 < seconds * 1000);
    }

    /**
     * Execute Javascript command
     * 
     * @param command
     */
    public void executeJavaScript(String command)
    {
        if (this instanceof JavascriptExecutor)
        {
            ((JavascriptExecutor) this).executeScript(command);
        }
    }

    /**
     * Execute Javascript command with {@link WebElement}
     * 
     * @param command
     * @param element {@link WebElement}
     */
    public void executeJavaScript(String command, WebElement element)
    {
        if (this instanceof JavascriptExecutor)
        {
            ((JavascriptExecutor) this).executeScript(command, element);
        }
    }

    /**
     * Click {@link WebElement} with JavaScript command
     * 
     * @param elementToClick
     */
    public void clickJS(WebElement elementToClick)
    {
        executeJavaScript("arguments[0].click();", elementToClick);
    }

    public boolean isAlertPresent() {
        try {
            this.switchTo().alert();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void handleModalDialogAcceptingAlert() {
        if (isAlertPresent()) {
            Alert alert = this.switchTo().alert();
            String alertText = alert.getText().trim();
            LOG.info("Alert data: " + alertText);
            alert.accept();
        }
    }

    public void handleModalDialogDismissingAlert() {
        if (isAlertPresent()) {
            Alert alert = this.switchTo().alert();
            String alertText = alert.getText().trim();
            LOG.info("Alert data: " + alertText);
            alert.dismiss();
        }
    }

    public void focusOnWebElement(WebElement webElement)
    {
        webElement.sendKeys(Keys.TAB);
    }

    /**
     * Wait until element is visible with retry
     * @param locator
     * @param retryCount
     */
    public void waitUntilElementIsVisibleWithRetry(By locator, int retryCount) {
        Parameter.checkIsMandotary("Locator", locator);
        int counter = 0;

        while(!isElementDisplayed(locator)&& counter <= retryCount)
        {
            waitInSeconds(2);
            counter++;
        }
    }

    /*
     * private boolean isDisplayedBasedOnLocator(By locator)
     * {
     * try
     * {
     * this.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
     * return this.findElement(locator).isDisplayed();
     * }
     * catch (NoSuchElementException nse)
     * {
     * // no log needed due to negative cases.
     * }
     * catch (TimeoutException toe)
     * {
     * // no log needed due to negative cases.
     * }
     * catch (StaleElementReferenceException ste)
     * {
     * // no log needed due to negative cases.
     * }
     * finally
     * {
     * this.manage().timeouts().implicitlyWait(Long.valueOf(properties.getImplicitWait()), TimeUnit.SECONDS);
     * }
     * return false;
     * }
     */

    /**
     * Wait until the given text is not present in the given Element
     *
     * @param element WebElement
     */
    public void waitUntilElementDoesNotContainText(WebElement element, String text)
    {
        Parameter.checkIsMandotary("Element", element);
        WebDriverWait wait = new WebDriverWait(this, properties.getExplicitWait());
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(element, text)));
    }
}