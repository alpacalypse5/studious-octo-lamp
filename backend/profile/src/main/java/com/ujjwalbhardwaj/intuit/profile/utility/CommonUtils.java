package com.ujjwalbhardwaj.intuit.profile.utility;

import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class CommonUtils {

    public  <T> Set<T> getOrEmptySet(Set<T> set) {
        return Optional.ofNullable(set).orElse(new HashSet<>());
    }

    public  <T> List<T> getOrEmptyList(List<T> list) {
        return Optional.ofNullable(list).orElse(new ArrayList<>());
    }

    public boolean isTimedOut(final Date date, final long timeoutInMinutes) {
        final Date currentDate = new Date();
        return (currentDate.getTime() - date.getTime()) > (timeoutInMinutes * 60 * 1000);
    }
}
