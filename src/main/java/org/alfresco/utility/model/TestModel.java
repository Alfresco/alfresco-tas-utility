package org.alfresco.utility.model;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.data.RandomData;
import org.apache.commons.lang3.RandomStringUtils;
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
     * @param model The java object model to convert
     * @throws JsonProcessingException Throws exceptions if the given object doesn't match to the POJO class model
     */
    public String toJson() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        // include only values that differ from default settings to be included
        mapper.setSerializationInclusion(Include.NON_DEFAULT);
        try
        {
            // return the json object
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
    

    /**
     * Sets random values for all fields of a TestModel without specified fields as ignoredFields
     * 
     * @param testModel all fields of testModel that need to be set
     * @param ignoredFields fields to be excluded
     * @throws Exception
     */
    protected static void setRandomValuesForAllFields(TestModel testModel, String... ignoredFields) throws Exception
    {
        // get all fields for testModel
        List<Field> allFields = getAllDeclaredFields(new LinkedList<Field>(), testModel.getClass());

        for (Field field : allFields)
        {
            if (!Arrays.asList(ignoredFields).contains(field.getName()))
            {
                field.setAccessible(true);

                if (field.getType().equals(List.class))
                {
                    List<String> newListValue = new ArrayList<String>();
                    newListValue.add(RandomData.getRandomAlphanumeric());
                    newListValue.add(RandomData.getRandomAlphanumeric());
                    field.set(testModel, newListValue);
                }
                else if (TestModel.class.isAssignableFrom(field.getType()))
                {
                    Object model = field.getType().newInstance();
                    setRandomValuesForAllFields((TestModel) model);
                    field.set(testModel, model);
                }
                else
                {
                    if (field.getType().equals(boolean.class))
                    {
                        field.set(testModel, true);
                    }
                    else if (field.getName().toLowerCase().endsWith("at"))
                    {
                        field.set(testModel, "2017-01-01T15:16:31.000+0000");
                    }
                    else if (field.getType().equals(String.class))
                    {
                        field.set(testModel, field.getName() + RandomStringUtils.randomAlphabetic(3));
                    }
                }
            }
        }

    }

    private static List<Field> getAllDeclaredFields(List<Field> fields, Class<?> classz)
    {
        if (classz.isAssignableFrom(TestModel.class))
        {
            return fields;
        }

        fields.addAll(Arrays.asList(classz.getDeclaredFields()));

        if (classz.getSuperclass() != null)
        {
            fields = getAllDeclaredFields(fields, classz.getSuperclass());
        }

        return fields;
    }
}
