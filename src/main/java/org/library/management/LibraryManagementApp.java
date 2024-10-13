package org.library.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LibraryManagementApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(LibraryManagementApp.class, args);
    }
}
