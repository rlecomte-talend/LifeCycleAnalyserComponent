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
package org.talend.components.lifecycle.service;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.talend.components.lifecycle.dataset.LifeCycleAnalysisDataSet;
import org.talend.components.lifecycle.output.LifeCycleAnalysisOutput;
import org.talend.components.lifecycle.source.LifeCycleAnalysisInput;
import org.talend.components.lifecycle.source.LifeCycleAnalysisPartitionMapper;
import org.talend.sdk.component.api.service.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LifeCycleAnalysisService implements Serializable {

    private final static String JDBC_CLASS = "org.apache.derby.jdbc.ClientDriver";

    // create table LOG_OUTPUT (iorder integer, lifeCycle varchar(100), subLifeCycle varchar(100), connector_id varchar(1000),
    // service_id
    // varchar(1000), thread_id varchar(1000), uuid_instanciated_constructor varchar(100), uuid_instanciated_postconstruct
    // varchar(100), uuid_instanciated_elementListener varchar(100), uuid_instanciated_aftergroup varchar(100),
    // uuid_instanciated_predestroy varchar(100), transient_uuid_instanciated_constructor varchar(100),
    // transient_uuid_instanciated_postconstruct varchar(100), transient_uuid_instanciated_elementListener varchar(100),
    // transient_uuid_instanciated_aftergroup varchar(100), transient_uuid_instanciated_predestroy varchar(100), timest bigint,
    // fixed varchar(50));
    private final static String INSERT_QUERY_OUTPUT = "(iorder, lifeCycle, subLifeCycle, connector_id, service_id, thread_id, uuid_instanciated_constructor, uuid_instanciated_postconstruct, uuid_instanciated_elementListener, uuid_instanciated_aftergroup, uuid_instanciated_predestroy, transient_uuid_instanciated_constructor, transient_uuid_instanciated_postconstruct, transient_uuid_instanciated_elementListener, transient_uuid_instanciated_aftergroup, transient_uuid_instanciated_predestroy, timest, fixed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // create table LOG_INPUT (iorder integer, lifeCycle varchar(100), subLifeCycle varchar(100),
    // mapper_uuid_instanciated_constructor varchar(100),connector_id varchar(1000),
    // service_id
    // varchar(1000), thread_id varchar(1000), uuid_instanciated_constructor varchar(100), uuid_instanciated_postconstruct
    // varchar(100), uuid_instanciated_producer varchar(100),
    // uuid_instanciated_predestroy varchar(100), transient_uuid_instanciated_constructor varchar(100),
    // transient_uuid_instanciated_postconstruct varchar(100), transient_uuid_instanciated_producer varchar(100),
    // transient_uuid_instanciated_predestroy varchar(100), timest bigint,
    // fixed varchar(50));
    private final static String INSERT_QUERY_INPUT = "(iorder, lifeCycle, subLifeCycle, mapper_uuid_instanciated_constructor, connector_id, service_id, thread_id, uuid_instanciated_constructor, uuid_instanciated_postconstruct, uuid_instanciated_producer, uuid_instanciated_predestroy, transient_uuid_instanciated_constructor, transient_uuid_instanciated_postconstruct, transient_uuid_instanciated_producer, transient_uuid_instanciated_predestroy, timest, fixed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // create table LOG_MAPPER (iorder integer, lifeCycle varchar(100), subLifeCycle varchar(100), connector_id varchar(1000),
    // service_id
    // varchar(1000), thread_id varchar(1000), uuid_instanciated_constructor varchar(100), uuid_instanciated_postconstruct
    // varchar(100), uuid_instanciated_producer varchar(100),
    // uuid_instanciated_predestroy varchar(100), transient_uuid_instanciated_constructor varchar(100),
    // transient_uuid_instanciated_postconstruct varchar(100), transient_uuid_instanciated_producer varchar(100),
    // transient_uuid_instanciated_predestroy varchar(100), timest bigint,
    // fixed varchar(50));
    private final static String INSERT_QUERY_MAPPER = "(iorder, lifeCycle, subLifeCycle, connector_id, service_id, thread_id, uuid_instanciated_constructor, uuid_instanciated_postconstruct, uuid_instanciated_producer, uuid_instanciated_predestroy, transient_uuid_instanciated_constructor, transient_uuid_instanciated_postconstruct, transient_uuid_instanciated_producer, transient_uuid_instanciated_predestroy, timest, fixed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private transient Connection conn = null;

    public LifeCycleAnalysisService() {
        log.info(this + " - Constructor");
    }

    public void displaySomething() {
        log.info(this + " - displaySomething()");
    }

    private synchronized Connection getConn(LifeCycleAnalysisDataSet dse) {
        if (this.conn != null) {
            return this.conn;
        }
        try {
            Class.forName(JDBC_CLASS);
            this.conn = DriverManager.getConnection(dse.getJdbcURL());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "Can't connect to database '" + dse.getJdbcURL() + "' with '" + JDBC_CLASS + "'. " + e.getMessage(), e);
        }

        return this.conn;
    }

    private synchronized PreparedStatement getPs(LifeCycleAnalysisDataSet conf, String table, String query) {
        try {
            Connection conn = this.getConn(conf);
            final String sql = "INSERT INTO " + conf.getSchema() + "." + table + " " + query;
            final PreparedStatement psOutput = conn.prepareStatement(sql);
            return psOutput;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "Can't connect to database '" + conf.getJdbcURL() + "' with '" + JDBC_CLASS + "'. " + e.getMessage(), e);
        }
    }

    public void log(int order, String lifeCycle, String subLifeCycle, LifeCycleAnalysisPartitionMapper mapper) {
        final PreparedStatement ps = getPs(mapper.getConfiguration().getDataSet(), mapper.getConfiguration().getTable_mapper(),
                INSERT_QUERY_MAPPER);
        try {
            ps.setInt(1, order);
            ps.setString(2, lifeCycle);
            ps.setString(3, subLifeCycle);
            ps.setString(4, Integer.toHexString(System.identityHashCode(mapper)));
            ps.setString(5, Integer.toHexString(System.identityHashCode(this)));
            ps.setString(6, ((Object) Thread.currentThread()).toString());

            ps.setString(7, "" + mapper.getUuid_instanciated_constructor());
            ps.setString(8, "" + mapper.getUuid_instanciated_assessor());
            ps.setString(9, "" + mapper.getUuid_instanciated_split());
            ps.setString(10, "" + mapper.getUuid_instanciated_emitter());

            ps.setString(11, "" + mapper.getTransient_uuid_instanciated_constructor());
            ps.setString(12, "" + mapper.getTransient_uuid_instanciated_assessor());
            ps.setString(13, "" + mapper.getTransient_uuid_instanciated_split());
            ps.setString(14, "" + mapper.getTransient_uuid_instanciated_emitter());

            ps.setLong(15, System.currentTimeMillis());

            Exception e = new Exception();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            ps.setString(16, "TRACE : " + sw.toString());

            final int n = ps.executeUpdate();
            if (n != 1) {
                throw new RuntimeException("executeUpdate didn't return '1' after insert but '" + n + "'");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't insert new log '" + lifeCycle + "' : " + e.getMessage(), e);
        }
    }

    public void log(int order, String lifeCycle, String subLifeCycle, LifeCycleAnalysisInput input) {
        final PreparedStatement ps = getPs(input.getConfiguration().getDataSet(), input.getConfiguration().getTable_input(),
                INSERT_QUERY_INPUT);
        try {
            ps.setInt(1, order);
            ps.setString(2, lifeCycle);
            ps.setString(3, subLifeCycle);
            ps.setString(4, "" + input.getMapper_uuid_instanciated_constructor());
            ps.setString(5, Integer.toHexString(System.identityHashCode(input)));
            ps.setString(6, Integer.toHexString(System.identityHashCode(this)));
            ps.setString(7, ((Object) Thread.currentThread()).toString());

            ps.setString(8, "" + input.getUuid_instanciated_constructor());
            ps.setString(9, "" + input.getUuid_instanciated_postconstruct());
            ps.setString(10, "" + input.getUuid_instanciated_producer());
            ps.setString(11, "" + input.getUuid_instanciated_predestroy());

            ps.setString(12, "" + input.getTransient_uuid_instanciated_constructor());
            ps.setString(13, "" + input.getTransient_uuid_instanciated_postconstruct());
            ps.setString(14, "" + input.getTransient_uuid_instanciated_producer());
            ps.setString(15, "" + input.getTransient_uuid_instanciated_predestroy());

            ps.setLong(16, System.currentTimeMillis());

            Exception e = new Exception();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            ps.setString(17, sw.toString());

            final int n = ps.executeUpdate();
            if (n != 1) {
                throw new RuntimeException("executeUpdate didn't return '1' after insert but '" + n + "'");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("*** Can't insert new log '" + lifeCycle + "' in table '"
                    + input.getConfiguration().getTable_input() + "': " + e.getMessage(), e);
        }
    }

    public void log(int order, String lifeCycle, String subLifeCycle, LifeCycleAnalysisOutput output) {
        final PreparedStatement ps = getPs(output.getConfiguration().getDataSet(), output.getConfiguration().getTable(),
                INSERT_QUERY_OUTPUT);
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

            Exception e = new Exception();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            ps.setString(18, sw.toString());

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
