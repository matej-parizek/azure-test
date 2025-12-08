package com.cgi.parizek.matej.utils.validators;

import com.cgi.parizek.matej.exceptions.InvalidParameterException;
import com.cgi.parizek.matej.exceptions.InvalidQueryException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;


@UtilityClass
@Slf4j
public class BaseValidator {
    public long parseId(String idStr){
        if (idStr == null  || idStr.isEmpty()){
            throw new InvalidParameterException("Id parameter cannot be parse to Long, id is null/empty." );
        }
        try{
            return Long.parseLong(idStr.trim());
        }catch (NumberFormatException e){
            log.error("Cannot parse id {}: ",idStr,e);
            throw new InvalidParameterException("Id parameter '"+ idStr+ "' cannot be parse to Long: " + e.getMessage());
        }
    }

    public int parsePage(String pageStr){
        if (pageStr == null  || pageStr.isEmpty()){
            throw new InvalidQueryException("Page query cannot be parse to Integer, page is null/empty." );
        }
        try{
            return Integer.parseInt(pageStr.trim());
        }catch (NumberFormatException e){
            log.error("Cannot parse page {}: ",pageStr,e);
            throw new InvalidQueryException("Page query '"+ pageStr+ "' cannot be parse to Integer: " + e.getMessage());
        }
    }
}
