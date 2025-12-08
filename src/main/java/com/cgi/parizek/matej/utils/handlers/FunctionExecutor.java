package com.cgi.parizek.matej.utils.handlers;

import com.microsoft.azure.functions.HttpResponseMessage;

@FunctionalInterface
public interface FunctionExecutor {
    HttpResponseMessage exec() throws Exception;
}