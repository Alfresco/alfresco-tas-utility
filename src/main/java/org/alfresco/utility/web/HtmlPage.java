package org.alfresco.utility.web;

import org.alfresco.utility.TasProperties;
import org.alfresco.utility.web.annotation.RenderWebElement;
import org.alfresco.utility.web.browser.WebDriverAware;
import org.alfresco.utility.web.renderer.Renderer;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Basic implementation of HtmlPage
 *
 * @author Paul.Brodner
 */
public abstract class HtmlPage extends WebDriverAware
{
    protected final Logger LOG = LoggerFactory.getLogger(HtmlPage.class);

    @Autowired
    protected TasProperties properties;

    public HtmlPage renderedPage()
    {
        /*
         * get the RenderWebElement annotation of all declared fields and
         * render them based on the rules defined
         */
        List<Field> allFields = getAllDeclaredFields(new LinkedList<Field>(), this.getClass());
        for (Field field : allFields)
        {
            for (Annotation annotation : field.getAnnotationsByType(RenderWebElement.class))
            {
                RenderWebElement renderAnnotation = (RenderWebElement) annotation;
                Renderer renderer = renderAnnotation.state().toInstance();

                Annotation[] allAnnotation = field.getAnnotations();


                for(Annotation tmpAnnotation : allAnnotation)
                {
                    if (tmpAnnotation instanceof FindBy)
                    {
                        FindBy findBy =  (FindBy) tmpAnnotation;
                        renderer.render(renderAnnotation, findBy, browser, properties);
                    }
                    else  if (tmpAnnotation instanceof FindAll)
                    {
                        FindBy[] allFindBy =  ((FindAll) tmpAnnotation).value();
                        for (FindBy by : allFindBy)
                        {
                            renderer.render(renderAnnotation, by, browser, properties);
                        }

                    }

                }
            }
        }
        return this;
    }

    /**
     * Get the title of the current page
     *
     * @return
     */
    public String getPageTitle()
    {
        return browser.getDriver().getTitle();
    }

    /**
     * Method for wait while balloon message about some changes hide.
     */
    public void waitUntilMessageDisappears()
    {
        browser.waitUntilElementDeletedFromDom(By.cssSelector("div[id='message_c'] span[class='message']"));
    }

    public void refresh(){ browser.refresh();}
}
