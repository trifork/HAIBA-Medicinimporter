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

import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import dk.nsi.haiba.medicinimporter.dao.HAIBADAO;
import dk.nsi.haiba.medicinimporter.dao.MedicinDAO;
import dk.nsi.haiba.medicinimporter.log.Log;
import dk.nsi.haiba.medicinimporter.status.ImportStatusRepository;

/*
 * Scheduled job, responsible for fetching new data from LPR, then send it to the RulesEngine for further processing
 */
public class ImportExecutor {
    private static Log log = new Log(Logger.getLogger(ImportExecutor.class));

    private boolean manualOverride;

    @Autowired
    HAIBADAO haibaDao;

    @Autowired
    MedicinDAO medicinDAO;

    @Autowired
    ImportStatusRepository statusRepo;
    
    @Value("${batchSize:1000}")
    long batchSize;

    @Scheduled(cron = "${cron.import.job}")
    public void run() {
        if (!isManualOverride()) {
            log.debug("Running Importer: " + new Date().toString());
            doProcess();
        } else {
            log.debug("Importer must be started manually");
        }
    }

    public void doProcess() {
        try {
            statusRepo.importStartedAt(new DateTime());
            long latestSyncId = haibaDao.getLatestSyncId();
            Collection<Medicin> medicinFromSyncId = medicinDAO.getMedicinFromSyncId(latestSyncId, batchSize);
            while (!medicinFromSyncId.isEmpty()) {
                log.debug("doProcess: copying " + medicinFromSyncId.size() + " rows from syncId " + latestSyncId);
                haibaDao.saveMedicinList(medicinFromSyncId);
                latestSyncId = haibaDao.getLatestSyncId();
                medicinFromSyncId = medicinDAO.getMedicinFromSyncId(latestSyncId, batchSize);
            }
            statusRepo.importEndedWithSuccess(new DateTime());
        } catch (Throwable e) {
            statusRepo.importEndedWithFailure(new DateTime(), e.getMessage());
            throw new RuntimeException("runParserOnInbox  failed", e); // to make sure the transaction rolls back
        }
    }

    public boolean isManualOverride() {
        return manualOverride;
    }

    public void setManualOverride(boolean manualOverride) {
        this.manualOverride = manualOverride;
    }
}
