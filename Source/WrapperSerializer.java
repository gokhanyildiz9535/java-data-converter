package com.gokhanyildiz9535;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WrapperSerializer {

    @JsonProperty("COL#1")
    public String col1;
    @JsonProperty("COL#2")
    public String col2;
    @JsonProperty("COL#3")
    public String col3;
    @JsonProperty("COL#4")
    public String col4;

    public String getCol1() {
        return col1;
    }

    public String getCol2() {
        return col2;
    }

    public String getCol3() {
        return col3;
    }

    public String getCol4() {
        return col4;
    }

    @Override
    public String toString() {
        return "WrapperClass{" +
                "col1='" + col1 + '\'' +
                ", col2='" + col2 + '\'' +
                ", col3='" + col3 + '\'' +
                ", col4='" + col4 + '\'' +
                '}';
    }
}
