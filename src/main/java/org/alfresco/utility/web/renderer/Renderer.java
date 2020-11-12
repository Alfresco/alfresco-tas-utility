package org.alfresco.utility.web.renderer;

import org.alfresco.utility.web.browser.WebBrowser;
import org.alfresco.utility.TasProperties;
import org.alfresco.utility.web.annotation.RenderWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

/**
 * @author Paul.Brodner
 *
 */
public interface Renderer
{
    void render(RenderWebElement renderAnnotation, FindBy findByAnnotation, WebBrowser browser, TasProperties properties);
    void render(RenderWebElement renderAnnotation, By by, WebBrowser browser, TasProperties properties);
}
