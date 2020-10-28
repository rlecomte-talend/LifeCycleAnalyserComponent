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
package org.talend.components.lifecycle.datastore;

import java.io.Serializable;

import org.talend.sdk.component.api.component.Icon;
import org.talend.sdk.component.api.configuration.type.DataStore;

import lombok.Data;

@DataStore("LifeCycleAnalysisDatastore")
@Data
@Icon(Icon.IconType.STAR)
public class LifeCycleAnalysisDatastore implements Serializable {

}
