package demo.filter;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("java:S3011")
public class ValueFilterParamSupport {

    private static final ValueParamConverterDefault PARAM_CONVERTER_DEFAULT = new ValueParamConverterDefault();

    public static class ValueFilterParamSupportException extends RuntimeException{
        public ValueFilterParamSupportException(String msg){super(msg);}
        public ValueFilterParamSupportException(Throwable throwable){super(throwable);}
    }

    public record ValueFilterParam(FilterParam filterParam, Object value){}

    public static <T> T fillValueIntoObjectFromRequestMap(Map<String, String[]> params, Class<T> typeFilterObject) throws ReflectiveOperationException, IllegalArgumentException, SecurityException{
        if( typeFilterObject.isRecord() ){
            return createNewInstanceFromRecord(typeFilterObject, collectFilterParamFromRequestMap(typeFilterObject, params));
        }else{
            return createNewInstance(typeFilterObject, params);
        }
    }

    private static <T> T createNewInstanceFromRecord(Class<T> typeFilterObject, Supplier<Stream<ValueFilterParam>> valueFilterParamStream) throws ReflectiveOperationException, IllegalArgumentException, SecurityException{
        var types = new Class[typeFilterObject.getRecordComponents().length];
        var values = new Object[typeFilterObject.getRecordComponents().length];
        
        for (int i = 0; i < typeFilterObject.getRecordComponents().length; i++) {
            types[i] = typeFilterObject.getRecordComponents()[i].getType();
            if(typeFilterObject.getRecordComponents()[i].isAnnotationPresent(FilterParam.class) ){
                final FilterParam filterParam = typeFilterObject.getRecordComponents()[i].getAnnotation(FilterParam.class);

                ValueFilterParam  valueFilterParam = valueFilterParamStream
                    .get()
                    .filter(v -> filterParam.param().equals(v.filterParam().param()) && filterParam.type() == v.filterParam().type())
                    .findFirst()
                    .orElse(new ValueFilterParam(filterParam, null));

                values[i] = valueFilterParam.value();
            }else{
                values[i] = null;
            }
        }
        return typeFilterObject.getDeclaredConstructor(types).newInstance(values);
    }

    private static <T> T createNewInstance(Class<T> typeFilterObject, Map<String, String[]> params) throws ReflectiveOperationException, IllegalArgumentException, SecurityException{

        if(typeFilterObject.getDeclaredConstructors().length > 1){
            throw new ValueFilterParamSupportException("Class %s must contains one public empty constructor".formatted(typeFilterObject));
        }

        T objectInstance = typeFilterObject.getDeclaredConstructor().newInstance();

        for ( Field f : objectInstance.getClass().getDeclaredFields() ){
            if( f.isAnnotationPresent(FilterParam.class) ){
                FilterParam filterParam = f.getAnnotation(FilterParam.class);

                if( CHECK_MAP_CONTAINS_FULL_NAME.test(params, filterParam)  ){
                    f.setAccessible(true);
                    f.set(
                        objectInstance,
                        GET_VALUE_PARAMETER_CONVERTER.apply(filterParam).converter(f.getType(), params.get(GET_FULL_NAME.apply(filterParam)))
                    );
                }
            }
        }
        return objectInstance;
    }

    public static  Supplier<Stream<ValueFilterParam>> collectFilterParamToBuildQuery(Object filterRequest){

        final Predicate<Field> onlyFilterParamWithNoIgnoresQuery = f ->  {
                try {
                    f.setAccessible(true);
                    return f.get(filterRequest) != null && f.isAnnotationPresent(FilterParam.class) && !f.getAnnotation(FilterParam.class).ignoresOnQuery();
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new ValueFilterParamSupportException(e);
                }
        };

        final Function<Field,ValueFilterParam> convertFieldToValueFilterParam = f -> {
                try {
                    f.setAccessible(true);
                    return new ValueFilterParam(
                        f.getAnnotation(FilterParam.class), 
                        f.get(filterRequest)
                    );
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new ValueFilterParamSupportException(e);
                }
         };

        return () -> Stream
                        .of(filterRequest.getClass().getDeclaredFields())
                        .filter(onlyFilterParamWithNoIgnoresQuery)
                        .map(convertFieldToValueFilterParam);
    }

    private static final Function<FilterParam,String> GET_FULL_NAME = 
        filterParam -> filterParam.requiresSufix() ? filterParam.param() + "." +  filterParam.type().getName() : filterParam.param();

    private static final BiPredicate<Map<String, String[]>, FilterParam> CHECK_MAP_CONTAINS_FULL_NAME = 
        (map, filterParam) ->  map.containsKey(GET_FULL_NAME.apply(filterParam));
        
    private static final BiPredicate<Map<String, String[]>, FilterParam> CHECK_PARAM_REQUIRES_SUFIX = 
        (map, filterParam) -> filterParam.requiresSufix() &&  map.containsKey(filterParam.param()) && CHECK_MAP_CONTAINS_FULL_NAME.test(map, filterParam);
    
    private static final Function<FilterParam, ValueParamConverter> GET_VALUE_PARAMETER_CONVERTER = filterParam -> {
                try {
                        return  filterParam.converter() == PARAM_CONVERTER_DEFAULT.getClass() ? 
                                PARAM_CONVERTER_DEFAULT : 
                                filterParam.converter().getDeclaredConstructor().newInstance();
                } catch (ReflectiveOperationException  | IllegalArgumentException | SecurityException e) {
                    throw new ValueFilterParamSupportException(e);
                }      
    };
    

    private static  Supplier<Stream<ValueFilterParam>> collectFilterParamFromRequestMap(Class<?> filterRequestClass, Map<String, String[]> params){
        return () -> Stream
            .of(filterRequestClass.getDeclaredFields())
            .filter(f -> f.isAnnotationPresent(FilterParam.class))
            .filter(f -> {
                FilterParam filterParam = f.getAnnotation(FilterParam.class);
       
                if(CHECK_PARAM_REQUIRES_SUFIX.test(params, filterParam)){
                   throw new ValueFilterParamSupportException("parameter '%s' requires type sufix. try send '%s'".formatted(filterParam.param() , GET_FULL_NAME.apply(filterParam)));
                }

                return CHECK_MAP_CONTAINS_FULL_NAME.test(params, filterParam);
            })
            .map(f -> {

                FilterParam filterParam = f.getAnnotation(FilterParam.class);

                return new ValueFilterParam(
                        filterParam, 
                        GET_VALUE_PARAMETER_CONVERTER.apply(filterParam).converter(
                            f.getType(), 
                            params.get(GET_FULL_NAME.apply(filterParam))
                        )
                    );

            });
    }
    
}
