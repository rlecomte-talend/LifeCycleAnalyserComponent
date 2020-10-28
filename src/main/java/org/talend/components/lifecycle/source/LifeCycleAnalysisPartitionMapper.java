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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.talend.components.lifecycle.service.I18nMessage;
import org.talend.components.lifecycle.service.LifeCycleAnalysisService;
import org.talend.sdk.component.api.component.Icon;
import org.talend.sdk.component.api.component.Version;
import org.talend.sdk.component.api.input.Assessor;
import org.talend.sdk.component.api.input.Emitter;
import org.talend.sdk.component.api.input.PartitionMapper;
import org.talend.sdk.component.api.input.PartitionSize;
import org.talend.sdk.component.api.input.Split;
import org.talend.sdk.component.api.meta.Documentation;
import org.talend.sdk.component.api.service.record.RecordBuilderFactory;

import lombok.extern.slf4j.Slf4j;

@Version(1)
@Icon(Icon.IconType.STAR)
@PartitionMapper(name = "LifeCycleAnalysisInput")
@Documentation("Input for life cycle analysis.")
@Slf4j
public class LifeCycleAnalysisPartitionMapper implements Serializable {

    protected final LifeCycleAnalysisInputConfiguration configuration;

    protected final RecordBuilderFactory recordBuilderFactory;

    protected final LifeCycleAnalysisService service;

    protected final I18nMessage i18n;

    public LifeCycleAnalysisPartitionMapper(LifeCycleAnalysisInputConfiguration configuration,
            RecordBuilderFactory recordBuilderFactory, LifeCycleAnalysisService service, I18nMessage i18n) {
        log.info(this + " - Constructor");

        this.configuration = configuration;
        this.recordBuilderFactory = recordBuilderFactory;
        this.i18n = i18n;
        this.service = service;
    }

    @Assessor
    public long estimateSize() {
        log.info(this + " - @Assessor");
        return 0;
    }

    @Split
    public List<LifeCycleAnalysisPartitionMapper> split(@PartitionSize final long bundleSize) {
        log.info(this + " - @Split");

        return IntStream.range(0, configuration.getSplits())
                .mapToObj(i -> new LifeCycleAnalysisPartitionMapper(configuration, recordBuilderFactory, service, i18n))
                .collect(Collectors.toList());
    }

    @Emitter
    public LifeCycleAnalysisInput createSource() {
        log.info(this + " - @Emitter");

        return new LifeCycleAnalysisInput(configuration, recordBuilderFactory, service);
    }
}
