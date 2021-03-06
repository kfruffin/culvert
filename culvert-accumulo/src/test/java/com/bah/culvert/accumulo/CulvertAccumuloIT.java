/**
 * Copyright 2011 Booz Allen Hamilton.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Booz Allen Hamilton licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bah.culvert.accumulo;

import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.mock.MockInstance;
import org.apache.hadoop.conf.Configuration;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bah.culvert.Client;
import com.bah.culvert.CulvertIntegrationTestUtility;
import com.bah.culvert.accumulo.database.AccumuloDatabaseAdapter;
import com.bah.culvert.adapter.DatabaseAdapter;

/**
 * Do a full integration test between culvert and accumulo.
 * <p>
 * Uses the general testing utility for integrating Culvert with any system.
 * @see CulvertIntegrationTestUtility
 */
public class CulvertAccumuloIT {

  private static Instance inst;
  private static final String INSTANCE_NAME = "TEST_INSTANCE";
  private static final Configuration conf = new Configuration();
  private static final String USERNAME = "user";
  private static final String PASSWORD = "password";

  /**
   * Setup the mock instance
   * @throws Exception
   */
  @BeforeClass
  public static void beforeClass() throws Exception {
    inst = new MockInstance(INSTANCE_NAME);
    conf.set(AccumuloConstants.INSTANCE_CLASS_KEY, MockInstance.class.getName());
    conf.set(AccumuloConstants.INSTANCE_NAME_KEY, INSTANCE_NAME);
    // conf.setLong(AccumuloConstants.MAX_LATENCY_KEY, 1000);
    // conf.setLong(AccumuloConstants.MAX_MEMORY_KEY, 1000);
    // conf.setInt(AccumuloConstants.MAX_THREADS_KEY, 10);
    conf.set(AccumuloConstants.USERNAME_KEY, USERNAME);
    conf.set(AccumuloConstants.PASSWORD_KEY, PASSWORD);
  }

  /**
   * Test that we read and write to/from the table with indexes properly
   * @throws Exception
   */
  @Test
  public void testReadWrite() throws Exception {
    // create the database for reading
    DatabaseAdapter database = new AccumuloDatabaseAdapter();
    database.setConf(conf);

    // setup the client and the database
    Client client = CulvertIntegrationTestUtility.prepare(database);

    // now test that we do insertion properly
    CulvertIntegrationTestUtility.testInsertion(client);

    // and that we can read the indexed value back out
    CulvertIntegrationTestUtility.testQuery(client);
  }
}
