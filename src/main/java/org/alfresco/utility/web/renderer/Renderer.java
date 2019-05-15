package org.alfresco.utility.web.renderer;

import org.alfresco.utility.web.browser.WebBrowser;
import org.alfresco.utility.TasProperties;
import org.alfresco.utility.web.annotation.RenderWebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Paul.Brodner
 *
 */
public interface Renderer
{
    public void render(RenderWebElement renderAnnotation, FindBy findByAnnotation, WebBrowser browser, TasProperties properties);
}
