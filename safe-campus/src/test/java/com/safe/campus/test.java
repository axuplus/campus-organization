package com.safe.campus;

import java.util.function.Supplier;

public class test {
    public Object getList(Supplier<Object> supplier){
        return supplier.get();
    }
}
