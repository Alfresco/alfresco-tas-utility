package org.alfresco.utility.model;

import java.io.IOException;

import org.alfresco.utility.LogFactory;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class TestModel implements Model
{
    static Logger LOG = LogFactory.getLogger();

    @Override
    public String toInfo()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public String toString()
    {
        return String.format("\n%s", toInfo());
    }
    
    /**
     * Converting object to JSON string
     *
     * @param model  The java object model to convert
     * @throws JsonProcessingException Throws exceptions if the given object doesn't match to the POJO class model
     */
    public String toJson() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        //include only values that differ from default settings to be included
        mapper.setSerializationInclusion(Include.NON_DEFAULT);
        try
        {
            //return the json object
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        }
        catch (JsonGenerationException e)
        {
            return e.toString();
        }
        catch (JsonMappingException e)
        {
            return e.toString();
        }
        catch (IOException e)
        {
            return e.toString();
        }
    }
}
