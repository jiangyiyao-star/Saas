-- 数据库初始化脚本
-- 创建库
create database if not exists saas;

-- 使用库
use saas;

-- 1.设备类型表 (device_type)
create table `device_type` (
                               `id` bigint(20) not null auto_increment comment '主键ID',
                               `type_name` varchar(64) not null comment '机型名称（如：双头智能售酒机）',
                               `config_info` varchar(255) default null comment '设备标准配置说明',
                               `key_parameters` json default null comment '关键参数模板(JSON格式)',
                               `is_deleted` tinyint(4) not null default '0' comment '软删除标记(0-正常，1-已删除)',
                               `created_at` datetime not null default current_timestamp comment '创建时间',
                               `updated_at` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
                               primary key (`id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='设备类型基础表';

-- 2.设备基础信息表 (device_info)
create table `device_info` (
                               `id` bigint(20) not null auto_increment comment '主键ID',
                               `device_sn` varchar(64) not null comment '设备唯一序列号/出厂ID',
                               `device_name` varchar(64) not null comment '设备自定义名称',
                               `type_id` bigint(20) not null comment '关联的设备类型ID (device_type.id)',
                               `owner_id` bigint(20) default null comment '归属店主/商户ID',
                               `status` tinyint(4) not null default '0' comment '设备状态(0-禁用, 1-在线, 2-离线, 3-故障)',
                               `province_code` varchar(20) default null comment '省份编码',
                               `city_code` varchar(20) default null comment '城市编码',
                               `district_code` varchar(20) default null comment '区县编码',
                               `address` varchar(255) default null comment '详细铺设地址',
                               `longitude` decimal(10,6) default null comment '经度 (地图分布展示用)',
                               `latitude` decimal(10,6) default null comment '纬度 (地图分布展示用)',
                               `qr_code_url` varchar(255) default null comment '设备绑定的收款码URL',
                               `custom_parameters` json default null comment '单机特有扩展参数配置(JSON)',
                               `last_heartbeat` datetime default null comment '最后一次心跳时间',
                               `is_deleted` tinyint(4) not null default '0' comment '软删除标记(0-正常，1-已删除)',
                               `created_at` datetime not null default current_timestamp comment '录入时间',
                               `updated_at` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
                               primary key (`id`),
                               unique key `uk_device_sn` (`device_sn`),
                               key `idx_type_id` (`type_id`),
                               key `idx_owner_id` (`owner_id`),
                               key `idx_status` (`status`),
                               key `idx_region` (`province_code`,`city_code`,`district_code`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='设备基础信息档案表';

-- 3.设备商品配置表 (device_product_config)
create table `device_product_config` (
                                         `id` bigint(20) not null auto_increment comment '主键ID',
                                         `device_sn` varchar(64) not null comment '关联设备序列号',
                                         `channel_no` tinyint(4) not null comment '物理通道号/酒头号(如：1, 2)',
                                         `product_id` bigint(20) not null comment '关联的全局商品ID',
                                         `device_price` decimal(10,2) not null comment '该设备当前的最终售卖价格',
                                         `current_stock` decimal(8,2) not null default '0.00' comment '当前剩余库存(关联配方联动扣减)',
                                         `created_at` datetime not null default current_timestamp comment '配置创建时间',
                                         `updated_at` datetime not null default current_timestamp on update current_timestamp comment '配置更新时间',
                                         primary key (`id`),
                                         unique key `uk_device_channel` (`device_sn`,`channel_no`),
                                         key `idx_product_id` (`product_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='设备通道与商品挂载配置表';

-- 4. 设备心跳流水表 (device_heartbeat_log)
create table `device_heartbeat_log` (
                                        `id` bigint(20) not null auto_increment comment '主键ID',
                                        `device_sn` varchar(64) not null comment '上报心跳的设备序列号',
                                        `report_time` datetime not null comment '心跳发生/上报的时间',
                                        `signal_strength` tinyint(4) default null comment '网络信号强度(0-100)',
                                        `temperature` decimal(5,2) default null comment '设备核心温度',
                                        `ip_address` varchar(64) default null comment '上报时的IP地址',
                                        `created_at` datetime not null default current_timestamp comment '系统落库时间',
                                        primary key (`id`),
                                        key `idx_device_sn` (`device_sn`),
                                        key `idx_report_time` (`report_time`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='设备心跳与实时工况流水表';


-- 5. 设备故障与告警表 (device_fault_log)
create table `device_fault_log` (
                                    `id` bigint(20) not null auto_increment comment '主键ID',
                                    `device_sn` varchar(64) not null comment '发生告警的设备序列号',
                                    `fault_code` varchar(32) not null comment '系统/硬件故障代码(如E01, WARN_STOCK)',
                                    `fault_desc` varchar(255) default null comment '告警/故障详细描述',
                                    `severity` tinyint(4) not null comment '严重等级(1-提示, 2-一般, 3-严重)',
                                    `status` tinyint(4) not null default '0' comment '处理状态(0-待处理, 1-处理中, 2-已解决)',
                                    `occur_time` datetime not null comment '告警首次发生时间',
                                    `resolve_time` datetime default null comment '告警解决/消除时间',
                                    `handler_id` bigint(20) default null comment '处理人员(运营或商户)ID',
                                    `resolve_remark` varchar(255) default null comment '处理备注(如：已现场换桶)',
                                    `created_at` datetime not null default current_timestamp comment '记录生成时间',
                                    `updated_at` datetime not null default current_timestamp on update current_timestamp comment '记录更新时间',
                                    primary key (`id`),
                                    key `idx_device_sn` (`device_sn`),
                                    key `idx_status` (`status`),
                                    key `idx_occur_time` (`occur_time`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='设备异常告警与运维处理表';

-- 6. 全局商品主表 (product_info)
create table `product_info` (
                                `id` bigint(20) not null auto_increment comment '主键ID',
                                `product_name` varchar(128) not null comment '商品名称(如：青岛经典生啤)',
                                `brand` varchar(64) default null comment '品牌名称(如：青岛啤酒)',
                                `category` varchar(64) default null comment '商品分类(如：黄啤、黑啤)',
                                `alcohol_abv` varchar(20) default null comment '酒精度(如：10°P / 4.0%vol)',
                                `capacity` int(11) not null comment '容量规格(单位：ml)',
                                `image_url` varchar(255) default null comment '商品展示图片URL',
                                `description` varchar(500) default null comment '商品描述/卖点介绍',
                                `base_price` decimal(10,2) default null comment '平台统一指导价',
                                `global_status` tinyint(4) not null default '1' comment '平台全局状态(1-全局上架，0-全局下架)',
                                `is_deleted` tinyint(4) not null default '0' comment '软删除标记(0-正常，1-已删除)',
                                `created_at` datetime not null default current_timestamp comment '创建时间',
                                `updated_at` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
                                primary key (`id`),
                                key `idx_product_name` (`product_name`),
                                key `idx_brand` (`brand`),
                                key `idx_category` (`category`),
                                key `idx_status` (`global_status`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='全局商品基础档案表';

-- 7. 原料基础信息表 (raw_material)
create table `raw_material` (
                                `id` bigint(20) not null auto_increment comment '主键ID',
                                `material_code` varchar(64) not null comment '原料唯一编码(用于对接ERP等)',
                                `material_name` varchar(128) not null comment '原料名称(如：青岛原浆生啤液)',
                                `category` varchar(64) default null comment '原料分类(如：酒水液体、辅助气体)',
                                `customer_id` bigint(20) default '0' comment '归属大客户ID (0代表平台通用原料，非0代表客户定制专供)',
                                `status` tinyint(4) not null default '1' comment '状态(1-正常可用，0-停用下架)',
                                `base_unit` varchar(20) not null comment '基础计量单位(如：L, ML, KG)',
                                `description` varchar(255) default null comment '原料备注描述',
                                `is_deleted` tinyint(4) not null default '0' comment '软删除标记(0-正常，1-已删除)',
                                `created_at` datetime not null default current_timestamp comment '创建时间',
                                `updated_at` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
                                primary key (`id`),
                                unique key `uk_material_code` (`material_code`),
                                key `idx_customer_id` (`customer_id`),
                                key `idx_category` (`category`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='底层原料基础信息表';

-- 8. 商品配方明细表 (product_formula)
create table `product_formula` (
                                   `id` bigint(20) not null auto_increment comment '主键ID',
                                   `product_id` bigint(20) not null comment '关联的最终售卖商品ID',
                                   `raw_material_id` bigint(20) not null comment '关联的消耗原料ID',
                                   `consume_qty` decimal(10,2) not null comment '消耗数量/比例(如：500.00)',
                                   `consume_unit` varchar(20) not null comment '消耗计量单位(如：ML)',
                                   `is_core` tinyint(4) not null default '1' comment '是否为核心原料(1-是，0-否)',
                                   `is_deleted` tinyint(4) not null default '0' comment '软删除标记(0-正常，1-已删除)',
                                   `created_at` datetime not null default current_timestamp comment '配置创建时间',
                                   `updated_at` datetime not null default current_timestamp on update current_timestamp comment '配置更新时间',
                                   primary key (`id`),
                                   key `idx_product_id` (`product_id`),
                                   key `idx_raw_material_id` (`raw_material_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='商品与原料的配方组成明细表';


-- 9. 厂商/商户商品定价表 (owner_product_price)
create table `owner_product_price` (
                                       `id` bigint(20) not null auto_increment comment '主键ID',
                                       `owner_id` bigint(20) not null comment '所属厂商/商户ID',
                                       `product_id` bigint(20) not null comment '关联的商品ID',
                                       `retail_price` decimal(10,2) not null comment '该商户设定的实际售卖单价',
                                       `owner_status` tinyint(4) not null default '1' comment '该商户维度的独立上下架状态(1-上架，0-下架)',
                                       `created_at` datetime not null default current_timestamp comment '定价创建时间',
                                       `updated_at` datetime not null default current_timestamp on update current_timestamp comment '价格更新时间',
                                       primary key (`id`),
                                       unique key `uk_owner_product` (`owner_id`,`product_id`),
                                       key `idx_product_id` (`product_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci comment='商户维度的商品独立定价表';



INSERT INTO `device_type` (`type_name`, `config_info`, `key_parameters`) VALUES
                                                                             ('单头台式智能机', '适合小型餐饮桌面摆放，1个制冷酒头', '{"power":"300W", "cooling_type":"风冷"}'),
                                                                             ('双头落地智能机', '适合大中型餐厅，2个独立制冷酒头', '{"power":"500W", "cooling_type":"水冷"}'),
                                                                             ('三头商用旗舰机', '适合酒吧夜店，3个独立制冷酒头', '{"power":"800W", "cooling_type":"水冷"}'),
                                                                             ('四头精酿专属机', '适合精酿酒馆，支持4种不同温度控制', '{"power":"1000W", "cooling_type":"水冷"}'),
                                                                             ('单头壁挂式打酒机', '节省空间，适合走廊或嵌入式安装', '{"power":"250W", "cooling_type":"风冷"}'),
                                                                             ('移动式双头售酒车', '自带蓄电池，适合户外大排档及夜市', '{"power":"400W", "battery":"60Ah"}'),
                                                                             ('桌面迷你单头机', '极致小巧，适合家用或高端VIP包间', '{"power":"150W", "cooling_type":"半导体制冷"}'),
                                                                             ('六头大型精酿墙', '大型商用设备，支持6款精酿同时售卖', '{"power":"1500W", "cooling_type":"双压缩机直冷"}'),
                                                                             ('自助扫码单头机', '纯自助无人工干预，带大屏交互', '{"power":"350W", "screen_size":"15.6寸"}'),
                                                                             ('双头冰沙啤酒机', '特殊机型，打出带有冰沙口感的啤酒', '{"power":"600W", "special_feature":"冰沙瞬冷"}');

INSERT INTO `product_info` (`product_name`, `brand`, `category`, `alcohol_abv`, `capacity`, `base_price`) VALUES
                                                                                                              ('青岛经典生啤 500ml', '青岛啤酒', '黄啤', '4.0%vol', 500, 18.00),
                                                                                                              ('雪花纯生啤酒 500ml', '雪花啤酒', '黄啤', '3.6%vol', 500, 15.00),
                                                                                                              ('百威金尊 500ml', '百威啤酒', '黄啤', '4.5%vol', 500, 22.00),
                                                                                                              ('燕京U8 500ml', '燕京啤酒', '黄啤', '2.5%vol', 500, 16.00),
                                                                                                              ('哈尔滨冰纯 500ml', '哈尔滨啤酒', '黄啤', '3.6%vol', 500, 12.00),
                                                                                                              ('福佳白啤酒 500ml', '福佳白', '白啤', '4.9%vol', 500, 28.00),
                                                                                                              ('1664白啤 500ml', '1664', '白啤', '5.0%vol', 500, 30.00),
                                                                                                              ('泰山原浆7天 500ml', '泰山原浆', '原浆生啤', '5.0%vol', 500, 25.00),
                                                                                                              ('罗斯福10号 330ml', '罗斯福', '修道院黑啤', '11.3%vol', 330, 45.00),
                                                                                                              ('迷失海岸海妖 500ml', '迷失海岸', 'IPA', '6.5%vol', 500, 35.00);

INSERT INTO `raw_material` (`material_code`, `material_name`, `category`, `customer_id`, `base_unit`) VALUES
                                                                                                          ('RM-QD-001', '青岛经典原浆酒液', '酒水液体', 0, 'L'),
                                                                                                          ('RM-XH-001', '雪花纯生酒液', '酒水液体', 0, 'L'),
                                                                                                          ('RM-BW-001', '百威金尊酒液', '酒水液体', 0, 'L'),
                                                                                                          ('RM-FJ-001', '福佳白原浆酒液', '酒水液体', 0, 'L'),
                                                                                                          ('RM-TS-001', '泰山7天原浆酒液', '酒水液体', 0, 'L'),
                                                                                                          ('RM-IPA-001', '迷失海岸IPA原浆', '酒水液体', 0, 'L'),
                                                                                                          ('RM-GAS-CO2', '食品级二氧化碳气体', '辅助气体', 0, 'KG'),
                                                                                                          ('RM-GAS-N2', '食品级氮气(用于世涛啤酒)', '辅助气体', 0, 'KG'),
                                                                                                          ('RM-CUP-500', '定制印花一次性塑料杯(500ml)', '包装耗材', 0, '个'),
                                                                                                          ('RM-CUST-001', '某连锁酒吧特供混酿原液', '酒水液体', 101, 'L'); -- 专属客户原料

INSERT INTO `product_formula` (`product_id`, `raw_material_id`, `consume_qty`, `consume_unit`, `is_core`) VALUES
                                                                                                              (1, 1, 500.00, 'ML', 1),    -- 青岛生啤500ml = 消耗 500ml 青岛原浆
                                                                                                              (1, 7, 0.05, 'KG', 0),      -- 青岛生啤500ml = 消耗 0.05kg CO2
                                                                                                              (1, 9, 1.00, '个', 0),      -- 青岛生啤500ml = 消耗 1个 杯子
                                                                                                              (2, 2, 500.00, 'ML', 1),    -- 雪花纯生500ml = 消耗 500ml 雪花酒液
                                                                                                              (3, 3, 500.00, 'ML', 1),    -- 百威金尊500ml = 消耗 500ml 百威酒液
                                                                                                              (6, 4, 500.00, 'ML', 1),    -- 福佳白500ml = 消耗 500ml 福佳白酒液
                                                                                                              (8, 5, 500.00, 'ML', 1),    -- 泰山原浆500ml = 消耗 500ml 泰山酒液
                                                                                                              (10, 6, 500.00, 'ML', 1),   -- 迷失海岸500ml = 消耗 500ml IPA酒液
                                                                                                              (9, 8, 0.02, 'KG', 0),      -- 罗斯福打酒 = 消耗 氮气辅助
                                                                                                              (2, 9, 1.00, '个', 0);      -- 雪花纯生500ml = 消耗 1个 杯子

INSERT INTO `owner_product_price` (`owner_id`, `product_id`, `retail_price`, `owner_status`) VALUES
                                                                                                 (101, 1, 20.00, 1), -- 商户101 把青岛生啤定价为 20元 (高于指导价)
                                                                                                 (101, 2, 16.00, 1), -- 商户101 把雪花定价为 16元
                                                                                                 (101, 3, 25.00, 1), -- 商户101 把百威定价为 25元
                                                                                                 (101, 6, 35.00, 1), -- 商户101 把福佳白定价为 35元
                                                                                                 (101, 8, 30.00, 1), -- 商户101 把泰山原浆定价为 30元
                                                                                                 (102, 1, 18.00, 1), -- 商户102 把青岛生啤定价为 18元 (平价)
                                                                                                 (102, 4, 15.00, 1), -- 商户102 把燕京U8定价为 15元 (促销)
                                                                                                 (102, 5, 12.00, 1), -- 商户102 把哈尔滨定价为 12元
                                                                                                 (102, 7, 28.00, 1), -- 商户102 把1664定价为 28元
                                                                                                 (102, 10, 38.00, 1);-- 商户102 把IPA定价为 38元


INSERT INTO `device_info` (`device_sn`, `device_name`, `type_id`, `owner_id`, `status`, `province_code`, `city_code`, `district_code`, `address`) VALUES
                                                                                                                                                      ('SN-A001', '杭州大悦城单头机', 1, 101, 1, '330000', '330100', '330105', '浙江省杭州市拱墅区大悦城B1层'),
                                                                                                                                                      ('SN-A002', '杭州滨江宝龙双头机', 2, 101, 1, '330000', '330100', '330108', '浙江省杭州市滨江区宝龙城1楼'),
                                                                                                                                                      ('SN-A003', '杭州西湖音乐酒吧', 3, 101, 1, '330000', '330100', '330106', '浙江省杭州市西湖区南山路88号'),
                                                                                                                                                      ('SN-A004', '杭州下沙大学城摊位', 6, 101, 2, '330000', '330100', '330118', '浙江省杭州市钱塘区高沙商业街(离线)'),
                                                                                                                                                      ('SN-A005', '宁波天一广场精酿店', 4, 101, 1, '330000', '330200', '330203', '浙江省宁波市海曙区天一广场'),
                                                                                                                                                      ('SN-B001', '上海静安寺台式机', 1, 102, 1, '310000', '310100', '310106', '上海市静安区愚园路1号'),
                                                                                                                                                      ('SN-B002', '上海新天地双头机', 2, 102, 3, '310000', '310100', '310101', '上海市黄浦区新天地(故障)'),
                                                                                                                                                      ('SN-B003', '上海陆家嘴自助机', 9, 102, 1, '310000', '310100', '310115', '上海市浦东新区正大广场'),
                                                                                                                                                      ('SN-B004', '上海徐家汇三头机', 3, 102, 1, '310000', '310100', '310104', '上海市徐汇区美罗城'),
                                                                                                                                                      ('SN-B005', '上海五角场迷你机', 7, 102, 0, '310000', '310100', '310110', '上海市杨浦区大学路(已禁用)');

INSERT INTO `device_product_config` (`device_sn`, `channel_no`, `product_id`, `device_price`, `current_stock`) VALUES
                                                                                                                   ('SN-A001', 1, 1, 20.00, 15000.00),  -- 挂载青岛，当前库存15升
                                                                                                                   ('SN-A002', 1, 1, 20.00, 8000.00),   -- 1号通道青岛
                                                                                                                   ('SN-A002', 2, 3, 25.00, 12000.00),  -- 2号通道百威
                                                                                                                   ('SN-A003', 1, 6, 35.00, 5000.00),   -- 1号福佳白
                                                                                                                   ('SN-A003', 2, 8, 30.00, 4500.00),   -- 2号泰山
                                                                                                                   ('SN-A003', 3, 10, 38.00, 18000.00), -- 3号IPA
                                                                                                                   ('SN-B001', 1, 2, 16.00, 20000.00),  -- 雪花
                                                                                                                   ('SN-B002', 1, 4, 15.00, 500.00),    -- 燕京 (库存见底，可能因为这个报了故障)
                                                                                                                   ('SN-B002', 2, 5, 12.00, 18000.00),  -- 哈尔滨
                                                                                                                   ('SN-B003', 1, 7, 28.00, 16000.00);  -- 1664


INSERT INTO `device_heartbeat_log` (`device_sn`, `report_time`, `signal_strength`, `temperature`, `ip_address`) VALUES
                                                                                                                    ('SN-A001', DATE_SUB(NOW(), INTERVAL 1 MINUTE), 95, 3.5, '192.168.1.101'),
                                                                                                                    ('SN-A001', DATE_SUB(NOW(), INTERVAL 2 MINUTE), 96, 3.5, '192.168.1.101'),
                                                                                                                    ('SN-A002', DATE_SUB(NOW(), INTERVAL 1 MINUTE), 88, 4.0, '192.168.1.102'),
                                                                                                                    ('SN-A002', DATE_SUB(NOW(), INTERVAL 2 MINUTE), 89, 4.0, '192.168.1.102'),
                                                                                                                    ('SN-A003', DATE_SUB(NOW(), INTERVAL 1 MINUTE), 100, 2.5, '192.168.1.103'),
                                                                                                                    ('SN-B001', DATE_SUB(NOW(), INTERVAL 1 MINUTE), 75, 4.2, '10.0.0.51'),
                                                                                                                    ('SN-B003', DATE_SUB(NOW(), INTERVAL 1 MINUTE), 92, 3.8, '10.0.0.53'),
                                                                                                                    ('SN-B004', DATE_SUB(NOW(), INTERVAL 1 MINUTE), 85, 3.1, '10.0.0.54'),
                                                                                                                    ('SN-A004', DATE_SUB(NOW(), INTERVAL 2 DAY), 10, 15.0, '192.168.1.104'), -- 离线设备最后一次心跳
                                                                                                                    ('SN-B002', DATE_SUB(NOW(), INTERVAL 5 MINUTE), 90, 12.5, '10.0.0.52'); -- 故障设备心跳(温度异常升高)

INSERT INTO `device_fault_log` (`device_sn`, `fault_code`, `fault_desc`, `severity`, `status`, `occur_time`, `resolve_time`, `handler_id`) VALUES
                                                                                                                                               ('SN-B002', 'WARN_TEMP_HIGH', '制冷系统异常，温度升至12.5度', 3, 0, DATE_SUB(NOW(), INTERVAL 1 HOUR), NULL, NULL), -- 待处理
                                                                                                                                               ('SN-B002', 'WARN_STOCK_LOW', '1号酒头库存低于预警线(500ml)', 2, 0, DATE_SUB(NOW(), INTERVAL 2 HOUR), NULL, NULL), -- 待处理
                                                                                                                                               ('SN-A004', 'ERR_OFFLINE', '设备心跳超时离线超过24小时', 2, 1, DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, 101), -- 处理中
                                                                                                                                               ('SN-A001', 'WARN_STOCK_LOW', '库存预警已消除', 1, 2, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 101), -- 已解决
                                                                                                                                               ('SN-A003', 'ERR_NETWORK', '网络信号极弱断连', 2, 2, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 101),
                                                                                                                                               ('SN-B004', 'ERR_COMPRESSOR', '压缩机电流过载', 3, 2, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), 102),
                                                                                                                                               ('SN-B001', 'WARN_CO2_LOW', '辅助气体二氧化碳压力不足', 2, 1, DATE_SUB(NOW(), INTERVAL 4 HOUR), NULL, 102),
                                                                                                                                               ('SN-B003', 'ERR_SCREEN', '触摸屏通信失败', 3, 0, DATE_SUB(NOW(), INTERVAL 30 MINUTE), NULL, NULL),
                                                                                                                                               ('SN-A005', 'WARN_DOOR_OPEN', '设备柜门未关严', 1, 2, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY), 101),
                                                                                                                                               ('SN-A002', 'ERR_FLOW_METER', '2号酒头流量计数据异常', 3, 2, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 14 DAY), 101);



