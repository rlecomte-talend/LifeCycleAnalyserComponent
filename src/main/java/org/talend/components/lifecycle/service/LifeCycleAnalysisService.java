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
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.talend.components.lifecycle.dataset.LifeCycleAnalysisDataSet;
import org.talend.components.lifecycle.output.LifeCycleAnalysisOutput;
import org.talend.sdk.component.api.service.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LifeCycleAnalysisService implements Serializable {

    private final static String JDBC_CLASS = "org.apache.derby.jdbc.ClientDriver";

    // create table LOG (iorder integer, lifeCycle varchar(100), subLifeCycle varchar(100), connector_id varchar(1000), service_id
    // varchar(1000), thread_id varchar(1000), uuid_instanciated_constructor varchar(100), uuid_instanciated_postconstruct
    // varchar(100), uuid_instanciated_elementListener varchar(100), uuid_instanciated_aftergroup varchar(100),
    // uuid_instanciated_predestroy varchar(100), transient_uuid_instanciated_constructor varchar(100),
    // transient_uuid_instanciated_postconstruct varchar(100), transient_uuid_instanciated_elementListener varchar(100),
    // transient_uuid_instanciated_aftergroup varchar(100), transient_uuid_instanciated_predestroy varchar(100), timest bigint,
    // fixed varchar(50));
    private final static String INSERT_QUERY = "(iorder, lifeCycle, subLifeCycle, connector_id, service_id, thread_id, uuid_instanciated_constructor, uuid_instanciated_postconstruct, uuid_instanciated_elementListener, uuid_instanciated_aftergroup, uuid_instanciated_predestroy, transient_uuid_instanciated_constructor, transient_uuid_instanciated_postconstruct, transient_uuid_instanciated_elementListener, transient_uuid_instanciated_aftergroup, transient_uuid_instanciated_predestroy, timest, fixed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private transient Connection conn = null;

    private transient PreparedStatement ps = null;

    public LifeCycleAnalysisService() {
        log.info(this + " - Constructor");
    }

    public void displaySomething() {
        log.info(this + " - displaySomething()");
    }

    private synchronized PreparedStatement getPs(LifeCycleAnalysisDataSet conf) {
        if (ps != null) {
            return ps;
        }

        try {
            Class.forName(JDBC_CLASS);
            this.conn = DriverManager.getConnection(conf.getJdbcURL());
            final String sql = "INSERT INTO " + conf.getSchema() + "." + conf.getTable() + " " + INSERT_QUERY;
            ps = conn.prepareStatement(sql);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "Can't connect to database '" + conf.getJdbcURL() + "' with '" + JDBC_CLASS + "'. " + e.getMessage(), e);
        }
        return this.ps;
    }

    public void log(int order, String lifeCycle, String subLifeCycle, LifeCycleAnalysisOutput output) {
        final PreparedStatement ps = getPs(output.getConfiguration().getDataSet());
        try {

            ps.setInt(1, order);
            ps.setString(2, lifeCycle);
            ps.setString(3, subLifeCycle);
            ps.setString(4, Integer.toHexString(System.identityHashCode(output)));
            ps.setString(5, Integer.toHexString(System.identityHashCode(this)));
            ps.setString(6, ((Object) Thread.currentThread()).toString());

            ps.setString(7, "" + output.getUuid_instanciated_constructor());
            ps.setString(8, "" + output.getUuid_instanciated_postconstruct());
            ps.setString(9, "" + output.getUuid_instanciated_elementListener());
            ps.setString(10, "" + output.getUuid_instanciated_aftergroup());
            ps.setString(11, "" + output.getUuid_instanciated_predestroy());

            ps.setString(12, "" + output.getTransient_uuid_instanciated_constructor());
            ps.setString(13, "" + output.getTransient_uuid_instanciated_postconstruct());
            ps.setString(14, "" + output.getTransient_uuid_instanciated_elementListener());
            ps.setString(15, "" + output.getTransient_uuid_instanciated_aftergroup());
            ps.setString(16, "" + output.getTransient_uuid_instanciated_predestroy());

            ps.setLong(17, System.currentTimeMillis());
            ps.setString(18, "" + output.getConfiguration().getVal());

            final int n = ps.executeUpdate();
            if (n != 1) {
                throw new RuntimeException("executeUpdate didn't return '1' after insert but '" + n + "'");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't insert new log '" + lifeCycle + "' : " + e.getMessage(), e);
        }
    }
}
