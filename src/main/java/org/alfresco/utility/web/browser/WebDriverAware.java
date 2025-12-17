package org.alfresco.utility.web.browser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.alfresco.utility.web.HtmlPage;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

public abstract class WebDriverAware
{
    protected WebBrowser browser;

    public WebBrowser getBrowser()
    {
        return browser;
    }

    public void setBrowser(WebBrowser webBrowser)
    {
        if (webBrowser.equals(this.browser))
        {
            //System.out.println("Already initialized browser for: " + this.getClass().getName());
            return;
        }

        this.browser = webBrowser;
        PageFactory.initElements(new HtmlElementDecorator(new HtmlElementLocatorFactory(webBrowser.getDriver())), this);

        List<Field> allFields = getAllDeclaredFields(new LinkedList<Field>(), this.getClass());
        for (Field field : allFields)
        {
            for (@SuppressWarnings("unused")
                    Annotation annotation : field.getAnnotationsByType(Autowired.class))
            {
                //System.out.println(this.getClass().getSimpleName() + " -> " + field.getName());
                try
                {
                    if (field.getClass().isInstance(HtmlPage.class))
                    {
                        field.setAccessible(true);
                        HtmlPage page = (HtmlPage) field.get(this);

                        page.setBrowser(this.browser);
                        field.set(this, page);
                    }
                }
                catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Backtrack algorithm to gather all declared fields within SuperClasse-es
     * but stopping on HtmlPage.class
     *
     * @param fields
     * @param type
     * @return
     */
    protected List<Field> getAllDeclaredFields(List<Field> fields, Class<?> type)
    {
        if (type.isAssignableFrom(HtmlPage.class))
        {
            return fields;
        }

        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null)
        {
            fields = getAllDeclaredFields(fields, type.getSuperclass());
        }
        return fields;
    }
}