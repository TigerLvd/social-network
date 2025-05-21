package com.highload.architect.soc.network.config.routingdatasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ReplicationRoutingDataSource routes connections by <code>@Transaction(readOnly=true|false)</code>
 */
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {
    private Logger log = LoggerFactory.getLogger(ReplicationRoutingDataSource.class);
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    protected Object determineCurrentLookupKey() {
        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            // Чередуем источники данных "read1" и "read2".
            int index = counter.getAndIncrement() % 2;
            String readDataSourceKey = index == 0 ? "read1" : "read2";
            log.info("current read dataSourceType : {}", readDataSourceKey);
            return readDataSourceKey;
        } else {
            log.info("current dataSourceType : write");
            return "write";
        }
    }
}
