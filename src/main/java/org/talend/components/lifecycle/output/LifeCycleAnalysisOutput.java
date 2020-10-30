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
package org.talend.components.lifecycle.output;

import java.io.Serializable;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.Data;
import org.talend.components.lifecycle.service.LifeCycleAnalysisService;
import org.talend.sdk.component.api.component.Icon;
import org.talend.sdk.component.api.component.Version;
import org.talend.sdk.component.api.meta.Documentation;
import org.talend.sdk.component.api.processor.AfterGroup;
import org.talend.sdk.component.api.processor.ElementListener;
import org.talend.sdk.component.api.processor.Processor;
import org.talend.sdk.component.api.record.Record;

import lombok.extern.slf4j.Slf4j;

@Version(1)
@Icon(Icon.IconType.STAR)
@Processor(name = "LifeCycleAnalysisOutput")
@Documentation("Output.")
@Data
@Slf4j
public class LifeCycleAnalysisOutput implements Serializable {

    private final static String BEGIN = "BEGIN";

    private final static String END = "END";

    private final static String CONSTRUCTOR = "CONSTRUCTOR";

    private final static String POST_CONSTRUCT = "POST_CONSTRUCT";

    private final static String ELT_LISTENER = "ELEMENT_LISTENER";

    private final static String AFTER_GROUP = "AFTER_GROUP";

    private final static String PRE_DESTROY = "PRE_DESTROY";

    private final LifeCycleAnalysisService service;

    private final LifeCycleAnalysisOutputConfiguration configuration;

    private UUID uuid_instanciated_constructor;

    private UUID uuid_instanciated_postconstruct;

    private UUID uuid_instanciated_elementListener;

    private UUID uuid_instanciated_aftergroup;

    private UUID uuid_instanciated_predestroy;

    private transient UUID transient_uuid_instanciated_constructor;

    private transient UUID transient_uuid_instanciated_postconstruct;

    private transient UUID transient_uuid_instanciated_elementListener;

    private transient UUID transient_uuid_instanciated_aftergroup;

    private transient UUID transient_uuid_instanciated_predestroy;

    public LifeCycleAnalysisOutput(LifeCycleAnalysisOutputConfiguration configuration, LifeCycleAnalysisService service) {
        this.configuration = configuration;

        service.log(10, CONSTRUCTOR, BEGIN, this);
        log.info(this + " - Constructor");

        this.service = service;

        uuid_instanciated_constructor = UUID.randomUUID();
        transient_uuid_instanciated_constructor = UUID.randomUUID();
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
        service.log(25, POST_CONSTRUCT, END, this);
    }

    @ElementListener
    public void onRecord(final Record record) {
        service.log(30, ELT_LISTENER, BEGIN, this);
        if (uuid_instanciated_elementListener == null) {
            uuid_instanciated_elementListener = UUID.randomUUID();
        }

        if (transient_uuid_instanciated_elementListener == null) {
            transient_uuid_instanciated_elementListener = UUID.randomUUID();
        }

        log.info(this + " - " + record);
        service.displaySomething();
        service.log(35, ELT_LISTENER, END, this);
    }

    @AfterGroup
    public void afterGroup() {
        service.log(40, AFTER_GROUP, BEGIN, this);
        if (uuid_instanciated_aftergroup == null) {
            uuid_instanciated_aftergroup = UUID.randomUUID();
        }
        if (transient_uuid_instanciated_aftergroup == null) {
            transient_uuid_instanciated_aftergroup = UUID.randomUUID();
        }

        log.info(this + " - @AfterGroup");
        service.log(45, AFTER_GROUP, END, this);
    }

    @PreDestroy
    public void release() {
        service.log(50, PRE_DESTROY, BEGIN, this);
        if (uuid_instanciated_predestroy == null) {
            uuid_instanciated_predestroy = UUID.randomUUID();
        }
        if (transient_uuid_instanciated_predestroy == null) {
            transient_uuid_instanciated_predestroy = UUID.randomUUID();
        }

        log.info(this + " - @PreDestroy");
        service.log(55, PRE_DESTROY, END, this);
    }
}
