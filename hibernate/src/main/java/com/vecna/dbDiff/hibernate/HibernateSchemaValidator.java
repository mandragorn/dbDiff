/**
 * Copyright 2011 Vecna Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
*/

package com.vecna.dbDiff.hibernate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.cfg.Configuration;

import com.vecna.dbDiff.business.catalogSchema.impl.DefaultCatalogSchemaResolverFactory;
import com.vecna.dbDiff.business.dbCompare.impl.RdbCompareError;
import com.vecna.dbDiff.business.dbCompare.impl.RdbDiffEngine;
import com.vecna.dbDiff.business.relationalDb.impl.RelationalDatabaseBeanImpl;
import com.vecna.dbDiff.dao.MetadataDao;
import com.vecna.dbDiff.dao.impl.GenericMetadataDaoImpl;
import com.vecna.dbDiff.model.CatalogSchema;
import com.vecna.dbDiff.model.relationalDb.RelationalDatabase;
import com.vecna.dbDiff.model.relationalDb.RelationalValidationException;

/**
 * Compares hibernate configuration schema to the schema of the live database the configuration points to.
 * @author ogolberg@vecna.com
 */
public class HibernateSchemaValidator {
  private final Configuration m_configuration;

  /**
   * Create a new instance
   * @param configuration hibernate configuration
   */
  public HibernateSchemaValidator(Configuration configuration) {
    m_configuration = configuration;
  }

  /**
   * Compare schemas
   * @return list of schema differences
   * @throws RelationalValidationException if schemas can't be retrieved/built
   * @throws SQLException if live schema can't be retrieved
   */
  public List<RdbCompareError> validate() throws RelationalValidationException, SQLException {
    String jdbcDriver = m_configuration.getProperty("hibernate.connection.driver_class");
    String jdbcUrl = m_configuration.getProperty("hibernate.connection.url");
    String jdbcUser= m_configuration.getProperty("hibernate.connection.username");
    String jdbcPassword = m_configuration.getProperty("hibernate.connection.password");

    CatalogSchema catalogSchema = DefaultCatalogSchemaResolverFactory.getCatalogSchemaResolver()
    .resolveCatalogSchema(jdbcDriver, jdbcUrl);

    RelationalDatabase hibernateSchema = new HibernateMappingsConverter(catalogSchema).convert(m_configuration,
                                                                                               m_configuration.buildMapping());

    Connection conn;

    conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);

    RelationalDatabase liveSchema;

    try {
      MetadataDao metadataDao = new GenericMetadataDaoImpl(conn);
      RelationalDatabaseBeanImpl rdbBean = new RelationalDatabaseBeanImpl();
      rdbBean.setMetadataDao(metadataDao);
      CatalogSchema cs = new CatalogSchema(null, "public");
      liveSchema = rdbBean.createRelationalDatabase(cs);
    } finally {
      conn.close();
    }

    return new RdbDiffEngine().compareRelationalDatabase(hibernateSchema, liveSchema);
  }
}
