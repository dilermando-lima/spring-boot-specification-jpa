package demo.filter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class ValueParamConverterDefault implements ValueParamConverter{
    @Override
    public <T> T converter(Class<T> typeToConvert, String[] value)  {
        if(value == null || value.length == 0) return null;
        if(typeToConvert == String.class )return typeToConvert.cast(value[0]);
        if(typeToConvert == Double.class ) return typeToConvert.cast(Double.parseDouble(value[0]));
        if(typeToConvert == Float.class ) return typeToConvert.cast(Float.parseFloat(value[0]));
        if(typeToConvert == Integer.class ) return typeToConvert.cast(Integer.parseInt(value[0]));
        if(typeToConvert == LocalDate.class ) return typeToConvert.cast(LocalDate.parse(value[0]));
        if(typeToConvert == LocalDateTime.class ) return typeToConvert.cast(LocalDateTime.parse(value[0]));
        if(typeToConvert == ZonedDateTime.class ) return typeToConvert.cast(ZonedDateTime.parse(value[0]));
        return null;
    }
}
