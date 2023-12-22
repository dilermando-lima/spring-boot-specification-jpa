package demo.filter;

public interface ValueParamConverter {
    public <T> T converter(Class<T> typeToConvert , String[] value);
}
