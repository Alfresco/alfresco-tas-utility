package org.alfresco.utility.model;

import java.util.Date;
import java.util.List;

public class CalendarEventModel extends TestModel
{
    private String title;
    private String where;
    private String description;
    private Date startDate;
    private Date endDate;
    private String timeStart;
    private String timeEnd;
    private boolean allDay;
    private List<String> tags;

    public CalendarEventModel(String title, String where, String description, Date startDate, Date endDate, String timeStart, String timeEnd, boolean allDay, List<String> tags)
    {
        this.title = title;
        this.where = where;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.allDay = allDay;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
