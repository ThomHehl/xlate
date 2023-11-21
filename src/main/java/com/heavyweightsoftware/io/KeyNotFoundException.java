package com.heavyweightsoftware.io;

import java.io.IOException;

public class KeyNotFoundException extends IOException {
    public KeyNotFoundException(String msg) {
        super(msg);
    }
}
