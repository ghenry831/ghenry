package com.lambda.demo1;

public class SubClass implements Factory{
    public Object getObject(){
        return new User();
    }
}
