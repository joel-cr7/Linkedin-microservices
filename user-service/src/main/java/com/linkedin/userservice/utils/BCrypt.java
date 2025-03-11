package com.linkedin.userservice.utils;


import org.springframework.stereotype.Component;

import static org.mindrot.jbcrypt.BCrypt.*;

/**
 * Implementing own hashing using external library jbcrypt as we are not using Spring Security
 */
public class BCrypt {

    /**
     * Hash the given string and return the hashed value
     * @param s
     * @return
     */
    public static String hash(String s){
        return hashpw(s, gensalt());
    }


    /**
     * Check if the plain password and its corresponding hashed value from DB match
     * @param passwordText
     * @param passwordHashed
     * @return
     */
    public static boolean match(String passwordText, String passwordHashed){
        return checkpw(passwordText, passwordHashed);
    }
}
