package com.cgi.parizek.matej.utils.validators;

import com.cgi.parizek.matej.exceptions.InvalidParameterException;
import com.cgi.parizek.matej.exceptions.InvalidQueryException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;


@UtilityClass
@Slf4j
/**
 * Validation for REST parameters, mainly numbers in path variables or queries
 */
public class BaseValidator {
    /**
     * Validate id
     * @param idStr - String id equivalent
     * @return {@code long}
     * @throws InvalidParameterException
     */
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

    /**
     * Validate page number in query
     * @param pageStr - String page number equivalent
     * @return {@code long}
     * @throws InvalidQueryException
     */
    public int parsePage(String pageStr){
        if (pageStr == null  || pageStr.isEmpty()){
            throw new InvalidQueryException("Page query cannot be parse to Integer, page is null/empty." );
        }
        try{
            var pageNumber = Integer.parseInt(pageStr.trim());
            if (pageNumber < 0)
                throw new InvalidQueryException("Page number in query with value:" +
                        " '"+ pageStr+ "' must be equal or greater than 0");
            return pageNumber;
        }catch (NumberFormatException e){
            log.error("Cannot parse page {}: ",pageStr,e);
            throw new InvalidQueryException("Page number in query with value: '"+ pageStr+ "' cannot be parse to Integer: " + e.getMessage());
        }
    }
}
