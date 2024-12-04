package com.hollysgang.sample.encryption.framework.core;

import java.lang.reflect.Field;

public class PITarget {
    private final boolean isFinal;
    private final Field field;
    private final String piType;

    public PITarget(boolean isFinal, Field field, String piType){
        this.isFinal = isFinal;
        this.field= field;
        this.piType = piType;
    }

    public Object getValue(Object dto) throws IllegalAccessException {
        return field.get(dto);
    }

    public void setValue(Object dto, String val) throws IllegalAccessException {
        field.set(dto, val);
    }

    public String getPIType(){
        return piType;
    }

    public boolean getIsFinal(){
        return isFinal;
    }
}
