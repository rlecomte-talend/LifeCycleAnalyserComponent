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
package org.talend.components.lifecycle.dataset;

import java.io.Serializable;

import org.talend.components.lifecycle.datastore.LifeCycleAnalysisDatastore;
import org.talend.sdk.component.api.component.Icon;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.type.DataSet;
import org.talend.sdk.component.api.configuration.ui.DefaultValue;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

import lombok.Data;

@DataSet("LifeCycleAnalysisDataSet")
@Icon(Icon.IconType.STAR)
@GridLayout({ @GridLayout.Row("datastore"), @GridLayout.Row("jdbcURL"), @GridLayout.Row("schema") })
@Data
public class LifeCycleAnalysisDataSet implements Serializable {

    @Option
    @Documentation("DataStore.")
    private LifeCycleAnalysisDatastore datastore;

    @Option
    @Documentation("A jdbc URL to store logs.")
    private String jdbcURL;

    @Option
    @Documentation("Table schema.")
    private String schema;

}
