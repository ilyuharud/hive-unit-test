package ru.vai.bigdata;

/**
 * Created by ilya on 18.06.17.
 */
public class Joiner {

    public Joiner(String separator){
        this.separator = separator;
    }

    private String separator = ",";

    private String defaultNullValue = "null";

    public String getSeparator() {
        return separator;
    }

    public String getDefaultNullValue() {
        return defaultNullValue;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void setDefaultNullValue(String defaultNullValue) {
        this.defaultNullValue = defaultNullValue;
    }

    public String join(Object... items) {
        String result = "";
        for(Object item: items){
            item = (item == null) ? this.defaultNullValue : item;
            result += (item.equals(items[items.length - 1]))
                    ? item.toString()
                    : item.toString() + separator;
        }
        return result;
    }
}
