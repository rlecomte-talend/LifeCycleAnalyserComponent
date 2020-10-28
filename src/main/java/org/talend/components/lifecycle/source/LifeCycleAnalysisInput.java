/*
 * Copyright (C) 2006-2020 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement
 * along with this program; if not, write to Talend SA
 * 9 rue Pages 92150 Suresnes, France
 */
package org.talend.components.lifecycle.source;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.talend.sdk.component.api.input.Producer;
import org.talend.sdk.component.api.record.Schema;
import org.talend.sdk.component.api.service.record.RecordBuilderFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LifeCycleAnalysisInput implements Serializable {

    protected final LifeCycleAnalysisInputConfiguration configuration;

    protected final RecordBuilderFactory recordBuilderFactory;

    private transient int nbRecords = 0;

    private transient boolean limitRecords;

    private transient long startTime;

    private transient boolean limitTime;

    private transient Schema schema;

    private transient String hostname;

    public LifeCycleAnalysisInput(LifeCycleAnalysisInputConfiguration configuration, RecordBuilderFactory recordBuilderFactory) {
        log.info(this + " - constructor");

        this.configuration = configuration;
        this.recordBuilderFactory = recordBuilderFactory;
    }

    @PostConstruct
    public void init() {
        log.info(this + " - @PostConstruct");

        startTime = System.currentTimeMillis();
        limitRecords = configuration.getRecordsToGenerate() > 0;
        limitTime = configuration.getMaxDuration() > 0;
        schema = recordBuilderFactory.newSchemaBuilder(Schema.Type.RECORD)
                .withEntry(recordBuilderFactory.newEntryBuilder().withName("hostname").withType(Schema.Type.STRING).build())
                .withEntry(recordBuilderFactory.newEntryBuilder().withName("thread").withType(Schema.Type.STRING).build())
                .withEntry(recordBuilderFactory.newEntryBuilder().withName("recordNumber").withType(Schema.Type.INT).build())
                .withEntry(recordBuilderFactory.newEntryBuilder().withName("date").withType(Schema.Type.DATETIME).build())
                .build();
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error(e.getMessage(), e);
            hostname = "N/A";
        }
    }

    @Producer
    public Object next() {
        if (limitRecords && nbRecords > configuration.getRecordsToGenerate()) {
            log.info(this + " - Max number of records reached.");
            return null;
        }
        if (limitTime && System.currentTimeMillis() - startTime > configuration.getMaxDuration() * 1000) {
            log.info(this + " - Time limit reached.");
            return null;
        }

        return recordBuilderFactory.newRecordBuilder(schema).withString("hostname", hostname)
                .withString("thread", Thread.currentThread().getName()).withInt("recordNumber", nbRecords++)
                .withDateTime("date", new Date()).build();
    }

    @PreDestroy
    public void release() {
        log.info(this + " - @PreDestroy");
    }
}
