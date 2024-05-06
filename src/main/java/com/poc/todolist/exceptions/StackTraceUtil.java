package com.poc.todolist.exceptions;

public final class StackTraceUtil
{
	/**
	 * Retrieve  first "caused by" error message with top one of a complex error scenario.
	 * @param throwable - received exception
	 * @return - error string
	 */
	public static String getRootCause(Throwable throwable) {
		Throwable rootCause = throwable;
		String topError = throwable.getMessage();
		if(null == rootCause.getCause()) // There is no caused by error
			return topError;
		while (rootCause.getCause() != null) {
			rootCause = rootCause.getCause();
		}
		String rootCauseMessage = rootCause.getMessage();
		return topError + ", caused by: " + rootCauseMessage;
	} // getRootCause

}


