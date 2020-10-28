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

import org.talend.components.lifecycle.dataset.LifeCycleAnalysisDataSet;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.ui.DefaultValue;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

import lombok.Data;

@Data
@Documentation("Configuration for input.")
@GridLayout({ @GridLayout.Row("dataSet"), @GridLayout.Row("recordsToGenerate"), @GridLayout.Row("maxDuration"),
        @GridLayout.Row("splits") })
public class LifeCycleAnalysisInputConfiguration {

    @Option
    @Documentation("DataSet.")
    private LifeCycleAnalysisDataSet dataSet;

    @Option
    @Documentation("Number of records to generate (0=infinity).")
    @DefaultValue("10")
    private int recordsToGenerate = 10;

    @Option
    @Documentation("Max duration of pipeline (seconds; 0 = no limit).")
    private int maxDuration;

    @Option
    @Documentation("Number of workers.")
    @DefaultValue("1")
    private int splits = 1;
}
