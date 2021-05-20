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
import java.util.List;
import java.util.UUID;
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

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Version(1)
@Icon(Icon.IconType.STAR)
@PartitionMapper(name = "LifeCycleAnalysisInput")
@Documentation("Input for life cycle analysis.")
@Slf4j
@Data
public class LifeCycleAnalysisPartitionMapper implements Serializable {

    private final static String BEGIN = "BEGIN";

    private final static String END = "END";

    private final static String CONSTRUCTOR = "CONSTRUCTOR";

    private final static String ASSESSOR = "ASSESSOR";

    private final static String SPLIT = "SPLIT";

    private final static String EMITTER = "EMITTER";

    private UUID uuid_instanciated_constructor;

    private UUID uuid_instanciated_assessor;

    private UUID uuid_instanciated_split;

    private UUID uuid_instanciated_emitter;

    private transient UUID transient_uuid_instanciated_constructor;

    private transient UUID transient_uuid_instanciated_assessor;

    private transient UUID transient_uuid_instanciated_split;

    private transient UUID transient_uuid_instanciated_emitter;

    protected final LifeCycleAnalysisInputConfiguration configuration;

    protected final RecordBuilderFactory recordBuilderFactory;

    protected final LifeCycleAnalysisService service;

    protected final I18nMessage i18n;

    public LifeCycleAnalysisPartitionMapper(LifeCycleAnalysisInputConfiguration configuration,
            RecordBuilderFactory recordBuilderFactory, LifeCycleAnalysisService service, I18nMessage i18n) {
        this.configuration = configuration;
        service.log(10, CONSTRUCTOR, BEGIN, this);

        uuid_instanciated_constructor = UUID.randomUUID();
        transient_uuid_instanciated_constructor = UUID.randomUUID();

        log.info(this + " - Constructor");

        this.recordBuilderFactory = recordBuilderFactory;
        this.i18n = i18n;
        this.service = service;
        service.log(15, CONSTRUCTOR, END, this);
    }

    @Assessor
    public long estimateSize() {
        service.log(20, ASSESSOR, BEGIN, this);
        if (uuid_instanciated_assessor == null) {
            uuid_instanciated_assessor = UUID.randomUUID();
        }

        if (transient_uuid_instanciated_assessor == null) {
            transient_uuid_instanciated_assessor = UUID.randomUUID();
        }

        log.info(this + " - @Assessor");
        service.log(25, ASSESSOR, END, this);
        return 0;
    }

    @Split
    public List<LifeCycleAnalysisPartitionMapper> split(@PartitionSize final long bundleSize) {
        service.log(30, SPLIT, BEGIN, this);
        log.info(this + " - @Split");

        if (uuid_instanciated_split == null) {
            uuid_instanciated_split = UUID.randomUUID();
        }

        if (transient_uuid_instanciated_split == null) {
            transient_uuid_instanciated_split = UUID.randomUUID();
        }

        final List<LifeCycleAnalysisPartitionMapper> collect = IntStream.range(0, configuration.getSplits())
                .mapToObj(i -> new LifeCycleAnalysisPartitionMapper(configuration, recordBuilderFactory, service, i18n))
                .collect(Collectors.toList());

        service.log(35, SPLIT, END, this);
        return collect;
    }

    @Emitter
    public LifeCycleAnalysisInput createSource() {
        service.log(40, EMITTER, BEGIN, this);
        log.info(this + " - @Emitter");

        if (uuid_instanciated_emitter == null) {
            uuid_instanciated_emitter = UUID.randomUUID();
        }

        if (transient_uuid_instanciated_emitter == null) {
            transient_uuid_instanciated_emitter = UUID.randomUUID();
        }

        final LifeCycleAnalysisInput lifeCycleAnalysisInput = new LifeCycleAnalysisInput(this.uuid_instanciated_constructor,
                configuration, recordBuilderFactory, service);
        service.log(45, EMITTER, END, this);
        return lifeCycleAnalysisInput;
    }
}
