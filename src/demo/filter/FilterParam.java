package demo.filter;

@java.lang.annotation.Inherited
@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.RECORD_COMPONENT})
@java.lang.annotation.Retention( java.lang.annotation.RetentionPolicy.RUNTIME )
public @interface FilterParam {
    TypeFilter type() default TypeFilter.EQUALS;
    String param();
    boolean requiresSufix() default true;
    Class<? extends ValueParamConverter> converter() default ValueParamConverterDefault.class;
    boolean ignoresOnQuery() default false;
}
