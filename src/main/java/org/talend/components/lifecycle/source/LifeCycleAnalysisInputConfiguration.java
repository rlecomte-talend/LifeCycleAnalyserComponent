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

import org.talend.components.lifecycle.dataset.LifeCycleAnalysisDataSet;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.ui.DefaultValue;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

import lombok.Data;

@Data
@Documentation("Configuration for input.")
@GridLayout({ @GridLayout.Row("dataSet"), @GridLayout.Row("table_mapper"), @GridLayout.Row("table_input"),
        @GridLayout.Row("recordsToGenerate"), @GridLayout.Row("maxDuration"), @GridLayout.Row("splits") })
public class LifeCycleAnalysisInputConfiguration implements Serializable {

    @Option
    @Documentation("DataSet.")
    private LifeCycleAnalysisDataSet dataSet;

    @Option
    @Documentation("Number of records to generate (0=infinity).")
    @DefaultValue("10")
    private int recordsToGenerate = 10;

    @Option
    @Documentation("Max duration of pipeline (seconds; 0 = no limit).")
    @DefaultValue("10")
    private int maxDuration = 10;

    @Option
    @Documentation("Number of workers.")
    @DefaultValue("1")
    private int splits = 1;

    @Option
    @Documentation("Fixed field value.")
    private String val;

    @Option
    @Documentation("Table name.")
    private String table_input;

    @Option
    @Documentation("Table name.")
    private String table_mapper;
}
