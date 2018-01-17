/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.dw.jstorm.example.batch;

import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.TopologyAssignException;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.TopologyBuilder;

import com.alibaba.jstorm.batch.BatchTopologyBuilder;
import com.alibaba.jstorm.batch.util.BatchDef;
import com.alibaba.jstorm.cluster.StormConfig;
import com.alibaba.jstorm.utils.JStormUtils;
import com.alibaba.jstorm.utils.LoadConf;

public class SimpleBatchTopology {
    private static String topologyName = "Batch";
    private static Map conf;

    public static TopologyBuilder SetBuilder() {
        BatchTopologyBuilder topologyBuilder = new BatchTopologyBuilder(topologyName);

        int spoutParallel = JStormUtils.parseInt(conf.get("topology.spout.parallel"), 1);

        BoltDeclarer boltDeclarer = topologyBuilder.setSpout("Spout",
                new SimpleSpout(), spoutParallel);
        int boltParallel = JStormUtils.parseInt(conf.get("topology.bolt.parallel"), 2);
        topologyBuilder.setBolt("Bolt", new SimpleBolt(), boltParallel).shuffleGrouping("Spout",BatchDef.COMMIT_STREAM_ID);

        return topologyBuilder.getTopologyBuilder();
    }

    public static void SetLocalTopology() throws Exception {
        TopologyBuilder builder = SetBuilder();

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(topologyName, conf, builder.createTopology());

        Thread.sleep(60000);

        cluster.shutdown();
    }

    public static void SetRemoteTopology() throws AlreadyAliveException,
            InvalidTopologyException, TopologyAssignException {
        TopologyBuilder builder = SetBuilder();
        StormSubmitter.submitTopology(topologyName, conf, builder.createTopology());
    }

    public static void main(String[] args) throws Exception {
    	Resource resource = new ClassPathResource("/META-INF/storm.yaml");
    		args=new String[]{resource.getURL().getPath()};
        if (args.length < 1) {
            System.err.println("Please input parameters topology.yaml");
            System.exit(-1);
        }

        conf = LoadConf.LoadYaml(args[0]);
        boolean isLocal = StormConfig.local_mode(conf);
        if (isLocal) {
            SetLocalTopology();
        } else {
            SetRemoteTopology();
        }
    }
}
