package org.lab41.dendrite.services.analysis;

import com.thinkaurelius.titan.core.TitanException;
import org.lab41.dendrite.models.JobMetadata;
import org.lab41.dendrite.services.MetadataService;
import org.lab41.dendrite.services.MetadataTx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class AnalysisService {

    Logger logger = LoggerFactory.getLogger(AnalysisService.class);

    @Autowired
    MetadataService metadataService;

    protected void setJobName(String jobId, String name) {
        MetadataTx tx = metadataService.newTransaction();
        try {
            JobMetadata jobMetadata = tx.getJob(jobId);
            jobMetadata.setName(name);
            tx.commit();
        } catch (TitanException e) {
            logger.debug("exception", e);
            throw e;
        }
    }

    protected void setJobState(String jobId, String state) {
        setJobState(jobId, state, null);
    }

    protected void setJobState(String jobId, String state, String msg) {
        MetadataTx tx = metadataService.newTransaction();
        try {
            JobMetadata jobMetadata = tx.getJob(jobId);
            jobMetadata.setState(state);
            jobMetadata.setMessage(msg);

            if (state.equals(JobMetadata.DONE)) {
                jobMetadata.setProgress(1.0f);
            }

            tx.commit();
        } catch (TitanException e) {
            logger.debug("exception", e);
            throw e;
        }
    }

    protected void setJobProgress(String jobId, float progress) {
        MetadataTx tx = metadataService.newTransaction();
        try {
            JobMetadata jobMetadata = tx.getJob(jobId);
            jobMetadata.setProgress(progress);
            tx.commit();
        } catch (TitanException e) {
            logger.debug("exception", e);
            throw e;
        }
    }
}
