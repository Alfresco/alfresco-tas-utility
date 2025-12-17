package org.alfresco.utility.web.browser;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventWebBrowserListener implements WebDriverListener {
    private static final Logger LOG = LoggerFactory.getLogger(EventWebBrowserListener.class);
    private String oldValue;

    @Override
    public void beforeClick(WebElement element) {
        LOG.debug("Trying to click '{}'", getElementName(element));
    }

    @Override
    public void afterClick(WebElement element) {
        LOG.info("Clicked on element '{}'", getElementName(element));
    }

    @Override
    public void afterFindElement(WebDriver driver, By by, WebElement element) {
        LOG.debug("'{}' - found", by);
    }

    @Override
    public void afterBack(WebDriver.Navigation navigation) {
        LOG.info("Navigated Back");
    }

    @Override
    public void afterForward(WebDriver.Navigation driver) {
        LOG.info("Navigated Forward");
    }

    @Override
    public void afterGet(WebDriver driver, String url) {
        LOG.info("Navigate to '{}'", url);
    }

    @Override
    public void afterExecuteScript(WebDriver driver, String script, Object[] args, Object result) {
        LOG.info("Ran script '{}'", script);
    }

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        Throwable cause = e.getCause(); // unwrap the actual exception
        String targetName = (target != null) ? target.getClass().getSimpleName() : "Unknown target";
        String methodName = (method != null) ? method.getName() : "Unknown method";

        LOG.error("Error occurred in [{}] while calling method [{}] with args {}",
                targetName, methodName, cause);
    }

    private String getElementName(WebElement element) {
        String foundBy = element.toString();
        if (foundBy != null) {
            int arrowIndex = foundBy.indexOf("->");
            if (arrowIndex >= 0) {
                return "By." + foundBy.substring(arrowIndex + 3, foundBy.length() - 1);
            }
        }
        return "unknown";
    }

    @Override
    public void beforeRefresh(WebDriver.Navigation navigation)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void afterRefresh(WebDriver.Navigation navigation)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void afterAccept(Alert alert)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void afterDismiss(Alert alert)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void beforeAccept(Alert alert)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void beforeDismiss(Alert alert)
    {
        // TODO Auto-generated method stub
    }
}
