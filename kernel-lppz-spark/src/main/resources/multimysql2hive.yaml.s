!!com.lppz.spark.bean.SparkHiveSqlBean {
configBean: !!com.lppz.spark.bean.SparkSqlConfigBean {
 rdbmsjdbcUrl: 'jdbc:mysql://192.168.37.246:3306/#schema#?useUnicode=true&characterEncoding=UTF8',
 rdbmsjdbcUser: 'root',
 rdbmsjdbcPasswd: 'KTqHDMg8r3q1w',
 rdbmsdbDriver: 'com.mysql.jdbc.Driver',
 schema: 'omsext'
},
mysqlBean: !!com.lppz.spark.bean.SparkMysqlDmlBean {
 tableName: 'orders',
 partitionColumn: 'sparkid',
 colList: 'id,orderid',
 numPartitions: '50',
 relateKey: 'myorder',
 total4Once: '500000',
 sql: "select * from orders where issuedate<#maxdate# and issuedate>=#mindate# "
},
sourcebean: !!com.lppz.spark.bean.Rdbms2HDfsBean {
 hiveschema: 'omsext',
 hivetableName: 'omsOrderSparkPar',
 mode: 'true',
 hpcList: [{col: "ds",type: "string",value: "#month#"}]
},
sparkMapbean: {id_1: !!com.lppz.spark.bean.SparkHiveSqlBean {
mysqsqlUseMain: false,
configBean: !!com.lppz.spark.bean.SparkSqlConfigBean {
 rdbmsjdbcUrl: 'jdbc:mysql://192.168.37.246:3306/#schema#?useUnicode=true&characterEncoding=UTF8',
 rdbmsjdbcUser: 'root',
 rdbmsjdbcPasswd: 'KTqHDMg8r3q1w',
 rdbmsdbDriver: 'com.mysql.jdbc.Driver',
 schema: 'omsext'
},
mysqlBean: !!com.lppz.spark.bean.SparkMysqlDmlBean {
 tableName: 'orderlines',
 numPartitions: '120',
 relateKey: 'myorder',
 colList: 'id'
},
sourcebean: !!com.lppz.spark.bean.Rdbms2HDfsBean {
 hiveschema: 'omsext',
 hivetableName: 'omsOrderlinesSparkPar2',
 mode: 'true',
 hpcList: [{col: "ds",type: "string",value: "#month#"}]
},

sparkMapbean: {id: !!com.lppz.spark.bean.SparkHiveSqlBean {

mysqsqlUseMain: true,

mysqlBean: !!com.lppz.spark.bean.SparkMysqlDmlBean {
 tableName: 'orderlinequantities',
 relateKey: 'orderline',
 numPartitions: '120'
},
sourcebean: !!com.lppz.spark.bean.Rdbms2HDfsBean {
 hiveschema: 'omsext',
 hivetableName: 'omssOrderlinequantitiesSparkPar3',
 mode: 'true',
 hpcList: [{col: "ds",type: "string",value: "#month#"}]
}

}

}
},

orderid_1: !!com.lppz.spark.bean.SparkHiveSqlBean {
mysqsqlUseMain: true,
mysqlBean: !!com.lppz.spark.bean.SparkMysqlDmlBean {
 tableName: 'order_sharding',
 numPartitions: '120',
 relateKey: 'orderid'
},
sourcebean: !!com.lppz.spark.bean.Rdbms2HDfsBean {
 hiveschema: 'omsext',
 hivetableName: 'omsOrder_shardingSparkPar4',
 mode: 'true',
 hpcList: [{col: "ds",type: "string",value: "#month#"}]
}

},
id_2: !!com.lppz.spark.bean.SparkHiveSqlBean {
mysqsqlUseMain: true,
mysqlBean: !!com.lppz.spark.bean.SparkMysqlDmlBean {
 tableName: 'busi_lp_deliverye_data',
 numPartitions: '120',
 relateKey: 'myorder',
 colList: 'id'
},
sourcebean: !!com.lppz.spark.bean.Rdbms2HDfsBean {
 hiveschema: 'omsext',
 hivetableName: 'omsbusi_lp_deliverye_dataSparkPar5',
 mode: 'true',
 hpcList: [{col: "ds",type: "string",value: "#month#"}]
},
sparkMapbean: {id_1: !!com.lppz.spark.bean.SparkHiveSqlBean {

mysqsqlUseMain: true,

mysqlBean: !!com.lppz.spark.bean.SparkMysqlDmlBean {
 tableName: 'busi_lp_deliverye_line_data',
 relateKey: 'mydelivery',
 numPartitions: '120'
},
sourcebean: !!com.lppz.spark.bean.Rdbms2HDfsBean {
 hiveschema: 'omsext',
 hivetableName: 'omsbusi_lp_deliverye_line_dataSparkPar6',
 mode: 'true',
 hpcList: [{col: "ds",type: "string",value: "#month#"}]
}

}

}



}

}
}