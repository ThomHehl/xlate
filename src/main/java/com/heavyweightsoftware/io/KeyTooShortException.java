package com.heavyweightsoftware.io;

import java.io.IOException;

public class KeyTooShortException extends IOException {
    public KeyTooShortException(String msg) {
        super(msg);
    }
}
