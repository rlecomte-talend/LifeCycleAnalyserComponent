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
package org.talend.components.lifecycle.service;

import java.io.Serializable;

import org.talend.sdk.component.api.service.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LifeCycleAnalysisService implements Serializable {

    public LifeCycleAnalysisService() {
        log.info(this + " - Constructor");
    }

    public void displaySomething() {
        log.info(this + " - displaySomething()");
    }
}
