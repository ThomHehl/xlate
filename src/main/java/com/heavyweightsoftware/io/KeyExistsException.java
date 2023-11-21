package com.heavyweightsoftware.io;

import java.io.IOException;

public class KeyExistsException extends IOException {
    public KeyExistsException(String msg) {
        super(msg);
    }
}
