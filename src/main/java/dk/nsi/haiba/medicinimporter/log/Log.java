/**
 * The MIT License
 *
 * Original work sponsored and donated by National Board of e-Health (NSI), Denmark
 * (http://www.nsi.dk)
 *
 * Copyright (C) 2011 National Board of e-Health (NSI), Denmark (http://www.nsi.dk)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dk.nsi.haiba.medicinimporter.log;

import org.apache.log4j.Logger;


/*
 * Wrapper class for Log4j - so the "is?enabled" methods are hidden away from regular code
 */
public class Log {
	
	Logger logger;
	
	public Log(Logger logger) {
		this.logger = logger;
	}

	public void trace(String message) {
		if(logger.isTraceEnabled()) {
			logger.trace(message);
		}
	}

	public void debug(String message) {
		if(logger.isDebugEnabled()) {
			logger.debug(message);
		}
	}

	public void debug(String message, Throwable t) {
		if(logger.isDebugEnabled()) {
			logger.debug(message, t);
		}
	}

	public void warn(String message) {
		logger.warn(message);
	}

	public void info(String message) {
		if(logger.isInfoEnabled()) {
			logger.info(message);
		}
	}

	public void error(String message) {
		logger.error(message);
	}
	
	public void error(String message, Throwable t) {
		logger.error(message, t);
	}
	
}
