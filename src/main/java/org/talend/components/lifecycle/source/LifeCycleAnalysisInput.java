/*
 * Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.talend.components.lifecycle.service.LifeCycleAnalysisService;
import org.talend.sdk.component.api.input.Producer;
import org.talend.sdk.component.api.record.Record;
import org.talend.sdk.component.api.record.Schema;
import org.talend.sdk.component.api.service.record.RecordBuilderFactory;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class LifeCycleAnalysisInput implements Serializable {

    private final static String BEGIN = "BEGIN";

    private final static String END = "END";

    private final static String CONSTRUCTOR = "CONSTRUCTOR";

    private final static String POST_CONSTRUCT = "POST_CONSTRUCT";

    private final static String PRODUCER = "PRODUCER";

    private final static String PRE_DESTROY = "PRE_DESTROY";

    private UUID uuid_instanciated_constructor;

    private UUID uuid_instanciated_postconstruct;

    private UUID uuid_instanciated_producer;

    private UUID uuid_instanciated_predestroy;

    private transient UUID transient_uuid_instanciated_constructor;

    private transient UUID transient_uuid_instanciated_postconstruct;

    private transient UUID transient_uuid_instanciated_producer;

    private transient UUID transient_uuid_instanciated_predestroy;

    protected final LifeCycleAnalysisInputConfiguration configuration;

    protected final RecordBuilderFactory recordBuilderFactory;

    protected final LifeCycleAnalysisService service;

    private int nbRecords;

    private boolean limitRecords;

    private long startTime;

    private boolean limitTime;

    private transient Schema schema;

    private String hostname;

    private UUID mapper_uuid_instanciated_constructor;

    public LifeCycleAnalysisInput(UUID mapper_uuid_instanciated_constructor, LifeCycleAnalysisInputConfiguration configuration,
            RecordBuilderFactory recordBuilderFactory, LifeCycleAnalysisService service) {
        this.configuration = configuration;
        this.mapper_uuid_instanciated_constructor = mapper_uuid_instanciated_constructor;

        service.log(10, CONSTRUCTOR, BEGIN, this);

        uuid_instanciated_constructor = UUID.randomUUID();
        transient_uuid_instanciated_constructor = UUID.randomUUID();

        log.info(this + " - constructor");

        this.recordBuilderFactory = recordBuilderFactory;
        this.service = service;

        startTime = System.currentTimeMillis();
        limitTime = configuration.getMaxDuration() > 0;
        nbRecords = 0;
        limitRecords = configuration.getRecordsToGenerate() > 0;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error(e.getMessage(), e);
            hostname = "N/A";
        }

        service.log(15, CONSTRUCTOR, END, this);
    }

    @PostConstruct
    public void init() {
        service.log(20, POST_CONSTRUCT, BEGIN, this);

        if (uuid_instanciated_postconstruct == null) {
            uuid_instanciated_postconstruct = UUID.randomUUID();
        }

        if (transient_uuid_instanciated_postconstruct == null) {
            transient_uuid_instanciated_postconstruct = UUID.randomUUID();
        }

        log.info(this + " - @PostConstruct");

        schema = recordBuilderFactory.newSchemaBuilder(Schema.Type.RECORD)
                .withEntry(recordBuilderFactory.newEntryBuilder().withName("hostname").withType(Schema.Type.STRING).build())
                .withEntry(recordBuilderFactory.newEntryBuilder().withName("thread").withType(Schema.Type.STRING).build())
                .withEntry(recordBuilderFactory.newEntryBuilder().withName("recordNumber").withType(Schema.Type.INT).build())
                .withEntry(recordBuilderFactory.newEntryBuilder().withName("date").withType(Schema.Type.DATETIME).build())
                .build();

        service.log(25, POST_CONSTRUCT, END, this);
    }

    @Producer
    public Object next() {
        service.log(30, PRODUCER, BEGIN, this);
        if (uuid_instanciated_producer == null) {
            uuid_instanciated_producer = UUID.randomUUID();
        }

        if (transient_uuid_instanciated_producer == null) {
            transient_uuid_instanciated_producer = UUID.randomUUID();
        }

        if (limitRecords && nbRecords > configuration.getRecordsToGenerate()) {
            log.info(this + " - Max number of records reached.");
            return null;
        }
        if (limitTime && System.currentTimeMillis() - startTime > configuration.getMaxDuration() * 1000) {
            log.info(this + " - Time limit reached.");
            return null;
        }
        final Record rec = recordBuilderFactory.newRecordBuilder(schema).withString("hostname", hostname)
                .withString("thread", Thread.currentThread().getName()).withInt("recordNumber", nbRecords++)
                .withDateTime("date", new Date()).build();

        service.log(35, PRODUCER, END, this);
        return rec;
    }

    @PreDestroy
    public void release() {
        service.log(40, PRE_DESTROY, BEGIN, this);
        if (uuid_instanciated_predestroy == null) {
            uuid_instanciated_predestroy = UUID.randomUUID();
        }
        if (transient_uuid_instanciated_predestroy == null) {
            transient_uuid_instanciated_predestroy = UUID.randomUUID();
        }

        log.info(this + " - @PreDestroy");
        service.log(45, PRE_DESTROY, END, this);
    }
}
