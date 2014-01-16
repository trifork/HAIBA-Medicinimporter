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
package dk.nsi.haiba.medicinimporter.importer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.common.base.Preconditions;

import dk.nsi.haiba.medicinimporter.parser.Parser;
import dk.nsi.haiba.medicinimporter.parser.ParserException;

public class MedicinParser implements Parser {

	private static final int SKS_CODE_START_INDEX = 3;
	private static final int SKS_CODE_END_INDEX = 23;

	private static final int CODE_TEXT_START_INDEX = 47;

	/**
	 * The field is actually 120 characters long. But the
	 * specification says only to use the first 60.
	 */
	private static final int CODE_TEXT_END_INDEX = 107;

	private static final int ENTRY_TYPE_START_INDEX = 0;
	private static final int ENTRY_TYPE_END_INDEX = 3;

	private static final char OPERATION_CODE_NONE = ' ';
	private static final char OPERATION_CODE_CREATE = '1';
	private static final char OPERATION_CODE_UPDATE = '3';

	private static final String RECORD_TYPE_HOSPITAL = "sgh";
	private static final String RECORD_TYPE_DEPARTMENT = "afd";

	private static final int OPERATION_CODE_INDEX = 187;
	private static final String FILE_ENCODING = "ISO8859-15";

	private static final DateTimeFormatter dateFormat = ISODateTimeFormat.basicDate();

	protected boolean validateInputStructure(File datadir) {
		File[] input = datadir.listFiles();

		Preconditions.checkNotNull(input, "input");
		Preconditions.checkArgument(input.length > 0, "At least one file should be present at this point.");

		return input.length == 1 && isValidFilePresent(input);
	}

	private boolean isValidFilePresent(File[] input) {
		final String filename = input[0].getName();

		return filename.equalsIgnoreCase("MEDICINFILNAVNTODO.TXT")
				|| filename.equalsIgnoreCase("OTHERMEDICINFILNAVNTODO");
	}

	@Override
	public void process(File datadir, String identifier) {
		Preconditions.checkState(validateInputStructure(datadir), "Input structure is invalid");

		File[] files = datadir.listFiles();

		try {
			Preconditions.checkArgument(files.length == 1, "Only one file should be present at this point.");

            long processed = 0;
			LineIterator lines = null;
			try {
				lines = FileUtils.lineIterator(files[0], FILE_ENCODING);

				List<MedicinDummy> dataset = innerParse(lines);
				// TODO - dao.savethislist(dataset)
				
                processed += dataset.size();
			} catch (IOException e) {
				throw new ParserException(e);
			} catch (Exception e) {
				// the persister throws these. Let's make them unchecked from here on at least
				throw new ParserException(e);
			} finally {
				LineIterator.closeQuietly(lines);
			}
		} catch (RuntimeException e) {

			throw e;
		}
	}

	@Override
	public String getHome() {
		return "sksimporter";
	}

	private List<MedicinDummy> innerParse(Iterator<String> lines) {
		List<MedicinDummy> dataset = new ArrayList<MedicinDummy>();

		while (lines.hasNext()) {
			MedicinDummy dummy = parseLine(lines.next());

			if (dummy != null) {
				dataset.add(dummy);
			}
		}

		return dataset;
	}

	private MedicinDummy parseLine(String line) {
		// Determine the record type.
		//
		String recordType = line.substring(ENTRY_TYPE_START_INDEX, ENTRY_TYPE_END_INDEX);

		if (!recordType.equals(RECORD_TYPE_DEPARTMENT) && !recordType.equals(RECORD_TYPE_HOSPITAL)) {
			throw new ParserException("Unknown record type. line=" + line);
		}

		// Since the old record types do not have a operation code (and we are not
		// interested in those records) we can ignore the line.
		//
		if (line.length() < OPERATION_CODE_INDEX + 1) {
			return null;
		}

		// Determine the operation code.
		//
		char code = line.charAt(OPERATION_CODE_INDEX);

		if (code == OPERATION_CODE_CREATE || code == OPERATION_CODE_UPDATE) {
			// Create and update are handled the same way.

			MedicinDummy dummy = new MedicinDummy();
			
			dummy.setDummy(line.substring(SKS_CODE_START_INDEX, SKS_CODE_END_INDEX).trim());

			//dummy.setValidFrom(dateFormat.parseDateTime(line.substring(23, 31)).toDate());
			//dummy.setValidTo(parseValidTo(line));

			return dummy;
		} else if (code == OPERATION_CODE_NONE) {
			return null;
		} else {
			throw new ParserException("SKS parser encountered an unknown operation code in line " + line + ". code=" + code);
		}
	}

    private Date parseValidTo(String line) {
        // ValidTo is inclusive
        Date validToInc = dateFormat.parseDateTime(line.substring(39, 47)).toDate();
        Calendar c = Calendar.getInstance();
        c.setTime(validToInc);
        // Add a day because validTo day is inclusive
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }
}
