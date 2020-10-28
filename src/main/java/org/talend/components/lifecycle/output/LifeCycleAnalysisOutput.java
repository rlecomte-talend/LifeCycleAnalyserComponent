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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

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
@Slf4j
public class LifeCycleAnalysisOutput implements Serializable {

    private final LifeCycleAnalysisOutputConfiguration configuration;

    private final LifeCycleAnalysisService service;

    public LifeCycleAnalysisOutput(LifeCycleAnalysisOutputConfiguration configuration, LifeCycleAnalysisService service) {
        log.info(this + " - Constructor");

        this.configuration = configuration;
        this.service = service;
    }

    @PostConstruct
    public void init() {
        log.info(this + " - @PostConstruct");
    }

    @ElementListener
    public void onRecord(final Record record) {
        log.info(this + " - " + record);
        service.displaySomething();
    }

    @AfterGroup
    public void afterGroup() {
        log.info(this + " - @AfterGroup");
    }

    @PreDestroy
    public void release() {
        log.info(this + " - @PreDestroy");
    }
}
