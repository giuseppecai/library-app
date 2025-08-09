package net.giuse.biblioteca.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LibraryConfig {

    @Value("${library.max-loans}")
    private int maxLoansAllowed;

    public int getMaxLoansAllowed() {
        return maxLoansAllowed;
    }
}
