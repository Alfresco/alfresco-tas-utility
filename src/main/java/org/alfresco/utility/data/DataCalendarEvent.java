package org.alfresco.utility.data;

import org.alfresco.dataprep.SitePagesService;
import org.alfresco.utility.model.CalendarEventModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.alfresco.utility.report.log.Step.STEP;

@Service
@Scope(value = "prototype")
public class DataCalendarEvent extends TestData<DataCalendarEvent>
{
    @Autowired
    SitePagesService sitePagesService;

    public CalendarEventModel createRandomCalendarEvent()
    {
        String title = RandomData.getRandomName("CalendarEvent");
        String where = "event location";
        String description = "event description";
        STEP(String.format("DATAPREP: Creating blog post %s with user %s in site %s", title, getCurrentUser().getUsername(), getCurrentSite()));
        sitePagesService.addCalendarEvent(getCurrentUser().getUsername(), getCurrentUser().getPassword(), getCurrentSite(), title, where, description, new Date(), new Date(), "","",  false, null);
        return new CalendarEventModel(title, where, description, new Date(), new Date(), "","",  false, null);
    }
}
