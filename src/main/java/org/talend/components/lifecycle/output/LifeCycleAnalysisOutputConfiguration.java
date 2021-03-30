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
package org.talend.components.lifecycle.output;

import java.io.Serializable;

import org.talend.components.lifecycle.dataset.LifeCycleAnalysisDataSet;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

import lombok.Data;

@Data
@GridLayout({ @GridLayout.Row("dataSet"), @GridLayout.Row("table"), @GridLayout.Row("val") })
@Documentation("Configuration for sink.")
public class LifeCycleAnalysisOutputConfiguration implements Serializable {

    @Option
    @Documentation("DataSet.")
    private LifeCycleAnalysisDataSet dataSet;

    @Option
    @Documentation("Fixed field value.")
    private String val;

    @Option
    @Documentation("Table name.")
    private String table;
}
